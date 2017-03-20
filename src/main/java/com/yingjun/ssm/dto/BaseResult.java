package com.yingjun.ssm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * 
 * @author yingjun
 *
 * ajax 请求的返回类型封装JSON结果
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResult<T> implements Serializable {


	private static final long serialVersionUID = -4185151304730685014L;

    /**
     * 表示是否成功
     */
	private boolean success;

    /**
     * 输出的数据
     */
    private T data;

    /**
     *失败信息
     */
    private String error;

    /**
     * 构造,失败情况下的输出
     * @param success
     * @param error
     */
    public BaseResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    /**
     * 构造，成功情况下的输出
     * @param success
     * @param data
     */
    public BaseResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "success=" + success +
                ", data=" + data +
                ", error='" + error + '\'' +
                '}';
    }
}
