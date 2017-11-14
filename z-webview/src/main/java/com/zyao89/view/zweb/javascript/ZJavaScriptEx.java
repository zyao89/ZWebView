package com.zyao89.view.zweb.javascript;

import android.support.annotation.NonNull;
import android.webkit.JavascriptInterface;

import com.zyao89.view.zweb.inter.IZWebHandler;

/**
 * 扩展暴露接口
 *
 * @author Zyao89
 * @date 2017/11/6.
 */
public class ZJavaScriptEx extends ZJavaScript implements IZMethodCallback
{
    public ZJavaScriptEx(@NonNull IZWebHandler web)
    {
        super(web);
    }

    @Override
    @JavascriptInterface
    public void saveData(String frameworkID)
    {
        getZMethodInterface().saveData(getZWebHandler());
    }

    @Override
    @JavascriptInterface
    public void loadData(String frameworkID)
    {
        getZMethodInterface().loadData(getZWebHandler());
    }

    @Override
    @JavascriptInterface
    public void showLoading(String frameworkID)
    {
        getZMethodInterface().showLoading(getZWebHandler());
    }

    @Override
    @JavascriptInterface
    public void hideLoading(String frameworkID)
    {
        getZMethodInterface().hideLoading(getZWebHandler());
    }

    @Override
    @JavascriptInterface
    public void tip(String frameworkID, String msg)
    {
        getZMethodInterface().tip(getZWebHandler(), msg);
    }
}
