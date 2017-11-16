package com.zyao89.view.zweb.inter;

import android.webkit.WebResourceResponse;

/**
 * 特殊状态监听
 *
 * @author Zyao89
 * @date 2017/11/7.
 */
public interface IZWebOnSpecialStateListener
{
    /**
     * title changed
     *
     * @param title
     */
    void onZWebReceivedTitle(IZWebHandler zWebHandler, String title);

    /**
     * 内部请求拦截处理
     *
     * @param url
     * @return
     */
    WebResourceResponse onInterceptRequest(IZWebHandler zWebHandler, String url);
}
