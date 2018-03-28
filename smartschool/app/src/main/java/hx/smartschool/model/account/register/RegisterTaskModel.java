package hx.smartschool.model.account.register;

/**
 * Created by DEV on 2018/2/13.
 */

public class RegisterTaskModel {
    private String url;
    private RegisterViewModel loginViewModel;

    public RegisterTaskModel(String url, RegisterViewModel loginViewModel) {
        this.url = url;
        this.loginViewModel = loginViewModel;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RegisterViewModel getLoginViewModel() {
        return loginViewModel;
    }

    public void setLoginViewModel(RegisterViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
    }
}
