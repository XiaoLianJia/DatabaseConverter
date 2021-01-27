package com.excel.database.converter.config.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.excel.database.converter.util.DateUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * <p>
 *     控制层API切面
 * </p>
 * @author zhangbin
 * @date 2020-06-09
 */
@Aspect
@Configuration
@Slf4j
public class ControllerApiAspect {

    @Resource
    private HttpServletRequest httpServletRequest;

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerLog() {}

    @Around("controllerLog()")
    public Object around(@NotNull ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取类和方法。
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        Class<?> clazz = method.getDeclaringClass();
        ApiOperation apiOperation = null;
        if (method.isAnnotationPresent(ApiOperation.class)) {
            apiOperation = method.getAnnotation(ApiOperation.class);
        }

        // 获取参数。
        Object[] parameters = joinPoint.getArgs();
        // 生成该请求的日志唯一码。
        String uuid = UUID.randomUUID().toString().toUpperCase();
        // 当前时间。
        long startDatetime = System.currentTimeMillis();

        log.info("==========================开始==========================");
        log.info("UUID={}，请求时间={}。", uuid, DateUtil.format(startDatetime, DateUtil.DEFAULT_FORMAT));
        log.info("UUID={}，请求路径={}。", uuid, httpServletRequest.getRequestURI());
        log.info("UUID={}，代码路径={}.{}。", uuid, clazz.toString(), method.getName());
        if (null != apiOperation) {
            log.info("UUID={}，API=【{}】。", uuid, apiOperation.value());
        }
        log.info("UUID={}，请求参数={}。", uuid, JSONObject.toJSONString(parameters));

        Object o = joinPoint.proceed();
        long endDatetime = System.currentTimeMillis();
        log.info("UUID={}，响应={}。", uuid, JSON.toJSONString(o));
        log.info("UUID={}，结束时间={}。", uuid, DateUtil.format(endDatetime, DateUtil.DEFAULT_FORMAT));
        log.info("UUID={}，执行耗时={}ms。", uuid, (endDatetime - startDatetime));
        log.info("==========================结束==========================");
        return o;
    }
}
