package kr.co.kakao.global.lock;

import kr.co.kakao.global.common.message.FailHttpMessage;
import kr.co.kakao.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {
    private final RedissonClient redissonClient;
    private final SpelExpressionParser parser = new SpelExpressionParser();

    @Around("@annotation(lockAnnotation)")
    public Object doWithLock(ProceedingJoinPoint joinPoint, DistributedLock lockAnnotation) throws Throwable {
        String key = parseKey(lockAnnotation.key(), joinPoint);
        RLock lock = redissonClient.getLock(key);

        boolean locked = false;

        try {
            locked = lock.tryLock(
                    lockAnnotation.waitTime(),
                    lockAnnotation.leaseTime(),
                    lockAnnotation.timeUnit());

            if (!locked) throw new BusinessException(FailHttpMessage.REDIS_LOCK_CONFLICT);

            return joinPoint.proceed();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException(FailHttpMessage.REDIS_LOCK_INTERRUPT);
        } finally {
            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private String parseKey(String keyExpression, ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        EvaluationContext context = new StandardEvaluationContext();

        String[] paramNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < paramNames.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }

        Expression expression = parser.parseExpression(keyExpression);
        return expression.getValue(context, String.class);
    }
}
