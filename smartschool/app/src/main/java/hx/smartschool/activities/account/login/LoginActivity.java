package hx.smartschool.activities.account.login;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import hx.smartschool.R;
import hx.smartschool.activities.BaseAppCompatActivity;
import hx.smartschool.activities.account.register.RegisterActivity;
import hx.smartschool.model.account.login.LoginTaskModel;
import hx.smartschool.model.account.login.LoginViewModel;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseAppCompatActivity {

    private LoginAsyncTask loginAsyncTask;

    // UI 元素
    private EditText v_editText_phone;    // 用户名
    private EditText v_editText_password; // 密码
    private Button v_btn_login; // 登陆按钮
    private Button v_btn_register; // 注册按钮

    //Geetter & Setter
    public LoginAsyncTask getLoginAsyncTask() {
        return loginAsyncTask;
    }

    public void setLoginAsyncTask(LoginAsyncTask loginAsyncTask) {
        this.loginAsyncTask = loginAsyncTask;
    }


    @Override
    public int initLayout() {
        setContentView(R.layout.activity_login);
        return 0;
    }

    @Override
    public void initControls() {
        // 界面元素
        v_editText_phone = (EditText) findViewById(R.id.edit_phone);
        v_editText_password = (EditText) findViewById(R.id.edit_password);
        v_btn_login = (Button) findViewById(R.id.ac_register_btn_login);
        v_btn_register = (Button) findViewById(R.id.ac_register_btn_register);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

        // 软键盘完成或者回车事件
        v_editText_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        // 登陆按钮点击
        v_btn_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        // 注册按钮点击
        v_btn_register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.this.doFinishThenStartActivity(LoginActivity.this, RegisterActivity.class);
            }
        });

    }

    @Override
    public void doOnStart() {

    }

    @Override
    public void doBackKeyPress() {
        loginAsyncTask = null;
    }

    /**
     * 尝试登陆系统
     */
    private void attemptLogin() {

        // 如果存在任务，则不处理
        if (loginAsyncTask != null) {
            return;
        }

        // Reset errors.
        v_editText_phone.setError(null);
        v_editText_password.setError(null);

        String phone = v_editText_phone.getText().toString();
        String password = v_editText_password.getText().toString();
        String url = getString(R.string.url_host) + getString(R.string.url_api_user_login);

        boolean cancel = false;
        View focusView = null;

        // 验证数据 - 账号名不为空
        if (TextUtils.isEmpty(phone)) {
            v_editText_phone.setError(getString(R.string.error_field_required));
            focusView = v_editText_phone;
            focusView.requestFocus();
            return;
        }

        // 验证密码规则
        if (TextUtils.isEmpty(password)) {
            v_editText_password.setError(getString(R.string.error_invalid_password));
            focusView = v_editText_password;
            focusView.requestFocus();
            return;
        }


        // 执行异步
        loginAsyncTask = new LoginAsyncTask(this);
        LoginViewModel vm = new LoginViewModel(phone, password);
        LoginTaskModel taskModel = new LoginTaskModel(url, vm);
        loginAsyncTask.execute(taskModel);

        Log.d("attemptLogin", "开始执行异步操作execute");
    }


}