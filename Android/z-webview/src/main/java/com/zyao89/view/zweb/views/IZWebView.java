package com.zyao89.view.zweb.views;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.webkit.WebResourceResponse;

/**
 * @author Zyao89
 * 2017/11/13.
 */

public interface IZWebView
{
    View getView();

    void onPause();

    void onResume();

    void destroy();

    void loadUrl(String url);

    @TargetApi(Build.VERSION_CODES.KITKAT)
    void evaluateJavascript(String script);

    void reload();

    boolean canGoBack();

    void goBack();

    void goForward();

    void addJavascriptInterface(Object object, String name);

    void setOnErrorListener(OnErrorListener listener);

    void setOnPageListener(OnPageListener listener);

    interface OnErrorListener
    {
        void onError(String type, Object message);
    }

    interface OnPageListener
    {
        void onReceivedTitle(String title);

        void onPageStart(String url);

        void onPageFinish(String url, boolean canGoBack, boolean canGoForward);

        void onProgressChanged(int newProgress);

        WebResourceResponse shouldInterceptRequest(String url);
    }
}
