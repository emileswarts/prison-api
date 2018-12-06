package net.syscon.elite.aop;

import lombok.extern.slf4j.Slf4j;
import net.syscon.elite.core.Constants;
import net.syscon.util.MdcUtility;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Slf4j
public class LoggingAspect {

    @Autowired
    private Environment env;

    @Pointcut("within(net.syscon.elite.repository..*) || within(net.syscon.elite.service..*) || within(net.syscon.elite.aop..*)")
    public void loggingPointcut() {
        // No code needed
    }

    @AfterThrowing(pointcut = "loggingPointcut()", throwing = "e")
    public void logAfterThrowing(final JoinPoint joinPoint, final Throwable e) {
        if (env.acceptsProfiles(Constants.SPRING_PROFILE_DEVELOPMENT)) {
            log.error("Exception in {}.{}() with cause = \'{}\' and exception = \'{}\'", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), e.getCause() != null? e.getCause() : "NULL", e.getMessage());

            e.printStackTrace();
        } else {
            log.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), e.getCause() != null? e.getCause() : "NULL");
        }
    }

    @Around("loggingPointcut()")
    public Object logAround(final ProceedingJoinPoint joinPoint) throws Throwable {
        LocalDateTime start = LocalDateTime.now();
        if (log.isDebugEnabled() && MdcUtility.isLoggingAllowed()) {
            log.debug(
                    "Enter: {}.{}()",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName());
        }
        try {
            final Object result = joinPoint.proceed();
            if (log.isDebugEnabled() && MdcUtility.isLoggingAllowed()) {
                log.debug(
                        "Exit: {}.{}() - Duration {} ms",
                        joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(),
                        Duration.between(start, LocalDateTime.now()).toMillis());
            }
            return result;
        } catch (final IllegalArgumentException e) {
            log.error(
                    "Illegal argument: {} in {}.{}()",
                    Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName());

            throw e;
        }
    }
}