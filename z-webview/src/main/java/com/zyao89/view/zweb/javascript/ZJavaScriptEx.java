package com.zyao89.view.zweb.javascript;

import android.support.annotation.NonNull;
import android.webkit.JavascriptInterface;

import com.zyao89.view.zweb.constants.InternalConstantName;
import com.zyao89.view.zweb.constants.InternalFunctionName;
import com.zyao89.view.zweb.inter.IZWebHandler;
import com.zyao89.view.zweb.inter.IZWebMethodInterface;
import com.zyao89.view.zweb.utils.JsUtils;

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
    public void saveData (String frameworkID, String oJson)
    {
        JSONObject jsonObject = JsUtils.json2Obj(oJson);
        final String sequence = jsonObject.optString(InternalConstantName.SEQUENCE);
        final JSONObject oData = jsonObject.optJSONObject(InternalConstantName.DATA);
        final String key = oData.optString(InternalConstantName.PARAM_KEY);
        final String value = oData.optString(InternalConstantName.PARAM_VALUE);

        final IZWebMethodInterface.IZDatabaseController zController = new ZDatabaseController(InternalFunctionName.DATABASE_CALLBACK, sequence);
        getZWebMethodInterface().saveData(getZWebHandler(), key, value, zController);
    }

    @Override
    @JavascriptInterface
    public void loadData (String frameworkID, String oJson)
    {
        JSONObject jsonObject = JsUtils.json2Obj(oJson);
        final String sequence = jsonObject.optString(InternalConstantName.SEQUENCE);
        final String key = jsonObject.optString(InternalConstantName.DATA);

        final IZWebMethodInterface.IZDatabaseController zController = new ZDatabaseController(InternalFunctionName.DATABASE_CALLBACK, sequence);
        getZWebMethodInterface().loadData(getZWebHandler(), key, zController);
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

    class ZDatabaseController extends ZRequireController implements IZWebMethodInterface.IZDatabaseController
    {
        ZDatabaseController (String function, String sequence)
        {
            super(function, sequence);
        }
    }
}
