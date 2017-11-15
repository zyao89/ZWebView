package com.zyao89.view.zweb.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityManager;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zyao89.view.zweb.constants.JavaScriptMethodName;
import com.zyao89.view.zweb.exceptions.ZWebException;
import com.zyao89.view.zweb.utils.JsCallJava;
import com.zyao89.view.zweb.utils.JsUtils;
import com.zyao89.view.zweb.utils.ZLog;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Zyao89
 * @date 2017/11/13.
 */

public class WebViewEx extends WebView
{
    private final HashMap<String, JsCallJava> mJsInterfaceMap = new HashMap<String, JsCallJava>();
    private FixedOnReceivedTitle mFixedOnReceivedTitle;
    private Boolean              mIsAccessibilityEnabledOriginal;

    public WebViewEx(Context context)
    {
        this(context, null);
    }

    public WebViewEx(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public WebViewEx(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context)
    {
        // 删除掉Android默认注册的JS接口
        removeSearchBoxImpl();
        mFixedOnReceivedTitle = new FixedOnReceivedTitle();
    }

    /**
     * 删除掉Android默认注册的JS接口
     */
    private void removeSearchBoxImpl()
    {
        super.removeJavascriptInterface(JavaScriptMethodName.SEARCH_BOX_JAVA_BRIDGE);
    }

    public final void setWebChromeClient(WebChromeClientEx client)
    {
        client.setFixedOnReceivedTitle(mFixedOnReceivedTitle);
        super.setWebChromeClient(client);
    }

    public final void setWebViewClient(WebViewClientEx client)
    {
        client.setFixedOnReceivedTitle(mFixedOnReceivedTitle);
        super.setWebViewClient(client);
    }

    public boolean handleJsInterface(String url, String message, String defaultValue, JsPromptResult result)
    {
        if (mJsInterfaceMap != null && JsCallJava.isSafeWebViewCallMsg(message))
        {
            JSONObject jsonObject = JsCallJava.getMsgJSONObject(message);
            String interfacedName = JsCallJava.getInterfacedName(jsonObject);
            if (interfacedName != null)
            {
                JsCallJava jsCallJava = mJsInterfaceMap.get(interfacedName);
                if (jsCallJava != null)
                {
                    result.confirm(jsCallJava.call(this, jsonObject));
                }
            }
            return true;
        }
        return false;
    }

    public void injectJavascriptInterfaces()
    {
        if (mJsInterfaceMap != null)
        {
            injectJavaScript();
        }
    }

    @Override
    public void destroy()
    {
        setVisibility(GONE);
        if (mJsInterfaceMap != null)
        {
            mJsInterfaceMap.clear();
        }
        removeAllViewsInLayout();
        releaseConfigCallback();

        resetAccessibilityEnabled();
        super.destroy();
    }

    @Override
    @Deprecated
    public final void setWebViewClient (WebViewClient client)
    {
        throw new ZWebException("WebViewClient is Deprecated...");
    }

    @Override
    @Deprecated
    public final void setWebChromeClient (WebChromeClient client)
    {
        throw new ZWebException("WebChromeClient is Deprecated...");
    }

    @Override
    @SuppressLint({"JavascriptInterface", "AddJavascriptInterface"})
    public void addJavascriptInterface (Object interfaceObj, String interfaceName)
    {
        if (TextUtils.isEmpty(interfaceName))
        {
            return;
        }

        // 如果在4.2以上，直接调用基类的方法来注册
        if (!JsUtils.notSupportInterface())
        {
            super.addJavascriptInterface(interfaceObj, interfaceName);
            ZLog.with(this).z("addJavascriptInterface support...");
            return;
        }

        mJsInterfaceMap.put(interfaceName, new JsCallJava(interfaceObj, interfaceName));
        injectJavaScript(interfaceName);
        ZLog.with(this).z("injectJavaScript, addJavascriptInterface.interfaceObj = " + interfaceObj + ", interfaceName = " + interfaceName);
    }

    protected void fixedAccessibilityInjectorExceptionForOnPageFinished(String url)
    {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN && getSettings().getJavaScriptEnabled() && mIsAccessibilityEnabledOriginal == null && isAccessibilityEnabled())
        {
            try
            {
                try
                {
                    URLEncoder.encode(String.valueOf(new URI(url)), "utf-8");
                    //                    URLEncodedUtils.parse(new URI(url), null); // AccessibilityInjector.getAxsUrlParameterValue
                }
                catch (IllegalArgumentException e)
                {
                    if (JavaScriptMethodName.BAD_PARAMETER.equals(e.getMessage()))
                    {
                        mIsAccessibilityEnabledOriginal = true;
                        setAccessibilityEnabled(false);
                        ZLog.with(this).e("fixedAccessibilityInjectorExceptionForOnPageFinished.url = " + url + e.getMessage());
                    }
                }
            }
            catch (Throwable e)
            {
                ZLog.with(this).e("fixedAccessibilityInjectorExceptionForOnPageFinished: " + e.getMessage());
            }
        }
    }

    private void injectJavaScript(@Nullable String interfaceName)
    {
        JsCallJava jsCallJava = mJsInterfaceMap.get(interfaceName);
        if (jsCallJava != null)
        {
            this.loadUrl(JsUtils.buildNotRepeatInjectJS(interfaceName, jsCallJava.getPreloadInterfaceJS()));
        }
    }

    private void injectJavaScript()
    {
        for (Map.Entry<String, JsCallJava> entry : mJsInterfaceMap.entrySet())
        {
            this.loadUrl(JsUtils.buildNotRepeatInjectJS(entry.getKey(), entry.getValue().getPreloadInterfaceJS()));
        }
    }

    private boolean isAccessibilityEnabled()
    {
        AccessibilityManager am = (AccessibilityManager) getContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
        return am != null && am.isEnabled();
    }

    private void setAccessibilityEnabled(boolean enabled)
    {
        AccessibilityManager am = (AccessibilityManager) getContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (am == null)
        {
            ZLog.with(this).d("AccessibilityManager is null... ");
            return;
        }
        try
        {
            @SuppressLint("PrivateApi") Method setAccessibilityState = am.getClass().getDeclaredMethod("setAccessibilityState", boolean.class);
            setAccessibilityState.setAccessible(true);
            setAccessibilityState.invoke(am, enabled);
            setAccessibilityState.setAccessible(false);
        }
        catch (Exception e)
        {
            ZLog.with(this).e("setAccessibilityEnabled: " + e.getMessage());
        }
    }

    private void resetAccessibilityEnabled()
    {
        if (mIsAccessibilityEnabledOriginal != null)
        {
            setAccessibilityEnabled(mIsAccessibilityEnabledOriginal);
        }
    }

    /**
     * 解决WebView内存泄漏问题
     */
    private void releaseConfigCallback()
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
        { // JELLY_BEAN
            try
            {
                Field field = WebView.class.getDeclaredField("mWebViewCore");
                field = field.getType().getDeclaredField("mBrowserFrame");
                field = field.getType().getDeclaredField("sConfigCallback");
                field.setAccessible(true);
                field.set(null, null);
            }
            catch (Exception e)
            {
                ZLog.with(this).z(e.getMessage());
            }
        }
        else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
        { // KITKAT
            try
            {
                @SuppressLint("PrivateApi") Field sConfigCallback = Class.forName("android.webkit.BrowserFrame").getDeclaredField("sConfigCallback");
                if (sConfigCallback != null)
                {
                    sConfigCallback.setAccessible(true);
                    sConfigCallback.set(null, null);
                }
            }
            catch (Exception e)
            {
                ZLog.with(this).z(e.getMessage());
            }
        }
    }
}
