package com.zyao89.view.zweb.views;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zyao89.view.zweb.utils.JsUtils;

/**
 * @author Zyao89
 * @date 2017/11/13.
 */

/* package */ class WebViewClientEx extends WebViewClient
{
    private FixedOnReceivedTitle mFixedOnReceivedTitle;

    /* package */ void setFixedOnReceivedTitle(@NonNull FixedOnReceivedTitle fixedOnReceivedTitle)
    {
        mFixedOnReceivedTitle = fixedOnReceivedTitle;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon)
    {
        mFixedOnReceivedTitle.onPageStarted();
        if (JsUtils.notSupportInterface())
        {
            if (view instanceof WebViewEx)
            {
                ((WebViewEx) view).injectJavascriptInterfaces();
                ((WebViewEx) view).fixedAccessibilityInjectorExceptionForOnPageFinished(url);
            }
        }
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url)
    {
        mFixedOnReceivedTitle.onPageFinished(view);
        super.onPageFinished(view, url);
    }
}
