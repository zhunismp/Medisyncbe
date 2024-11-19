package com.mahidol.drugapi.common.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class SchedulerService {
    private static final Logger logger = LoggerFactory.getLogger(SchedulerService.class);
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);

    public static void scheduled(long period, Runnable fn) {
        long initialDelay = calculateInitialDelay(period);
        scheduler.scheduleAtFixedRate(fn, initialDelay, period, TimeUnit.MILLISECONDS);

        logger.info("Initial delay: " + initialDelay);
        logger.info("Period: " + period);
    }

    private static long calculateInitialDelay(long period) {
        long nowMillis = System.currentTimeMillis();
        long elapsedSinceLastPeriod = nowMillis % period;
        long initialDelay = period - elapsedSinceLastPeriod;

        // Offset 1 second
        return initialDelay > 0 ? initialDelay + 1000 : 0;
    }
}
