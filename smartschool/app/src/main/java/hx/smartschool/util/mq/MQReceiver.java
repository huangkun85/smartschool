package hx.smartschool.util.mq;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import hx.smartschool.R;
import hx.smartschool.util.SharedPreferencesHelper;
import hx.smartschool.util.hw.HwCmdModel;
import hx.smartschool.util.ui.StorageListHelper;


/**
 * 消息队列接收者
 */
public class MQReceiver {

    private Context _context;
    private Gson gson;


    private String exchangeName;
    private String hostName;
    private String virtualHost;
    private String username;
    private String password;


    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;
    private Consumer consumer;
    private String[] bindingKeys;

    // 添加监听器
    private HashMap<String, OnMsgReceivedEventListener> listenerMap;

    private Thread mThread;


    public MQReceiver(Context context) {

        _context = context;
        gson = new Gson();
        listenerMap = new HashMap<String, OnMsgReceivedEventListener>();

        exchangeName = _context.getString(R.string.rabbitmq_exchangeName_receive);
        hostName = _context.getString(R.string.rabbitmq_host);
        virtualHost = _context.getString(R.string.rabbitmq_vhost);
        username = _context.getString(R.string.rabbitmq_username);
        password = _context.getString(R.string.rabbitmq_password);
        initFactory();

    }

    /**
     * 初始化工厂
     */
    private void initFactory() {
        factory = new ConnectionFactory();
        factory.setUsername(this.username);
        factory.setPassword(this.password);
        factory.setVirtualHost(this.virtualHost);
        factory.setHost(this.hostName);
    }


    /**
     * 初始化连接
     *
     * @return Channel
     * @throws Exception
     */
    private Channel initChannel() throws Exception {
        if (connection == null || channel == null || !connection.isOpen() || !channel.isOpen()) {

            connection = factory.newConnection();
            channel = connection.createChannel();
            Log.d("MQ.R.initChannel", "MQ创建Connection");
        }
        Log.d("MQ.R.initChannel", "MQ获取原有Connection");

        return channel;

    }


    /**
     * 初始化Comsumer
     *
     * @throws Exception
     */
    private void initComsumer() throws Exception {


        channel.exchangeDeclare(exchangeName, "topic");
        String queueName = channel.queueDeclare().getQueue();

        for (String bindingKey : bindingKeys) {
            channel.queueBind(queueName, exchangeName, bindingKey);
            Log.d("MQ.R.StartConnect", "添加Topic的bindingKey绑定: " + bindingKey);
        }

        //开始消费者监听
        consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                super.handleDelivery(consumerTag, envelope, properties, body);

                String message = new String(body, "UTF-8");

                Log.d("MQ.Rev.Msg", message);


                // 反序列化
                Type type = new TypeToken<HwCmdModel>() {

                }.getType();

                HwCmdModel model = gson.fromJson(message, type);


                // 收到的消息 缓存
                int cmdType = model.getExtCmdType();

                String pn = _context.getString(R.string.app_store_preferences_name);
                String StoreKey;
                int listCount;
                String storeJson;
                ArrayList<HwCmdModel> storeList;

                if (cmdType == 1) {
                    Log.d("MQ.LK", "新上传命令，类型1，LK");
                    StoreKey = "LK." + model.getFormatHwCode();

                    //读取内部存储的列表
                    storeJson = SharedPreferencesHelper.readString(_context, pn, StoreKey);
                    Log.d("Store.Read.LK", "读LK存储：" + StoreKey + " 内容 = " + storeJson);


                    //数据存在的情况
                    if (null != storeJson && storeJson.length() > 5) {

                        type = new TypeToken<ArrayList<HwCmdModel>>() {

                        }.getType();

                        storeList = gson.fromJson(storeJson, type);
                        Log.d("Store.J2List", storeJson);
                    } else {
                        storeList = new ArrayList<HwCmdModel>();
                        Log.d("Store.NewList", "Create New List");
                    }


                    listCount = StorageListHelper.addItemToList(storeList, model);
                    //列表转json
                    storeJson = gson.toJson(storeList);

                    //保存
                    SharedPreferencesHelper.writeString(_context, pn, StoreKey, storeJson);
                    Log.d("Store.Write.LK", "写入LK 到 Share：" + StoreKey + " 列表长度 = " + listCount);

                }

                if (cmdType == -14) {
                    //心率上传的保存
                    Log.d("MQ.HEART", "新上传命令，类型-14，HEART");
                    StoreKey = "HEART." + model.getFormatHwCode();

                    //读取内部存储的列表
                    storeJson = SharedPreferencesHelper.readString(_context, pn, StoreKey);
                    Log.d("Store.Read.HEART", "读HEART存储：" + StoreKey + " 内容 = " + storeJson);

                    //数据存在的情况
                    if (null != storeJson && storeJson.length() > 5) {

                        type = new TypeToken<ArrayList<HwCmdModel>>() {

                        }.getType();

                        storeList = gson.fromJson(storeJson, type);
                        Log.d("Store.J2List", storeJson);
                    } else {
                        storeList = new ArrayList<HwCmdModel>();
                        Log.d("Store.NewList", "Create New List");
                    }

                    listCount = StorageListHelper.addItemToList(storeList, model);

                    //列表转json
                    storeJson = gson.toJson(storeList);

                    //保存
                    SharedPreferencesHelper.writeString(_context, pn, StoreKey, storeJson);
                    Log.d("Store.Write.HEART", "写入HEART 到 Share：" + StoreKey + " 列表长度 = " + listCount);

                }


                if (cmdType == 23 || cmdType == 24) {

                    //心率上传的保存
                    Log.d("MQ.Location", "新上传命令，定位");
                    StoreKey = "LOC." + model.getFormatHwCode();

                    //读取内部存储的列表
                    storeJson = SharedPreferencesHelper.readString(_context, pn, StoreKey);
                    Log.d("Store.Read.Location", "读 LOC 存储：" + StoreKey + " 内容 = " + storeJson);

                    //数据存在的情况
                    if (null != storeJson && storeJson.length() > 5) {

                        type = new TypeToken<ArrayList<HwCmdModel>>() {

                        }.getType();

                        storeList = gson.fromJson(storeJson, type);
                        Log.d("Store.J2List", storeJson);
                    } else {
                        storeList = new ArrayList<HwCmdModel>();
                        Log.d("Store.NewList", "Create New List");
                    }

                    listCount = StorageListHelper.addItemToList(storeList, model);

                    //列表转json
                    storeJson = gson.toJson(storeList);

                    //保存
                    SharedPreferencesHelper.writeString(_context, pn, StoreKey, storeJson);
                    Log.d("Store.Write.LOC", "写入 LOC 到 Share：" + StoreKey + " 列表长度 = " + listCount);

                }


                //转发消息到监听器
                for (Map.Entry<String, OnMsgReceivedEventListener> entry : listenerMap.entrySet()) {
                    String itemKey = entry.getKey();
                    OnMsgReceivedEventListener listener = entry.getValue();
                    Log.d("MQ.R.New.Listener", "监听器转发消息:[" + itemKey + "]");
                    listener.newMqMsg(model);
                }


            }
        };

        channel.basicConsume(queueName, true, consumer);
        Log.d("MQ.R.StartConnect", "正在监听...." + Arrays.toString(bindingKeys));

    }


    /**
     * 构造函数
     *
     * @param exchangeName
     * @param hostName
     * @param virtualHost
     * @param username
     * @param password
     */
    public MQReceiver(String exchangeName, String hostName, String virtualHost, String username, String password) {
        super();
        initFactory();
    }


    /**
     * 增加监听器
     *
     * @param listenerKey
     * @param listener
     */
    public void addListener(String listenerKey, OnMsgReceivedEventListener listener) {

        Log.d("MQReceiver.addListener", "增加监听器");

        if (listenerMap.containsKey(listener)) {
            Log.d("MQReceiver.addListener", "替换 MQ Listener");
            removeListener(listenerKey);
            listenerMap.put(listenerKey, listener);

        } else {
            listenerMap.put(listenerKey, listener);
            Log.d("MQReceiver.addListener", "新增 MQ Listener");
        }
    }

    /**
     * 移除一个监听器
     *
     * @param listenerKey
     */
    public void removeListener(String listenerKey) {

        Log.d("MQReceiver.addListener", "删除MQ Listener");
        listenerMap.remove(listenerKey);
    }


    /**
     * 开始监听器
     */
    public void startListener(String[] bindingKeys) {

        this.bindingKeys = bindingKeys;

        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    initChannel();
                    initComsumer();
                } catch (Exception e) {
                    Log.e("doInBackground", e.getMessage(), e);
                }
            }
        });

        mThread.start();


    }


    public Thread closeChannel() {


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    mThread.interrupt();
                    mThread = null;

                    connection.close();
                    connection = null;

                } catch (Exception ex) {
                    Log.e("closeChannel", ex.getMessage(), ex);
                }


            }
        });

        thread.start();
        Log.d("MQ.R.Async", "MQ后台任务进程取消");
        return thread;

    }


}
