package hx.smartschool.fragments.pedo;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import hx.smartschool.R;
import hx.smartschool.activities.main.MainActivity;
import hx.smartschool.fragments.BaseFragment;
import hx.smartschool.util.SharedPreferencesHelper;
import hx.smartschool.util.hw.ExtCmdParameter;
import hx.smartschool.util.hw.HwCmdBuilder;
import hx.smartschool.util.hw.HwCmdModel;
import hx.smartschool.util.hw.HwSendModel;
import hx.smartschool.util.mq.OnMsgReceivedEventListener;


/**
 * 计步器Fragment，打开显示最后一次数据，图标显示过去10天的数据，切换记录数据状态
 */
public class PedometerFragment extends BaseFragment {

    // UI设置
    private TextView mHwCode;
    private TextView mPedoMeterDisplay;
    private TextView mTime1_Start;
    private TextView mTime1_End;
    private Button mBtn_SetTime;
    private Switch mSwitch_OnOff;

    // 初始化参数

    private static final String ARG_PHONE = "ARG_PHONE";
    private static final String ARG_HWCODE = "ARG_HWCODE";

    private String mArgPhone;
    private String mArgHwCode;

    // 消息队列监听器名字
    private static final String PEDOMETER_LISTENER_KEY = "MQListener_PedoMeter";

    // 消息队列监听器对象
    private OnMsgReceivedEventListener msgReceivedListener;

    // 消息处理器，用于UI重绘
    private Handler mHandler;


    //状态保持类
    private PedoFragmentState pedoFragmentState;


    /**
     * 默认无参数构造函数
     */
    public PedometerFragment() {

    }


    public static PedometerFragment newInstance(String phone, String hwCode) {
        PedometerFragment fragment = new PedometerFragment();
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

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pedometer, container, false);

        mPedoMeterDisplay = (TextView) view.findViewById(R.id.fragment_pedometer_display);

        mHwCode = (TextView) view.findViewById(R.id.fragment_pedometer_hwcode);
        mTime1_Start = (TextView) view.findViewById(R.id.fragment_pedometer_time1_start);
        mTime1_End = (TextView) view.findViewById(R.id.fragment_pedometer_time1_end);
        mBtn_SetTime = (Button) view.findViewById(R.id.fragment_pedometer_btn_set_time);
        mSwitch_OnOff = (Switch) view.findViewById(R.id.fragment_pedometer_switch);

        //设置状态属性
        mHwCode.setText("硬件编码:" + this.mArgHwCode);//来源于参数

        if (pedoFragmentState != null) {
            mTime1_Start.setText(pedoFragmentState.getTime_start()); //来源于状态
            mTime1_End.setText(pedoFragmentState.getTime_end());//来源于状态
            mSwitch_OnOff.setChecked(pedoFragmentState.isSwitchOn());//来源于状态
        }

        //加载缓存的LK列表，获取最后一次 步数内容

        String StoreKey = "LK." + this.mArgHwCode;

        //读取内部存储的LK列表
        String lkJson = SharedPreferencesHelper.readString(this.getContext(), this.preferencesName, StoreKey);

        Log.d("MQ.Rev.LK.Json", "Data:" + lkJson);

        Type type = new TypeToken<ArrayList<HwCmdModel>>() {

        }.getType();

        ArrayList<HwCmdModel> lkList = null;

        try {
            //获取当前列表
            lkList = mainActivity.getGson().fromJson(lkJson, type);
        } catch (Exception ex) {
            lkList=null;
        }


        mPedoMeterDisplay.setText("[步数]等待手表连接数据");

        if (lkList != null && lkList.size() > 0) {
            HwCmdModel lastModel = lkList.get(lkList.size() - 1);
            mPedoMeterDisplay.setText("[步数]" + lastModel.getExtCmdParameter().getSteps());
        }


        return view;
    }

    @Override
    public void initListener(View view) {


        //使用handler来更新UI
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                switch (msg.what) {

                    case 0:
                        Log.d("handleMessage", "0");

                        Bundle bundle = msg.getData();
                        String steps = bundle.getString("data");
                        mPedoMeterDisplay.setText(steps);
                        mainActivity.showShortToast("更新步数");
                        break;

                    case 1:

                        Log.d("handleMessage", "1");
                        mainActivity.showLongToast("开关设置成功");
                        break;

                    case 2:
                        Log.d("handleMessage", "2");
                        mainActivity.showLongToast("计步器时间段设置成功");
                        break;


                    default:
                        super.handleMessage(msg);
                        break;
                }
            }
        };

        // 接受信息
        msgReceivedListener = new OnMsgReceivedEventListener() {
            @Override
            public void newMqMsg(HwCmdModel receiveModel) {

                int cmdType = receiveModel.getExtCmdType();
                ExtCmdParameter extCmdParameter = receiveModel.getExtCmdParameter();


                Log.d("计步器Listener收到新消息", "Cmd_Type:" + cmdType + "para:" + (extCmdParameter == null));


                if (cmdType == 1) {

                    int steps = extCmdParameter.getSteps();
                    Bundle bundle = new Bundle();
                    bundle.putString("data", "步数：" + steps);

                    Message message = new Message();
                    message.what = 0;
                    message.arg1 = steps;
                    message.setData(bundle);

                    mHandler.sendMessageDelayed(message, 100);
                    return;
                }

                if (cmdType == 5) {

                    mHandler.sendEmptyMessage(1);
                    return;
                }

                if (cmdType == 16) {
                    mHandler.sendEmptyMessage(2);
                    return;

                }


            }
        };

        // 添加监听器
        mainActivity.getRabitMQService().getMqReceiver().addListener(this.PEDOMETER_LISTENER_KEY, msgReceivedListener);


        mTime1_Start.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        String strHour = String.format("%02d", hourOfDay);
                        String strMinute = String.format("%02d", minute);

                        mTime1_Start.setText(strHour + ":" + strMinute);
                    }
                };


                TimePickerDialog dialog = new TimePickerDialog(PedometerFragment.this.getContext(), listener, 0, 0, true);

                dialog.show();//显示日期设置对话框

                return false;
            }
        });


        mTime1_End.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        String strHour = String.format("%02d", hourOfDay);
                        String strMinute = String.format("%02d", minute);
                        mTime1_End.setText(strHour + ":" + strMinute);
                    }
                };


                TimePickerDialog dialog = new TimePickerDialog(PedometerFragment.this.getContext(), listener, 0, 0, true);

                dialog.show();//显示日期设置对话框

                return false;
            }
        });


        /**
         * 时间设置开关
         */
        mBtn_SetTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String hwCode = mArgHwCode;
                String T1_Start = mTime1_Start.getText().toString();
                String T1_End = mTime1_End.getText().toString();

                String Ts1 = T1_Start + "-" + T1_End;
                String[] wTs = {Ts1, Ts1, Ts1};

                HwSendModel mqModel = HwCmdBuilder.WALKTIME(hwCode, wTs);
                String json = mainActivity.getGson().toJson(mqModel);

                mainActivity.getRabitMQService().getMqSender().SendMessage(json);
            }
        });

        /**
         * 按键开关
         */
        mSwitch_OnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("PetoMete.Switch", "开关 " + isChecked);

                if (isChecked) {
                    HwSendModel mqModel = HwCmdBuilder.PEDO1(mArgHwCode);
                    String json = mainActivity.getGson().toJson(mqModel);
                    mainActivity.getRabitMQService().getMqSender().SendMessage(json);

                } else {

                    HwSendModel mqModel = HwCmdBuilder.PEDO0(mArgHwCode);
                    String json = mainActivity.getGson().toJson(mqModel);
                    mainActivity.getRabitMQService().getMqSender().SendMessage(json);
                }

            }
        });


    }


    @Override
    public void SaveFragmentState() {

        //保存当前Fragment的设置状态
        pedoFragmentState = new PedoFragmentState();
        pedoFragmentState.setSwitchOn(mSwitch_OnOff.isChecked());
        pedoFragmentState.setTime_start(mTime1_Start.getText().toString());
        pedoFragmentState.setTime_end(mTime1_End.getText().toString());

        String strJson = mainActivity.getGson().toJson(pedoFragmentState);

        SharedPreferencesHelper.writeString(mainActivity, this.preferencesName, "PedometerFragment", strJson);
        Log.d("Pedo.Save", "保存状态：" + strJson);

    }

    @Override
    public void LoadFragmentState() {

        //不可以放在OnAttach中运行

        String strJson = SharedPreferencesHelper.readString(mainActivity, this.preferencesName, "PedometerFragment");
        Log.d("Pedo.Load", "获取存储：" + strJson);

        if (null != strJson && strJson.length() > 5) {

            // 反序列化
            Type type = new TypeToken<PedoFragmentState>() {

            }.getType();

            pedoFragmentState = mainActivity.getGson().fromJson(strJson, type);
            Log.d("Pedo.Load", "加载状态 对象 " + strJson);
        }


    }


}
