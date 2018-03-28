package hx.smartschool.util.hw;


import java.util.List;

/**
 * JSON 后的传输格式
 * {"CmdType":5,"HwCode":"3924220812","Status":1}
 */


public class HwSendModel {

    private int CmdType;

    private String HwCode;

    private int Status;

    private int Interval;

    private String Message;

    private  String Phone;

    private String[] ArrrayTimeSpan;

    private List<HwContactModel> ContactList;


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

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getInterval() {
        return Interval;
    }

    public void setInterval(int interval) {
        Interval = interval;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String[] getArrrayTimeSpan() {
        return ArrrayTimeSpan;
    }

    public void setArrrayTimeSpan(String[] arrrayTimeSpan) {
        ArrrayTimeSpan = arrrayTimeSpan;
    }

    public List<HwContactModel> getContactList() {
        return ContactList;
    }

    public void setContactList(List<HwContactModel> contactList) {
        ContactList = contactList;
    }
}
