package com.platon.browser.aop;

import com.platon.browser.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 定时任务切面---添加链路id
 *
 * @date 2021/4/27
 */
@Slf4j
@Component
@Aspect
public class TaskAspect {

    /**
     * 需要打印信息的task类---有些定时任务执行太快，不适合输出日志，有需要的自行添加即可
     */
    private static final Set<String> taskLog = new HashSet<String>() {{
        add("com.platon.browser.task.NetworkStatUpdateTask");
        add("com.platon.browser.task.AddressUpdateTask");
        add("com.platon.browser.task.ErcTokenUpdateTask");
        add("com.platon.browser.task.NodeOptTask");
        add("com.platon.browser.task.NodeUpdateTask");
        add("com.platon.browser.task.UpdateTokenQtyTask");
    }};

    @Pointcut(value = "@annotation(com.xxl.job.core.handler.annotation.XxlJob)")
    public void access() {

    }

    @Before("access()")
    public void doBefore(JoinPoint joinPoint) {
        CommonUtil.putTraceId();
        Signature signature = joinPoint.getSignature();
        String className = signature.getDeclaringTypeName();
        String methodName = signature.getName();
        if (taskLog.contains(className)) {
            log.info("定时任务:类名[{}]---方法[{}]开始...", className, methodName);
        }
    }

    @After("access()")
    public void after(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        String className = signature.getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        if (taskLog.contains(className)) {
            log.info("定时任务:类名[{}]---方法[{}]结束...", className, methodName);
        }
        CommonUtil.removeTraceId();
    }

}
