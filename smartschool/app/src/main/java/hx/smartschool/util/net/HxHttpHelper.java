package hx.smartschool.util.net;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by DEV on 2018/2/12.
 */

public class HxHttpHelper {

    public static HxHttpResponse DoPost(String strUrl, String strJson) throws Exception {
        InputStream inputStream = null;

        ObjectOutputStream objectOutputStream = null;

        HttpURLConnection connection = null;
        HxHttpResponse hxHttpResponse = null;

        URL url = new URL(strUrl);
        connection = (HttpURLConnection) url.openConnection();

        // 设置是否从httpUrlConnection的读写;
        connection.setDoOutput(true);
        connection.setDoInput(true);

        // 设置不用缓存
        connection.setUseCaches(false);

        // 设置请求方式
        connection.setRequestMethod("POST");

        // 设置编码格式 和请求类型
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        connection.connect();
        // 会自动隐含调用connect

        byte[] bytesToWrite = strJson.getBytes("UTF-8");


        connection.getOutputStream().write(bytesToWrite);
        // 刷新对象输出流，将任何字节都写入潜在的流中（些处为ObjectOutputStream）
        connection.getOutputStream().flush();
        connection.getOutputStream().close();

        //响应码 和响应内容
        int code = connection.getResponseCode();

        if (code == 200) {
            inputStream = connection.getInputStream();
        } else {
            inputStream = connection.getErrorStream();
        }

        String result = InputStreamTOString(inputStream);

        hxHttpResponse = new HxHttpResponse(code, result);

        return hxHttpResponse;

    }


    public static String InputStreamTOString(InputStream in) throws Exception {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[2048];
        int count = -1;
        while ((count = in.read(data, 0, 2048)) != -1)
            outStream.write(data, 0, count);

        data = null;
        return new String(outStream.toByteArray(), "UTF-8");
    }

}
