package hx.smartschool.model.account.login;

import java.util.List;

import hx.smartschool.model.hardware.HwModel;

/**
 * Created by DEV on 2018/2/12.
 */

public class LoginResultModel {

    private boolean isValidLogin;

    private String message;

    private List<HwModel> HwList;


    public LoginResultModel(boolean isValidLogin, String message, List<HwModel> hwList) {
        this.isValidLogin = isValidLogin;
        this.message = message;
        HwList = hwList;
    }

    public boolean isValidLogin() {
        return isValidLogin;
    }

    public void setValidLogin(boolean validLogin) {
        isValidLogin = validLogin;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<HwModel> getHwList() {
        return HwList;
    }

    public void setHwList(List<HwModel> hwList) {
        HwList = hwList;
    }
}
