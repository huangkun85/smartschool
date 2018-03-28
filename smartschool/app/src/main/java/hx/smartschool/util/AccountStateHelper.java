package hx.smartschool.util;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import hx.smartschool.R;
import hx.smartschool.model.AccountStateStoreModel;

/**
 * 对登陆状态的维护与管理，使用本地缓存
 */

public class AccountStateHelper {

    private Gson gson;
    private Context context;
    private String storePreference;
    private String storeAccountStateKey;


    public AccountStateHelper(Context context) {

        this.gson = new Gson();
        this.context = context;

        storePreference = this.context.getString(R.string.app_store_preferences_name);
        storeAccountStateKey = this.context.getString(R.string.app_store_account_state);
    }

    /**
     * 获取账户状态
     *
     * @return
     */
    public AccountStateStoreModel getAccountState() {


        // 从共享存储中读取Json
        String strAccountStatusJson = SharedPreferencesHelper.readString(this.context, storePreference, storeAccountStateKey);
        if (strAccountStatusJson == null) {
            Log.i("AccountStateHelper", "获取用户状态为空");
            return null;
        }

        // 反序列化
        Type type = new TypeToken<AccountStateStoreModel>() {

        }.getType();

        AccountStateStoreModel model = gson.fromJson(strAccountStatusJson, type);
        Log.d("AccountStateHelper", "获取用户状态为：" + strAccountStatusJson);
        return model;
    }


    /**
     * 设置账户状态
     *
     * @param accountState
     */
    public void setAccountState(AccountStateStoreModel accountState) {


        storePreference = context.getString(R.string.app_store_preferences_name);
        storeAccountStateKey = context.getString(R.string.app_store_account_state);

        String json1 = gson.toJson(accountState);
        // Json写入共享存储
        SharedPreferencesHelper.writeString(context, storePreference, storeAccountStateKey, json1);
        Log.i("AccountStateHelper", "设置用户状态为：" + json1);
    }


    /**
     * 设置账户状态
     *
     * @param json
     * @return
     */
    public AccountStateStoreModel setAccountState(String json) {


        //反序列化
        Type type = new TypeToken<AccountStateStoreModel>() {

        }.getType();

        AccountStateStoreModel model = gson.fromJson(json, type);

        SharedPreferencesHelper.writeString(context, storePreference, storeAccountStateKey, json);
        Log.i("AccountStateHelper", "设置用户状态为：" + json);
        return model;

    }


    /**
     * 设置位Null
     */
    public void clearAccountState() {

        SharedPreferencesHelper.writeString(context, storePreference, storeAccountStateKey, null);
        Log.i("AccountStateHelper", "设置用户状态为：null");


    }


}
