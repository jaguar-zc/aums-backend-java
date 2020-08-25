package cn.stackflow.aums.common.utils;

import java.lang.management.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-08-25 15:47
 */
public class JVMResource {
    private static NumberFormat fmtI = new DecimalFormat("###,###", new DecimalFormatSymbols(Locale.ENGLISH));
    private static NumberFormat fmtD = new DecimalFormat("###,##0.000", new DecimalFormatSymbols(Locale.ENGLISH));

    private static final int Kb = 1024;

    public static Map<String, Object> getSummary() {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
        ThreadMXBean threads = ManagementFactory.getThreadMXBean();
        MemoryMXBean mem = ManagementFactory.getMemoryMXBean();
        ClassLoadingMXBean cl = ManagementFactory.getClassLoadingMXBean();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("jvmName", runtime.getVmName() + " version " + runtime.getVmVersion());
        map.put("jvmJavaVer", System.getProperty("java.version"));
        map.put("jvmUptime", toDuration(runtime.getUptime()));
        map.put("threadsLive", threads.getThreadCount());
        map.put("threadsDaemon", threads.getDaemonThreadCount());
        map.put("threadsPeak", threads.getPeakThreadCount());
        map.put("threadsTotal", threads.getTotalStartedThreadCount());
        map.put("heapCurr", mem.getHeapMemoryUsage().getUsed() / Kb);
        map.put("heapMax", mem.getHeapMemoryUsage().getMax() / Kb);
        map.put("heapCommitted", mem.getHeapMemoryUsage().getCommitted() / Kb);
        map.put("osName", os.getName() + " " + os.getVersion());
        map.put("osArch", os.getArch());
        map.put("osCores", os.getAvailableProcessors());
        map.put("clsCurrLoaded", cl.getLoadedClassCount());
        map.put("clsLoaded", cl.getTotalLoadedClassCount());
        map.put("clsUnloaded", cl.getUnloadedClassCount());
        return map;
    }

    protected static String printSizeInKb(double size) {
        return fmtI.format((long) (size / 1024)) + " kbytes";
    }

    protected static String toDuration(double uptime) {
        uptime /= 1000;
        if (uptime < 60) {
            return fmtD.format(uptime) + " seconds";
        }
        uptime /= 60;
        if (uptime < 60) {
            long minutes = (long) uptime;
            String s = fmtI.format(minutes) + (minutes > 1 ? " minutes" : " minute");
            return s;
        }
        uptime /= 60;
        if (uptime < 24) {
            long hours = (long) uptime;
            long minutes = (long) ((uptime - hours) * 60);
            String s = fmtI.format(hours) + (hours > 1 ? " hours" : " hour");
            if (minutes != 0) {
                s += " " + fmtI.format(minutes) + (minutes > 1 ? " minutes" : " minute");
            }
            return s;
        }
        uptime /= 24;
        long days = (long) uptime;
        long hours = (long) ((uptime - days) * 24);
        String s = fmtI.format(days) + (days > 1 ? " days" : " day");
        if (hours != 0) {
            s += " " + fmtI.format(hours) + (hours > 1 ? " hours" : " hour");
        }
        return s;
    }

}
