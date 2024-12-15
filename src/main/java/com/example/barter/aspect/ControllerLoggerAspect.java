package com.example.barter.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class ControllerLoggerAspect {

    @Around("execution(* com.example.barter.controller.*.*(..))")
    public Object controllerLogger(ProceedingJoinPoint jp) throws Throwable {
        final String methodName = jp.getSignature().getName();
        final Object[] methodArgs = jp.getArgs();
        long startTime = System.currentTimeMillis();

        log.info("Executing route: {}, with arguments: {}", methodName, methodArgs);

        Object response;
        try {
            response = jp.proceed();
        } catch (Throwable e) {
            log.error("Error in {}: {} - {}", methodName, e.getClass().getSimpleName(), e.getMessage());
            throw e;
        }

        long executionTime = System.currentTimeMillis() - startTime;
        final int statusCode = ((ResponseEntity<?>) response).getStatusCode().value();

        log.info("Method {} completed with status code {} in {} ms", methodName, statusCode, executionTime);

        return response;
    }
}
