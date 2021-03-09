package net.bytecms.core.utils;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public  class HardWareMonitor {

    static Map<String,Object> hardWareInfoMap = new HashMap<>(16);

    public static Map<String,Object>  hardWareInfo(){
        synchronized (HardWareMonitor.class){
            SystemInfo systemInfo = new SystemInfo();
            HardWareMonitor.printlnCpuInfo(systemInfo);
            HardWareMonitor.MemInfo(systemInfo);
            HardWareMonitor.hardDrive(systemInfo);
            HardWareMonitor.getThread();
            HardWareMonitor.setSysInfo();
            HardWareMonitor.setJvmInfo();
        }
        return  hardWareInfoMap;
    }

    private static void printlnCpuInfo(SystemInfo systemInfo){
        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        // 睡眠1s
        try {
            TimeUnit.SECONDS.sleep(1);
            long[] ticks = processor.getSystemCpuLoadTicks();
            long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
            long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
            long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
            long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
            long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
            long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
            long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
            long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
            long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
            hardWareInfoMap.put("cpu_core_total", processor.getLogicalProcessorCount());// cpu核数
            hardWareInfoMap.put("cpu_sys_use", new DecimalFormat("#.##%").format(cSys * 1.0 / totalCpu));// cpu系统使用率
            hardWareInfoMap.put("cpu_usr_use", new DecimalFormat("#.##%").format(user * 1.0 / totalCpu));// cpu用户使用率
            hardWareInfoMap.put("cpu_wait_use", new DecimalFormat("#.##%").format(iowait * 1.0 / totalCpu));// cpu当前等待率
            hardWareInfoMap.put("cpu_use", new DecimalFormat("#.##%").format(1.0-(idle * 1.0 / totalCpu)));// cpu当前使用率
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void MemInfo(SystemInfo systemInfo){
        GlobalMemory memory = systemInfo.getHardware().getMemory();
        //总内存
        long totalByte = memory.getTotal();
        //剩余
        long acaliableByte = memory.getAvailable();
        hardWareInfoMap.put("memory_total", formatByte(totalByte));// 总内存
        hardWareInfoMap.put("memory_use_total", formatByte(totalByte-acaliableByte));// 使用内存
        hardWareInfoMap.put("memory_surplus_total", formatByte(acaliableByte));// 剩余内存
        hardWareInfoMap.put("memory_use_rate", new DecimalFormat("#.##%").format((totalByte-acaliableByte)*1.0/totalByte));// 剩余内存
    }

    public static void hardDrive(SystemInfo systemInfo){
        HWDiskStore[] hwDiskStores = systemInfo.getHardware().getDiskStores();
        List<Map<String,Object>> list =new ArrayList<>(16);
        long totalSize = 0;
        for(HWDiskStore hwDiskStore:hwDiskStores){
            totalSize+=hwDiskStore.getSize();
        }
        BigDecimal total = new BigDecimal(totalSize);
        if(hwDiskStores!=null&&hwDiskStores.length>0){
            for(HWDiskStore hwDiskStore:hwDiskStores){
                HWPartition[] hwPartitions=hwDiskStore.getPartitions();
                if(hwPartitions!=null&&hwPartitions.length>0){
                    for(HWPartition hwPartition:hwPartitions){
                        Map<String,Object> hardDriveMap = new HashMap<>(16);
                        BigDecimal rate=new BigDecimal(hwPartition.getSize()).divide(total, 2, RoundingMode.HALF_UP);
                        String name = Checker.BeNotBlank(hwPartition.getMountPoint())?
                        hwPartition.getMountPoint().replace("\\",""): hwPartition.getIdentification();
                        hardDriveMap.put(name,rate);
                        list.add(hardDriveMap);
                    }
                }
            }
        }
        hardWareInfoMap.put("hardDrive_rate_total", list);// 占比容量
        hardWareInfoMap.put("hardDrive_total", formatByte(totalSize));// 总容量
    }

    public static void setSysInfo(){
        Properties props = System.getProperties();
        //系统名称
        String osName = props.getProperty("os.name");
        //架构名称
        String osArch = props.getProperty("os.arch");
        String javaVersion = props.getProperty("java.version");
        hardWareInfoMap.put("os_name", osName);// 操作系统名
        hardWareInfoMap.put("os_arch", osArch);// 系统架构
        hardWareInfoMap.put("java_version", javaVersion);// javaVersion
    }

    public static void setJvmInfo(){
        Properties props = System.getProperties();
        Runtime runtime = Runtime.getRuntime();
        //jvm总内存
        long jvmTotalMemoryByte = runtime.totalMemory();
        //jvm最大可申请
        long jvmMaxMoryByte = runtime.maxMemory();
        //空闲空间
        long freeMemoryByte = runtime.freeMemory();
        //jdk版本
        String jdkVersion = props.getProperty("java.version");
        //jdk路径
        String jdkHome = props.getProperty("java.home");
        hardWareInfoMap.put("jvm_total", formatByte(jvmTotalMemoryByte));// jvm内存总量
        hardWareInfoMap.put("jvm_use_total", formatByte(jvmTotalMemoryByte-freeMemoryByte));// jvm已使用内存
        hardWareInfoMap.put("jvm_surplus_total", formatByte(freeMemoryByte));// jvm剩余内存
        hardWareInfoMap.put("jvm_use_rate", new DecimalFormat("#.##%").format((jvmTotalMemoryByte-freeMemoryByte)*1.0/jvmTotalMemoryByte));// jvm内存使用率
        hardWareInfoMap.put("java_version", jdkVersion);// java版本
        hardWareInfoMap.put("java_home", jdkHome);// jdkHome
    }

    public static void getThread(){
        ThreadGroup currentGroup =Thread.currentThread().getThreadGroup();
        while (currentGroup.getParent()!=null){
            // 返回此线程组的父线程组
            currentGroup=currentGroup.getParent();
        }
        //此线程组中活动线程的估计数
        int noThreads = currentGroup.activeCount();
        Thread[] lstThreads = new Thread[noThreads];
        //把对此线程组中的所有活动子组的引用复制到指定数组中。
        currentGroup.enumerate(lstThreads);
        hardWareInfoMap.put("threads", lstThreads);// jdkHome
//        for (Thread thread : lstThreads) {
//            System.out.println("线程数量："+noThreads+" 线程id：" + thread.getId() + " 线程名称：" + thread.getName() + " 线程状态：" + thread.getState());
//        }
    }

    public static String formatByte(long byteNumber){
        //换算单位
        double FORMAT = 1024.0;
        double kbNumber = byteNumber/FORMAT;
        if(kbNumber<FORMAT){
            return new DecimalFormat("#.##KB").format(kbNumber);
        }
        double mbNumber = kbNumber/FORMAT;
        if(mbNumber<FORMAT){
            return new DecimalFormat("#.##MB").format(mbNumber);
        }
        double gbNumber = mbNumber/FORMAT;
        if(gbNumber<FORMAT){
            return new DecimalFormat("#.##GB").format(gbNumber);
        }
        double tbNumber = gbNumber/FORMAT;
        return new DecimalFormat("#.##TB").format(tbNumber);
    }
}
