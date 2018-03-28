package hx.smartschool.fragments.setting;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import hx.smartschool.R;
import hx.smartschool.fragments.BaseFragment;
import hx.smartschool.model.AccountStateStoreModel;
import hx.smartschool.util.net.HxHttpHelper;
import hx.smartschool.util.net.HxHttpResponse;


public class BindDeviceFragment extends BaseFragment {

    //ui
    private EditText mHwCode;
    private EditText mSmsMobileNumber;
    private Button mBtnSave;
    private TextView mTxtPhone;


    //启动参数
    private static final String ARG_PHONE = "ARG_PHONE";
    private static final String ARG_HWCODE = "ARG_HWCODE";
    private static final String ARG_SOSPHONE = "ARG_SOSPHONE";

    private String argPhone;
    private String argHwCode;
    private String argSosPhone;


    private SaveBindMainDeviceAsyncTask saveBindMainDeviceAsyncTask;


    public BindDeviceFragment() {
        // Required empty public constructor
    }

    /**
     * 创建实例
     *
     * @param phone
     * @param hwCode
     * @return
     */
    public static BindDeviceFragment newInstance(String phone, String hwCode, String sosPhone) {
        BindDeviceFragment fragment = new BindDeviceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PHONE, phone);
        args.putString(ARG_HWCODE, hwCode);
        args.putString(ARG_SOSPHONE, sosPhone);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initParameter(Bundle argBundle) {
        if (argBundle != null) {
            argPhone = argBundle.getString(ARG_PHONE);
            argHwCode = argBundle.getString(ARG_HWCODE);
            argSosPhone = argBundle.getString(ARG_SOSPHONE);
        }

    }

    @Override
    public View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bind_device, container, false);

        mTxtPhone = view.findViewById(R.id.fragment_bind_device_text_phone);
        mHwCode = view.findViewById(R.id.fragment_bind_device_edit_hwcode);
        mSmsMobileNumber = view.findViewById(R.id.fragment_bind_device_edit_sms);
        mBtnSave = view.findViewById(R.id.fragment_bind_device_btn_save);


        mHwCode.setText(argHwCode);
        mTxtPhone.setText("当前用户" + argPhone);

        mSmsMobileNumber.setText(argSosPhone);
        return view;
    }

    @Override
    public void initListener(View view) {

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = getString(R.string.url_host) + getString(R.string.url_api_user_bind_main_device);

                String hwCode = mHwCode.getText().toString();
                String hwSmsMobile = mSmsMobileNumber.getText().toString();

                mainActivity.doShowLoadingDialog(0);

                saveBindMainDeviceAsyncTask = new SaveBindMainDeviceAsyncTask();
                saveBindMainDeviceAsyncTask.execute(url, argPhone, hwCode, hwSmsMobile);


            }
        });
    }

    @Override
    public void SaveFragmentState() {

    }

    @Override
    public void LoadFragmentState() {

    }

    private class RequestModel {

        public String PhoneNumber;
        public String HwCode;
        public String SosPhone;


        public RequestModel(String phoneNumber, String hwCode, String sosPhone) {
            PhoneNumber = phoneNumber;
            HwCode = hwCode;
            SosPhone = sosPhone;
        }

        public String getPhoneNumber() {
            return PhoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            PhoneNumber = phoneNumber;
        }

        public String getHwCode() {
            return HwCode;
        }

        public void setHwCode(String hwCode) {
            HwCode = hwCode;
        }

        public String getSosPhone() {
            return SosPhone;
        }

        public void setSosPhone(String sosPhone) {
            SosPhone = sosPhone;
        }
    }


    /**
     * 处理对外发请求
     */
    protected class SaveBindMainDeviceAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mainActivity.doHideLoadingDialog(200);
            mainActivity.showLongToast(s);
        }


        @Override
        protected String doInBackground(String... strings) {

            String url = strings[0];
            String phone = strings[1];
            String hwCode = strings[2];
            String SosPhone = strings[3];

            RequestModel model = new RequestModel(phone, hwCode, SosPhone);
            String jsonData = mainActivity.getGson().toJson(model);

            HxHttpResponse res = null;
            try {
                res = HxHttpHelper.DoPost(url, jsonData);

                if (res.getStatusCode() == 200) {
                    // 重设状态信息
                    AccountStateStoreModel accountStateStoreModel = mainActivity.getAccountStateHelper().getAccountState();
                    accountStateStoreModel.setMainHwCode(hwCode);
                    mainActivity.getAccountStateHelper().setAccountState(accountStateStoreModel);

                    // 重设监听信息
                    String[] bindingKey = {"3G." + hwCode};
                    mainActivity.getRabitMQService().getMqReceiver().closeChannel().join();
                    mainActivity.getRabitMQService().getMqReceiver().startListener(bindingKey);
                }

                Log.d("BindDevice.HttpCode", res.toString());

            } catch (Exception e) {

                e.printStackTrace();
            }


            return res.getContent();
        }
    }


}
