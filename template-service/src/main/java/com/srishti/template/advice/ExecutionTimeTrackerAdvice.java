package com.srishti.template.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class ExecutionTimeTrackerAdvice {

    Logger logger = LoggerFactory.getLogger(ExecutionTimeTrackerAdvice.class);

    @Around("@annotation(com.srishti.template.advice.TrackExecutionTime)")
    public Object trackExecutionTime(ProceedingJoinPoint point) throws Throwable {
        Long startTime = System.currentTimeMillis();
        Object obj = point.proceed();
        Long endTime = System.currentTimeMillis();
        logger.info("Method Name: " + point.getSignature() + " time taken to execute: " + (endTime - startTime));
        return obj;
    }
}
