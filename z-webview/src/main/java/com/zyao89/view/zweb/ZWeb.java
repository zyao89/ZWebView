package com.zyao89.view.zweb;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.zyao89.view.zweb.constants.InternalConstantName;
import com.zyao89.view.zweb.constants.InternalFunctionName;
import com.zyao89.view.zweb.exceptions.ZWebException;
import com.zyao89.view.zweb.inter.IZWeb;
import com.zyao89.view.zweb.utils.ZLog;
import com.zyao89.view.zweb.views.IZWebView;
import com.zyao89.view.zweb.views.ZWebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * @author Zyao89
 * @date 2017/11/13.
 */
public class ZWeb implements IZWeb, IZWebView.OnPageListener, IZWebView.OnErrorListener
{
    private final UUID mFrameworkUUID;
    private final IZWebView mZWebView;
    private final ZWebConfig mZWebConfig;

    public ZWeb (Context context, ZWebConfig config)
    {
        mFrameworkUUID = UUID.randomUUID();
        mZWebConfig = config;

        mZWebView = new ZWebView(context);

        mZWebView.setOnPageListener(this);
        mZWebView.setOnErrorListener(this);
    }

    @Override
    public String getFrameworkUUID ()
    {
        return mFrameworkUUID.toString();
    }

    @Override
    public boolean callJS (String js)
    {
        loadJS(js);
        return true;
    }

    @Override
    public boolean execJS (String function, JSONObject json)
    {
        return execJS(function, null, json);
    }

    @Override
    public boolean callReceiver (String method, JSONObject json)
    {
        return execJS(InternalFunctionName.CALL_RECEIVER, method, json);
    }

    private boolean execJS (String function, String method, JSONObject json)
    {
        final String js;
        if (method == null && json == null)
        {
            js = String.format("javascript:%s.%s(\"%s\");", InternalFunctionName.MAIN_CALL_OBJ, function, String.valueOf(getFrameworkUUID()));
        }
        else if (json == null)
        {
            js = String.format("javascript:%s.%s(\"%s\",\"%s\");", InternalFunctionName.MAIN_CALL_OBJ, function, String.valueOf(getFrameworkUUID()), method);
        }
        else if (method == null)
        {
            js = String.format("javascript:%s.%s(\"%s\",%s);", InternalFunctionName.MAIN_CALL_OBJ, function, String.valueOf(getFrameworkUUID()), json);
        }
        else
        {
            js = String.format("javascript:%s.%s(\"%s\",\"%s\",%s);", InternalFunctionName.MAIN_CALL_OBJ, function, String.valueOf(getFrameworkUUID()), method, json);
        }
        ZLog.with(this).d(js);
        loadJS(js);
        return true;
    }

    private void loadJS (final String js)
    {
        if (TextUtils.isEmpty(js))
        {
            return;
        }
        if (getWebView() != null)
        {
            getWebView().post(new Runnable()
            {
                @Override
                public void run ()
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    {
                        mZWebView.evaluateJavascript(js);
                    }
                    else
                    {
                        mZWebView.loadUrl(js);
                    }
                }
            });
        }
    }

    @Override
    public void onReceivedTitle (String title)
    {

    }

    @Override
    public void onPageStart (String url)
    {

    }

    @Override
    public void onPageFinish (String url, boolean canGoBack, boolean canGoForward)
    {
        initFramework();
    }

    @Override
    public void onError (String type, Object message)
    {
        ZLog.with(this).w("onError: type = " + type + ", message = " + message);
    }

    /**
     * 初始化JS框架
     */
    private void initFramework ()
    {
        try
        {
            JSONObject initParams = new JSONObject();
            initParams.put(InternalConstantName.OS, InternalConstantName.ANDROID);
            initParams.put(InternalConstantName.VERSION, Build.VERSION.SDK_INT);
            initParams.put(InternalConstantName.INTERNAL_NAME, mZWebConfig.getInterName());
            initParams.put(InternalConstantName.EXPOSED_NAME, mZWebConfig.getExposedName());
            this.execJS(InternalFunctionName.INIT_FRAMEWORK, initParams);
        }
        catch (JSONException e)
        {
            throw new ZWebException("initFramework is Failed... ", e);
        }
    }

    /*package*/ IZWebView getZWebView ()
    {
        return mZWebView;
    }

    /*package*/ View getWebView ()
    {
        return mZWebView.getView();
    }

    /*package*/ void onActivityStart ()
    {

    }

    /*package*/ void onActivityPause ()
    {
        mZWebView.onPause();
    }

    /*package*/ void onActivityResume ()
    {
        mZWebView.onResume();
    }

    /*package*/ void onActivityStop ()
    {

    }

    /*package*/ void onActivityDestroy ()
    {
        mZWebView.destroy();
    }

    /*package*/ boolean onActivityBack ()
    {
        if (mZWebView.canGoBack())
        {
            mZWebView.goBack();
            return true;
        }
        return false;
    }
}
