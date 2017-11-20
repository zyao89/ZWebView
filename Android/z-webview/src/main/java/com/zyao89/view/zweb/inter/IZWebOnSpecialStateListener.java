package com.zyao89.view.zweb.inter;

import android.support.annotation.WorkerThread;
import android.webkit.WebResourceResponse;

/**
 * 特殊状态监听
 *
 * @author Zyao89
 * 2017/11/7.
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
    @WorkerThread
    WebResourceResponse onInterceptRequest(IZWebHandler zWebHandler, String url);
}
