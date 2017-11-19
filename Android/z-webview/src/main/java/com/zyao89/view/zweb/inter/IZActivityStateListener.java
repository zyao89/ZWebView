package com.zyao89.view.zweb.inter;

import android.view.ViewGroup;

/**
 * @author Zyao89
 * @date 2017/11/6.
 */
public interface IZActivityStateListener
{
    void onActivityCreate(ViewGroup rootView);

    void onActivityStart();

    void onActivityPause();

    void onActivityResume();

    void onActivityStop();

    void onActivityDestroy();

    boolean onActivityBack();
}
