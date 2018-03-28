package hx.smartschool.model;

import java.util.Date;


/**
 * 保存到缓存中的登陆模型
 */
public class AccountStateStoreModel {

    private String phone;
    private String email;
    private String mainHwCode;
    private String sosPhone;
    private Date loginTime; //最后登陆时间


    public AccountStateStoreModel(String phone, String email, String mainHwCode, String sosPhone, Date loginTime) {
        this.phone = phone;
        this.email = email;
        this.mainHwCode = mainHwCode;
        this.sosPhone = sosPhone;
        this.loginTime = loginTime;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMainHwCode() {
        return mainHwCode;
    }

    public void setMainHwCode(String mainHwCode) {
        this.mainHwCode = mainHwCode;
    }

    public String getSosPhone() {
        return sosPhone;
    }

    public void setSosPhone(String sosPhone) {
        this.sosPhone = sosPhone;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }
}
