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
     * @param zWeb
     * @param width
     * @param height
     */
    void onZWebCreated(IZWeb zWeb, int width, int height);

    /**
     * 异常
     *
     * @param zWeb
     * @param errorCode
     * @param message
     */
    void onZWebException(IZWeb zWeb, long errorCode, String message);

    /**
     * 请求处理
     *
     * @param zWeb
     * @param url
     * @param method
     * @param data
     * @param type
     * @param controller
     */
    void onZWebRequire(IZWeb zWeb, String url, String method, String data, String type, IZRequireController controller);

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
     * 预留
     *
     * @param zWeb
     * @param oJson
     */
    void onZWebMessage(IZWeb zWeb, String oJson);

    /**
     * 销毁
     *
     * @param zWeb
     */
    void onZWebDestroy(IZWeb zWeb);

    /**
     * 存数数据
     *
     * @param zWeb
     */
    void saveData(IZWeb zWeb);

    /**
     * 从数据库读取数据
     *
     * @param zWeb
     */
    void loadData(IZWeb zWeb);

    /**
     * 显示等待框
     *
     * @param zWeb
     */
    void showLoading(IZWeb zWeb);

    /**
     * 隐藏等待框
     *
     * @param zWeb
     */
    void hideLoading(IZWeb zWeb);

    /**
     * 提示
     *
     * @param zWeb
     * @param msg
     */
    void tip(IZWeb zWeb, String msg);
}
