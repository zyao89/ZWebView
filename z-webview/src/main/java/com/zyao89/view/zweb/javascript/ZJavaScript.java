package com.zyao89.view.zweb.javascript;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.webkit.JavascriptInterface;

import com.zyao89.view.zweb.constants.InternalFunctionName;
import com.zyao89.view.zweb.exceptions.ZWebException;
import com.zyao89.view.zweb.impl.DefaultZMethodInterface;
import com.zyao89.view.zweb.inter.IZMethodInterface;
import com.zyao89.view.zweb.inter.IZWeb;
import com.zyao89.view.zweb.utils.Utils;
import com.zyao89.view.zweb.utils.ZLog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 基本暴露接口
 *
 * @author Zyao89
 * @date 2017/11/6.
 */
/* package */ class ZJavaScript implements IZRenderListener
{
    @NonNull
    private final IZWeb             mZWeb;
    @NonNull
    private final IZMethodInterface mDefaultZMethodInterface;
    private       IZMethodInterface mZMethodInterface;

    /* package */ ZJavaScript(@NonNull IZWeb zWeb)
    {
        mZWeb = zWeb;
        mDefaultZMethodInterface = new DefaultZMethodInterface();
    }

    public void setOnMethodImplement(@NonNull IZMethodInterface interfaceObj)
    {
        mZMethodInterface = interfaceObj;
    }

    @NonNull
    protected final IZMethodInterface getZMethodInterface()
    {
        if (mZMethodInterface == null)
        {
            ZLog.with(this).w("IZMethodInterface未实现，现调用 setOnMethodImplement()...");
            return mDefaultZMethodInterface;
        }
        return mZMethodInterface;
    }

    @NonNull
    public IZWeb getZWeb()
    {
        return mZWeb;
    }

    protected final String getFrameworkID()
    {
        return getZWeb().getFrameworkUUID();
    }

    protected final boolean execJS(String function, JSONObject json)
    {
        return mZWeb.execJS(function, json);
    }

    @Override
    @JavascriptInterface
    public void onZWebCreated(String frameworkID, String size)
    {
        JSONObject jsonObject = Utils.json2Obj(size);
        String width = jsonObject.optString("width");
        String height = jsonObject.optString("height");

        getZMethodInterface().onZWebCreated(getZWeb(), Integer.parseInt(width, 10), Integer.parseInt(height, 10));
    }

    @Override
    @JavascriptInterface
    public void onZWebException(String frameworkID, long errCode, String msg)
    {
        ZLog.with(this).d("zzzzz  onZWebException errCode： " + errCode + "， msg： " + msg);

        getZMethodInterface().onZWebException(getZWeb(), errCode, msg);
    }

    @Override
    @JavascriptInterface
    public void onZWebRequire(String frameworkID, String oJson)
    {
        ZLog.with(this).d("zzzzz  onZWebRequire： " + frameworkID + "， oJson：" + oJson);

        JSONObject jsonObject = Utils.json2Obj(oJson);
        final String sequence = jsonObject.optString("Sequence");
        final String url = jsonObject.optString("Url");
        final String method = jsonObject.optString("ZMethod");
        final String data = jsonObject.optString("Data");
        final String type = jsonObject.optString("Type");

        getZMethodInterface().onZWebRequire(getZWeb(), url, method, data, type, new IZMethodInterface.IZRequireController()
        {
            @Override
            public void result(boolean isSuccess)
            {
                JSONObject json = convert(sequence, isSuccess, null);
                ZJavaScript.this.execJS(InternalFunctionName.REQUIRE_CALLBACK, json);
            }

            @Override
            public void result(boolean isSuccess, String data)
            {
                JSONObject json = convert(sequence, isSuccess, data);
                ZJavaScript.this.execJS(InternalFunctionName.REQUIRE_CALLBACK, json);
            }

            @Override
            public void result(boolean isSuccess, JSONObject data)
            {
                JSONObject json = convert(sequence, isSuccess, data.toString());
                ZJavaScript.this.execJS(InternalFunctionName.REQUIRE_CALLBACK, json);
            }

            private JSONObject convert(@NonNull String sequence, boolean isSuccess, @Nullable String data)
            {
                try
                {
                    JSONObject json = new JSONObject();
                    json.put("Sequence", sequence);
                    json.put("Result", isSuccess ? "success" : "error");
                    if (data != null)
                    {
                        json.put("Data", data);
                    }
                    return json;
                }
                catch (JSONException e)
                {
                    throw new ZWebException("onZWebRequire is Failed... ", e);
                }
            }
        });
    }

    @Override
    @JavascriptInterface
    public void onZWebMessage(String frameworkID, String oJson)
    {
        ZLog.with(this).d("zzzzz  postMessage： " + frameworkID + "， oJson：" + oJson);

        getZMethodInterface().onZWebMessage(getZWeb(), oJson);
    }

    @Override
    @JavascriptInterface
    public void onZWebDestroy(String frameworkID)
    {
        ZLog.with(this).d("zzzzz  onZWebDestroy： " + frameworkID);

        getZMethodInterface().onZWebDestroy(getZWeb());
    }
}
