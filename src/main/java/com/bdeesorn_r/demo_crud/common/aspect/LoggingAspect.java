package com.bdeesorn_r.demo_crud.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

@Aspect
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class); 

    @Around("execution(* com.bdeesorn_r.demo_crud.controller.*Controller.*(..))")
    public Object controllerLogger(ProceedingJoinPoint pjp) throws Throwable {
        String className = pjp.getTarget().getClass().getSimpleName();
        String methodName = pjp.getSignature().getName();

        String strFormat = "[Controller][%s][%s] >>>>> %s: ";
        
        StopWatch stopWatch = new StopWatch(className + "_" + methodName);
        stopWatch.start();
        logger.info(String.format(strFormat, className, methodName, "Start") + stopWatch.getTotalTimeMillis());

        Object result = pjp.proceed();

        stopWatch.stop();
        logger.info(String.format(strFormat, className, methodName, "End") + stopWatch.getTotalTimeMillis());

        return result;
    }

    @Around("execution(* com.bdeesorn_r.demo_crud.service.*Service.*(..))")
    public Object serviceLogger(ProceedingJoinPoint pjp) throws Throwable {
        String className = pjp.getTarget().getClass().getSimpleName();
        String methodName = pjp.getSignature().getName();

        String strFormat = "[Service][%s][%s] >>>>> %s: ";
        
        StopWatch stopWatch = new StopWatch(className + "_" + methodName);
        stopWatch.start();
        logger.info(String.format(strFormat, className, methodName, "Start") + stopWatch.getTotalTimeMillis());

        Object result = pjp.proceed();

        stopWatch.stop();
        logger.info(String.format(strFormat, className, methodName, "End") + stopWatch.getTotalTimeMillis());

        return result;
    }

    @Around("execution(* com.bdeesorn_r.demo_crud.repository.*Repository.*(..))")
    public Object repositoryLogger(ProceedingJoinPoint pjp) throws Throwable {
        String className = pjp.getTarget().getClass().getSimpleName();
        String methodName = pjp.getSignature().getName();

        String strFormat = "[Repository][%s][%s] >>>>> %s: ";
        
        StopWatch stopWatch = new StopWatch(className + "_" + methodName);
        stopWatch.start();
        logger.info(String.format(strFormat, className, methodName, "Start") + stopWatch.getTotalTimeMillis());

        Object result = pjp.proceed();

        stopWatch.stop();
        logger.info(String.format(strFormat, className, methodName, "End") + stopWatch.getTotalTimeMillis());

        return result;
    }

    @AfterThrowing(pointcut = "execution(* com.bdeesorn_r.demo_crud.**.*(..))", throwing = "exception")
    public void exceptionLogger(JoinPoint jp, Throwable exception) throws Throwable {
        String className = jp.getTarget().getClass().getSimpleName();
        String methodName = jp.getSignature().getName();

        String strFormat = "[Error][%s][%s] >>>>> ";

        logger.info(String.format(strFormat, className, methodName) + exception.getMessage());
    }
}
