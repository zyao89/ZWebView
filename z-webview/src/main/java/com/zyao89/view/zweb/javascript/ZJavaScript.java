package com.zyao89.view.zweb.javascript;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.webkit.JavascriptInterface;

import com.zyao89.view.zweb.constants.InternalConstantName;
import com.zyao89.view.zweb.constants.InternalFunctionName;
import com.zyao89.view.zweb.exceptions.ZWebException;
import com.zyao89.view.zweb.impl.DefaultZMethodInterface;
import com.zyao89.view.zweb.inter.IZMethodInterface;
import com.zyao89.view.zweb.inter.IZWebHandler;
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
    private final IZWebHandler mZWebHandler;
    @NonNull
    private final IZMethodInterface mDefaultZMethodInterface;
    private IZMethodInterface mZMethodInterface;

    /* package */ ZJavaScript (@NonNull IZWebHandler zWeb)
    {
        mZWebHandler = zWeb;
        mDefaultZMethodInterface = new DefaultZMethodInterface();
    }

    public void setOnMethodImplement (@NonNull IZMethodInterface interfaceObj)
    {
        mZMethodInterface = interfaceObj;
    }

    @NonNull
    protected final IZMethodInterface getZMethodInterface ()
    {
        if (mZMethodInterface == null)
        {
            ZLog.with(this).w("IZMethodInterface未实现，现调用 setOnMethodImplement()...");
            return mDefaultZMethodInterface;
        }
        return mZMethodInterface;
    }

    @NonNull
    protected final IZWebHandler getZWebHandler ()
    {
        return mZWebHandler;
    }

    protected final String getFrameworkID ()
    {
        return getZWebHandler().getFrameworkUUID();
    }

    protected final boolean execJS (String function, JSONObject json)
    {
        return mZWebHandler.execJS(function, json);
    }

    @Override
    @JavascriptInterface
    public void onZWebCreated (String frameworkID, String size)
    {
        JSONObject jsonObject = Utils.json2Obj(size);
        String width = jsonObject.optString("width");
        String height = jsonObject.optString("height");

        getZMethodInterface().onZWebCreated(getZWebHandler(), Integer.parseInt(width, 10), Integer.parseInt(height, 10));
    }

    @Override
    @JavascriptInterface
    public void onZWebException (String frameworkID, long errCode, String msg)
    {
        ZLog.with(this).d("zzzzz  onZWebException errCode： " + errCode + "， msg： " + msg);

        getZMethodInterface().onZWebException(getZWebHandler(), errCode, msg);
    }

    @Override
    @JavascriptInterface
    public void onZWebRequire (String frameworkID, String oJson)
    {
        ZLog.with(this).d("zzzzz  onZWebRequire： " + frameworkID + "， oJson：" + oJson);

        JSONObject jsonObject = Utils.json2Obj(oJson);
        final String sequence = jsonObject.optString(InternalConstantName.SEQUENCE);
        final String url = jsonObject.optString(InternalConstantName.URL);
        final String method = jsonObject.optString(InternalConstantName.METHOD);
        final String data = jsonObject.optString(InternalConstantName.DATA);
        final String type = jsonObject.optString(InternalConstantName.TYPE);

        getZMethodInterface().onZWebRequire(getZWebHandler(), url, method, data, type, new ZController(InternalFunctionName.REQUIRE_CALLBACK, sequence));
    }

    @Override
    @JavascriptInterface
    public void onZWebMessage (String frameworkID, String oJson)
    {
        ZLog.with(this).d("zzzzz  postMessage： " + frameworkID + "， oJson：" + oJson);

        JSONObject jsonObject = Utils.json2Obj(oJson);
        final String sequence = jsonObject.optString(InternalConstantName.SEQUENCE);
        final String cmd = jsonObject.optString(InternalConstantName.CMD);
        final String data = jsonObject.optString(InternalConstantName.DATA);

        getZMethodInterface().onZWebMessage(getZWebHandler(), cmd, data, new ZController(InternalFunctionName.MESSAGE_CALLBACK, sequence));
    }

    @Override
    @JavascriptInterface
    public void onZWebDestroy (String frameworkID)
    {
        ZLog.with(this).d("zzzzz  onZWebDestroy： " + frameworkID);

        getZMethodInterface().onZWebDestroy(getZWebHandler());
    }

    class ZController implements IZMethodInterface.IZMessageController, IZMethodInterface.IZRequireController
    {
        private final String mFunctionName;
        private final String mSequence;

        public ZController (String function, String sequence)
        {
            mFunctionName = function;
            mSequence = sequence;
        }

        @Override
        public void result (boolean isSuccess)
        {
            JSONObject json = convert(mSequence, isSuccess, null);
            ZJavaScript.this.execJS(mFunctionName, json);
        }

        @Override
        public void result (boolean isSuccess, String data)
        {
            JSONObject json = convert(mSequence, isSuccess, data);
            ZJavaScript.this.execJS(mFunctionName, json);
        }

        @Override
        public void result (boolean isSuccess, JSONObject data)
        {
            JSONObject json = convert(mSequence, isSuccess, data.toString());
            ZJavaScript.this.execJS(mFunctionName, json);
        }

        private JSONObject convert (@NonNull String sequence, boolean isSuccess, @Nullable String data)
        {
            try
            {
                JSONObject json = new JSONObject();
                json.put(InternalConstantName.SEQUENCE, sequence);
                json.put(InternalConstantName.RESULT, isSuccess ? InternalConstantName.SUCCESS : InternalConstantName.ERROR);
                if (data != null)
                {
                    json.put(InternalConstantName.DATA, data);
                }
                return json;
            }
            catch (JSONException e)
            {
                throw new ZWebException("ZController convert is Failed... ", e);
            }
        }
    }
}
