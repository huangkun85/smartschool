package hx.smartschool.fragments.map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
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
 * 地图定位，加载时候显示上一次的定位
 */
public class LocationFragment extends BaseFragment {

    private static final String ARG_PHONE = "ARG_PHONE";
    private static final String ARG_HWCODE = "ARG_HWCODE";

    private String mArg_Phone;
    private String mArg_HwCode;

    //UI
    private Button mBtnRemoteLocation;
    private MapView mMapView;

    private TextView mHwCode;
    private TextView mMapLocationAddress;
    private TextView mMapLocarionPrecision;

    //地图
    private GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private BaiduMap mBaiduMap = null;


    private int locCount = 10;
    private List<HwCmdModel> locDataList;


    private Handler mHandler;

    //消息队列监听器名字
    private final String LOCATION_FRAGMENT_LISTENER_KEY = "MQListener_Location";

    //消息队列监听器对象
    private OnMsgReceivedEventListener msgReceivedListener;


    public LocationFragment() {
        // Required empty public constructor
    }


    public static LocationFragment newInstance(String phone, String hwCode) {
        LocationFragment fragment = new LocationFragment();
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

        locDataList = loadLatestLocationData();

    }


    @Override
    public View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location, container, false);

        mBtnRemoteLocation = view.findViewById(R.id.fragment_location_btn_start);
        mMapView = view.findViewById(R.id.fragment_location_mapview);
        mHwCode = view.findViewById(R.id.fragment_location_hwcode);

        mMapLocationAddress = view.findViewById(R.id.fragment_location_address);
        mMapLocarionPrecision = view.findViewById(R.id.fragment_location_precision);


        mBaiduMap = mMapView.getMap();
        mHwCode.setText("硬件编码：" + mArg_HwCode);

        // 初始化搜索模块
        mSearch = GeoCoder.newInstance();

        return view;
    }


    @Override
    public void initListener(View view) {

        // 接受消息队列信息
        msgReceivedListener = new OnMsgReceivedEventListener() {
            @Override
            public void newMqMsg(HwCmdModel receiveModel) {

                int cmdType = receiveModel.getExtCmdType();
                ExtCmdParameter extCmdParameter = receiveModel.getExtCmdParameter();

                Log.d("地图 Listener收到新消息", "Cmd_Type:" + cmdType + "para 是否为空:" + (extCmdParameter == null));


                if (cmdType == 15) {

                    mHandler.sendEmptyMessage(101);
                    return;
                }

                if (cmdType == 23 || cmdType == 24) {

                    String lat = extCmdParameter.getLatitude();
                    String lon = extCmdParameter.getLongitude();

                    Bundle bundle = new Bundle();
                    bundle.putString("lat", lat);
                    bundle.putString("lon", lon);
                    bundle.putString("precision", extCmdParameter.getLocationStatus());


                    Message message = new Message();
                    message.what = 102;
                    message.setData(bundle);

                    mHandler.sendMessage(message);
                    return;

                }


            }
        };

        // 添加监听器
        mainActivity.getRabitMQService().getMqReceiver().addListener(this.LOCATION_FRAGMENT_LISTENER_KEY, msgReceivedListener);


        //UI
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {

                    case 102:

                        Bundle bundle = msg.getData();
                        String lat = bundle.getString("lat");
                        String lon = bundle.getString("lon");
                        String precision = bundle.getString("precision");

                        mMapLocarionPrecision.setText("精度：" + precision);
                        renderMap(lat, lon);
                        break;

                    case 101:

                        Log.d("handleMessage", "1");
                        mainActivity.showLongToast("手表定位开始");
                        break;


                    default:
                        super.handleMessage(msg);
                        break;
                }
            }
        };


        /**
         *按钮
         */
        mBtnRemoteLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                double lat = 22.550935f;
//                double lon = 114.1367283f;
//                LatLng ptCenter = new LatLng(lat, lon);
//                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter).newVersion(0));

                //下发定位
                HwSendModel hwSendModel = HwCmdBuilder.CR(mArg_HwCode);
                String json = mainActivity.getGson().toJson(hwSendModel);
                mainActivity.getRabitMQService().getMqSender().SendMessage(json);


            }
        });


        /**
         * 地图显示的更新的回掉方法
         */
        mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {

                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    mMapLocationAddress.setText("地图位置未能载入");
                    return;
                }

                // 缩放级别
                float zoomLevel = Float.parseFloat("20");
                //mBaiduMap.clear();

                MapStatus ms = new MapStatus.Builder(mBaiduMap.getMapStatus())
                        .target(result.getLocation())
                        .zoom(zoomLevel)
                        .build();

                MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(ms);

                mBaiduMap.animateMapStatus(u);

                //添加标记层
                mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation()).icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_marka)));


                mMapLocationAddress.setText("定位位置：" + result.getAddress() + "附近");

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
     * 加载完View之后再加载地图
     */
    @Override
    public void onStart() {
        super.onStart();

        if (locDataList.size() > 0) {

            HwCmdModel model = locDataList.get(locDataList.size() - 1);

            String lat = model.getExtCmdParameter().getLatitude();
            String lon = model.getExtCmdParameter().getLongitude();

            Bundle bundle = new Bundle();
            bundle.putString("lat", lat);
            bundle.putString("lon", lon);
            bundle.putString("precision", model.getExtCmdParameter().getLocationStatus());

            Message message = new Message();
            message.what = 102;
            message.setData(bundle);


            mHandler.sendMessageDelayed(message, 200);
            showLongToast("上一次位置数据");

        } else {
            showLongToast("没有上一次位置数据");
        }


    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        mSearch.destroy();
        super.onDestroy();
    }


    /**
     * 加载最后10条位置数据
     *
     * @return
     */
    private List<HwCmdModel> loadLatestLocationData() {

        ArrayList<HwCmdModel> storeList = null;

        String pn = mainActivity.getString(R.string.app_store_preferences_name);
        String StoreKey = "LOC." + mArg_HwCode;

        //读取内部存储的列表
        String storeJson = SharedPreferencesHelper.readString(mainActivity, pn, StoreKey);
        Log.d("HEART.Fragment.Read", "读 LOC 存储：" + StoreKey + " 内容 = " + storeJson);

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

        if (storeList.size() <= locCount) {
            System.out.println("不够" + locCount + "个元素");
            return storeList;
        }

        // 取最后n个元素
        System.out.println("取最后" + locCount + "个元素");
        return storeList.subList(storeList.size() - locCount, storeList.size());


    }


    /**
     * 渲染地图，需要在消息中调用，不阻挡线程
     *
     * @param lat
     * @param lon
     */

    private void renderMap(String lat, String lon) {

        double latD = Double.parseDouble(lat);
        double lonD = Double.parseDouble(lon);

        LatLng ptCenter = new LatLng(latD, lonD);
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter).newVersion(0));

    }

}
