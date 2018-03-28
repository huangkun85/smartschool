package hx.smartschool.activities.splash;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;

import hx.smartschool.R;
import hx.smartschool.activities.BaseAppCompatActivity;
import hx.smartschool.activities.account.login.LoginActivity;
import hx.smartschool.activities.main.MainActivity;
import hx.smartschool.model.AccountStateStoreModel;


public class SplashActivity extends BaseAppCompatActivity {

    private View mContentView; // 整个页面
    private TextView mTimeout; //计数器


    private int mDuration;
    private Handler mHideHandler = null;
    private Runnable mFinishSplashRunnable = null;


    @Override
    public int initLayout() {


        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this.getApplicationContext());

        setContentView(R.layout.activity_splash);
        return 0;
    }

    @Override
    public void initControls() {

        // layout 对象
        mContentView = this.find(R.id.activity_splash_layout_main);


        //显示全屏
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        mTimeout = this.find(R.id.activity_splash_tv_timeout);
        mTimeout.setText(mDuration + "s");


    }

    @Override
    public void initData() {

        String duration = this.getString(R.string.splash_timeout_second);
        mDuration = Integer.parseInt(duration);
    }

    @Override
    public void initListener() {

        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("SplashActivity", "点击屏幕");
                NextActivity();
            }
        });

        //循环相关
        mFinishSplashRunnable = new Runnable() {
            @SuppressLint("InlinedApi")
            @Override
            public void run() {

                if (mDuration > 0) {
                    mDuration -= 1;
                    mTimeout.setText(mDuration + "s");
                    mHideHandler.postDelayed(mFinishSplashRunnable, 1000);
                    return;
                }

                if (!SplashActivity.this.isFinishing()) {
                    NextActivity();
                }


            }
        };
    }

    @Override
    public void doOnStart() {
        //执行
        mHideHandler = new Handler();
        mHideHandler.post(mFinishSplashRunnable);

        //清除当前数据
        //this.getAccountStateHelper().clearAccountState();


    }

    @Override
    public void doBackKeyPress() {
        Log.d("Splash", "触发finish事件");
        finish();
    }

    /**
     * //判断跳转到登陆页面还是主页面
     */
    private void NextActivity() {

        AccountStateStoreModel store = this.getAccountStateHelper().getAccountState();

        Log.d("NextActivity", "NextActivity");

        if (null == store) {

            Log.d("NextActivity", "跳转Login 页面");
            this.doFinishThenStartActivity(this, LoginActivity.class);

        } else {
            Log.d("NextActivity", "跳转Main页面");
            this.doFinishThenStartActivity(this, MainActivity.class);

        }


    }


}
