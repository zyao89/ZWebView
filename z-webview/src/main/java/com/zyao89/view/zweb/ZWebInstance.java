package com.zyao89.view.zweb;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.zyao89.view.zweb.exceptions.ZWebException;
import com.zyao89.view.zweb.inter.IZActivityStateListener;
import com.zyao89.view.zweb.inter.IZMethodInterface;
import com.zyao89.view.zweb.inter.IZWebHandler;
import com.zyao89.view.zweb.javascript.ZJavaScriptEx;
import com.zyao89.view.zweb.services.ServiceFactory;
import com.zyao89.view.zweb.views.IZWebView;

/**
 * @author Zyao89
 * @date 2017/11/13.
 */
public class ZWebInstance implements IZActivityStateListener
{
    private static Application    sApplication;
    private final  ZWebConfig     mZWebConfig;
    private final  ZWebHandler    mZWebHandler;
    private final  ServiceFactory mServiceFactory;

    private ZWeb          mZWeb;
    private IZWebView     mZWebView;
    private ZJavaScriptEx mZJavaScript;

    private ZWebInstance(ZWebConfig config)
    {
        mZWebConfig = config;
        mZWebHandler = new ZWebHandler();
        mServiceFactory = new ServiceFactory(mZWebHandler);
    }

    public static void init(@NonNull Application application)
    {
        sApplication = application;
    }

    public IZWebHandler getZWebHandler()
    {
        return mZWebHandler;
    }

    public ZWebConfig getZConfig()
    {
        return mZWebConfig;
    }

    public static ZWebInstance createInstance()
    {
        return createInstance(new ZWebConfig.Builder().build());
    }

    public static ZWebInstance createInstance(ZWebConfig config)
    {
        if (sApplication == null)
        {
            throw new ZWebException("Not Run init...");
        }
        return new ZWebInstance(config);
    }

    private void initWebView(ViewGroup rootView)
    {
        if (mZWeb == null)
        {
            mZWeb = new ZWeb(sApplication, getZConfig());
        }
        mZWebHandler.setZWeb(mZWeb);
        mZWebView = mZWeb.getZWebView();
        mZWebView.setShowLoading(getZConfig().isShowLoading());
        rootView.addView(mZWeb.getWebView(), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public final void addJavascriptInterface(@NonNull Object interfaceObj)
    {
        if (mZWebView == null)
        {
            throw new ZWebException("First call onActivityCreate(), Please...");
        }
        mZWebView.addJavascriptInterface(interfaceObj, getZConfig().getExposedName());
    }

    public final void setOnMethodImplement(@NonNull IZMethodInterface interfaceObj)
    {
        if (mZJavaScript == null)
        {
            throw new ZWebException("First call onActivityCreate(), Please...");
        }
        mZJavaScript.setOnMethodImplement(interfaceObj);
    }

    public <T> T create(final Class<T> service)
    {
        return mServiceFactory.create(service);
    }

    @Override
    public void onActivityCreate(@Nullable ViewGroup rootView, String mainHtml)
    {
        initWebView(rootView);
        initMainHtml(mainHtml);
    }

    private void initMainHtml(String mainHtml)
    {
        mZJavaScript = new ZJavaScriptEx(mZWebHandler);
        mZWebView.addJavascriptInterface(mZJavaScript, getZConfig().getInterName());
        mZWebView.loadUrl(mainHtml);
    }

    @Override
    public void onActivityStart()
    {
        if (mZWeb != null)
        {
            mZWeb.onActivityStart();
        }
    }

    @Override
    public void onActivityPause()
    {
        if (mZWeb != null)
        {
            mZWeb.onActivityPause();
        }
    }

    @Override
    public void onActivityResume()
    {
        if (mZWeb != null)
        {
            mZWeb.onActivityResume();
        }
    }

    @Override
    public void onActivityStop()
    {
        if (mZWeb != null)
        {
            mZWeb.onActivityStop();
        }
    }

    @Override
    public void onActivityDestroy()
    {
        if (mZWeb != null)
        {
            mZWeb.onActivityDestroy();
        }
    }

    @Override
    public boolean onActivityBack()
    {
        if (mZWeb != null)
        {
            return mZWeb.onActivityBack();
        }
        return false;
    }
}
