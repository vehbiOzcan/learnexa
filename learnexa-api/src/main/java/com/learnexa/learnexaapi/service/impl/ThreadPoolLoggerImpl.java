
package com.learnexa.learnexaapi.service.impl;

import com.sun.management.OperatingSystemMXBean;
import com.learnexa.learnexaapi.service.IThreadPoolLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ThreadPoolLoggerImpl implements IThreadPoolLogger {

    @Autowired
    @Qualifier(value = "FileUploadTaskExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

    private String logFilePath;

    @PostConstruct
    public void init() {
        String directoryPath = "D:/threadLogs";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        logFilePath = directoryPath + "/threadLogs_" + timestamp + ".txt";

        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    @Override
    @Scheduled(fixedRate = 1000)
    public void logThreadPoolStatus() {
        String logMessage = generateLogMessage();
        writeLogToFile(logMessage);
    }

    private String generateLogMessage() {
        int activeThreads = taskExecutor.getActiveCount();
        int queueSize = taskExecutor.getThreadPoolExecutor().getQueue().size();
        double cpuLoad = getCpuLoad();
        long usedMemory = getUsedMemory();

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return String.format("%s - Active Threads: %d, Queue Size: %d, CPU: %.2f%%, RAM: %dMB",
                timestamp, activeThreads, queueSize, cpuLoad, usedMemory);
    }

    private double getCpuLoad() {
        OperatingSystemMXBean osBean =
                (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        return osBean.getSystemCpuLoad() * 100;
    }

    private long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        return (totalMemory - freeMemory) / (1024 * 1024);
    }

    private void writeLogToFile(String logMessage) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath, true))) {
            writer.write(logMessage);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

