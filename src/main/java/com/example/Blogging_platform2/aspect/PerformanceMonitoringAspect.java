package com.example.Blogging_platform2.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class PerformanceMonitoringAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(PerformanceMonitoringAspect.class);

    // If a method takes longer than this, we'll log a warning
    private static final long SLOW_METHOD_THRESHOLD_MS = 1000;

    @Around("execution(* com.example.Blogging_platform2.service.*.*(..))")
    public Object monitorPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        // Get method information
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        
        // Record start time (in milliseconds)
        long startTime = System.currentTimeMillis();
        
        // Declare result variable
        Object result = null;
        
        try {

            //  This is where your service method actually runs
            result = joinPoint.proceed();
            
            return result;
            
        } finally {
            // Record end time
            long endTime = System.currentTimeMillis();
            
            // Calculate duration
            long duration = endTime - startTime;
            
            // Log performance based on duration
            if (duration > SLOW_METHOD_THRESHOLD_MS) {
                // Slow method - log as warning
                logger.warn("  SLOW METHOD: {}.{} took {} ms",
                           className, methodName, duration);
            } else {
                // Normal speed - log as info
                logger.info("  Performance: {}.{} took {} ms",
                           className, methodName, duration);
            }
        }
    }

    @Around("execution(* com.example.Blogging_platform2.controller.*.*(..)) && @within(org.springframework.web.bind.annotation.RestController)")
    public Object monitorControllerPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        
        long startTime = System.currentTimeMillis();
        Object result = null;
        
        try {
            result = joinPoint.proceed();
            return result;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            
            // API endpoints should be fast (< 500ms is good)
            if (duration > 500) {
                logger.warn("  SLOW API: {}.{} took {} ms",
                           className, methodName, duration);
            } else {
                logger.debug(" API Performance: {}.{} took {} ms",
                            className, methodName, duration);
            }
        }
    }
}
