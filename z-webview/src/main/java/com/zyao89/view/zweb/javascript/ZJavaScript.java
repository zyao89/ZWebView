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

import java.lang.reflect.Method;

/**
 * 基本暴露接口
 *
 * @author Zyao89
 * @date 2017/11/6.
 */
/* package */ class ZJavaScript implements IZRenderListener
{
    @NonNull
    private final IZWebHandler      mZWebHandler;
    @NonNull
    private final IZMethodInterface mDefaultZMethodInterface;
    private       IZMethodInterface mZMethodInterface;

    /* package */ ZJavaScript(@NonNull IZWebHandler zWeb)
    {
        mZWebHandler = zWeb;
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
    protected final IZWebHandler getZWebHandler()
    {
        return mZWebHandler;
    }

    protected final String getFrameworkID()
    {
        return getZWebHandler().getFrameworkUUID();
    }

    protected final boolean execJS(String function, JSONObject json)
    {
        return mZWebHandler.execJS(function, json);
    }

    @Override
    @JavascriptInterface
    public void onZWebCreated(String frameworkID, String size)
    {
        JSONObject jsonObject = Utils.json2Obj(size);
        String width = jsonObject.optString("width");
        String height = jsonObject.optString("height");

        getZMethodInterface().onZWebCreated(getZWebHandler(), Integer.parseInt(width, 10), Integer.parseInt(height, 10));
    }

    @Override
    @JavascriptInterface
    public void onZWebException(String frameworkID, long errCode, String msg)
    {
        ZLog.with(this).d("zzzzz  onZWebException errCode： " + errCode + "， msg： " + msg);

        getZMethodInterface().onZWebException(getZWebHandler(), errCode, msg);
    }

    @Override
    @JavascriptInterface
    public void onZWebRequire(String frameworkID, String oJson)
    {
        ZLog.with(this).d("zzzzz  onZWebRequire： " + frameworkID + "， oJson：" + oJson);

        JSONObject jsonObject = Utils.json2Obj(oJson);
        final String sequence = jsonObject.optString(InternalConstantName.SEQUENCE);
        final String url = jsonObject.optString(InternalConstantName.URL);
        final String method = jsonObject.optString(InternalConstantName.METHOD);
        final String data = jsonObject.optString(InternalConstantName.DATA);
        final String type = jsonObject.optString(InternalConstantName.TYPE);

        final ZRequireController zController = new ZRequireController(InternalFunctionName.REQUIRE_CALLBACK, sequence);
        getZMethodInterface().onZWebRequire(getZWebHandler(), url, method, data, type, zController);
    }

    @Override
    @JavascriptInterface
    public void onZWebMessage(String frameworkID, String oJson)
    {
        ZLog.with(this).d("zzzzz  postMessage： " + frameworkID + "， oJson：" + oJson);

        JSONObject jsonObject = Utils.json2Obj(oJson);
        final String sequence = jsonObject.optString(InternalConstantName.SEQUENCE);
        final String cmd = jsonObject.optString(InternalConstantName.CMD);
        final String data = jsonObject.optString(InternalConstantName.DATA);

        final ZMessageController zController = new ZMessageController(InternalFunctionName.MESSAGE_CALLBACK, sequence, cmd, data);
        getZMethodInterface().onZWebMessage(getZWebHandler(), cmd, data, zController);
    }

    @Override
    @JavascriptInterface
    public void onZWebDestroy(String frameworkID)
    {
        ZLog.with(this).d("zzzzz  onZWebDestroy： " + frameworkID);

        getZMethodInterface().onZWebDestroy(getZWebHandler());
    }

    class ZRequireController implements IZMethodInterface.IZRequireController
    {
        final String mFunctionName;
        final String mSequence;

        ZRequireController(String function, String sequence)
        {
            this.mFunctionName = function;
            this.mSequence = sequence;
        }

        @Override
        public void result(boolean isSuccess)
        {
            JSONObject json = convert(this.mSequence, isSuccess, null);
            ZJavaScript.this.execJS(this.mFunctionName, json);
        }

        @Override
        public void result(boolean isSuccess, String data)
        {
            JSONObject json = convert(this.mSequence, isSuccess, data);
            ZJavaScript.this.execJS(this.mFunctionName, json);
        }

        @Override
        public void result(boolean isSuccess, @NonNull JSONObject data)
        {
            JSONObject json = convert(this.mSequence, isSuccess, data.toString());
            ZJavaScript.this.execJS(this.mFunctionName, json);
        }

        private JSONObject convert(@NonNull String sequence, boolean isSuccess, @Nullable String data)
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
                throw new ZWebException(this.mFunctionName + " --> ZRequireController convert is Failed... ", e);
            }
        }
    }

    class ZMessageController extends ZRequireController implements IZMethodInterface.IZMessageController
    {

        private final String mCmd;
        private final String mData;

        ZMessageController(String function, String sequence, String cmd, String data)
        {
            super(function, sequence);
            this.mCmd = cmd;
            this.mData = data;
        }

        @Override
        public <T> void parseMessage(@NonNull T object)
        {
            //获取方法
            Method method = null;
            try
            {
                method = object.getClass().getDeclaredMethod(this.mCmd, String.class);
                if (method.getDeclaringClass() == Object.class)
                {
                    throw new ZWebException("Not support CMD...");
                }
                method.setAccessible(true);
                //调用方法
                Object result = method.invoke(object, this.mData);
                if (result == null || result instanceof Void)
                {
                    this.result(true);
                }
                else if (result instanceof String)
                {
                    this.result(true, (String) result);
                }
                else if (result instanceof JSONObject)
                {
                    this.result(true, (JSONObject) result);
                }
                else
                {
                    throw new ZWebException("Not support return type...");
                }
            }
            catch (NoSuchMethodException e)
            {
                try
                {
                    method = object.getClass().getDeclaredMethod(this.mCmd, String.class, IZMethodInterface.IZMessageController.class);
                    if (method.getDeclaringClass() == Object.class)
                    {
                        throw new ZWebException("Not support CMD...");
                    }
                    method.setAccessible(true);
                    //调用方法
                    method.invoke(object, this.mData, this);
                }
                catch (Exception e1)
                {
                    e1.printStackTrace();
                    this.result(false, e1.getMessage());
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                this.result(false, e.getMessage());
            }
        }
    }
}
