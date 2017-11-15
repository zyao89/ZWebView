package com.zyao89.view.zweb;

import com.zyao89.view.zweb.impl.DefaultZWebMethodInterface;
import com.zyao89.view.zweb.impl.DefaultZWebOnStateListener;
import com.zyao89.view.zweb.inter.IZWebMethodInterface;
import com.zyao89.view.zweb.inter.IZWebOnStateListener;

import java.util.LinkedList;
import java.util.List;

/**
 * 参数配置
 *
 * @author Zyao89
 * @date 2017/11/13.
 */
public class ZWebConfig
{
    private final String               mInterName;
    private final String               mExposedName;
    private final String               mUrl;
    private final boolean              mIsShowLoading;
    private final IZWebMethodInterface mZWebMethodInterface;
    private final IZWebOnStateListener mZWebOnStateListener;
    private final List<String>         mInjectJSs;
    private final boolean              mIsFileAccess;

    ZWebConfig(Builder builder)
    {
        this.mUrl = builder.mUrl;
        this.mInterName = builder.mInterName;
        this.mExposedName = builder.mExposedName;
        this.mIsShowLoading = builder.mIsShowLoading;
        this.mZWebMethodInterface = builder.mZWebMethodInterface == null ? new DefaultZWebMethodInterface() : builder.mZWebMethodInterface;
        this.mZWebOnStateListener = builder.mZWebOnStateListener == null ? new DefaultZWebOnStateListener() : builder.mZWebOnStateListener;
        this.mInjectJSs = builder.mInjectJSList;
        this.mIsFileAccess = builder.mIsFileAccess;
    }

    public String url()
    {
        return mUrl;
    }

    public String getInterName()
    {
        return mInterName;
    }

    public String getExposedName()
    {
        return mExposedName;
    }

    public boolean isShowLoading()
    {
        return mIsShowLoading;
    }

    public IZWebMethodInterface getZWebMethodInterface()
    {
        return mZWebMethodInterface;
    }

    public IZWebOnStateListener getZWebOnStateListener()
    {
        return mZWebOnStateListener;
    }

    public List<String> getInjectJSs()
    {
        return mInjectJSs;
    }

    public boolean isFileAccess()
    {
        return mIsFileAccess;
    }

    public static class Builder
    {
        /**
         * 对内接口名称
         */
        private final String               mInterName           = "ZWeb";
        /**
         * 对外暴露接口名称
         */
        private       String               mExposedName         = "ZWeb_Android_APP";
        /**
         * 主页URL
         */
        private       String               mUrl                 = null;
        /**
         * 是否显示内部加载等待
         */
        private       boolean              mIsShowLoading       = true;
        /**
         * Native UI 实现
         */
        private       IZWebMethodInterface mZWebMethodInterface = null;
        /**
         * 框架生命周期监听
         */
        private       IZWebOnStateListener mZWebOnStateListener = null;
        /**
         * 在加载完成后需要注入的JS
         */
        private       List<String>         mInjectJSList        = null;
        /**
         * 是否加载的是本地文件，默认true
         */
        private       boolean              mIsFileAccess        = true;

        public Builder(String url)
        {
            if (url == null) { throw new NullPointerException("url == null"); }
            this.mUrl = url;
            this.mInjectJSList = new LinkedList<>();
        }

        public Builder setExposedName(String exposedName)
        {
            this.mExposedName = exposedName;
            return this;
        }

        public Builder setShowLoading(boolean showLoading)
        {
            this.mIsShowLoading = showLoading;
            return this;
        }

        public Builder setNativeMethodImplement(IZWebMethodInterface webMethodInterface)
        {
            this.mZWebMethodInterface = webMethodInterface;
            return this;
        }

        public Builder setOnStateListener(IZWebOnStateListener webOnStateListener)
        {
            this.mZWebOnStateListener = webOnStateListener;
            return this;
        }

        public Builder addInjectJS(String injectJS)
        {
            this.mInjectJSList.add(injectJS);
            return this;
        }

        public Builder closeFileAccess()
        {
            this.mIsFileAccess = false;
            return this;
        }

        public ZWebConfig build()
        {
            return new ZWebConfig(this);
        }
    }
}
