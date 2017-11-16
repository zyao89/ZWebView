package com.zyao89.view.zweb.impl;

import android.webkit.WebResourceResponse;

import com.zyao89.view.zweb.inter.IZWebHandler;
import com.zyao89.view.zweb.inter.IZWebOnSpecialStateListener;
import com.zyao89.view.zweb.utils.ZLog;

/**
 * @author Zyao89
 * @date 2017/11/8.
 */
public class DefaultZWebOnSpecialStateListener implements IZWebOnSpecialStateListener
{
    @Override
    public void onZWebReceivedTitle (IZWebHandler zWebHandler, String title)
    {
        ZLog.with(this).z("onZWebReceivedTitle ==> title：" + title);
    }

    @Override
    public WebResourceResponse onInterceptRequest (IZWebHandler zWebHandler, String url)
    {
        ZLog.with(this).z("onInterceptRequest: " + url);
        return null;
    }
}
