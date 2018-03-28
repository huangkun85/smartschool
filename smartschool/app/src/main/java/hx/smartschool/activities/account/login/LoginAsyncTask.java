package hx.smartschool.activities.account.login;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import hx.smartschool.R;
import hx.smartschool.activities.main.MainActivity;
import hx.smartschool.model.AccountStateStoreModel;
import hx.smartschool.model.account.login.LoginTaskModel;
import hx.smartschool.model.account.login.LoginTaskResultModel;
import hx.smartschool.util.net.HxHttpHelper;
import hx.smartschool.util.net.HxHttpResponse;

/**
 * 异步事件处理类
 */
public class LoginAsyncTask extends AsyncTask<LoginTaskModel, Integer, LoginTaskResultModel> {

    private LoginActivity loginActivity;
    private Gson gson;


    /**
     * 构造函数
     *
     * @param loginActivity
     */
    public LoginAsyncTask(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
        gson = loginActivity.getGson();
    }

    /**
     * 后台处理事件
     *
     * @param loginTaskModels
     * @return
     */
    @Override
    protected LoginTaskResultModel doInBackground(LoginTaskModel... loginTaskModels) {

        String jsonData = gson.toJson(loginTaskModels[0].getLoginViewModel());
        String url = loginTaskModels[0].getUrl();

        HxHttpResponse res = null;
        LoginTaskResultModel loginTaskResultModel = null;

        try {

            loginActivity.doShowLoadingDialog(100);
            // 发起登陆请求
            res = HxHttpHelper.DoPost(url, jsonData);
            Log.d("login_result", res.toString());

            loginTaskResultModel = new LoginTaskResultModel();
            loginTaskResultModel.setHttpResponse(res);


            //登陆成功消息
            if (res.getStatusCode() == 200) {

                String strJson = res.getContent();

                // 写入share存储
                AccountStateStoreModel model = loginActivity.getAccountStateHelper().setAccountState(strJson);

                //设置结果
                loginTaskResultModel.setValid(true);
                loginTaskResultModel.setAccountStateStoreModel(model);
                loginTaskResultModel.setDisplayMessage(loginActivity.getString(R.string.info_result_login_success));

            } else {
                loginTaskResultModel.setValid(false);
                loginTaskResultModel.setAccountStateStoreModel(null);
                loginTaskResultModel.setDisplayMessage(res.getContent());
            }

            return loginTaskResultModel;

        } catch (Exception e) {
            Log.e("LoginAsyncTask", "doInBackground: ", e);

            loginTaskResultModel.setValid(false);
            loginTaskResultModel.setAccountStateStoreModel(null);
            loginTaskResultModel.setDisplayMessage(loginActivity.getString(R.string.info_result_login_exception));

            return loginTaskResultModel;
        }
    }


    /**
     * 消息结果处理
     *
     * @param loginTaskResultModel
     */
    @Override
    protected void onPostExecute(LoginTaskResultModel loginTaskResultModel) {
        super.onPostExecute(loginTaskResultModel);

        loginActivity.doHideLoadingDialog(100);
        loginActivity.setLoginAsyncTask(null);
        loginActivity.showLongToast(loginTaskResultModel.getDisplayMessage());

        //登陆成功的消息处理
        if (loginTaskResultModel.isValid()) {
            loginActivity.doFinishThenStartActivity(loginActivity, MainActivity.class);
        }
    }

    @Override
    protected void onCancelled(LoginTaskResultModel loginTaskResultModel) {
        super.onCancelled(loginTaskResultModel);
        loginActivity.setLoginAsyncTask(null);
    }
}
