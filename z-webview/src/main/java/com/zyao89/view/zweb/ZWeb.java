package com.zyao89.view.zweb;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceResponse;

import com.zyao89.view.zweb.constants.InternalConstantName;
import com.zyao89.view.zweb.constants.InternalFunctionName;
import com.zyao89.view.zweb.exceptions.ZWebException;
import com.zyao89.view.zweb.inter.IZWeb;
import com.zyao89.view.zweb.utils.Utils;
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
    @NonNull
    private final UUID mFrameworkUUID;
    @NonNull
    private final IZWebView mZWebView;
    @NonNull
    private final ZWebConfig mZWebConfig;

    public ZWeb (@NonNull Context context, @NonNull ZWebConfig config)
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

    @Override
    public boolean refresh ()
    {
        getZWebView().reload();
        return true;
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
        loadJS(js);
        return true;
    }

    private void loadJS (final String js)
    {
        if (TextUtils.isEmpty(js))
        {
            return;
        }
        if (getView() != null)
        {
            getView().post(new Runnable()
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
        ZLog.with(this).z(js);
    }

    @Override
    public void onReceivedTitle (String title)
    {
        this.getZWebConfig().getZWebOnStateListener().onZWebReceivedTitle(title);
    }

    @Override
    public void onPageStart (String url)
    {

    }

    @Override
    public void onPageFinish (String url, boolean canGoBack, boolean canGoForward)
    {
        injectBridgeJS();
        initFramework();
    }

    @Override
    public WebResourceResponse shouldInterceptRequest (String url)
    {
        return this.getZWebConfig().getZWebOnStateListener().onInterceptRequest(url);
    }

    @Override
    public void onError (String type, Object message)
    {
        ZLog.with(this).w("onError: type = " + type + ", message = " + message);
    }

    /**
     * 加载完成后注入JS
     */
    private void injectBridgeJS ()
    {
        String[] injectJS = getZWebConfig().getInjectJS();
        if (injectJS != null)
        {
            for (String path : injectJS)
            {
                ZLog.with(this).z("injectBridgeJS: path = " + path);
                String jsContent = Utils.assetFile2Str(getView().getContext(), path);
                ZLog.with(this).z("injectBridgeJS: jsContent = " + jsContent);
                getZWebView().loadUrl("javascript:" + jsContent);
            }
        }
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
            initParams.put(InternalConstantName.INTERNAL_NAME, this.getZWebConfig().getInterName());
            initParams.put(InternalConstantName.EXPOSED_NAME, this.getZWebConfig().getExposedName());
            this.execJS(InternalFunctionName.INIT_FRAMEWORK, initParams);
        }
        catch (JSONException e)
        {
            throw new ZWebException("initFramework is Failed... ", e);
        }
    }

    @NonNull
    private ZWebConfig getZWebConfig ()
    {
        return mZWebConfig;
    }

    @NonNull
    /*package*/ IZWebView getZWebView ()
    {
        return mZWebView;
    }

    /*package*/ View getView ()
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
