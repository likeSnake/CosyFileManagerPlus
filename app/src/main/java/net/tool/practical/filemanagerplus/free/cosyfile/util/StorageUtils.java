package net.tool.practical.filemanagerplus.free.cosyfile.util;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.text.DecimalFormat;

public class StorageUtils {

    // 获取总存储容量（单位：GB）
    public static String getTotalStorage() {
        long totalBytes = getTotalStorageBytes();
        double v = bytesToGB(totalBytes);
        return formatGB(v);
    }

    // 获取已使用的存储容量（单位：GB）
    public static String getUsedStorage() {
        long usedBytes = getUsedStorageBytes();
        double v = bytesToGB(usedBytes);
        return formatGB(v);
    }

    // 获取可用的存储容量（单位：GB）
    public static String getAvailableStorage() {
        long availableBytes = getAvailableStorageBytes();
        double v = bytesToGB(availableBytes);
        return formatGB(v);
    }
    public static String formatGB(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        return decimalFormat.format(value)+"GB";
    }

    // 获取总存储容量（单位：字节）
    private static long getTotalStorageBytes() {
        File path = Environment.getDataDirectory();
        StatFs statFs = new StatFs(path.getAbsolutePath());
        long blockSize = statFs.getBlockSizeLong();
        long totalBlocks = statFs.getBlockCountLong();
        return blockSize * totalBlocks;
    }

    // 获取已使用的存储容量（单位：字节）
    private static long getUsedStorageBytes() {
        File path = Environment.getDataDirectory();
        StatFs statFs = new StatFs(path.getAbsolutePath());
        long blockSize = statFs.getBlockSizeLong();
        long totalBlocks = statFs.getBlockCountLong();
        long availableBlocks = statFs.getAvailableBlocksLong();
        return blockSize * (totalBlocks - availableBlocks);
    }

    // 获取可用的存储容量（单位：字节）
    private static long getAvailableStorageBytes() {
        File path = Environment.getDataDirectory();
        StatFs statFs = new StatFs(path.getAbsolutePath());
        long blockSize = statFs.getBlockSizeLong();
        long availableBlocks = statFs.getAvailableBlocksLong();
        return blockSize * availableBlocks;
    }

    // 将字节转换为GB
    private static double bytesToGB(long bytes) {
        double kilobyte = 1024;
        double megabyte = kilobyte * 1024;
        double gigabyte = megabyte * 1024;
        return bytes / gigabyte;
    }
}