package com.zyao89.view.zweb.javascript;

import android.webkit.JavascriptInterface;

/**
 * @author Zyao89
 * @date 2017/11/6.
 */
public interface IZRenderListener
{
    @JavascriptInterface
    void onCreated (String frameworkID, String oSize);

    @JavascriptInterface
    void onException (String frameworkID, long errCode, String oMsg);

    @JavascriptInterface
    void onRequire (String frameworkID, String oData);

    @JavascriptInterface
    void onMessage (String frameworkID, String oJson);

    @JavascriptInterface
    void onDestroy (String frameworkID);

    @JavascriptInterface
    void onLog (String frameworkID, String oData);
}
