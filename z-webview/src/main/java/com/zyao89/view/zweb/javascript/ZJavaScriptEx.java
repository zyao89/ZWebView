package com.zyao89.view.zweb.javascript;

import android.support.annotation.NonNull;
import android.webkit.JavascriptInterface;

import com.zyao89.view.zweb.constants.InternalConstantName;
import com.zyao89.view.zweb.inter.IZWebHandler;
import com.zyao89.view.zweb.utils.Utils;

import org.json.JSONObject;

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
    public void saveData (String frameworkID, String oData)
    {
        JSONObject jsonObject = Utils.json2Obj(oData);
        String key = jsonObject.optString(InternalConstantName.PARAM_KEY);
        String value = jsonObject.optString(InternalConstantName.PARAM_VALUE);

        getZWebMethodInterface().saveData(getZWebHandler(), key, value);
    }

    @Override
    @JavascriptInterface
    public void loadData (String frameworkID, String key)
    {
        getZWebMethodInterface().loadData(getZWebHandler(), key);
    }

    @Override
    @JavascriptInterface
    public void showLoading(String frameworkID)
    {
        getZWebMethodInterface().showLoading(getZWebHandler());
    }

    @Override
    @JavascriptInterface
    public void hideLoading(String frameworkID)
    {
        getZWebMethodInterface().hideLoading(getZWebHandler());
    }

    @Override
    @JavascriptInterface
    public void tip(String frameworkID, String msg)
    {
        getZWebMethodInterface().tip(getZWebHandler(), msg);
    }
}
