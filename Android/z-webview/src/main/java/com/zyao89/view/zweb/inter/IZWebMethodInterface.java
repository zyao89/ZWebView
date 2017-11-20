package com.zyao89.view.zweb.inter;

import android.support.annotation.NonNull;

import org.json.JSONObject;

/**
 * Native View 协议
 *
 * @author Zyao89
 * 2017/11/7.
 */
public interface IZWebMethodInterface
{
    /**
     * 存数数据
     *
     * @param zWebHandler
     * @param key
     * @param value
     */
    void saveData (IZWebHandler zWebHandler, String key, String value, IZDatabaseController zController);

    /**
     * 从数据库读取数据
     *  @param zWebHandler
     * @param key
     * @param zController
     */
    void loadData (IZWebHandler zWebHandler, String key, IZDatabaseController zController);

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

    /**
     * 数据操作结果控制协议
     */
    interface IZDatabaseController
    {
        /**
         * 数据操作结果处理
         *
         * @param isSuccess 成功 or 失败
         */
        void result (boolean isSuccess);

        /**
         * 数据操作结果处理
         *
         * @param isSuccess 成功 or 失败
         * @param data      操作结果数据
         */
        void result (boolean isSuccess, String data);

        /**
         * 数据操作结果处理
         *
         * @param isSuccess 成功 or 失败
         * @param data      操作结果数据
         */
        void result (boolean isSuccess, @NonNull JSONObject data);
    }
}
