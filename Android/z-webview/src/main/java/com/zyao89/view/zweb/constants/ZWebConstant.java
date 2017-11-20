package com.zyao89.view.zweb.constants;

import android.support.annotation.RawRes;

import com.zyao89.view.zweb.R;

/**
 * 对外常量
 *
 * @author Zyao89
 * 2017/11/17.
 */
public interface ZWebConstant
{
    String MAIN_HTML      = "file:///android_asset/index.html";
    String MAIN_HTML_TEST = "file:///android_res/raw/index_test.html";
    @RawRes
    int MAIN_FRAMEWORK         = R.raw.zweb;
    @RawRes
    int MAIN_FRAMEWORK_MIN     = R.raw.zweb_min;
    @RawRes
    int MAIN_FRAMEWORK_EXTENDS = R.raw.zweb_extends;
}
