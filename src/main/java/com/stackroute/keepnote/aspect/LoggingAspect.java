package com.stackroute.keepnote.aspect;
import java.util.Arrays;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
/* Annotate this class with @Aspect and @Component */
@Component
@Aspect
public class LoggingAspect {
    /*
     * Write loggers for each of the methods of controller, any particular method
     * will have all the four aspectJ annotation
     * (@Before, @After, @AfterReturning, @AfterThrowing).
     */
    
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);
    
     @Pointcut("execution(* com.stackroute.keepnote.controller.*.*(..))")
        protected void loggingOperation() {}
    
    @Before("loggingOperation()")
    public void beforeMethod(JoinPoint point) {
        log.info("before");
        //log.debug("Entering method " + point.getSignature().getName() + "...");
        log.debug("Signature name : " + point.getSignature().getName());
        log.debug("Arguments : " + Arrays.toString(point.getArgs()));
        log.info("done");
    }
    
    @After("loggingOperation()")
    public void afterMethod(JoinPoint point) {
        log.info("after");
        log.debug("Signature name : " + point.getSignature().getName());
        log.debug("Arguments : " + Arrays.toString(point.getArgs()));
    }
    
    @AfterReturning(pointcut="loggingOperation()", returning = "result")
    public void afterRunning(JoinPoint joinPoint, Object result) {
        log.info("returning");
         log.info("Exiting from Method :"+joinPoint.getSignature().getName());
            log.info("Return value :"+result);
    }
    
    @AfterThrowing(pointcut="loggingOperation()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e)
    {
        log.error("An exception has been thrown in "+ joinPoint.getSignature().getName() + "()");
        log.error("Cause :"+e.getCause());
    }
}



