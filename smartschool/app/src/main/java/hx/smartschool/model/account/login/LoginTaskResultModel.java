package hx.smartschool.model.account.login;

import hx.smartschool.model.AccountStateStoreModel;
import hx.smartschool.util.net.HxHttpResponse;


/**
 * 处理登陆后的结果
 */
public class LoginTaskResultModel {

    private boolean isValid;
    private AccountStateStoreModel accountStateStoreModel;
    private String displayMessage;
    private HxHttpResponse HttpResponse;


    public LoginTaskResultModel() {
        super();
    }

    public LoginTaskResultModel(boolean isValid, AccountStateStoreModel accountStateStoreModel, String displayMessage, HxHttpResponse httpResponse) {
        this.isValid = isValid;
        this.accountStateStoreModel = accountStateStoreModel;
        this.displayMessage = displayMessage;
        HttpResponse = httpResponse;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public AccountStateStoreModel getAccountStateStoreModel() {
        return accountStateStoreModel;
    }

    public void setAccountStateStoreModel(AccountStateStoreModel accountStateStoreModel) {
        this.accountStateStoreModel = accountStateStoreModel;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }

    public HxHttpResponse getHttpResponse() {
        return HttpResponse;
    }

    public void setHttpResponse(HxHttpResponse httpResponse) {
        HttpResponse = httpResponse;
    }
}
