package hx.smartschool.fragments.sos;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import hx.smartschool.R;
import hx.smartschool.activities.main.MainActivity;
import hx.smartschool.fragments.BaseFragment;
import hx.smartschool.util.hw.HwCmdBuilder;
import hx.smartschool.util.hw.HwCmdModel;
import hx.smartschool.util.hw.HwSendModel;
import hx.smartschool.util.mq.OnMsgReceivedEventListener;


public class SosFragment extends BaseFragment {

    private static final String ARG_PHONE = "ARG_PHONE";
    private static final String ARG_HWCODE = "ARG_HWCODE";

    private String mArgPhone;
    private String mArgHwCode;

    // UI

    private EditText mText_SOS1;
    private Button mBtn_SOS1;

    private EditText mText_SOS2;
    private Button mBtn_SOS2;

    private EditText mText_SOS3;
    private Button mBtn_SOS3;

    private Button mBtnSaveAll;


    //消息发送用
    private MainActivity mainActivity;

    //消息处理器，用于UI重绘
    private Handler mHandler;

    //消息队列监听器名字
    private String PEDOMETER_LISTENER_KEY = "MQListener_SOS";

    //消息队列监听器对象
    private OnMsgReceivedEventListener msgReceivedListener;


    public SosFragment() {
        // Required empty public constructor
    }


    public static SosFragment newInstance(String phone, String hwCode) {
        SosFragment fragment = new SosFragment();
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

        View view = inflater.inflate(R.layout.fragment_sos, container, false);

        mText_SOS1 = (EditText) view.findViewById(R.id.fragment_sos_layout_sos1_phone);
        mBtn_SOS1 = (Button) view.findViewById(R.id.fragment_sos_layout_sos1_btn_save);

        mText_SOS2 = (EditText) view.findViewById(R.id.fragment_sos_layout_sos2_phone);
        mBtn_SOS2 = (Button) view.findViewById(R.id.fragment_sos_layout_sos2_btn_save);

        mText_SOS3 = (EditText) view.findViewById(R.id.fragment_sos_layout_sos3_phone);
        mBtn_SOS3 = (Button) view.findViewById(R.id.fragment_sos_layout_sos3_btn_save);

        mBtnSaveAll = (Button) view.findViewById(R.id.fragment_sos_btn_save_all);


        return view;
    }

    @Override
    public void initListener(View view) {

        initMsgListener();
        initActionListener();

    }

    @Override
    public void SaveFragmentState() {

    }

    @Override
    public void LoadFragmentState() {

    }


    private void initMsgListener() {
        // 消息队列接受信息监听器
        msgReceivedListener = new OnMsgReceivedEventListener() {
            @Override
            public void newMqMsg(HwCmdModel receiveModel) {

                int cmdType = receiveModel.getExtCmdType();
                // Message处理
                if (cmdType == 9 || cmdType == 10 || cmdType == 11 || cmdType == 12) {
                    mHandler.sendEmptyMessage(cmdType);
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

                    case 9:
                        mainActivity.showLongToast("保存SOS1电话成功");
                        break;

                    case 10:
                        mainActivity.showLongToast("保存SOS2电话成功");
                        break;

                    case 11:
                        mainActivity.showLongToast("保存SOS3电话成功");
                        break;

                    case 12:
                        mainActivity.showLongToast("保存SOS全部电话成功");
                        break;
                    default:
                        super.handleMessage(msg);
                        break;
                }
            }
        };


    }


    private void initActionListener() {

        mBtn_SOS1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mText_SOS1.getText().toString();

                if (phone.length() > 0) {
                    HwSendModel mqModel = HwCmdBuilder.SOS1(mArgHwCode, phone);
                    String json = mainActivity.getGson().toJson(mqModel);
                    mainActivity.getRabitMQService().getMqSender().SendMessage(json);
                    mainActivity.showShortToast("正在保存SOS1");
                }


            }
        });

        mBtn_SOS2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mText_SOS2.getText().toString();

                if (phone.length() > 0) {
                    HwSendModel mqModel = HwCmdBuilder.SOS2(mArgHwCode, phone);
                    String json = mainActivity.getGson().toJson(mqModel);
                    mainActivity.getRabitMQService().getMqSender().SendMessage(json);
                    mainActivity.showShortToast("正在保存SOS2");
                }


            }
        });


        mBtn_SOS3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mText_SOS3.getText().toString();

                if (phone.length() > 0) {
                    HwSendModel mqModel = HwCmdBuilder.SOS3(mArgHwCode, phone);
                    String json = mainActivity.getGson().toJson(mqModel);
                    mainActivity.getRabitMQService().getMqSender().SendMessage(json);
                    mainActivity.showShortToast("正在保存SOS3");
                }


            }
        });


    }


}
