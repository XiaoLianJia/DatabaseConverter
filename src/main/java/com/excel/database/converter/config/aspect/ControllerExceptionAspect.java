package com.excel.database.converter.config.aspect;

import com.excel.database.converter.enums.HttpResponseCodeEnum;
import com.excel.database.converter.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *     控制层异常处理切面
 * </p>
 * @author zhangbin
 * @date 2020-07-15
 */
@RestControllerAdvice
@Slf4j
public class ControllerExceptionAspect {

    @Resource
    private HttpServletRequest httpServletRequest;

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public HttpResponse<Object> exceptionHandler(@NotNull Exception e) {
        log.error("{}接口异常。", httpServletRequest.getRequestURI());
        log.error(e.getMessage(), e);
        return HttpResponse.error(HttpResponseCodeEnum.ERROR_SYSTEM.getCode(),
                String.format("%s：%s", HttpResponseCodeEnum.ERROR_SYSTEM.getMsg(), e.getMessage()));
    }
}
