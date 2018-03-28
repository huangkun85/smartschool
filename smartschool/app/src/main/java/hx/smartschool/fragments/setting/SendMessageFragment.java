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


public class SendMessageFragment extends BaseFragment {

    private static final String ARG_PHONE = "ARG_PHONE";
    private static final String ARG_HWCODE = "ARG_HWCODE";

    private String mArgPhone;
    private String mArgHwCode;

    private MainActivity mainActivity;

    //UI

    private EditText mMessageContent;
    private Button mBtnSend;
    private TextView hwCode;


    //消息处理器，用于UI重绘
    private Handler mHandler;

    //消息队列监听器名字
    private String PEDOMETER_LISTENER_KEY = "MQListener_Send_Message";

    //消息队列监听器对象
    private OnMsgReceivedEventListener msgReceivedListener;


    public SendMessageFragment() {
        // Required empty public constructor
    }


    public static SendMessageFragment newInstance(String phone, String hwCode) {
        SendMessageFragment fragment = new SendMessageFragment();
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


        View view = inflater.inflate(R.layout.fragment_send_message, container, false);

        mMessageContent = (EditText) view.findViewById(R.id.fragment_send_message_edit);
        mBtnSend = (Button) view.findViewById(R.id.fragment_send_message_btn_send);
        hwCode = (TextView) view.findViewById(R.id.fragment_send_message_text_hwcode);

        hwCode.setText("手表编码：" + mArgHwCode);

        return view;
    }




    @Override
    public void initListener(View view) {

        //**点击发送按钮 -开始
        mBtnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.d("SendMsg.Click", "按键发送信息");
                attempSendMessage();


            }
        });


        // 软键盘完成或者回车事件
        mMessageContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {

                    Log.d("SendMsg.Click", "软键盘发送信息");
                    attempSendMessage();
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
                if (cmdType == 6) {
                    mHandler.sendEmptyMessage(6);
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

                    case 6:
                        mainActivity.showLongToast("短信发送成功");
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


    private  void  attempSendMessage(){

        String msgContent = mMessageContent.getText().toString();
        HwSendModel mqModel = HwCmdBuilder.MESSAGE(mArgHwCode, msgContent);
        String json = mainActivity.getGson().toJson(mqModel);
        mainActivity.getRabitMQService().getMqSender().SendMessage(json);


    }


}
