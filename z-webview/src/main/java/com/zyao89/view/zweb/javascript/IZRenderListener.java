package com.zyao89.view.zweb.javascript;

import android.webkit.JavascriptInterface;

/**
 * @author Zyao89
 * @date 2017/11/6.
 */
public interface IZRenderListener
{
    @JavascriptInterface
    void onZWebCreated(String frameworkID, String size);

    @JavascriptInterface
    void onZWebException(String frameworkID, long errCode, String msg);

    @JavascriptInterface
    void onZWebRequire(String frameworkID, String oData);

    @JavascriptInterface
    void onZWebMessage (String frameworkID, String oJson);

    @JavascriptInterface
    void onZWebDestroy(String frameworkID);

    @JavascriptInterface
    void onZWebLog (String frameworkID, String msg);
}
