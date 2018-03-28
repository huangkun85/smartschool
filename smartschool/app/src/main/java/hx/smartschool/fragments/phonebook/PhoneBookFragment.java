package hx.smartschool.fragments.phonebook;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import hx.smartschool.R;
import hx.smartschool.activities.main.MainActivity;
import hx.smartschool.fragments.BaseFragment;
import hx.smartschool.util.hw.HwCmdBuilder;
import hx.smartschool.util.hw.HwContactModel;
import hx.smartschool.util.hw.HwCmdModel;
import hx.smartschool.util.hw.HwSendModel;
import hx.smartschool.util.mq.OnMsgReceivedEventListener;

public class PhoneBookFragment extends BaseFragment {


    private static final String ARG_PHONE = "ARG_PHONE";
    private static final String ARG_HWCODE = "ARG_HWCODE";

    private String mArgPhone;
    private String mArgHwCode;

    // UI

    private EditText mName1;
    private EditText mPhone1;

    private EditText mName2;
    private EditText mPhone2;

    private EditText mName3;
    private EditText mPhone3;

    private EditText mName4;
    private EditText mPhone4;

    private EditText mName5;
    private EditText mPhone5;

    private Button mBtnSave;

    //消息发送用
    private MainActivity mainActivity;

    //消息处理器，用于UI重绘
    private Handler mHandler;

    //消息队列监听器名字
    private String PEDOMETER_LISTENER_KEY = "MQListener_PHONE_BOOK";

    //消息队列监听器对象
    private OnMsgReceivedEventListener msgReceivedListener;


    public PhoneBookFragment() {
        // Required empty public constructor
    }


    public static PhoneBookFragment newInstance(String phone, String hwCode) {
        PhoneBookFragment fragment = new PhoneBookFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PHONE, phone);
        args.putString(ARG_HWCODE, hwCode);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void initParameter(Bundle argBundle) {
        if (argBundle != null) {
            mArgPhone = argBundle.getString(ARG_PHONE);
            mArgHwCode = argBundle.getString(ARG_HWCODE);
        }

        mainActivity = (MainActivity) this.getActivity();
    }

    @Override
    public View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_phone_book, container, false);

        mName1 = (EditText) view.findViewById(R.id.control_contact_name1);
        mPhone1 = (EditText) view.findViewById(R.id.control_contact_phone1);

        mName2 = (EditText) view.findViewById(R.id.control_contact_name2);
        mPhone2 = (EditText) view.findViewById(R.id.control_contact_phone2);

        mName3 = (EditText) view.findViewById(R.id.control_contact_name3);
        mPhone3 = (EditText) view.findViewById(R.id.control_contact_phone3);

        mName4 = (EditText) view.findViewById(R.id.control_contact_name4);
        mPhone4 = (EditText) view.findViewById(R.id.control_contact_phone4);

        mName5 = (EditText) view.findViewById(R.id.control_contact_name5);
        mPhone5 = (EditText) view.findViewById(R.id.control_contact_phone5);

        mBtnSave = (Button) view.findViewById(R.id.fragment_phonebook_btn_save);


        return view;
    }

    @Override
    public void initListener(View view) {

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<HwContactModel> data = new ArrayList<HwContactModel>();

                String strName = mName1.getText().toString();
                String strPhone = mPhone1.getText().toString();

                if (strName.length() > 0 && strName.length() > 0) {
                    data.add(new HwContactModel(strPhone, strPhone));
                }


                strName = mName2.getText().toString();
                strPhone = mPhone2.getText().toString();

                if (strName.length() > 0 && strName.length() > 0) {
                    data.add(new HwContactModel(strPhone, strPhone));
                }

                strName = mName3.getText().toString();
                strPhone = mPhone3.getText().toString();

                if (strName.length() > 0 && strName.length() > 0) {
                    data.add(new HwContactModel(strPhone, strPhone));
                }


                strName = mName4.getText().toString();
                strPhone = mPhone4.getText().toString();

                if (strName.length() > 0 && strName.length() > 0) {
                    data.add(new HwContactModel(strPhone, strPhone));
                }


                strName = mName5.getText().toString();
                strPhone = mPhone5.getText().toString();

                if (strName.length() > 0 && strName.length() > 0) {
                    data.add(new HwContactModel(strPhone, strPhone));
                }

                HwSendModel mqModel = HwCmdBuilder.PHONEBOOK(mArgHwCode, data);
                String json = mainActivity.getGson().toJson(mqModel);
                mainActivity.getRabitMQService().getMqSender().SendMessage(json);

            }


        });


        // 消息队列接受信息监听器
        msgReceivedListener = new OnMsgReceivedEventListener() {
            @Override
            public void newMqMsg(HwCmdModel receiveModel) {

                int cmdType = receiveModel.getExtCmdType();
                // Message处理
                if (cmdType == 4) {
                    mHandler.sendEmptyMessage(4);
                    return;
                }

            }
        };

        // 添加消息队列监听器
        mainActivity.getRabitMQService().getMqReceiver().addListener(this.PEDOMETER_LISTENER_KEY, msgReceivedListener);

        // 消息队列监听器 - 结束

        //使用handler来更新UI
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                switch (msg.what) {

                    case 4:
                        mainActivity.showAlertDialog("电话本", "保存电话本成功");
                        break;

                    default:
                        super.handleMessage(msg);
                        break;
                }
            }
        };


    }

    @Override
    public void SaveFragmentState() {

    }

    @Override
    public void LoadFragmentState() {

    }

}
