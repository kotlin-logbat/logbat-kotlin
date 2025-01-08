package info.logbat.dev.aop

import info.logbat.dev.service.CountTestService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component

@Aspect
@Component
class CountTestAspect (
    private val countTestService: CountTestService
){
    @Pointcut("@annotation(info.logbat.dev.aop.CountTest)")
    fun countTestPointcut(){}

    @Around("countTestPointcut()")
    fun countTest(joinPoint: ProceedingJoinPoint): Any{
        var proceed = joinPoint.proceed();
        countTestService.increaseSuccessCount().runCatching {
            countTestService.increaseErrorCount()
        };
        return proceed;
    }
}