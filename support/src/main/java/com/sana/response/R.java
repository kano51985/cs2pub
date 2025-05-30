package com.sana.response;

import cn.hutool.core.util.ObjectUtil;
import com.sana.constants.HttpStatus;
import lombok.Data;

import java.util.HashMap;
import java.util.Objects;

@Data
public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;
    /**
     * 状态码
     */
    public static final String CODE_TAG = "code";
    /**
     * 返回内容
     */
    public static final String MSG_TAG = "msg";
    /**
     * 数据对象
     */
    public static final String DATA_TAG = "data";
    /**
     * 初始化一个新创建的 AjaxResult 对象，使其表示一个空消息。
     */
    public R() {
    }
    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code 状态码
     * @param msg 返回内容
     */
    public R(int code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }
    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code 状态码
     * @param msg 返回内容
     * @param data 数据对象
     */
    public R(int code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (ObjectUtil.isNotNull(data)) {
            super.put(DATA_TAG, data);
        }
    }
    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static R success() {
        return R.success("操作成功");
    }
    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static R success(Object data) {
        return R.success("操作成功", data);
    }
    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static R success(String msg) {
        return R.success(msg, null);
    }
    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static R success(String msg, Object data) {
        return new R(HttpStatus.SUCCESS, msg, data);
    }
    /**
     * 返回警告消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static R warn(String msg) {
        return R.warn(msg, null);
    }
    /**
     * 返回警告消息
     *
     * @param msg 返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static R warn(String msg, Object data) {
        return new R(HttpStatus.WARN, msg, data);
    }
    /**
     * 返回错误消息
     *
     * @return 错误消息
     */
    public static R error() {
        return R.error("操作失败");
    }
    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 错误消息
     */
    public static R error(String msg) {
        return R.error(msg, null);
    }
    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @param data 数据对象
     * @return 错误消息
     */
    public static R error(String msg, Object data) {
        return new R(HttpStatus.ERROR, msg, data);
    }
    /**
     * 返回错误消息
     *
     * @param code 状态码
    ShiTian
    定义业务异常
     * @param msg 返回内容
     * @return 错误消息
     */
    public static R error(int code, String msg) {
        return new R(code, msg, null);
    }
    /**
     * 是否为成功消息
     *
     * @return 结果
     */
    public boolean isSuccess() {
        return Objects.equals(HttpStatus.SUCCESS, this.get(CODE_TAG));
    }
    /**
     * 是否为警告消息
     *
     * @return 结果
     */
    public boolean isWarn() {
        return Objects.equals(HttpStatus.WARN, this.get(CODE_TAG));
    }
    /**
     * 是否为错误消息
     *
     * @return 结果
     */
    public boolean isError() {
        return Objects.equals(HttpStatus.ERROR, this.get(CODE_TAG));
    }
    /**
     * 方便链式调用
     *
     * @param key 键
     * @param value 值
     * @return 数据对象
     */
    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
