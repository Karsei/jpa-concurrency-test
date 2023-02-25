package kr.pe.karsei.concurrency.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DistributedLock {
    /**
     * Lock 이름
     */
    String key();

    /**
     * 시간 단위
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * Lock 획득하기 위한 대기 시간
     */
    long waitTime() default 5L;

    /**
     * Lock 획득 시간
     */
    long leaseTime() default 3L;
}
