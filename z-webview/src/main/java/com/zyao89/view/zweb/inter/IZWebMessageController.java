package com.zyao89.view.zweb.inter;

import android.support.annotation.NonNull;

import org.json.JSONObject;

/**
 * 对外开放接口
 *
 * @author Zyao89
 * @date 2017/11/15.
 */
public interface IZWebMessageController
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
}
