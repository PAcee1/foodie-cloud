package com.pacee1.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author pace
 * @version v1.0
 * @Type ResponseResult.java
 * @Desc 自定义响应数据结构
 *      本类可提供给 H5/ios/安卓/公众号/小程序 使用
 *   	前端接受此类数据（json object)后，可自行根据业务去实现相关功能
 *
 *  	200：表示成功
 *  	500：表示错误，错误信息在msg字段中
 *  	501：bean验证错误，不管多少个错误都以map形式返回
 *  	502：拦截器拦截到用户token出错
 *  	555：异常抛出信息
 *  	556: 用户qq校验异常
 * @date 2020/5/17 15:16
 */
public class ResponseResult {

    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private Object data;
    
    @JsonIgnore
    private String ok;	// 不使用

    public static ResponseResult build(Integer status, String msg, Object data) {
        return new ResponseResult(status, msg, data);
    }

    public static ResponseResult build(Integer status, String msg, Object data, String ok) {
        return new ResponseResult(status, msg, data, ok);
    }
    
    public static ResponseResult ok(Object data) {
        return new ResponseResult(data);
    }

    public static ResponseResult ok() {
        return new ResponseResult(null);
    }
    
    public static ResponseResult errorMsg(String msg) {
        return new ResponseResult(500, msg, null);
    }
    
    public static ResponseResult errorMap(Object data) {
        return new ResponseResult(501, "error", data);
    }
    
    public static ResponseResult errorTokenMsg(String msg) {
        return new ResponseResult(502, msg, null);
    }
    
    public static ResponseResult errorException(String msg) {
        return new ResponseResult(555, msg, null);
    }
    
    public static ResponseResult errorUserQQ(String msg) {
        return new ResponseResult(556, msg, null);
    }

    public ResponseResult() {

    }

    public ResponseResult(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
    
    public ResponseResult(Integer status, String msg, Object data, String ok) {
        this.status = status;
        this.msg = msg;
        this.data = data;
        this.ok = ok;
    }

    public ResponseResult(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public Boolean isOK() {
        return this.status == 200;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

	public String getOk() {
		return ok;
	}

	public void setOk(String ok) {
		this.ok = ok;
	}

}
