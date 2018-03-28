package hx.smartschool.util.hw;


import java.util.List;

public class HwCmdBuilder {


    /**
     * 关机指令
     * @param hwCode
     * @return
     */
    public static HwSendModel POWEROFF(String hwCode) {

        HwSendModel model = new HwSendModel();
        model.setCmdType(18);
        model.setHwCode(hwCode);
        return model;
    }



    /**
     * 恢复出厂设置
     * @param hwCode
     * @return
     */
    public static HwSendModel FACTORY(String hwCode) {

        HwSendModel model = new HwSendModel();
        model.setCmdType(-1);
        model.setHwCode(hwCode);
        return model;
    }


    /**
     * 重启手表
     * @param hwCode
     * @return
     */
    public static HwSendModel RESET(String hwCode) {

        HwSendModel model = new HwSendModel();
        model.setCmdType(0);
        model.setHwCode(hwCode);
        return model;
    }



    /**
     * 查看版本
     * @param hwCode
     * @return
     */
    public static HwSendModel VERSION(String hwCode) {

        HwSendModel model = new HwSendModel();
        model.setCmdType(2);
        model.setHwCode(hwCode);
        return model;
    }



    /**
     * 查看版本
     * @param hwCode
     * @return
     */
    public static HwSendModel FIND(String hwCode) {

        HwSendModel model = new HwSendModel();
        model.setCmdType(3);
        model.setHwCode(hwCode);
        return model;
    }




    /**
     * 电话本
     *
     * @param hwCode
     * @return
     */
    public static HwSendModel PHONEBOOK(String hwCode, List<HwContactModel> phoneContacts) {

        HwSendModel model = new HwSendModel();
        model.setCmdType(4);
        model.setHwCode(hwCode);
        model.setContactList(phoneContacts);
        return model;
    }




    /**
     * 打开计步器
     *
     * @param hwCode
     * @return
     */
    public static HwSendModel PEDO1(String hwCode) {

        HwSendModel model = new HwSendModel();
        model.setCmdType(5);
        model.setHwCode(hwCode);
        model.setStatus(1);
        return model;
    }


    /**
     * 关闭计步器
     *
     * @param hwCode
     * @return
     */
    public static HwSendModel PEDO0(String hwCode) {

        HwSendModel model = new HwSendModel();
        model.setCmdType(5);
        model.setHwCode(hwCode);
        model.setStatus(0);
        return model;
    }


    /**
     * 发送消息
     * @param hwCode
     * @param message
     * @return
     */
    public static HwSendModel MESSAGE(String hwCode, String message) {

        HwSendModel model = new HwSendModel();
        model.setCmdType(6);
        model.setHwCode(hwCode);
        model.setMessage(message);
        return model;
    }


    /**
     * 发送消息
     * @param hwCode
     * @param phone
     * @return
     */
    public static HwSendModel CALL(String hwCode, String phone) {

        HwSendModel model = new HwSendModel();
        model.setCmdType(8);
        model.setHwCode(hwCode);
        model.setPhone(phone);
        return model;
    }


    /**
     * Sos1
     * @param hwCode
     * @param phone
     * @return
     */
    public static HwSendModel SOS1(String hwCode, String phone) {

        HwSendModel model = new HwSendModel();
        model.setCmdType(9);
        model.setHwCode(hwCode);
        model.setPhone(phone);
        return model;
    }




    /**
     * Sos2
     * @param hwCode
     * @param phone
     * @return
     */
    public static HwSendModel SOS2(String hwCode, String phone) {

        HwSendModel model = new HwSendModel();
        model.setCmdType(10);
        model.setHwCode(hwCode);
        model.setPhone(phone);
        return model;
    }


    /**
     * Sos2
     * @param hwCode
     * @param phone
     * @return
     */
    public static HwSendModel SOS3(String hwCode, String phone) {

        HwSendModel model = new HwSendModel();
        model.setCmdType(11);
        model.setHwCode(hwCode);
        model.setPhone(phone);
        return model;
    }






    /**
     * 关闭计步器
     *
     * @param hwCode
     * @return
     */
    public static HwSendModel WALKTIME(String hwCode, String[] TimeSpans) {

        HwSendModel model = new HwSendModel();
        model.setCmdType(16);
        model.setHwCode(hwCode);
        model.setArrrayTimeSpan(TimeSpans);
        return model;
    }


    /**
     * 下发心跳通知 0，1 n
     *
     * @param hwCode
     * @param interval
     * @return
     */
    public static HwSendModel HRSTART(String hwCode, int interval) {

        HwSendModel model = new HwSendModel();
        model.setCmdType(13);
        model.setHwCode(hwCode);
        model.setInterval(interval);
        return model;
    }


    /**
     * 下发远程定位命令
     *
     * @param hwCode
     */
    public static HwSendModel CR(String hwCode) {

        HwSendModel model = new HwSendModel();
        model.setCmdType(15);
        model.setHwCode(hwCode);
        model.setStatus(1);
        return model;
    }

}