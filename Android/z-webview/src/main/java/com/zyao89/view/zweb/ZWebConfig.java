package com.zyao89.view.zweb;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.RawRes;
import android.support.annotation.VisibleForTesting;

import com.zyao89.view.zweb.constants.InjectionMode;
import com.zyao89.view.zweb.constants.ZWebConstant;
import com.zyao89.view.zweb.impl.DefaultZWebMethodInterface;
import com.zyao89.view.zweb.impl.DefaultZWebOnSpecialStateListener;
import com.zyao89.view.zweb.impl.DefaultZWebOnStateListener;
import com.zyao89.view.zweb.inter.IZWebMethodInterface;
import com.zyao89.view.zweb.inter.IZWebOnSpecialStateListener;
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
    private final String                      mInterName;
    private final String                      mExposedName;
    private final String                      mUrl;
    private final boolean                     mIsShowLoading;
    private final IZWebMethodInterface        mZWebMethodInterface;
    private final IZWebOnStateListener        mZWebOnStateListener;
    private final IZWebOnSpecialStateListener mZWebOnSpecialStateListener;
    private final List<JSFile>                mInjectJSFiles;
    private final boolean                     mIsFileAccess;
    @ColorInt
    private final int                         mBackgroundColor;
    private final InjectionMode               mInjectionMode;

    ZWebConfig (Builder builder)
    {
        this.mUrl = builder.mUrl;
        this.mInterName = builder.mInterName;
        this.mExposedName = builder.mExposedName;
        this.mIsShowLoading = builder.mIsShowLoading;
        this.mZWebMethodInterface = builder.mZWebMethodInterface == null ? new DefaultZWebMethodInterface() : builder.mZWebMethodInterface;
        this.mZWebOnStateListener = builder.mZWebOnStateListener == null ? new DefaultZWebOnStateListener() : builder.mZWebOnStateListener;
        this.mZWebOnSpecialStateListener = builder.mZWebOnSpecialStateListener == null ? new DefaultZWebOnSpecialStateListener() : builder.mZWebOnSpecialStateListener;
        this.mInjectJSFiles = builder.mInjectJSFileList;
        this.mIsFileAccess = builder.mIsFileAccess;
        this.mBackgroundColor = builder.mBackgroundColor;
        this.mInjectionMode = builder.mInjectionMode;
    }

    public String url ()
    {
        return mUrl;
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

    public IZWebOnSpecialStateListener getZWebOnSpecialStateListener ()
    {
        return mZWebOnSpecialStateListener;
    }

    public List<JSFile> getInjectJSFiles ()
    {
        return mInjectJSFiles;
    }

    public boolean isFileAccess ()
    {
        return mIsFileAccess;
    }

    @ColorInt
    public int getBackgroundColor ()
    {
        return mBackgroundColor;
    }

    public InjectionMode getInjectionMode()
    {
        return mInjectionMode;
    }

    public static class Builder
    {
        /**
         * 对内接口名称
         */
        private final String                      mInterName                  = "ZWeb";
        /**
         * 对外暴露接口名称
         */
        private       String                      mExposedName                = "ZWeb_Android_APP";
        /**
         * 主页URL
         */
        private       String                      mUrl                        = null;
        /**
         * 是否显示内部加载等待
         */
        private       boolean                     mIsShowLoading              = true;
        /**
         * Native UI 实现
         */
        private       IZWebMethodInterface        mZWebMethodInterface        = null;
        /**
         * 框架生命周期监听
         */
        private       IZWebOnStateListener        mZWebOnStateListener        = null;
        /**
         * 特殊方法监听
         */
        private       IZWebOnSpecialStateListener mZWebOnSpecialStateListener = null;
        /**
         * 在加载完成后需要注入的JS
         */
        private       LinkedList<JSFile>          mInjectJSFileList           = null;
        /**
         * 是否加载的是本地文件，默认true
         */
        private       boolean                     mIsFileAccess               = true;
        /**
         * WebView背景颜色（默认白色）
         */
        @ColorInt
        private       int                         mBackgroundColor            = Color.WHITE;
        /**
         * 依赖VueZWeb注入调用（默认使用Vue作为依赖）
         */
        private       InjectionMode               mInjectionMode              = InjectionMode.Normal;

        public Builder (String url)
        {
            if (url == null)
            {
                throw new NullPointerException("url == null");
            }
            this.mUrl = url;
            this.mInjectJSFileList = new LinkedList<>();
        }

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
            this.mZWebMethodInterface = webMethodInterface;
            return this;
        }

        public Builder setOnStateListener (IZWebOnStateListener webOnStateListener)
        {
            this.mZWebOnStateListener = webOnStateListener;
            return this;
        }

        public Builder setOnSpecialStateListener (IZWebOnSpecialStateListener webOnSpecialStateListener)
        {
            this.mZWebOnSpecialStateListener = webOnSpecialStateListener;
            return this;
        }

        public Builder addInjectJSAssetsFile (String injectJSFilePath)
        {
            this.mInjectJSFileList.add(JSFile.create(JSFile.ASSETS, injectJSFilePath));
            return this;
        }

        public Builder addInjectJSRawFile (@RawRes int injectJSRawID)
        {
            this.mInjectJSFileList.add(JSFile.create(JSFile.RAW, injectJSRawID));
            return this;
        }

        /**
         * 自动添加框架
         *
         * @return this
         */
        public Builder autoInjectFramework ()
        {
            this.mInjectJSFileList.addFirst(JSFile.create(JSFile.RAW, ZWebConstant.MAIN_FRAMEWORK_MIN));
            return this;
        }

        /**
         * 自动注入扩展JS代码
         *
         * @return this
         */
        public Builder autoInjectExtendsJS()
        {
            this.mInjectJSFileList.add(JSFile.create(JSFile.RAW, ZWebConstant.MAIN_FRAMEWORK_EXTENDS));
            return this;
        }

        public Builder closeFileAccess ()
        {
            this.mIsFileAccess = false;
            return this;
        }

        public Builder setBackgroundColor (int backgroundColor)
        {
            mBackgroundColor = backgroundColor;
            return this;
        }

        public Builder setInjectionMode(InjectionMode injectionMode)
        {
            mInjectionMode = injectionMode;
            return this;
        }

        public ZWebConfig build ()
        {
            return new ZWebConfig(this);
        }
    }

    static class JSFile
    {
        final static int ASSETS = 0x01;
        final static int RAW = 0x02;
        private final int mType;

        private JSFile (int type)
        {
            this.mType = type;
        }

        private static JSAssetsFile create (int type, String path)
        {
            return new JSAssetsFile(type, path);
        }

        private static JSRawFile create (int type, @RawRes int rawID)
        {
            return new JSRawFile(type, rawID);
        }

        public int getType ()
        {
            return mType;
        }
    }

    static class JSAssetsFile extends JSFile
    {
        private final String mPath;

        private JSAssetsFile (int type, String path)
        {
            super(type);
            this.mPath = path;
        }

        public String getPath ()
        {
            return mPath;
        }
    }

    static class JSRawFile extends JSFile
    {
        private final @RawRes
        int mRawID;

        private JSRawFile (int type, @RawRes int rawID)
        {
            super(type);
            this.mRawID = rawID;
        }

        @RawRes
        public int getRawID ()
        {
            return mRawID;
        }
    }
}
