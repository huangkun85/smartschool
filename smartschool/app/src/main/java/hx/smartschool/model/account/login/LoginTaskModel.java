package hx.smartschool.model.account.login;


public class LoginTaskModel {

    private String url;
    private LoginViewModel loginViewModel;

    public LoginTaskModel(String url, LoginViewModel loginViewModel) {
        this.url = url;
        this.loginViewModel = loginViewModel;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LoginViewModel getLoginViewModel() {
        return loginViewModel;
    }

    public void setLoginViewModel(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
    }
}
