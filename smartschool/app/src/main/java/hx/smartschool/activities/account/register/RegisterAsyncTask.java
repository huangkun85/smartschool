package hx.smartschool.activities.account.register;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import hx.smartschool.activities.account.login.LoginActivity;
import hx.smartschool.model.account.register.RegisterTaskModel;
import hx.smartschool.model.account.register.RegisterTaskResultModel;
import hx.smartschool.util.net.HxHttpHelper;
import hx.smartschool.util.net.HxHttpResponse;

/**
 * 异步事件处理类
 */
public class RegisterAsyncTask extends AsyncTask<RegisterTaskModel, Integer, RegisterTaskResultModel> {


    private RegisterActivity registerActivity;
    private Gson gson;


    public RegisterAsyncTask(RegisterActivity registerActivity) {
        this.registerActivity = registerActivity;
        gson = this.registerActivity.getGson();
    }

    @Override
    protected RegisterTaskResultModel doInBackground(RegisterTaskModel... registerTaskModels) {

        String jsonData = gson.toJson(registerTaskModels[0].getLoginViewModel());
        String url = registerTaskModels[0].getUrl();

        RegisterTaskResultModel registerTaskResultModel = new RegisterTaskResultModel();
        HxHttpResponse res = null;


        try {

            registerActivity.doShowLoadingDialog(100);
            res = HxHttpHelper.DoPost(url, jsonData);

            registerTaskResultModel.setHttpResponse(res);
            registerTaskResultModel.setDisplayMessage(res.getContent());

            if (res.getStatusCode() == 200) {
                registerTaskResultModel.setValid(true);

            } else {
                registerTaskResultModel.setValid(false);
            }


        } catch (Exception e) {
            Log.e("RegisterAsyncTask", "doInBackground: ", e);
            registerTaskResultModel.setValid(false);
            registerTaskResultModel.setDisplayMessage("网络异常，暂时无法使用");

        }

        return registerTaskResultModel;
    }

    @Override
    protected void onPostExecute(RegisterTaskResultModel registerTaskResultModel) {
        super.onPostExecute(registerTaskResultModel);


        registerActivity.doHideLoadingDialog(100);
        registerActivity.setRegisterAsyncTask(null);
        registerActivity.showLongToast(registerTaskResultModel.getDisplayMessage());

        //登陆成功的场合
        if (registerTaskResultModel.isValid()) {
            this.registerActivity.doFinishThenStartActivity(this.registerActivity, LoginActivity.class);
        }


    }

    @Override
    protected void onCancelled(RegisterTaskResultModel registerTaskResultModel) {
        super.onCancelled(registerTaskResultModel);
        registerActivity.setRegisterAsyncTask(null);

    }


}
