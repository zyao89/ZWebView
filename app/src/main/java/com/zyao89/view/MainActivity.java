package com.zyao89.view;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zyao89.view.zweb.ZWebInstance;
import com.zyao89.view.zweb.inter.IZMethodInterface;
import com.zyao89.view.zweb.inter.IZWeb;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements IZMethodInterface
{
    public final static String ROOT      = "root";
    public final static String MAIN_HTML = "file:///android_asset/index.html";
    private LinearLayout   mRootView;
    private ZWebInstance   mZWebInstance;
    private OkHttpClient   mOkHttpClient;
    private RequireService mRequireService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            View decorView = getWindow().getDecorView();
            int option =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            //            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            if (getActionBar() != null)
            {
                getActionBar().hide();
            }
            if (getSupportActionBar() != null)
            {
                getSupportActionBar().hide();
            }
        }

        super.onCreate(savedInstanceState);

        mRootView = new LinearLayout(this);
        mRootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mRootView.setOrientation(LinearLayout.VERTICAL);
        mRootView.setContentDescription(ROOT);
        setContentView(mRootView);

        int colors[] = {0xff2D0081, 0xff8B3097, 0xffD14E7A};
        GradientDrawable bg = new GradientDrawable(GradientDrawable.Orientation.TR_BL, colors);
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN)
        {
            mRootView.setBackgroundDrawable(bg);
        }
        else
        {
            mRootView.setBackground(bg);
        }
        mRootView.setPadding(0, 60, 0, 0);

        Button GoBack = new Button(this);
        GoBack.setText("GoBack");
        GoBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mRequireService.goBack();
            }
        });
        mRootView.addView(GoBack);

        Button GoForward = new Button(this);
        GoForward.setText("GoForward");
        GoForward.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mRequireService.goForward();
            }
        });
        mRootView.addView(GoForward);

        Button Refresh = new Button(this);
        Refresh.setText("Refresh");
        Refresh.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mRequireService.refresh();
            }
        });
        mRootView.addView(Refresh);

        mZWebInstance = ZWebInstance.createInstance();
        mZWebInstance.onActivityCreate(mRootView, MAIN_HTML);

        mZWebInstance.setOnMethodImplement(this);

        initOkHttpClient();

        initRequireService();
    }

    private void initOkHttpClient()
    {
        mOkHttpClient = new OkHttpClient();
    }

    private void initRequireService()
    {
        mRequireService = mZWebInstance.create(RequireService.class);
    }

    @Override
    public void onZWebCreated(IZWeb zWeb, int width, int height)
    {
        boolean a = mRequireService.a("2b", "9999", 66);
        System.out.println("结果打印： " + a);
        //        mRequireService.initParam("小明同学", 1, 0xfff);
    }

    @Override
    public void onZWebException(IZWeb zWeb, long errorCode, String message)
    {

    }

    @Override
    public void onZWebRequire(IZWeb zWeb, String url, String method, String data, String type, final IZRequireController controller)
    {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                controller.result(false, "请求失败啦。。。");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                controller.result(true, response.body().string());
            }
        });
    }

    @Override
    public void onZWebMessage(IZWeb zWeb, String oJson)
    {

    }

    @Override
    public void onZWebDestroy(IZWeb zWeb)
    {

    }

    @Override
    public void saveData(IZWeb zWeb)
    {

    }

    @Override
    public void loadData(IZWeb zWeb)
    {

    }

    @Override
    public void showLoading(IZWeb zWeb)
    {

    }

    @Override
    public void hideLoading(IZWeb zWeb)
    {

    }

    @Override
    public void tip(IZWeb zWeb, String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop()
    {
        mZWebInstance.onActivityStop();
        super.onStop();
    }

    @Override
    protected void onStart()
    {
        mZWebInstance.onActivityStart();
        super.onStart();
    }

    @Override
    protected void onResume()
    {
        mZWebInstance.onActivityResume();
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        mZWebInstance.onActivityPause();
        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        mZWebInstance.onActivityDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed()
    {
        if (!mZWebInstance.onActivityBack())
        {
            super.onBackPressed();
        }
    }
}
