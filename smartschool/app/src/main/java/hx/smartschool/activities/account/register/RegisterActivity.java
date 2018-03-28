package hx.smartschool.activities.account.register;

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
import hx.smartschool.activities.account.login.LoginActivity;
import hx.smartschool.model.account.register.RegisterTaskModel;
import hx.smartschool.model.account.register.RegisterViewModel;

/**
 * A login screen that offers login via email/password.
 */
public class RegisterActivity extends BaseAppCompatActivity {

    private RegisterAsyncTask registerAsyncTask;


    private EditText _editText_phone; // 用户名
    private EditText _editText_email;
    private EditText _editText_password; // 密码
    private EditText _editText_confirm_password; // 密码
    private Button _button_signin; // 登陆按钮
    private Button _button_register; // 注册按钮


    public RegisterAsyncTask getRegisterAsyncTask() {
        return registerAsyncTask;
    }

    public void setRegisterAsyncTask(RegisterAsyncTask registerAsyncTask) {
        this.registerAsyncTask = registerAsyncTask;
    }


    @Override
    public int initLayout() {
        setContentView(R.layout.activity_register);
        return 0;
    }

    @Override
    public void initControls() {
        // Set up the login form.
        _editText_phone = (EditText) findViewById(R.id.ac_register_edit_phone);
        _editText_email = (EditText) findViewById(R.id.ac_register_edit_email);
        _editText_password = (EditText) findViewById(R.id.ac_register_edit_password);
        _editText_confirm_password = (EditText) findViewById(R.id.ac_register_edit_confirm_password);

        _button_signin = (Button) findViewById(R.id.ac_register_btn_login);
        _button_register = (Button) findViewById(R.id.ac_register_btn_register);

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

        // 软键盘完成或者回车事件
        _editText_confirm_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        // 登陆监听器
        _button_signin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                doFinishThenStartActivity(RegisterActivity.this, LoginActivity.class);
            }
        });

        // 注册按钮监听器
        _button_register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

    }

    @Override
    public void doOnStart() {

    }

    @Override
    public void doBackKeyPress() {

    }


    /**
     *
     */
    private void attemptRegister() {

        Log.d("Register", "attemptRegister");

        // 如果存在任务，则不处理
        if (registerAsyncTask != null) {
            return;
        }

        // Reset errors.
        _editText_phone.setError(null);
        _editText_email.setError(null);
        _editText_password.setError(null);
        _editText_confirm_password.setError(null);

        String phone = _editText_phone.getText().toString();
        String email = _editText_email.getText().toString();
        String password = _editText_password.getText().toString();
        String confirmPassword = _editText_confirm_password.getText().toString();
        String url = getString(R.string.url_host) + getString(R.string.url_api_user_register);

        boolean cancel = false;
        View focusView = null;

        // 验证数据 - 账号名不为空
        if (TextUtils.isEmpty(phone)) {
            _editText_phone.setError(getString(R.string.error_field_required));
            focusView = _editText_phone;
            focusView.requestFocus();
            return;
        }

        // 验证密码规则
        if (TextUtils.isEmpty(password)) {
            _editText_password.setError(getString(R.string.error_invalid_password));
            focusView = _editText_password;
            focusView.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)|| !confirmPassword.equals(password)) {
            _editText_confirm_password.setError(getString(R.string.error_invalid_confirm_password));
            focusView = _editText_confirm_password;
            focusView.requestFocus();
            return;
        }

        RegisterViewModel registerViewModel = new RegisterViewModel(phone, email, password, confirmPassword);
        RegisterTaskModel registerTaskModel = new RegisterTaskModel(url, registerViewModel);
        registerAsyncTask = new RegisterAsyncTask(this);
        registerAsyncTask.execute(registerTaskModel);
    }
}

