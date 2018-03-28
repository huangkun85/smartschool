package hx.smartschool.model.account.login;

/**
 * Created by DEV on 2018/2/12.
 */

public class LoginViewModel {
    private String PhoneNumber;
    private String Password;

    public LoginViewModel(String phoneNumber, String password) {
        PhoneNumber = phoneNumber;
        Password = password;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
