package projecthds.herodotusutils.config;

import com.cleanroommc.configanytime.ConfigAnytime;
import net.minecraftforge.common.config.Config;
import projecthds.herodotusutils.HerodotusUtils;

@Config(modid = HerodotusUtils.MOD_ID)
public class HDSUConfig {
    public static boolean debug = false;
    public static int RequiredRandomTicksForQuartz = 2;// 需要的随机刻数量
    public static int MinUpdatesForQuartz = 42;// 锂石英生成的最小更新次数
    public static int MaxUpdatesForQuartz = 52;// 锂石英生成的最大更新次数
    public static int PowderDropCountForQuartz = 7;// 粉末掉落数量

    public static int UpdateRateThresholdForQuartz = 5;// 每秒最大允许更新次数
    public static int RateCheckIntervalForQuartz = 20;// 检测间隔（20 ticks = 1秒）

    public static int DebounceIntervalForQuartz = 1;// 消抖间隔（单位：tick）
    public static int MaxBurstUpdatesForQuartz = 1;// 允许的最大突发更新数
    public static boolean OverloadExplosionBreakBlock = false;

    public static boolean nullpinterWelcomeEnabled = true;

    static {
        ConfigAnytime.register(HDSUConfig.class);
    }
}
