package kr.pe.karsei.concurrency.config;

import kr.pe.karsei.concurrency.util.CustomSpringELParser;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {
    private static final String REDISSON_KEY_PREFIX = "RLOCK_";
    private final RedissonClient redissonClient;
    private final DistributedLockTransaction lockTransaction;

    @Around("@annotation(kr.pe.karsei.concurrency.config.DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        if (distributedLock.category().equals(DistributedLockCategory.REDISSON)) {
            String key = REDISSON_KEY_PREFIX + CustomSpringELParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key());

            RLock rLock = redissonClient.getLock(key);

            try {
                boolean available = rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());
                if (!available) return false;

                return lockTransaction.proceed(joinPoint);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
                throw new InterruptedException();
            } finally {
                rLock.unlock();
            }
        }

        throw new InterruptedException("지원하지 않는 Lock 사용 방식입니다.");
    }
}
