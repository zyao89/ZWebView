package com.zyao89.view.zweb;

import com.zyao89.view.zweb.impl.DefaultZWebMethodInterface;
import com.zyao89.view.zweb.impl.DefaultZWebOnStateListener;
import com.zyao89.view.zweb.inter.IZWebMethodInterface;
import com.zyao89.view.zweb.inter.IZWebOnStateListener;

/**
 * 参数配置
 *
 * @author Zyao89
 * @date 2017/11/13.
 */
public class ZWebConfig
{
    private final String mInterName;
    private final String mExposedName;
    private final boolean mIsShowLoading;
    private final IZWebMethodInterface mZWebMethodInterface;
    private final IZWebOnStateListener mZWebOnStateListener;
    private final String[] mInjectJS;
    private final boolean mIsLocalFile;

    ZWebConfig (Builder builder)
    {
        this.mInterName = builder.mInterName;
        this.mExposedName = builder.mExposedName;
        this.mIsShowLoading = builder.mIsShowLoading;
        this.mZWebMethodInterface = builder.mZWebMethodInterface == null ? new DefaultZWebMethodInterface() : builder.mZWebMethodInterface;
        this.mZWebOnStateListener = builder.mZWebOnStateListener == null ? new DefaultZWebOnStateListener() : builder.mZWebOnStateListener;
        this.mInjectJS = builder.mInjectJS;
        this.mIsLocalFile = builder.mIsLocalFile;
    }

    public String getInterName ()
    {
        return mInterName;
    }

    public String getExposedName ()
    {
        return mExposedName;
    }

    public boolean isShowLoading ()
    {
        return mIsShowLoading;
    }

    public IZWebMethodInterface getZWebMethodInterface ()
    {
        return mZWebMethodInterface;
    }

    public IZWebOnStateListener getZWebOnStateListener ()
    {
        return mZWebOnStateListener;
    }

    public String[] getInjectJS ()
    {
        return mInjectJS;
    }

    public boolean isLocalFile ()
    {
        return mIsLocalFile;
    }

    public static class Builder
    {
        /**
         * 对内接口名称
         */
        private final String mInterName = "ZWeb";
        /**
         * 对外暴露接口名称
         */
        private String mExposedName = "ZWeb_Android_APP";
        /**
         * 是否显示内部加载等待
         */
        private boolean mIsShowLoading = true;
        /**
         * Native UI 实现
         */
        private IZWebMethodInterface mZWebMethodInterface = null;
        /**
         * 框架生命周期监听
         */
        private IZWebOnStateListener mZWebOnStateListener = null;
        /**
         * 在加载完成后需要注入的JS
         */
        private String[] mInjectJS = null;
        /**
         * 是否加载的是本地文件
         */
        private boolean mIsLocalFile = true;

        public Builder setExposedName (String exposedName)
        {
            this.mExposedName = exposedName;
            return this;
        }

        public Builder setShowLoading (boolean showLoading)
        {
            this.mIsShowLoading = showLoading;
            return this;
        }

        public Builder setNativeMethodImplement (IZWebMethodInterface webMethodInterface)
        {
            mZWebMethodInterface = webMethodInterface;
            return this;
        }

        public Builder setOnStateListener (IZWebOnStateListener webOnStateListener)
        {
            mZWebOnStateListener = webOnStateListener;
            return this;
        }

        public Builder setInjectJS (String... injectJS)
        {
            mInjectJS = injectJS;
            return this;
        }

        public Builder setLoadAssetsFile (boolean isLocalFile)
        {
            mIsLocalFile = isLocalFile;
            return this;
        }

        public ZWebConfig build ()
        {
            return new ZWebConfig(this);
        }
    }
}
