package com.pacee1.user.controller.center;

import com.pacee1.user.pojo.Users;
import com.pacee1.user.pojo.bo.center.CenterUserBO;
import com.pacee1.user.pojo.vo.UsersVO;
import com.pacee1.user.service.center.CenterUserService;
import com.pacee1.utils.CookieUtils;
import com.pacee1.utils.JsonUtils;
import com.pacee1.utils.RedisOperator;
import com.pacee1.pojo.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Created by pace
 * @Date 2020/6/15 15:36
 * @Classname CenterController
 */
@RestController
@RequestMapping("userInfo")
@Api(value = "用户相关接口",tags = "用户相关接口")
public class CenterUserController {

    @Value("${pacee1.image.faceLocation}")
    private String IMAGE_USER_FACE_LOCATION;
    @Value("${pacee1.image.serverUrl}")
    private String IMAGE_SERVER_URL;

    @Autowired
    private CenterUserService centerUserService;
    @Autowired
    private RedisOperator redisOperator;

    @PostMapping("/update")
    @ApiOperation(value = "修改用户信息",notes = "修改用户信息接口")
    public ResponseResult update(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId,
            @RequestBody @Valid CenterUserBO centerUserBO,
            BindingResult bindingResult,
            HttpServletRequest request, HttpServletResponse response){
        if(userId == null){
            return ResponseResult.errorMsg("用户不存在");
        }

        // 校验传入的用户信息是否正确，使用Hibernate的Validate
        if(bindingResult.hasErrors()){
            Map<String,Object> errorMap = getErrorMap(bindingResult);
            return ResponseResult.errorMap(errorMap);
        }

        Users users = centerUserService.updateUserInfo(userId,centerUserBO);

        // 清空用户敏感信息
        users = setUserNull(users);

        // 保存Session到Redis
        String userToken = UUID.randomUUID().toString().trim();
        redisOperator.set("redis_user_token:" + users.getId(),userToken);
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(users,usersVO);
        usersVO.setUserUniqueToken(userToken);

        // 设置cookie
        CookieUtils.setCookie(request,response,"user",
                JsonUtils.objectToJson(usersVO),true);

        return ResponseResult.ok();
    }

    @PostMapping("/uploadFace")
    @ApiOperation(value = "修改用户头像",notes = "修改用户头像接口")
    public ResponseResult uploadFace(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId,
            @ApiParam(name = "file",value = "用户头像",required = true)
            MultipartFile file,
            HttpServletRequest request, HttpServletResponse response){
        if(userId == null){
            return ResponseResult.errorMsg("用户不存在");
        }

        // 图片保存本地地址
        String fileSpace = IMAGE_USER_FACE_LOCATION;
        // 在space基础上，以每个用户的id创建文件夹，保存到各个文件夹中
        String uploadFilePrefix = File.separator + userId;
        // 保存文件新名称 face-userId.jpg
        String newFileName = null;

        // 开始文件上传
        if(file != null){
            // 文件名称
            String filename = file.getOriginalFilename();
            if(StringUtils.isNotBlank(filename)){
                // 文件重命名，face-userId.jpg
                String[] strings = filename.split("\\.");
                String suffix = strings[strings.length - 1];
                newFileName = "face-" + userId + "."+ suffix;
                // 拼接最终路径，space+prefix+文件名
                String finalFilePath = fileSpace + uploadFilePrefix + File.separator + newFileName;

                // 判断文件格式是否正确
                if(!suffix.toLowerCase().equals("png") &&
                        !suffix.toLowerCase().equals("jpg") &&
                        !suffix.toLowerCase().equals("jpeg")){
                    return ResponseResult.errorMsg("文件格式不正确");
                }

                // 创建文件并创建文件夹
                File uploadFile = new File(finalFilePath);
                if(uploadFile.getParentFile() != null){
                    uploadFile.getParentFile().mkdirs();
                    FileOutputStream outputStream = null;
                    try {
                        // 拷贝文件
                        outputStream = new FileOutputStream(uploadFile);
                        InputStream inputStream = file.getInputStream();
                        IOUtils.copy(inputStream,outputStream);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        if(outputStream != null){
                            try {
                                outputStream.flush();
                                outputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }else {
            return ResponseResult.errorMap("头像不存在");
        }

        // 更新用户信息到数据库
        // 组装图片网络路径
        String faceUrl = IMAGE_SERVER_URL + userId + File.separator + newFileName;
        Users users = centerUserService.updateUserFace(userId, faceUrl);

        // 清空用户敏感信息
        users = setUserNull(users);

        // 保存Session到Redis
        String userToken = UUID.randomUUID().toString().trim();
        redisOperator.set("redis_user_token:" + users.getId(),userToken);
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(users,usersVO);
        usersVO.setUserUniqueToken(userToken);

        // 设置cookie
        CookieUtils.setCookie(request,response,"user",
                JsonUtils.objectToJson(usersVO),true);

        return ResponseResult.ok();
    }

    /**
     * 获取校验出的错误信息封装
     * @param bindingResult
     * @return
     */
    private Map<String, Object> getErrorMap(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        Map<String,Object> errorMap = new HashMap<>();
        for (FieldError fieldError : fieldErrors) {
            // 错误字段
            String field = fieldError.getField();
            // 错误信息
            String defaultMessage = fieldError.getDefaultMessage();
            errorMap.put(field,defaultMessage);
        }
        return errorMap;
    }

    private Users setUserNull(Users user) {
        user.setPassword(null);
        user.setBirthday(null);
        user.setCreatedTime(null);
        user.setEmail(null);
        user.setUpdatedTime(null);
        return user;
    }
}
