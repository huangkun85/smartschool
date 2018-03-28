package hx.smartschool.fragments.setting;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import hx.smartschool.R;
import hx.smartschool.fragments.BaseFragment;
import hx.smartschool.util.SharedPreferencesHelper;
import hx.smartschool.util.hw.ExtCmdParameter;
import hx.smartschool.util.hw.HwCmdBuilder;
import hx.smartschool.util.hw.HwCmdModel;
import hx.smartschool.util.hw.HwSendModel;
import hx.smartschool.util.mq.OnMsgReceivedEventListener;


public class HwSettingFragment extends BaseFragment {


    private static final String ARG_PHONE = "ARG_PHONE";
    private static final String ARG_HWCODE = "ARG_HWCODE";


    private String mArgPhone;
    private String mArgHwCode;


    //ui
    private LinearLayout mWatchVersion;
    private LinearLayout mWatchFind;
    private LinearLayout mWatchReboot;
    private LinearLayout mWatchShutdown;

    private LinearLayout mWatchMessage;
    private LinearLayout mWatchCall;
    private LinearLayout mWatchPhonebook;
    private LinearLayout mWatchSos; //SOS
    private LinearLayout mWatchFactory; //回复出厂
    private LinearLayout mWatchBinding; //手表绑定


    private TextView mHwCode;
    private TextView mBattery;
    private TextView mHwVersion;


    //消息处理器，用于UI重绘
    private Handler mHandler;

    //消息队列监听器名字
    private String PEDOMETER_LISTENER_KEY = "MQListener_Basic_Setting";

    //消息队列监听器对象
    private OnMsgReceivedEventListener msgReceivedListener;


    private int dataCount = 10;
    private List<HwCmdModel> lkList;


    public HwSettingFragment() {
        // Required empty public constructor
    }


    public static HwSettingFragment newInstance(String phone, String hwCode) {
        HwSettingFragment fragment = new HwSettingFragment();
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

        lkList = loadLKData();

    }


    @Override
    public View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hw_setting, container, false);
        //ui

        mWatchVersion = (LinearLayout) view.findViewById(R.id.fragment_basic_setting_item_watch_version);
        mWatchFind = (LinearLayout) view.findViewById(R.id.fragment_basic_setting_item_watch_find);
        mWatchReboot = (LinearLayout) view.findViewById(R.id.fragment_basic_setting_item_watch_reboot);
        mWatchShutdown = (LinearLayout) view.findViewById(R.id.fragment_basic_setting_item_watch_shutdown);

        mWatchMessage = (LinearLayout) view.findViewById(R.id.fragment_basic_setting_item_watch_message);
        mWatchCall = (LinearLayout) view.findViewById(R.id.fragment_basic_setting_item_watch_call);
        mWatchPhonebook = (LinearLayout) view.findViewById(R.id.fragment_basic_setting_item_watch_phonebook);
        mWatchSos = (LinearLayout) view.findViewById(R.id.fragment_basic_setting_item_watch_sos);
        mWatchFactory = (LinearLayout) view.findViewById(R.id.fragment_basic_setting_item_watch_factory);
        mWatchBinding = (LinearLayout) view.findViewById(R.id.fragment_basic_setting_item_watch_binding);


        mHwCode = (TextView) view.findViewById(R.id.fragment_hw_setting_text_hwcode);
        mBattery = (TextView) view.findViewById(R.id.fragment_hw_setting_text_battery);
        mHwVersion = (TextView) view.findViewById(R.id.fragment_basic_setting_hw_version);

        mHwCode.setText("手表编码:\t" + mArgHwCode);
        mBattery.setText("手表电量:\t正在获取...");

        Log.d("HW.Setting.LK", "Size:" + lkList.size());
        if (lkList != null && lkList.size() > 0) {
            HwCmdModel hwCmdModel = lkList.get(lkList.size() - 1);
            int batteryPercentage = hwCmdModel.getExtCmdParameter().getBatteryPercentage();
            mBattery.setText("手表电量:\t" + batteryPercentage + "%");
        }


        return view;
    }

    @Override
    public void initListener(View view) {
        initUIMsgListener();
        initActionListener();
    }

    @Override
    public void SaveFragmentState() {

    }

    @Override
    public void LoadFragmentState() {

    }


    /**
     * 动作触发
     */
    private void initActionListener() {

        // 01 查看版本号
        mWatchVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("hwSetting.Click", "查看版本号");

                HwSendModel mqModel = HwCmdBuilder.VERSION(mArgHwCode);
                String json = mainActivity.getGson().toJson(mqModel);
                mainActivity.getRabitMQService().getMqSender().SendMessage(json);
            }
        });

        // 02寻找手表
        mWatchFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("hwSetting.Click", "寻找手表");

                HwSendModel mqModel = HwCmdBuilder.FIND(mArgHwCode);
                String json = mainActivity.getGson().toJson(mqModel);
                mainActivity.getRabitMQService().getMqSender().SendMessage(json);
            }
        });


        mWatchReboot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("hwSetting.Click", "重启手表");
                HwSendModel mqModel = HwCmdBuilder.RESET(mArgHwCode);
                String json = mainActivity.getGson().toJson(mqModel);
                mainActivity.getRabitMQService().getMqSender().SendMessage(json);
            }
        });


        mWatchShutdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("hwSetting.Click", "手表关闭");
                HwSendModel mqModel = HwCmdBuilder.POWEROFF(mArgHwCode);
                String json = mainActivity.getGson().toJson(mqModel);
                mainActivity.getRabitMQService().getMqSender().SendMessage(json);
            }
        });


        mWatchFactory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("hwSetting.Click", "回复出厂设置");
                HwSendModel mqModel = HwCmdBuilder.FACTORY(mArgHwCode);
                String json = mainActivity.getGson().toJson(mqModel);
                mainActivity.getRabitMQService().getMqSender().SendMessage(json);
            }
        });


        mWatchMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("hwSetting.Click", "发送消息");
                //加载Fragment
                mListener.onFragmentInteraction(1, "MESSAGE");

            }
        });


        mWatchCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("hwSetting.Click", "发送消息");
                //加载Fragment
                mListener.onFragmentInteraction(1, "CALL");

            }
        });


        mWatchPhonebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("hwSetting.Click", "电话本");
                //加载Fragment
                mListener.onFragmentInteraction(1, "PHONEBOOK");

            }
        });

        mWatchSos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("hwSetting.Click", "SOS");
                //加载Fragment
                mListener.onFragmentInteraction(1, "SOS");

            }
        });


        mWatchBinding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("hwSetting.Click", "BINDING");
                //加载Fragment
                mListener.onFragmentInteraction(1, "BINDING");

            }
        });


    }


    /**
     * ui 消息 监听器
     */
    private void initUIMsgListener() {

        //使用handler来更新UI
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                String data = msg.getData().getString("data");

                switch (msg.what) {


                    case 0:

                        mainActivity.showAlertDialog("重启", data);
                        break;

                    case 1:

                        mBattery.setText(data);
                        mainActivity.showLongToast("状态更新");
                        break;

                    case 2:

                        mHwVersion.setText(data);
                        mainActivity.showAlertDialog("硬件版本号", data);
                        break;

                    case 3:

                        mainActivity.showAlertDialog("寻找手机", "手机振铃中");
                        break;


                    case 18:
                        mainActivity.showAlertDialog("关机", data);
                        break;


                    default:
                        super.handleMessage(msg);
                        break;
                }
            }
        };


        // 消息队列接受信息监听器
        msgReceivedListener = new OnMsgReceivedEventListener() {
            @Override
            public void newMqMsg(HwCmdModel receiveModel) {

                int cmdType = receiveModel.getExtCmdType();
                ExtCmdParameter extCmdParameter = receiveModel.getExtCmdParameter();
                // LK处理
                if (cmdType == 1) {

                    int batteryPercentage = extCmdParameter.getBatteryPercentage();

                    Bundle bundle = new Bundle();
                    bundle.putString("data", "手表电量:\t" + batteryPercentage + "%");

                    Message message = new Message();
                    message.what = 1;
                    message.setData(bundle);

                    mHandler.sendMessageDelayed(message, 100);
                    return;
                }

                // 版本处理
                if (cmdType == 2) {

                    Bundle bundle = new Bundle();
                    bundle.putString("data", extCmdParameter.getHwVersion());

                    Message message = new Message();
                    message.what = 2;
                    message.setData(bundle);

                    mHandler.sendMessageDelayed(message, 100);

                    return;
                }

                // 版本处理
                if (cmdType == 3) {
                    mHandler.sendEmptyMessage(3);
                    return;
                }

                // 0 Reset 重启
                if (cmdType == 0) {

                    Bundle bundle = new Bundle();
                    bundle.putString("data", "手表正在重启");
                    Message message = new Message();
                    message.what = 0;
                    message.setData(bundle);
                    mHandler.sendMessageDelayed(message, 100);
                    return;

                }

                // 0 Reset 关机
                if (cmdType == 18) {

                    Bundle bundle = new Bundle();
                    bundle.putString("data", "手表正在关机");

                    Message message = new Message();
                    message.what = 18;
                    message.setData(bundle);

                    mHandler.sendMessageDelayed(message, 100);

                    return;

                }


            }
        };

        // 添加消息队列监听器
        mainActivity.getRabitMQService().getMqReceiver().addListener(this.PEDOMETER_LISTENER_KEY, msgReceivedListener);


    }


    /**
     * 装载最新的10条LK列表
     *
     * @return
     */
    private List<HwCmdModel> loadLKData() {

        List<HwCmdModel> storeList = null;

        String pn = this.mainActivity.getString(R.string.app_store_preferences_name);

        String StoreKey = "LK." + mArgHwCode;

        //读取内部存储的列表
        String storeJson = SharedPreferencesHelper.readString(mainActivity, pn, StoreKey);
        Log.d("Setting.Fragment.Read", "读LK存储：" + StoreKey + " 内容 = " + storeJson);

        //数据存在的情况
        if (null != storeJson && storeJson.length() > 5) {

            Type type = new TypeToken<List<HwCmdModel>>() {

            }.getType();

            storeList = mainActivity.getGson().fromJson(storeJson, type);
            Log.d("Store.J2List", storeJson);
        } else {
            storeList = new ArrayList<HwCmdModel>();
            Log.d("Store.NewList", "Create New List");
        }

        if (storeList.size() <= dataCount) {
            System.out.println("不够" + dataCount + "个元素");
            return storeList;

        }
        // 取最后10个
        System.out.println("取最后" + dataCount + "个元素");
        List<HwCmdModel> result = storeList.subList(storeList.size() - dataCount, storeList.size());
        return result;


    }


}
