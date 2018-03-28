package hx.smartschool.model.account.register;

/**
 * Created by DEV on 2018/2/13.
 */

public class RegisterResultModel {

    private boolean isValidLogin;
    private String message;

    public RegisterResultModel(boolean isValidLogin, String message) {

        this.isValidLogin = isValidLogin;
        this.message = message;
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
}
