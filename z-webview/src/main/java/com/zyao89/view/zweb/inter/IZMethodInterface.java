package com.zyao89.view.zweb.inter;

import org.json.JSONObject;

/**
 * 协议
 *
 * @author Zyao89
 * @date 2017/11/7.
 */
public interface IZMethodInterface
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
     * @param url
     * @param method
     * @param data
     * @param type
     * @param controller
     */
    void onZWebRequire(IZWebHandler zWebHandler, String url, String method, String data, String type, IZRequireController controller);

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
        void result(boolean isSuccess, JSONObject data);
    }

    /**
     * 异步消息请求
     *
     * @param zWebHandler
     * @param oJson
     */
    void onZWebMessage(IZWebHandler zWebHandler, String cmd, String oJson, IZMessageController controller);

    /**
     * 请求结果控制协议
     */
    interface IZMessageController
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
        void result(boolean isSuccess, JSONObject data);
    }

    /**
     * 销毁
     *
     * @param zWebHandler
     */
    void onZWebDestroy(IZWebHandler zWebHandler);

    /**
     * 存数数据
     *
     * @param zWebHandler
     */
    void saveData(IZWebHandler zWebHandler);

    /**
     * 从数据库读取数据
     *
     * @param zWebHandler
     */
    void loadData(IZWebHandler zWebHandler);

    /**
     * 显示等待框
     *
     * @param zWebHandler
     */
    void showLoading(IZWebHandler zWebHandler);

    /**
     * 隐藏等待框
     *
     * @param zWebHandler
     */
    void hideLoading(IZWebHandler zWebHandler);

    /**
     * 提示
     *
     * @param zWebHandler
     * @param msg
     */
    void tip(IZWebHandler zWebHandler, String msg);
}
