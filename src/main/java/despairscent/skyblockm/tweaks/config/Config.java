package despairscent.skyblockm.tweaks.config;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

import java.io.FileReader;
import java.io.FileWriter;

import static despairscent.skyblockm.tweaks.ModUtils.LOGGER;
import static despairscent.skyblockm.tweaks.ModUtils.i18n;

public class Config {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().addSerializationExclusionStrategy(new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return false;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return Modules.class == clazz;
        }
    }).create();

    private static final String FILENAME = "skyblockm-tweaks.json";

    public static final Config DEFAULT = new Config();

    public static final int KEY_UNDEFINED = -1;
    public static final short MODIFIER_UNDEFINED = 0;

    @Deprecated
    private Modules modules = null;

    public FpsOptimizeConfig fpsOptimize = new FpsOptimizeConfig();

    public StorageTargetingFixConfig storageTargetingFix = new StorageTargetingFixConfig();

    public MoreTooltipInfoConfig moreTooltipInfo = new MoreTooltipInfoConfig();

    public RenderItemInsideConfig renderItemInside = new RenderItemInsideConfig();

    @SerializedName(value = "textInputLagFix", alternate = {"inputLagFix"})
    public TextInputLagFixConfig textInputLagFix = new TextInputLagFixConfig();

    public EsTerminalScrollConfig esTerminalScroll = new EsTerminalScrollConfig();

    public CompactGenomeConfig compactGenome = new CompactGenomeConfig();

    public HideHiddenArmorStandsConfig hideHiddenArmorStands = new HideHiddenArmorStandsConfig();

    @Deprecated
    private static class Modules {
        public boolean fpsOptimize = true;
        public boolean storageTargetingFix = true;
        public boolean moreTooltipInfo = true;
        public boolean renderItemInside = true;
        public boolean inputLagFix = true;
        public boolean esTerminalScroll = true;
        public boolean compactGenome = false;
        public boolean hideHiddenArmorStands = false;
    }

    // GSON делает поле в конце, хочу в начале
    // public abstract static class ModuleConfig {
    //     public boolean enabled = true;
    // }

    public static class FpsOptimizeConfig {
        public boolean enabled = true;

        public boolean modelsCaching = true;
    }

    public static class StorageTargetingFixConfig {
        public boolean enabled = true;
    }

    public static class MoreTooltipInfoConfig {
        public boolean enabled = true;

        public boolean storage = true;
        public boolean fluidStorage = true;
        public boolean crystalMemory = true;
    }

    public static class RenderItemInsideConfig {
        public boolean enabled = true;

        public RenderItemInsideSub esPattern = new RenderItemInsideSub(0xFF9D9DFF);
        public RenderItemInsideSub storage = new RenderItemInsideSub(0xFFAEA78B);
        public RenderItemInsideSub crystalMemory = new RenderItemInsideSub(0xFF4A4A4A);
    }

    public static class RenderItemInsideSub {
        public boolean enabled = true;
        public boolean renderAlways = false;
        public boolean drawOriginal = true; // drawContainer
        public int bgColor;

        RenderItemInsideSub(int bgColor) {
            this.bgColor = bgColor;
        }
    }

    public static class TextInputLagFixConfig {
        public boolean enabled = true;

        public boolean recipesSearch = true;
        public boolean esTerminalSearch = true;
    }

    public static class EsTerminalScrollConfig {
        public boolean enabled = true;

        public boolean wheel = true;
        public int wheelModifier = KEY_UNDEFINED;
        public int keyDown = KEY_UNDEFINED;
        // public short keyDownModifiers = MODIFIER_UNDEFINED;
        public int keyUp = KEY_UNDEFINED;
        // public short keyUpModifiers = MODIFIER_UNDEFINED;
        public int actionLimitWheel = 0;
        public int actionLimitKey = 2;

        public int boostKey = InputUtil.GLFW_KEY_LEFT_SHIFT;
        public EsTerminalScrollBoostKeyType boostKeyType = EsTerminalScrollBoostKeyType.ACTIVATE;
    }

    public enum EsTerminalScrollBoostKeyType {
        ACTIVATE(i18n("config.esTerminalScroll.boostKeyType.activate")),
        DEACTIVATE(i18n("config.esTerminalScroll.boostKeyType.deactivate"));

        public final Text optionName;

        EsTerminalScrollBoostKeyType(Text optionName) {
            this.optionName = optionName;
        }
    }

    public static class CompactGenomeConfig {
        public boolean enabled = false;
    }

    public static class HideHiddenArmorStandsConfig {
        public boolean enabled = false;
    }

    public static Config load() {
        try (FileReader reader = new FileReader(FabricLoader.getInstance().getConfigDir().resolve(FILENAME).toFile())) {
            Config config = GSON.fromJson(reader, Config.class);

            if (config.modules != null) {
                config.fpsOptimize.enabled = config.modules.fpsOptimize;
                config.storageTargetingFix.enabled = config.modules.storageTargetingFix;
                config.moreTooltipInfo.enabled = config.modules.moreTooltipInfo;
                config.renderItemInside.enabled = config.modules.renderItemInside;
                config.textInputLagFix.enabled = config.modules.inputLagFix;
                config.esTerminalScroll.enabled = config.modules.esTerminalScroll;
                config.compactGenome.enabled = config.modules.compactGenome;
                config.hideHiddenArmorStands.enabled = config.modules.hideHiddenArmorStands;
            }

            return config;
        } catch (Exception e) {
            LOGGER.error("Load config error", e);
            return new Config();
        }
    }

    public void save() {
        try (FileWriter writer = new FileWriter(FabricLoader.getInstance().getConfigDir().resolve(FILENAME).toFile())) {
            GSON.toJson(this, writer);
        } catch (Exception e) {
            LOGGER.error("Save config error", e);
        }
    }

}
