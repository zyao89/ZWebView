package com.zyao89.view.zweb;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebResourceResponse;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.zyao89.view.zweb.constants.InjectionMode;
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

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zyao89
 * @date 2017/11/13.
 */
public class ZWeb implements IZWeb, IZWebView.OnPageListener, IZWebView.OnErrorListener
{
    @NonNull
    private final UUID                mFrameworkUUID;
    @NonNull
    private final IZWebView           mZWebView;
    @NonNull
    private final ZWebHandler         mZWebHandler;
    @NonNull
    private final ZWebConfig          mZWebConfig;
    @NonNull
    private final Map<String, String> mInjectBridgeJSCache;

    private ViewGroup   mRootView;
    private ProgressBar mProgressBar;

    public ZWeb(@NonNull ZWebHandler webHandler)
    {
        mFrameworkUUID = UUID.randomUUID();
        mZWebHandler = webHandler;
        mZWebConfig = webHandler.getZWebConfig();
        mInjectBridgeJSCache = new ConcurrentHashMap<>();

        final ZWebView zWebView = new ZWebView(ZWebInstance.sApplication);
        zWebView.setConfig(mZWebConfig);
        mZWebView = zWebView;

        mZWebView.setOnPageListener(this);
        mZWebView.setOnErrorListener(this);

        webHandler.setZWeb(this);
    }

    @Override
    public String getFrameworkUUID()
    {
        return mFrameworkUUID.toString();
    }

    @Override
    public boolean callJS(String js)
    {
        loadJS(js);
        return true;
    }

    @Override
    public boolean execJS(String function, JSONObject json)
    {
        return execJS(function, null, json);
    }

    @Override
    public boolean callReceiver(String method, JSONObject json)
    {
        return execJS(InternalFunctionName.CALL_RECEIVER, method, json);
    }

    @Override
    public boolean refresh()
    {
        getZWebView().reload();
        return true;
    }

    private boolean execJS(String function, String method, JSONObject json)
    {
        final String js;
        if (method == null && json == null)
        {
            js = String.format("%s.%s(\"%s\");", InternalFunctionName.MAIN_CALL_OBJ, function, String.valueOf(getFrameworkUUID()));
        }
        else if (json == null)
        {
            js = String.format("%s.%s(\"%s\",\"%s\");", InternalFunctionName.MAIN_CALL_OBJ, function, String.valueOf(getFrameworkUUID()), method);
        }
        else if (method == null)
        {
            js = String.format("%s.%s(\"%s\",%s);", InternalFunctionName.MAIN_CALL_OBJ, function, String.valueOf(getFrameworkUUID()), json);
        }
        else
        {
            js = String.format("%s.%s(\"%s\",\"%s\",%s);", InternalFunctionName.MAIN_CALL_OBJ, function, String.valueOf(getFrameworkUUID()), method, json);
        }
        loadJS(js);
        return true;
    }

    private void loadJS(final String js)
    {
        if (TextUtils.isEmpty(js))
        {
            return;
        }
        post(new Runnable()
        {
            @Override
            public void run()
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                {
                    mZWebView.evaluateJavascript("javascript:" + js);
                }
                else
                {
                    mZWebView.loadUrl("javascript:" + js);
                }
            }
        });
        ZLog.with(this).z(js);
    }

    @Override
    public void onReceivedTitle(String title)
    {
        this.getZWebConfig().getZWebOnSpecialStateListener().onZWebReceivedTitle(mZWebHandler, title);
    }

    @Override
    public void onPageStart(String url)
    {

    }

    @Override
    public void onPageFinish(String url, boolean canGoBack, boolean canGoForward)
    {
        if (getZWebConfig().getInjectionMode() == InjectionMode.VuePlugin)
        {
            injectBridgeJS();
            initFramework();
        }
        else if (getZWebConfig().getInjectionMode() == InjectionMode.Normal)
        {
            initFramework();
        }
    }

    @Override
    public void onProgressChanged(int newProgress)
    {
        showProgressBar(newProgress != 100);
    }

    @Override
    @WorkerThread
    public WebResourceResponse shouldInterceptRequest(String url)
    {
        // 这里可以通过协议做注入操作
        // 比如说： zweb://  开头的协议，后面跟上命令
        if (getZWebConfig().getInjectionMode() == InjectionMode.Protocol && url.startsWith(InternalConstantName.PROTOCOL_Z_WEB))
        {
            int startLen = InternalConstantName.PROTOCOL_Z_WEB.length();
            final String protocolName = url.substring(startLen);
            return protocolBridgeJS(protocolName);
        }
        return this.getZWebConfig().getZWebOnSpecialStateListener().onInterceptRequest(mZWebHandler, url);
    }

    private void showProgressBar(boolean show)
    {
        if (!getZWebConfig().isShowLoading())
        {
            return;
        }
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onError(String type, Object message)
    {
        ZLog.with(this).w("onError: type = " + type + ", message = " + message);
    }

    /**
     * 加载JS文件
     *
     * @return js文本
     */
    private String loadBridgeJS()
    {
        final List<ZWebConfig.JSFile> injectJSs = getZWebConfig().getInjectJSFiles();
        StringBuilder jsContent = new StringBuilder();
        for (ZWebConfig.JSFile jsFile : injectJSs)
        {
            String jsStr = null;
            switch (jsFile.getType())
            {
                case ZWebConfig.JSFile.RAW:
                    int rawID = ((ZWebConfig.JSRawFile) jsFile).getRawID();
                    jsStr = mInjectBridgeJSCache.get("RAW_ID#" + String.valueOf(rawID));
                    if (jsStr == null)
                    {
                        ZLog.with(this).z("injectBridgeJS: rawID = " + rawID);
                        jsStr = JsUtils.rawFile2Str(getView().getContext(), rawID);
                        mInjectBridgeJSCache.put("RAW_ID#" + String.valueOf(rawID), jsStr);
                    }
                    break;
                case ZWebConfig.JSFile.ASSETS:
                    String path = ((ZWebConfig.JSAssetsFile) jsFile).getPath();
                    jsStr = mInjectBridgeJSCache.get(path);
                    if (jsStr == null)
                    {
                        ZLog.with(this).z("injectBridgeJS: path = " + path);
                        jsStr = JsUtils.assetFile2Str(getView().getContext(), path);
                        mInjectBridgeJSCache.put(path, jsStr);
                    }
                    break;
                default:
                    break;
            }
            if (jsStr != null)
            {
                jsContent.append(jsStr);
            }
        }
        return jsContent.toString();
    }

    /**
     * 加载完成后注入JS
     */
    private void injectBridgeJS()
    {
        // 注入JS信息
        final String jsContent = loadBridgeJS();
        if (!TextUtils.isEmpty(jsContent))
        {
            ZLog.with(this).z("injectBridgeJS: jsContent = " + jsContent);
            getZWebView().loadUrl("javascript:" + jsContent);
        }
    }

    /**
     * 通过协议注入
     */
    private WebResourceResponse protocolBridgeJS(final String protocolName)
    {
        if (TextUtils.isEmpty(protocolName))
        {
            throw new ZWebException("protocolName is null...");
        }
        if (InternalConstantName.PROTOCOL_INIT.equals(protocolName))
        {
            final String jsContent = loadBridgeJS();
            if (!TextUtils.isEmpty(jsContent))
            {
                ZLog.with(this).z("protocolBridgeJS: jsContent = " + jsContent);
                final String finalStr = jsContent + buildInitFrameworkJS();
                return new WebResourceResponse("text/javascript", "UTF-8", new ByteArrayInputStream(finalStr.getBytes()));
            }
        }
        else
        {
            throw new ZWebException("There is no protocol: " + protocolName + ", Only support '__init__', eg: <script type=\"text/javascript\" src=\"zweb://__init__\"></script>");
        }
        return null;
    }

    /**
     * 初始化JS框架
     */
    private void initFramework()
    {
        this.callJS(buildInitFrameworkJS());
    }

    /**
     * 构建JS框架初始化函数
     *
     * @return
     */
    private String buildInitFrameworkJS()
    {
        try
        {
            JSONObject initParams = new JSONObject();
            initParams.put(InternalConstantName.OS, InternalConstantName.ANDROID);
            initParams.put(InternalConstantName.VERSION, Build.VERSION.SDK_INT);
            initParams.put(InternalConstantName.INTERNAL_NAME, this.getZWebConfig().getInterName());
            initParams.put(InternalConstantName.EXPOSED_NAME, this.getZWebConfig().getExposedName());
            return JsUtils.buildJsFunction(InternalFunctionName.MAIN_CALL_OBJ, InternalFunctionName.INIT_FRAMEWORK, getFrameworkUUID(), initParams.toString());
        }
        catch (JSONException e)
        {
            throw new ZWebException("initFramework is Failed... ", e);
        }
    }

    @NonNull
    private ZWebConfig getZWebConfig()
    {
        return mZWebConfig;
    }

    @NonNull
    /*package*/ IZWebView getZWebView()
    {
        return mZWebView;
    }

    /*package*/ ViewGroup getView()
    {
        if (mRootView == null)
        {
            mRootView = new FrameLayout(ZWebInstance.sApplication);
            mRootView.addView(mZWebView.getView());
            initProgressBar(mRootView);
        }
        return mRootView;
    }

    private void initProgressBar(ViewGroup rootView)
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

    /*package*/ void onActivityStart()
    {

    }

    /*package*/ void onActivityPause()
    {
        mZWebView.onPause();
    }

    /*package*/ void onActivityResume()
    {
        mZWebView.onResume();
    }

    /*package*/ void onActivityStop()
    {

    }

    /*package*/ void onActivityDestroy()
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

    /*package*/ boolean onActivityBack()
    {
        if (mZWebView.canGoBack())
        {
            mZWebView.goBack();
            return true;
        }
        return false;
    }

    private void post(Runnable runnable)
    {
        if (getView() == null)
        {
            return;
        }
        getView().post(runnable);
    }
}
