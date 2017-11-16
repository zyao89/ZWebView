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
     * 创建成功回调
     *
     * @param zWebHandler
     * @param width
     * @param height
     */
    void onZWebCreated(IZWebHandler zWebHandler, int width, int height);

    /**
     * 异常
     *
     * @param zWebHandler
     * @param errorCode
     * @param message
     */
    void onZWebException(IZWebHandler zWebHandler, long errorCode, String message);

    /**
     * 请求处理
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
     * @param data
     */
    void onZWebMessage(IZWebHandler zWebHandler, String cmd, String data, IZMessageController controller);

    /**
     * 销毁
     *
     * @param zWebHandler
     */
    void onZWebDestroy(IZWebHandler zWebHandler);

    /**
     * 日志信息
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
