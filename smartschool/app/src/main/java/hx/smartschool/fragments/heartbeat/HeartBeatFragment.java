package hx.smartschool.fragments.heartbeat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
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


/**
 * 获取实时心率，显示最近的10次，通过消息队列监听器触发事件，
 * 后台操作：实时数据获取完成后，在下一次LK的时候，统一更新定时心率
 */
public class HeartBeatFragment extends BaseFragment {

    private ImageView mBtn_Start;
    private TextView mText_Hwcode;
    private TextView mText_HeartBeat;
    private BarChart mChart;

    private int hearbeatCount = 10;//显示最新的次数
    //设置数据
    private List<HwCmdModel> hearbeatStoreList;


    private static final String ARG_PHONE = "ARG_PHONE";
    private static final String ARG_HWCODE = "ARG_HWCODE";

    private String mArg_Phone;
    private String mArg_HwCode;


    private Handler mHandler;


    //消息队列监听器名字
    private final String PEDOMETER_LISTENER_KEY = "MQListener_HeartBeat";

    //消息队列监听器对象
    private OnMsgReceivedEventListener msgReceivedListener;


    public HeartBeatFragment() {
        // Required empty public constructor
    }


    public static HeartBeatFragment newInstance(String phone, String hwCode) {
        HeartBeatFragment fragment = new HeartBeatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PHONE, phone);
        args.putString(ARG_HWCODE, hwCode);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void initParameter(Bundle argBundle) {

        if (argBundle != null) {
            mArg_Phone = getArguments().getString(ARG_PHONE);
            mArg_HwCode = getArguments().getString(ARG_HWCODE);
        }
        // 初始化加载心率
        hearbeatStoreList = loadLatestHeartBeatData();
    }

    @Override
    public View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_heart_beat, container, false);

        mBtn_Start = view.findViewById(R.id.fragment_heartbeat_imgview);
        mText_Hwcode = view.findViewById(R.id.fragment_heartbeat_display_hwcode);
        mText_HeartBeat = view.findViewById(R.id.fragment_heartbeat_display_meter);
        mText_Hwcode.setText("硬件编码：" + mArg_HwCode);

        if (hearbeatStoreList != null && hearbeatStoreList.size() > 0) {


            int lstHeartBeat = hearbeatStoreList.get(hearbeatStoreList.size() - 1).getExtCmdParameter().getHwHeartBeat();
            mText_HeartBeat.setText(lstHeartBeat + "");

            // 显示图形
            mChart = (BarChart) view.findViewById(R.id.fragment_heartbeat_display_chart);
            // 显示边界
            mChart.setDrawBorders(true);

            // 设置描述
            Description desc = new Description();
            desc.setText("健康心率60-100跳/分");
            mChart.setDescription(desc);


            renderChart(hearbeatStoreList);
        }


        return view;
    }

    @Override
    public void initListener(View view) {

        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {


                    case 0:
                        Log.d("handleMessage", "0");
                        mText_HeartBeat.setText("" + msg.arg1);

                        hearbeatStoreList = loadLatestHeartBeatData();

                        renderChart(hearbeatStoreList);

                        mainActivity.showLongToast("更新心跳数据");
                        break;

                    case 1:

                        Log.d("handleMessage", "1");
                        mainActivity.showLongToast("心跳检测指令下发成功");
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


                if (cmdType == 13) {

                    mHandler.sendEmptyMessage(1);
                    return;
                }

                if (cmdType == -14) {
                    Message message = new Message();
                    message.what = 0;
                    message.arg1 = extCmdParameter.getHwHeartBeat();

                    mHandler.sendMessage(message);
                    return;

                }


            }
        };

        // 添加监听器
        mainActivity.getRabitMQService().getMqReceiver().addListener(this.PEDOMETER_LISTENER_KEY, msgReceivedListener);

        //下发心跳设置
        mBtn_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HwSendModel mqModel = HwCmdBuilder.HRSTART(mArg_HwCode, 1);
                String json = mainActivity.getGson().toJson(mqModel);
                mainActivity.getRabitMQService().getMqSender().SendMessage(json);
            }

        });


    }

    @Override
    public void SaveFragmentState() {

    }

    @Override
    public void LoadFragmentState() {

    }

    /**
     * 读取最近的N条数据
     *
     * @return
     */

    private List<HwCmdModel> loadLatestHeartBeatData() {

        ArrayList<HwCmdModel> storeList = null;

        String pn = mainActivity.getString(R.string.app_store_preferences_name);
        String StoreKey = "HEART." + mArg_HwCode;

        //读取内部存储的列表
        String storeJson = SharedPreferencesHelper.readString(mainActivity, pn, StoreKey);
        Log.d("HEART.Fragment.Read", "读HEART存储：" + StoreKey + " 内容 = " + storeJson);

        //数据存在的情况
        if (null != storeJson && storeJson.length() > 5) {

            Type type = new TypeToken<ArrayList<HwCmdModel>>() {

            }.getType();

            storeList = mainActivity.getGson().fromJson(storeJson, type);
            Log.d("Store.J2List", storeJson);
        } else {
            storeList = new ArrayList<HwCmdModel>();
            Log.d("Store.NewList", "Create New List");
        }

        if (storeList.size() <= hearbeatCount) {
            System.out.println("不够" + hearbeatCount + "个元素");
            return storeList;

        }
        // 取最后10个
        System.out.println("取最后" + hearbeatCount + "个元素");
        return storeList.subList(storeList.size() - hearbeatCount, storeList.size());


    }


    /**
     * 重绘图标
     *
     * @param dataList
     */
    private void renderChart(List<HwCmdModel> dataList) {


        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            int hb = dataList.get(i).getExtCmdParameter().getHwHeartBeat();
            entries.add(new BarEntry(i, hb));
        }

        BarDataSet dataSet = new BarDataSet(entries, "最近" + dataList.size() + "次心率曲线图");
        BarData data = new BarData(dataSet);

        mChart.clear();

        // 最大最小值处理
        mChart.setAutoScaleMinMaxEnabled(true);
        // 设置动画
        mChart.animateXY(2000, 2000);


        mChart.setData(data);

        mChart.notifyDataSetChanged(); // let the chart know it's data changed
        mChart.invalidate();

    }


}
