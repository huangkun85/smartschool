package hx.smartschool.util.mq;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import hx.smartschool.R;


public class MQSender {

    //基础参数
    private String hostName;
    private String virtualHost;
    private String username;
    private String password;

    //MQ参数
    private String queueName;
    private String exchangeName = "amq.direct";
    private String routingKey = "ANY";

    //MQ对象
    private ConnectionFactory factory = null;
    private Connection connection = null;
    private Channel channel = null;

    private SendAsyncTask sendAsyncTask;

    private Context _context;
    private Gson gson;

    public MQSender(Context context) {
        gson = new Gson();

        _context = context;

        //exchangeName = _context.getString(R.string.rabbitmq_exchangeName_receive);
        queueName = _context.getString(R.string.rabbitmq_queuename_send);

        hostName = _context.getString(R.string.rabbitmq_host);
        virtualHost = _context.getString(R.string.rabbitmq_vhost);
        username = _context.getString(R.string.rabbitmq_username);
        password = _context.getString(R.string.rabbitmq_password);
    }


    /**
     * 向消息队列发送内容
     *
     * @param message
     * @throws Exception
     */
    public void SendMessage(final String message) {

        Log.d("MQ.Sender", "开始发送:" + message);
        // 链接工厂
        if (factory == null) {
            factory = new ConnectionFactory();

            factory.setUsername(username);
            factory.setPassword(password);
            factory.setHost(hostName);
            factory.setVirtualHost(virtualHost);

            Log.d("MQ.Sender", "创建MQ工厂:");
        }

        Log.d("MQ.Sender", "获取MQ工厂:");


        // 执行异步
        sendAsyncTask = new SendAsyncTask();

        sendAsyncTask.execute(message);


    }

    public void close() throws Exception {
        channel.close();
        connection.close();
    }

    /**
     * 异步处理任务
     */
    private class SendAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {

            String message = strings[0];
            Log.d("MQ.Sender", "异步发送:" + message);

            try {
                if (connection == null || !connection.isOpen() || channel == null || !channel.isOpen()) {

                    // 创建发送到中心链接和通道
                    connection = factory.newConnection();
                    channel = connection.createChannel();
                    channel.exchangeDeclare(exchangeName, "direct", true);
                    channel.queueDeclare(queueName, false, false, false, null);
                    channel.queueBind(queueName, exchangeName, routingKey);
                }

                //发送
                byte[] messageBodyBytes = message.getBytes();
                channel.basicPublish(exchangeName, routingKey, null, messageBodyBytes);

                Log.d("MQ.Sender", "异步发送:" + message);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }


            return null;
        }
    }


}
