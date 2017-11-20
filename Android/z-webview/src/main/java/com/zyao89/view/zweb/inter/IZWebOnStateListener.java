package com.zyao89.view.zweb.inter;

import android.support.annotation.NonNull;

import org.json.JSONObject;

/**
 * 状态协议
 *
 * @author Zyao89
 * @date 2017/11/7.
 */
public interface IZWebOnStateListener
{
    /**
     * 框架初始化成功后回调
     *
     * @param zWebHandler
     * @param width 宽
     * @param height 高
     */
    void onZWebCreated(IZWebHandler zWebHandler, int width, int height);

    /**
     * 异常（JS如果有异常会在这里回调）
     *
     * @param zWebHandler
     * @param errorCode 错误码
     * @param message 错误信息
     */
    void onZWebException(IZWebHandler zWebHandler, long errorCode, String message);

    /**
     * 网络请求处理
     *
     * @param zWebHandler
     * @param url         链接
     * @param method      请求方法
     * @param data        数据
     * @param type        返回类型
     * @param controller
     */
    void onZWebRequire(IZWebHandler zWebHandler, String url, String method, String data, String type, IZRequireController controller);

    /**
     * 异步消息请求
     *
     * @param zWebHandler
     * @param cmd 命令名称
     * @param data 数据
     */
    void onZWebMessage(IZWebHandler zWebHandler, String cmd, String data, IZMessageController controller);

    /**
     * JS销毁
     *
     * @param zWebHandler
     */
    void onZWebDestroy(IZWebHandler zWebHandler);

    /**
     * JS日志信息回调，可在这里记录或打印
     *
     * @param zWebHandler
     * @param type        类型
     * @param msg         信息
     */
    void onZWebLog(IZWebHandler zWebHandler, String type, String msg);

    /**
     * 请求结果控制协议
     */
    interface IZRequireController
    {
        /**
         * 请求结果处理
         *
         * @param isSuccess 成功 or 失败
         */
        void result(boolean isSuccess);

        /**
         * 请求结果处理
         *
         * @param isSuccess 成功 or 失败
         * @param data      请求结果数据
         */
        void result(boolean isSuccess, String data);

        /**
         * 请求结果处理
         *
         * @param isSuccess 成功 or 失败
         * @param data      请求结果数据
         */
        void result(boolean isSuccess, @NonNull JSONObject data);
    }

    /**
     * 消息结果控制协议
     */
    interface IZMessageController
    {
        /**
         * 消息结果处理
         *
         * @param isSuccess 成功 or 失败
         */
        void result(boolean isSuccess);

        /**
         * 消息结果处理
         *
         * @param isSuccess 成功 or 失败
         * @param data      消息结果数据
         */
        void result(boolean isSuccess, String data);

        /**
         * 消息结果处理
         *
         * @param isSuccess 成功 or 失败
         * @param data      消息结果数据
         */
        void result(boolean isSuccess, @NonNull JSONObject data);

        /**
         * 自动解析为对象中相应方法
         *
         * @param object 对象
         * @param <T>
         */
        <T> void parseMessage(@NonNull T object);
    }
}
