package hx.smartschool.util.mq;

/**
 * 发送消息队列模型
 */
public class MQSendJsonModel {

    private int CmdType;

    private String HwCode;

    private int Interval;


    public int getCmdType() {
        return CmdType;
    }

    public void setCmdType(int cmdType) {
        CmdType = cmdType;
    }

    public String getHwCode() {
        return HwCode;
    }

    public void setHwCode(String hwCode) {
        HwCode = hwCode;
    }

    public int getInterval() {
        return Interval;
    }

    public void setInterval(int interval) {
        Interval = interval;
    }
}
