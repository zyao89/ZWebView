package com.zyao89.view.zweb.views;

import android.webkit.WebBackForwardList;
import android.webkit.WebView;

import com.zyao89.view.zweb.utils.ZLog;

/**
 * @author Zyao89
 * @date 2017/11/13.
 */
/* package */ class FixedOnReceivedTitle
{
    private WebChromeClientEx mWebChromeClient;
    private boolean           mIsOnReceivedTitle;

    /* package */ void setWebChromeClient(WebChromeClientEx webChromeClient)
    {
        mWebChromeClient = webChromeClient;
    }

    /* package */ void onPageStarted()
    {
        mIsOnReceivedTitle = false;
    }

    /* package */ void onPageFinished(WebView view)
    {
        if (!mIsOnReceivedTitle && mWebChromeClient != null)
        {
            WebBackForwardList list = null;
            try
            {
                list = view.copyBackForwardList();
            }
            catch (NullPointerException e)
            {
                ZLog.with(this).w(e.getMessage());
            }
            if (list != null && list.getSize() > 0 && list.getCurrentIndex() >= 0 && list.getItemAtIndex(list.getCurrentIndex()) != null)
            {
                String previousTitle = list.getItemAtIndex(list.getCurrentIndex()).getTitle();
                mWebChromeClient.onReceivedTitle(view, previousTitle);
            }
        }
    }

    /* package */ void onReceivedTitle()
    {
        mIsOnReceivedTitle = true;
    }
}
