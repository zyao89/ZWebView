package com.zyao89.view.zweb.javascript;

import android.webkit.JavascriptInterface;

/**
 * @author Zyao89
 * @date 2017/11/7.
 */

public interface IZMethodCallback
{
    @JavascriptInterface
    void saveData (String frameworkID, String oJson);

    @JavascriptInterface
    void loadData (String frameworkID, String oJson);

    @JavascriptInterface
    void showLoading(String frameworkID);

    @JavascriptInterface
    void hideLoading(String frameworkID);

    @JavascriptInterface
    void tip(String frameworkID, String msg);
}
