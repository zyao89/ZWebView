package com.zyao89.view;

import android.app.Application;

import com.zyao89.view.zweb.ZWebInstance;

/**
 * @author Zyao89
 * @date on 2017/11/6.
 */
public class CustomApp extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        ZWebInstance.init(this);
    }
}
