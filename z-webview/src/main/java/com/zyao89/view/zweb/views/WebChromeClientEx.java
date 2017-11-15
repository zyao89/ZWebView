package com.zyao89.view.zweb.views;

import android.support.annotation.NonNull;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.zyao89.view.zweb.utils.JsUtils;
import com.zyao89.view.zweb.utils.ZLog;

/**
 * @author Zyao89
 * @date 2017/11/13.
 */

/* package */ class WebChromeClientEx extends WebChromeClient
{
    private FixedOnReceivedTitle mFixedOnReceivedTitle;

    /* package */ void setFixedOnReceivedTitle(@NonNull FixedOnReceivedTitle fixedOnReceivedTitle)
    {
        mFixedOnReceivedTitle = fixedOnReceivedTitle;
        fixedOnReceivedTitle.setWebChromeClient(this);
    }

    @Override
    public void onProgressChanged (WebView view, int newProgress)
    {
        if (JsUtils.notSupportInterface())
        {
            if (view instanceof WebViewEx)
            {
                ((WebViewEx) view).injectJavascriptInterfaces();
            }
        }
        super.onProgressChanged(view, newProgress);
    }

    @Override
    public void onReceivedTitle(WebView view, String title)
    {
        mFixedOnReceivedTitle.onReceivedTitle();
    }

    @Override
    public boolean onJsPrompt (WebView view, String url, String message, String defaultValue, JsPromptResult result)
    {
        ZLog.with(this).z("onJsPrompt:" + url + "  message:" + message + "  d:" + defaultValue + "  ");
        if (JsUtils.notSupportInterface())
        {
            if (view instanceof WebViewEx)
            {
                if (((WebViewEx) view).handleJsInterface(url, message, defaultValue, result))
                {
                    return true;
                }
            }
        }
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }
}
