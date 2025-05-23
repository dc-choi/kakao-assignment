package kr.co.kakao.global.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {
    String key();
    long waitTime() default 5L; // 락 대기 시간 (초)
    long leaseTime() default 3L; // 락 보유 시간 (초)
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
