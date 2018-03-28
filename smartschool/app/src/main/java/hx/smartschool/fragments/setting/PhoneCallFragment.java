package hx.smartschool.fragments.setting;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import hx.smartschool.R;
import hx.smartschool.activities.main.MainActivity;
import hx.smartschool.fragments.BaseFragment;
import hx.smartschool.util.hw.HwCmdBuilder;
import hx.smartschool.util.hw.HwCmdModel;
import hx.smartschool.util.hw.HwSendModel;
import hx.smartschool.util.mq.OnMsgReceivedEventListener;


public class PhoneCallFragment extends BaseFragment {

    private static final String ARG_PHONE = "ARG_PHONE";
    private static final String ARG_HWCODE = "ARG_HWCODE";

    private String mArgPhone;
    private String mArgHwCode;



    //UI

    private EditText mPhone;
    private Button mBtnCall;
    private TextView hwCode;


    private MainActivity mainActivity;

    //消息处理器，用于UI重绘
    private Handler mHandler;

    //消息队列监听器名字
    private String PEDOMETER_LISTENER_KEY = "MQListener_PHONE_Call";

    //消息队列监听器对象
    private OnMsgReceivedEventListener msgReceivedListener;



    public PhoneCallFragment() {
        // Required empty public constructor
    }


    public static PhoneCallFragment newInstance(String phone, String hwCode) {
        PhoneCallFragment fragment = new PhoneCallFragment();
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

        View view = inflater.inflate(R.layout.fragment_phone_call, container, false);
        mPhone = (EditText) view.findViewById(R.id.fragment_phone_edit);
        mBtnCall = (Button) view.findViewById(R.id.fragment_phone_btn_call);
        hwCode = (TextView) view.findViewById(R.id.fragment_phone_text_hwcode);
        hwCode.setText("手表编码：" + mArgHwCode);
        return view;
    }


    @Override
    public void initListener(View view) {

        //**点击发送按钮 -开始
        mBtnCall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.d("SendMsg.Click", "按键");
                attempAction();


            }
        });


        // 软键盘完成或者回车事件
        mPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {

                    Log.d("SendMsg.Click", "软键盘");
                    attempAction();
                    return true;
                }
                return false;
            }
        });


        //**点击发送按钮 -结束


        // 消息队列监听器 - 开始


        // 消息队列接受信息监听器
        msgReceivedListener = new OnMsgReceivedEventListener() {
            @Override
            public void newMqMsg(HwCmdModel receiveModel) {

                int cmdType = receiveModel.getExtCmdType();
                // Message处理
                if (cmdType == 8) {
                    mHandler.sendEmptyMessage(8);
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

                    case 8:
                        mainActivity.showAlertDialog("拨号", "手表正在拨打号码[" + mPhone.getText().toString() + "]");
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


    private void attempAction() {

        String data = mPhone.getText().toString();
        HwSendModel mqModel = HwCmdBuilder.CALL(mArgHwCode, data);
        String json = mainActivity.getGson().toJson(mqModel);
        mainActivity.getRabitMQService().getMqSender().SendMessage(json);
    }


}
