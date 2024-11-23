package com.example.barter.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ControllerLoggerAspect {

    @Around("execution(* com.example.barter.controller.*.*(..))")
    public Object controllerLogger(ProceedingJoinPoint jp) throws Throwable {
        final String methodName = jp.getSignature().getName();

        log.info("executing route {}", methodName);

        Object response;
        try
        {
            response  = jp.proceed();
        }
        catch (Throwable e)
        {
            log.error("error in {} : {}-{}",methodName,e.getClass().getSimpleName(),e.getMessage());
            throw e;
        }

        final int statusCode =  ((ResponseEntity<?>) response).getStatusCode().value();


        log.info("method {} : status code {}",methodName,statusCode);

        return response;

    }
}
