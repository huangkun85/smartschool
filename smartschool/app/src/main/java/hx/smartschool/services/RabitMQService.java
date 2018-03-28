package hx.smartschool.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import hx.smartschool.util.mq.MQReceiver;
import hx.smartschool.util.mq.MQSender;


/**
 * RabbitMQ 服务 OnCreate --> Bind
 */
public class RabitMQService extends Service {

    //消息队列消费者
    private MQReceiver mqReceiver;

    //消息队列发送者
    private MQSender mqSender;


    //消费者队列的订阅bingdingKey
    private String bindingKeys[];


    public MQReceiver getMqReceiver() {
        return mqReceiver;
    }

    public MQSender getMqSender() {
        return mqSender;
    }


    /**
     * 默认构造函数
     */
    public RabitMQService() {

    }


    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("MQ后台服务", "onUnbind");

        this.mqReceiver.closeChannel();
        this.mqReceiver = null;
        this.mqSender = null;
        stopSelf();
        Log.d("MQ后台服务", "onUnbind-关闭");
        return super.onUnbind(intent);
    }


    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d("MQ后台服务", "onRebind");
    }


    @Override
    public IBinder onBind(Intent intent) {

        Log.d("MQ后台服务", "onBind");

        String data = intent.getExtras().getString("data");

        RabitMQBinder rabitMQBinder = new RabitMQBinder(this);


        bindingKeys = new String[]{data};

        this.mqReceiver.startListener(bindingKeys);

        Log.d("MQ后台服务", "开始监听后台服务:" + data);

        return rabitMQBinder;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MQ后台服务", "Distroy Service");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MQ后台服务", "OnCreate");
        mqReceiver = new MQReceiver(this.getApplicationContext());
        mqSender = new MQSender(this.getApplicationContext());
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("MQ后台服务", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }


}
