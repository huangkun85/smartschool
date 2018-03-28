package hx.smartschool.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import hx.smartschool.R;
import hx.smartschool.util.AccountStateHelper;
import hx.smartschool.util.LoadingDialogUtil;


/**
 * 基础试图
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity implements IBaseAppCompatActivity {

    private static final int LOADDING_SHOW = 10;
    private static final int LOADDING_HIDE = 11;

    private static final int MQ_SENDER_STOP = 21;
    private static final int MQ_SENDER_SEND_MSG = 22;

    private static final int MQ_RECEIVER_START = 30;
    private static final int MQ_RECEIVER_STOP = 31;


    private String appversion;

    private Gson gson = null;

    private AccountStateHelper accountStateHelper;

    // Loading 加载页面
    private Dialog mLoadingDialog = null;

    // Hander 消息处理器
    private Handler mLoadingDialogHandler = null;


    /**
     * 跳转 Activity
     *
     * @param context
     * @param cls
     */
    public void doFinishThenStartActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
        this.finish();

    }

    /**
     * 寻找子项
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T find(int viewId) {
        return (T) findViewById(viewId);
    }

    /**
     * 打开Loading 对话框
     *
     * @param delayInterval
     */
    public void doShowLoadingDialog(int delayInterval) {
        mLoadingDialogHandler.sendEmptyMessageDelayed(LOADDING_SHOW, delayInterval);
    }

    /**
     * 关闭Loading 对话框
     *
     * @param delayInterval
     */

    public void doHideLoadingDialog(int delayInterval) {
        mLoadingDialogHandler.sendEmptyMessageDelayed(LOADDING_HIDE, delayInterval);
    }


    /**
     * 短暂显示Toast提示(来自res)
     **/
    public void showShortToast(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
    }

    /**
     * 短暂显示Toast提示(来自String)
     **/
    public void showShortToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Toast提示(来自res)
     **/
    public void showLongToast(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_LONG).show();
    }

    /**
     * 长时间显示Toast提示(来自String)
     **/
    public void showLongToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }


    /**
     * 含有标题和内容的对话框
     **/
    public AlertDialog showAlertDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title)
                .setMessage(message).show();
        return alertDialog;
    }

    /**
     * 含有标题、内容、两个按钮的对话框
     *
     * @param title
     * @param message
     * @param positiveText
     * @param onPositiveClickListener
     * @param negativeText
     * @param onNegativeClickListener
     * @return
     */
    public AlertDialog showAlertDialog(String title,
                                       String message,
                                       String positiveText,
                                       String negativeText,
                                       DialogInterface.OnClickListener onPositiveClickListener,
                                       DialogInterface.OnClickListener onNegativeClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveText, onPositiveClickListener)
                .setNegativeButton(negativeText, onNegativeClickListener)
                .show();

        return alertDialog;
    }

    /**
     * 含有标题、内容、图标、两个按钮的对话框
     **/
    public AlertDialog showAlertDialog(String title, String message,
                                       int icon, String positiveText,
                                       DialogInterface.OnClickListener onPositiveClickListener,
                                       String negativeText,
                                       DialogInterface.OnClickListener onNegativeClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title)
                .setMessage(message).setIcon(icon)
                .setPositiveButton(positiveText, onPositiveClickListener)
                .setNegativeButton(negativeText, onNegativeClickListener)
                .show();
        return alertDialog;
    }


    /**
     * 含有标题、自定义内容、图标、两个按钮的对话框
     **/
    public AlertDialog showAlertDialog(String title,
                                       View view,
                                       int icon,
                                       String positiveText,
                                       DialogInterface.OnClickListener onPositiveClickListener,
                                       String negativeText,
                                       DialogInterface.OnClickListener onNegativeClickListener) {


        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setView(view)
                .setIcon(icon)
                .setPositiveButton(positiveText, onPositiveClickListener)
                .setNegativeButton(negativeText, onNegativeClickListener)
                .show();
        return alertDialog;
    }


    /**
     * 默认退出
     **/
    public void defaultFinish() {
        super.finish();
    }


    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        appversion = getString(R.string.app_version);
        gson = new Gson();
        accountStateHelper = new AccountStateHelper(this);

        //Loading 动画
        mLoadingDialogHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                Log.d("MainActivity", "msg:" + msg);
                switch (msg.what) {

                    case LOADDING_HIDE:
                        LoadingDialogUtil.closeDialog(mLoadingDialog);
                        Log.d("BaseAppCompatActivity", "LOADDING_HIDE");
                        break;

                    case LOADDING_SHOW:
                        mLoadingDialog = LoadingDialogUtil.createLoadingDialog(BaseAppCompatActivity.this, "加载中...");
                        Log.d("BaseAppCompatActivity", "LOADDING_SHOW");
                        break;

                    default:
                        super.handleMessage(msg);
                        break;
                }
            }
        };


        initData();
        initLayout();
        initControls();
        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("BaseActivity", "Onstart");
        doOnStart();
    }

    @Override
    public void onBackPressed() {
        doBackKeyPress();
        super.onBackPressed();
    }


    public Gson getGson() {
        return gson;
    }

    public AccountStateHelper getAccountStateHelper() {
        return accountStateHelper;
    }


    /**
     * 发送消息队列
     *
     * @param strMessage
     */
    public void doMQSenderSendMessage(String strMessage) {

        Message msg = new Message();

        msg.what = MQ_SENDER_SEND_MSG;
        Bundle bundle = new Bundle();
        bundle.putString("msg", strMessage);  //往Bundle中存放数据
        msg.setData(bundle);//mes利用Bundle传递数据
        this.mLoadingDialogHandler.sendMessage(msg);
        Log.d("doMQReceiverStart", "MQ_SENDER_SEND_MSG");
    }

    /**
     * 关闭发送者
     */
    public void doMQSenderClose() {

        this.mLoadingDialogHandler.sendEmptyMessage(MQ_SENDER_STOP);
    }


    /**
     * 开始运行消息队列监听器
     */
    public void doMQReceiverStart(String[] bingingKeys) {

        String json = this.gson.toJson(bingingKeys);
        Message msg = new Message();

        msg.what = MQ_RECEIVER_START;
        Bundle bundle = new Bundle();
        bundle.putString("bingingKeys", json);  //往Bundle中存放数据

        msg.setData(bundle);//mes利用Bundle传递数据

        this.mLoadingDialogHandler.sendMessage(msg);

        Log.d("doMQReceiverStart", "doMQReceiverStart");

    }

    /**
     * 关闭接收者
     */
    public void doMQReceiverStop() {
        this.mLoadingDialogHandler.sendEmptyMessage(MQ_RECEIVER_STOP);

    }

}
