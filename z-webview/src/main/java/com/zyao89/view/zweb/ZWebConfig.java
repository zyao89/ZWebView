package com.zyao89.view.zweb;

/**
 * 参数配置
 *
 * @author Zyao89
 * @date 2017/11/13.
 */
public class ZWebConfig
{
    private final String  mInterName;
    private final String  mExposedName;
    private final boolean mIsShowLoading;

    ZWebConfig(Builder builder)
    {
        this.mInterName = builder.mInterName;
        this.mExposedName = builder.mExposedName;
        this.mIsShowLoading = builder.mIsShowLoading;
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

    public static class Builder
    {
        /**
         * 对内接口名称
         */
        private final String  mInterName     = "ZWeb";
        /**
         * 对外暴露接口名称
         */
        private       String  mExposedName   = "ZWeb_Android_APP";
        /**
         * 是否显示内部加载等待
         */
        private       boolean mIsShowLoading = true;

        public void setExposedName(String exposedName)
        {
            this.mExposedName = exposedName;
        }

        public void setShowLoading(boolean showLoading)
        {
            this.mIsShowLoading = showLoading;
        }

        public ZWebConfig build()
        {
            return new ZWebConfig(this);
        }
    }
}
