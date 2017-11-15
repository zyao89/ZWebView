package com.zyao89.view.zweb.views;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.zyao89.view.zweb.utils.ZLog;

/**
 * @author Zyao89
 * @date 2017/11/13.
 */
public class ZWebView implements IZWebView
{
    private Context mContext;
    private FrameLayout mRootView;
    private WebViewEx mWebView;
    private ProgressBar mProgressBar;

    private boolean mShowLoading = true;

    private OnErrorListener mOnErrorListener;
    private OnPageListener mOnPageListener;

    public ZWebView (Context context)
    {
        mContext = context;
    }

    @Override
    public View getView ()
    {
        if (mRootView == null)
        {
            mRootView = new FrameLayout(mContext);
            mRootView.setBackgroundColor(Color.WHITE);

            //mContext.getApplicationContext();
            mWebView = new WebViewEx(mContext);
            FrameLayout.LayoutParams wvLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            wvLayoutParams.gravity = Gravity.CENTER;
            mWebView.setLayoutParams(wvLayoutParams);
            mWebView.setBackgroundColor(Color.TRANSPARENT);
            mRootView.addView(mWebView);
            initWebView(mWebView);

            mProgressBar = new ProgressBar(mContext);
            showProgressBar(false);
            FrameLayout.LayoutParams pLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            mProgressBar.setLayoutParams(pLayoutParams);
            pLayoutParams.gravity = Gravity.CENTER;
            mRootView.addView(mProgressBar);
        }
        return mRootView;
    }

    @Override
    public void onPause ()
    {
        if (getWebView() == null)
        {
            return;
        }
        getWebView().pauseTimers();
        getWebView().onPause();
    }

    @Override
    public void onResume ()
    {
        if (getWebView() == null)
        {
            return;
        }
        getWebView().onResume();
        getWebView().resumeTimers();
    }

    @Override
    public void destroy ()
    {
        if (getWebView() != null)
        {
            getWebView().getSettings().setJavaScriptEnabled(false);
            getWebView().clearHistory();
            getWebView().clearView();
            getWebView().removeAllViews();
            getWebView().freeMemory();
            getWebView().destroy();
            mWebView = null;
        }

        if (getRootView() != null)
        {
            getRootView().removeAllViews();
            getRootView().destroyDrawingCache();

            ViewParent parent = getRootView().getParent();
            if (parent != null && parent instanceof ViewGroup)
            {
                ((ViewGroup) parent).removeAllViewsInLayout();
            }

            mRootView = null;
        }
    }

    @Override
    public void loadUrl (String url)
    {
        if (getWebView() == null)
        {
            return;
        }
        getWebView().loadUrl(url);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void evaluateJavascript (String script)
    {
        if (getWebView() == null)
        {
            return;
        }
        getWebView().evaluateJavascript(script, null);
    }

    @Override
    public void reload ()
    {
        if (getWebView() == null)
        {
            return;
        }
        getWebView().reload();
    }

    @Override
    public boolean canGoBack ()
    {
        if (getWebView() == null)
        {
            return false;
        }
        return getWebView().canGoBack();
    }

    @Override
    public void goBack ()
    {
        if (getWebView() == null)
        {
            return;
        }
        getWebView().goBack();
    }

    @Override
    public void goForward ()
    {
        if (getWebView() == null)
        {
            return;
        }
        getWebView().goForward();
    }

    @Override
    public void setShowLoading (boolean shown)
    {
        mShowLoading = shown;
    }

    @Override
    @SuppressLint({"JavascriptInterface", "AddJavascriptInterface"})
    public void addJavascriptInterface (Object object, String name)
    {
        if (getWebView() == null)
        {
            return;
        }
        getWebView().addJavascriptInterface(object, name);
    }

    @Override
    public void setOnErrorListener (OnErrorListener listener)
    {
        mOnErrorListener = listener;
    }

    @Override
    public void setOnPageListener (OnPageListener listener)
    {
        mOnPageListener = listener;
    }

    private void showProgressBar (boolean shown)
    {
        if (mShowLoading)
        {
            mProgressBar.setVisibility(shown ? View.VISIBLE : View.GONE);
        }
    }

    private void showWebView (boolean shown)
    {
        mWebView.setVisibility(shown ? View.VISIBLE : View.INVISIBLE);
    }

    @Nullable
    private WebViewEx getWebView ()
    {
        //TODO: remove this, duplicate with getView semantically.
        return mWebView;
    }

    @Nullable
    private FrameLayout getRootView ()
    {
        return mRootView;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView (WebViewEx wv)
    {
        WebSettings settings = wv.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setDomStorageEnabled(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDefaultTextEncodingName("utf-8");//设置编码格式

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {//适配5.0不允许http和https混合使用情况
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            wv.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            wv.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
        {
            wv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        settings.setTextZoom(100);
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setSupportMultipleWindows(false);
        //是否阻塞加载网络图片  协议http or https
        settings.setBlockNetworkImage(false);
        //允许加载本地文件html  file协议
        settings.setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            //通过 file url 加载的 Javascript 读取其他的本地文件 .建议关闭
            settings.setAllowFileAccessFromFileURLs(true);
            //允许通过 file url 加载的 Javascript 可以访问其他的源，包括其他的文件和 http，https 等其他的源
            settings.setAllowUniversalAccessFromFileURLs(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        }
        else
        {
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        settings.setLoadWithOverviewMode(true);
        settings.setNeedInitialFocus(true);
        settings.setGeolocationEnabled(true);

        wv.setWebViewClient(new WebViewClientEx()
        {

            @Override
            public boolean shouldOverrideUrlLoading (WebView view, String url)
            {
                view.loadUrl(url);
                ZLog.with(this).z("onPageOverride " + url);
                return true;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest (WebView view, String url)
            {
                if (mOnPageListener != null)
                {
                    return mOnPageListener.shouldInterceptRequest(url);
                }
                return null;
            }

            @Override
            public void onReceivedError (WebView view, WebResourceRequest request, WebResourceError error)
            {
                super.onReceivedError(view, request, error);
                if (mOnErrorListener != null)
                {
                    mOnErrorListener.onError("error", "page error");
                }
            }

            @Override
            public void onReceivedHttpError (WebView view, WebResourceRequest request, WebResourceResponse errorResponse)
            {
                if (mOnErrorListener != null)
                {
                    mOnErrorListener.onError("error", "http error");
                }
            }

            @Override
            public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error)
            {
                handler.proceed(); // 接受网站证书
            }

            @Override
            public void onPageStarted (WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);
                ZLog.with(this).z("onPageStarted " + url);
                if (mOnPageListener != null)
                {
                    mOnPageListener.onPageStart(url);
                }
            }

            @Override
            public void onPageFinished (WebView view, String url)
            {
                super.onPageFinished(view, url);
                ZLog.with(this).z("onPageFinished " + url);
                if (mOnPageListener != null)
                {
                    mOnPageListener.onPageFinish(url, view.canGoBack(), view.canGoForward());
                }
            }

        });

        wv.setWebChromeClient(new WebChromeClientEx()
        {
            @Override
            public void onReceivedTitle (WebView view, String title)
            {
                super.onReceivedTitle(view, title);
                if (mOnPageListener != null)
                {
                    mOnPageListener.onReceivedTitle(view.getTitle());
                }
            }

            @Override
            public void onProgressChanged (WebView view, int newProgress)
            {
                super.onProgressChanged(view, newProgress);
                showWebView(newProgress == 100);
                showProgressBar(newProgress != 100);
                ZLog.with(this).z("onPageProgressChanged " + newProgress);
            }
        });
    }
}
