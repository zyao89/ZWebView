package com.zyao89.view;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zyao89.view.zweb.ZWebConfig;
import com.zyao89.view.zweb.ZWebInstance;
import com.zyao89.view.zweb.constants.InjectionMode;
import com.zyao89.view.zweb.constants.ZWebConstant;
import com.zyao89.view.zweb.inter.IZWebHandler;
import com.zyao89.view.zweb.inter.IZWebMethodInterface;
import com.zyao89.view.zweb.inter.IZWebOnStateListener;
import com.zyao89.view.zweb.utils.ZLog;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements IZWebMethodInterface, IZWebOnStateListener
{
    private final static String              ROOT      = "root";
    //        private final static String MAIN_HTML = "http://200.200.200.220:8080";
    private final        Map<String, String> mDatabase = new HashMap<>();
    private FrameLayout    mRootView;
    private ZWebInstance   mZWebInstance;
    private OkHttpClient   mOkHttpClient;
    private RequireService mRequireService;
    private ParseMessage   mParseMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            //            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        if (getActionBar() != null)
        {
            getActionBar().hide();
        }
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().hide();
        }

        super.onCreate(savedInstanceState);

        LinearLayout groupView = new LinearLayout(this);
        groupView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        groupView.setOrientation(LinearLayout.VERTICAL);
        groupView.setContentDescription(ROOT);
        setContentView(groupView);

        int colors[] = {0xff2D0081, 0xff8B3097, 0xffD14E7A};
        GradientDrawable bg = new GradientDrawable(GradientDrawable.Orientation.TR_BL, colors);
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN)
        {
            groupView.setBackgroundDrawable(bg);
        }
        else
        {
            groupView.setBackground(bg);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            groupView.setPadding(0, 60, 0, 0);
        }

        //        Button GoBack = new Button(this);
        //        GoBack.setText("GoBack");
        //        GoBack.setOnClickListener(new View.OnClickListener()
        //        {
        //            @Override
        //            public void onClick (View v)
        //            {
        //                mRequireService.goBack();
        //            }
        //        });
        //        groupView.addView(GoBack);
        //
        //        Button GoForward = new Button(this);
        //        GoForward.setText("GoForward");
        //        GoForward.setOnClickListener(new View.OnClickListener()
        //        {
        //            @Override
        //            public void onClick (View v)
        //            {
        //                mRequireService.goForward();
        //            }
        //        });
        //        groupView.addView(GoForward);

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
        groupView.addView(Refresh);

        mRootView = new FrameLayout(this);
        mRootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        groupView.addView(mRootView);

        initZWeb();

        initOkHttpClient();

        initRequireService();

    }

    @Override
    protected void onStart()
    {
        mZWebInstance.onActivityStart();
        super.onStart();
    }

    @Override
    protected void onStop()
    {
        mZWebInstance.onActivityStop();
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        mZWebInstance.onActivityDestroy();
        super.onDestroy();
    }

    private void initZWeb()
    {
        ZWebConfig config = new ZWebConfig
//                .Builder(ZWebConstant.MAIN_HTML_TEST)
                .Builder(ZWebConstant.MAIN_HTML)
                .setOnStateListener(this)
                .setNativeMethodImplement(this)
//                .setOnSpecialStateListener(this)
                .setInjectionMode(InjectionMode.VuePlugin)
                .autoInjectFramework()
                .autoInjectExtendsJS()
//                .addInjectJSAssetsFile("js/manifest.js")
//                .addInjectJSAssetsFile("js/vendor.js")
//                .addInjectJSAssetsFile("js/app.js")
//                .autoInjectExtendsJS()
//                .addInjectJSAssetsFile("framework/zweb.min.js")
//                .addInjectJSAssetsFile("js/test.js")
//                .addInjectJSRawFile(R.raw.index_test)
                .build();
        mZWebInstance = ZWebInstance.createInstance(config);
        mZWebInstance.onActivityCreate(mRootView);

        mParseMessage = new ParseMessage();
    }

    private void initOkHttpClient()
    {
        mOkHttpClient = new OkHttpClient.Builder().hostnameVerifier(new HostnameVerifier()
        {
            @Override
            public boolean verify(String hostname, SSLSession session)
            {
                return true;
            }
        }).build();
    }

    private void initRequireService()
    {
        mRequireService = mZWebInstance.create(RequireService.class);
    }

    @Override
    public void onZWebCreated(IZWebHandler zWebHandler, int width, int height)
    {
        boolean result = mRequireService.init("2b", "9999", 66);
        System.out.println("init 结果打印： " + result);
        //        mRequireService.initParam("小明同学", 1, 0xfff);

        final long millis = System.currentTimeMillis();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                long time = System.currentTimeMillis() - millis;
                System.out.println(time);
                mRequireService.callA("我是一个延迟饭送的坚挺消息。。。", "小A你好啊！", time);
            }
        }, 5000);
    }

    @Override
    public void onZWebException(IZWebHandler zWebHandler, long errorCode, String message)
    {
        // JS如果有异常会在这里回调
    }

    @Override
    public void onZWebRequire(IZWebHandler zWebHandler, String url, String method, String data, String type, final IZRequireController controller)
    {
        // 这里可以实现原生网络请求
        Request request = new Request.Builder().url(url).build();
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
    public void onZWebMessage(IZWebHandler zWebHandler, String cmd, String oJson, IZMessageController controller)
    {
        //      controller.result(true, "我是你想要的消息");

        // 消息处理可以这样做
        controller.parseMessage(this.mParseMessage);
    }

    @Override
    public void onZWebDestroy(IZWebHandler zWebHandler)
    {
        // JS销毁
    }

    @Override
    public void onZWebLog(IZWebHandler zWebHandler, String type, String msg)
    {
        // JS打印
        ZLog.with(this).d("onLog ==> type: " + type + ", msg: " + msg);
    }

    @Override
    public void saveData(IZWebHandler zWebHandler, String key, String value, IZDatabaseController zController)
    {
        // 这里可以存数据库
        String result = mDatabase.put(key, value);
        if (result == null)
        {
            zController.result(false);
        }
        else
        {
            zController.result(true, result);
        }
    }

    @Override
    public void loadData(IZWebHandler zWebHandler, String key, IZDatabaseController zController)
    {
        // 这里可以取数据
        String result = mDatabase.get(key);
        if (result == null)
        {
            zController.result(false);
        }
        else
        {
            zController.result(true, result);
        }
    }

    @Override
    public void showLoading(IZWebHandler zWebHandler)
    {

    }

    @Override
    public void hideLoading(IZWebHandler zWebHandler)
    {

    }

    @Override
    public void tip(IZWebHandler zWebHandler, String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed()
    {
        if (!mZWebInstance.onActivityBack())
        {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause()
    {
        mZWebInstance.onActivityPause();
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        mZWebInstance.onActivityResume();
        super.onResume();
    }
}
