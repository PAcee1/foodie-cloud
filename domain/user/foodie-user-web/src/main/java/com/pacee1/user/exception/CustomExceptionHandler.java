package com.pacee1.user.exception;

import com.pacee1.pojo.ResponseResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * @Created by pace
 * @Date 2020/6/16 10:43
 * @Classname CustomException
 */
@RestControllerAdvice // 增强
public class CustomExceptionHandler {

    //MaxUploadSizeExceededException异常捕获
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseResult handlerMaxUploadFile(MaxUploadSizeExceededException e){
        return ResponseResult.errorMsg("文件大小超过500kb限制");
    }
}
