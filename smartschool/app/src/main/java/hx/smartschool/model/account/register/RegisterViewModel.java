package hx.smartschool.model.account.register;

public class RegisterViewModel {

    private String PhoneNumber;
    private String Email;
    private String Password;
    private String ConfirmPassword;

    public RegisterViewModel() {
        super();
    }

    public RegisterViewModel(String phoneNumber, String email, String password, String confirmPassword) {
        PhoneNumber = phoneNumber;
        Email = email;
        Password = password;
        ConfirmPassword = confirmPassword;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        ConfirmPassword = confirmPassword;
    }
}
