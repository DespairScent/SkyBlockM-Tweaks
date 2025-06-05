package despairscent.skyblockm.tweaks.modules.compactgenome;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

import static despairscent.skyblockm.tweaks.ModUtils.CONFIG;
import static despairscent.skyblockm.tweaks.ModUtils.getLiteralNested;

public class CompactGenomeModule {

    private static final GenomeType<?>[] FORMAT = new GenomeType[]{
            Genomes.SPEED,
            Genomes.LIFESPAN,
            Genomes.FERTILITY,
            null,
            Genomes.NOCTURNAL,
            Genomes.FLYER,
            null,
            Genomes.EFFECT,
            null,
            Genomes.TEMPERATURE,
            Genomes.HUMIDITY,
            Genomes.FLOWERS
    };

    public static void init() {
        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            if (!CONFIG.modules.compactGenome) {
                return;
            }

            int start = 0;
            for (; start < lines.size(); start++) {
                if ("Изученные гены:".equals(getLiteralNested(lines.get(start), 0))) {
                    break;
                }
            }
            if (start == lines.size()) {
                return;
            }

            var genomeSet = new GenomeSet();

            int end = start + 1;
            while (end + 3 <= lines.size()) {
                String name = getLiteralNested(lines.get(end), 0);
                if (name == null || !name.startsWith("• ")) {
                    break;
                }
                name = name.substring(2);

                GenomeParser<?> parser = GenomeParser.get(name);
                if (parser == null || !parseLines(genomeSet, parser, lines, end)) {
                    end = -1;
                    break;
                }

                end += 3;
            }

            if (end < start || genomeSet.isEmpty()) {
                return;
            }
            lines.subList(start, end).clear();

            lines.addAll(start, generateLines(genomeSet, FORMAT));
        });
    }

    private static <G> boolean parseLines(GenomeSet genomeSet, GenomeParser<G> parser, List<Text> lines, int i) {
        String firstStr = getLiteralNested(lines.get(i + 1), 1);
        String secondStr = getLiteralNested(lines.get(i + 2), 1);
        if (firstStr == null || secondStr == null) {
            return false;
        }

        G first = parser.valueParser().apply(firstStr.trim());
        G second = parser.valueParser().apply(secondStr.trim());
        if (first == null || second == null) {
            return false;
        }

        GenomeVariant firstVariant = GenomeVariant.parse(getLiteralNested(lines.get(i + 1), 3));
        GenomeVariant secondVariant = GenomeVariant.parse(getLiteralNested(lines.get(i + 2), 3));

        genomeSet.put(parser.type(), first, firstVariant, second, secondVariant);
        return true;
    }

    private static List<Text> generateLines(GenomeSet genomeSet, GenomeType<?>[] format) {
        var lines = new ArrayList<Text>();
        Text empty = Text.empty();

        boolean writingSection = false;
        for (GenomeType<?> type : format) {
            if (type == null) {
                if (writingSection) {
                    lines.add(empty);
                    writingSection = false;
                }
                continue;
            }

            if (!genomeSet.contains(type)) {
                continue;
            }
            writingSection = true;

            GenomePair<?> pair = genomeSet.get(type);
            lines.add(Text.literal(Formatting.GOLD + GenomeCompacter.get(type) + ": " + Formatting.WHITE + GenomeCompacter.get(type, pair.first()) +
                    (pair.first() != pair.second() ? Formatting.GRAY + " (" + GenomeCompacter.get(type, pair.second()) + Formatting.RESET + Formatting.GRAY + ")" : "")));
        }

        if (!lines.isEmpty() && lines.get(lines.size() - 1) == empty) {
            return lines.subList(0, lines.size() - 1);
        }

        return lines;
    }

}
