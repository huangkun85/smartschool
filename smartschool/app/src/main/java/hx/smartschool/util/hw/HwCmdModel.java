package hx.smartschool.util.hw;


import java.util.Date;

public class HwCmdModel {

    private String ExtRawCommand;
    private Date ExtReceiveTime;
    private boolean ExtIsCmdValid;
    private int ExtCmdStatus;
    private int ExtCmdType;
    private ExtCmdParameter ExtCmdParameter;
    private String FormatHwVendor;
    private String FormatHwCode;
    private String FormatHexLength;
    private String FormatConent;

    public void setExtRawCommand(String ExtRawCommand) {
        this.ExtRawCommand = ExtRawCommand;
    }

    public String getExtRawCommand() {
        return ExtRawCommand;
    }

    public void setExtReceiveTime(Date ExtReceiveTime) {
        this.ExtReceiveTime = ExtReceiveTime;
    }

    public Date getExtReceiveTime() {
        return ExtReceiveTime;
    }

    public void setExtIsCmdValid(boolean ExtIsCmdValid) {
        this.ExtIsCmdValid = ExtIsCmdValid;
    }

    public boolean getExtIsCmdValid() {
        return ExtIsCmdValid;
    }

    public void setExtCmdStatus(int ExtCmdStatus) {
        this.ExtCmdStatus = ExtCmdStatus;
    }

    public int getExtCmdStatus() {
        return ExtCmdStatus;
    }

    public void setExtCmdType(int ExtCmdType) {
        this.ExtCmdType = ExtCmdType;
    }

    public int getExtCmdType() {
        return ExtCmdType;
    }

    public void setExtCmdParameter(ExtCmdParameter ExtCmdParameter) {
        this.ExtCmdParameter = ExtCmdParameter;
    }

    public ExtCmdParameter getExtCmdParameter() {
        return ExtCmdParameter;
    }

    public void setFormatHwVendor(String FormatHwVendor) {
        this.FormatHwVendor = FormatHwVendor;
    }

    public String getFormatHwVendor() {
        return FormatHwVendor;
    }

    public void setFormatHwCode(String FormatHwCode) {
        this.FormatHwCode = FormatHwCode;
    }

    public String getFormatHwCode() {
        return FormatHwCode;
    }

    public void setFormatHexLength(String FormatHexLength) {
        this.FormatHexLength = FormatHexLength;
    }

    public String getFormatHexLength() {
        return FormatHexLength;
    }

    public void setFormatConent(String FormatConent) {
        this.FormatConent = FormatConent;
    }

    public String getFormatConent() {
        return FormatConent;
    }

}
