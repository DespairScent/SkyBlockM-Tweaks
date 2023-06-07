package despairscent.skyblockm.tweaks.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import despairscent.skyblockm.tweaks.ModUtils;
import net.fabricmc.loader.api.FabricLoader;

import java.io.FileReader;
import java.io.FileWriter;

public class Config {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILENAME = "skyblockm-tweaks.json";

    public static final Config DEFAULT = new Config();

    public General general = new General();
    public Optimize optimize = new Optimize();
    public QoL qol = new QoL();

    public static class General {
        public boolean optimize = true;
    }

    public static class Optimize {
        public boolean armorStandCramming = true;
        public boolean modelsCaching = true;
        public boolean spectatorArmorStands = true;
    }

    public static class QoL {
        public boolean storageTargetingFix = true;
        public boolean moreTooltipInfo = true;
    }

    public static Config load() {
        try (FileReader reader = new FileReader(FabricLoader.getInstance().getConfigDir().resolve(FILENAME).toFile())) {
            return GSON.fromJson(reader, Config.class);
        } catch (Exception e) {
            ModUtils.LOGGER.error("Load config error", e);
            return new Config();
        }
    }

    public void save() {
        try (FileWriter writer = new FileWriter(FabricLoader.getInstance().getConfigDir().resolve(FILENAME).toFile())) {
            GSON.toJson(this, writer);
        } catch (Exception e) {
            ModUtils.LOGGER.error("Save config error", e);
        }
    }

}
