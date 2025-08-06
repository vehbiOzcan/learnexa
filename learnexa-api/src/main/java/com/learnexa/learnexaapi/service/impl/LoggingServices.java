package com.learnexa.learnexaapi.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class LoggingServices {

    private static final Logger logger = LoggerFactory.getLogger(LoggingServices.class);

    private final ThreadPoolTaskExecutor taskExecutor;

    @Autowired

    public LoggingServices(@Qualifier("FileUploadTaskExecutor") ThreadPoolTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Scheduled(fixedRate = 1000)  // 5 saniyede bir loglama yapacak
    public void logThreadPoolStats() {
        /*
        int activeThreads = taskExecutor.getActiveCount();
        int queueSize = taskExecutor.getThreadPoolExecutor().getQueue().size();
        int corePoolSize = taskExecutor.getCorePoolSize();
        int maxPoolSize = taskExecutor.getMaxPoolSize();

        logger.info("Aktif Thread Sayısı: {}, Kuyrukta Bekleyen İş Sayısı: {}, Core Pool Size: {}, Max Pool Size: {}",
                activeThreads, queueSize, corePoolSize, maxPoolSize);
         */
        int activeThreads = taskExecutor.getActiveCount();
        int queueSize = taskExecutor.getThreadPoolExecutor().getQueue().size();
        int largestPoolSize = taskExecutor.getThreadPoolExecutor().getLargestPoolSize(); // En büyük boyut

        logger.info("Aktif Thread Sayısı: {}, Kuyrukta Bekleyen İş Sayısı: {}, Largest Pool Size: {}",
                activeThreads, queueSize, largestPoolSize);
    }
}
