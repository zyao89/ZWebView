package com.zyao89.view.zweb;

import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebResourceResponse;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.zyao89.view.zweb.constants.InternalConstantName;
import com.zyao89.view.zweb.constants.InternalFunctionName;
import com.zyao89.view.zweb.exceptions.ZWebException;
import com.zyao89.view.zweb.inter.IZWeb;
import com.zyao89.view.zweb.utils.JsUtils;
import com.zyao89.view.zweb.utils.ZLog;
import com.zyao89.view.zweb.views.IZWebView;
import com.zyao89.view.zweb.views.ZWebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
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
    private final ZWebHandler mZWebHandler;
    @NonNull
    private final ZWebConfig mZWebConfig;

    private ViewGroup mRootView;
    private ProgressBar mProgressBar;

    public ZWeb (@NonNull ZWebHandler webHandler)
    {
        mFrameworkUUID = UUID.randomUUID();
        mZWebHandler = webHandler;
        mZWebConfig = webHandler.getZWebConfig();

        final ZWebView zWebView = new ZWebView(ZWebInstance.sApplication);
        zWebView.setConfig(mZWebConfig);
        mZWebView = zWebView;

        mZWebView.setOnPageListener(this);
        mZWebView.setOnErrorListener(this);

        webHandler.setZWeb(this);
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
        this.getZWebConfig().getZWebOnSpecialStateListener().onZWebReceivedTitle(mZWebHandler, title);
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
    public void onProgressChanged (int newProgress)
    {
        showProgressBar(newProgress != 100);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest (String url)
    {
        return this.getZWebConfig().getZWebOnSpecialStateListener().onInterceptRequest(mZWebHandler, url);
    }

    private void showProgressBar (boolean show)
    {
        if (!getZWebConfig().isShowLoading())
        {
            return;
        }
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
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
        // 注入JS信息
        final List<ZWebConfig.JSFile> injectJSs = getZWebConfig().getInjectJSFiles();
        for (ZWebConfig.JSFile jsFile : injectJSs)
        {
            String jsContent = null;
            switch (jsFile.getType())
            {
                case ZWebConfig.JSFile.RAW:
                    int rawID = ((ZWebConfig.JSRawFile) jsFile).getRawID();
                    ZLog.with(this).z("injectBridgeJS: rawID = " + rawID);
                    jsContent = JsUtils.rawFile2Str(getView().getContext(), rawID);
                    break;
                case ZWebConfig.JSFile.ASSETS:
                    String path = ((ZWebConfig.JSAssetsFile) jsFile).getPath();
                    ZLog.with(this).z("injectBridgeJS: path = " + path);
                    jsContent = JsUtils.assetFile2Str(getView().getContext(), path);
                    break;
                default:
                    break;
            }
            if (jsContent != null)
            {
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

    /*package*/ ViewGroup getView ()
    {
        if (mRootView == null)
        {
            mRootView = new FrameLayout(ZWebInstance.sApplication);
            mRootView.addView(mZWebView.getView());
            initProgressBar(mRootView);
        }
        return mRootView;
    }

    private void initProgressBar (ViewGroup rootView)
    {
        if (mProgressBar == null)
        {
            mProgressBar = new ProgressBar(ZWebInstance.sApplication);
            mProgressBar.setVisibility(View.GONE);
            FrameLayout.LayoutParams pLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            mProgressBar.setLayoutParams(pLayoutParams);
            pLayoutParams.gravity = Gravity.CENTER;
            rootView.addView(mProgressBar);
        }
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

        if (mRootView != null)
        {
            mRootView.removeAllViews();
            mRootView.destroyDrawingCache();

            ViewParent parent = mRootView.getParent();
            if (parent != null && parent instanceof ViewGroup)
            {
                ((ViewGroup) parent).removeAllViewsInLayout();
            }

            mRootView = null;
        }
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
