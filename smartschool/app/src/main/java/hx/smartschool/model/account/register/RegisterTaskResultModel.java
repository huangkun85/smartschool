package hx.smartschool.model.account.register;

import hx.smartschool.util.net.HxHttpResponse;

/**
 * Created by DEV on 2018/2/13.
 */

public class RegisterTaskResultModel {

    private boolean isValid;
    private String displayMessage;
    private String resultData;

    private HxHttpResponse HttpResponse;


    public RegisterTaskResultModel() {
        super();
    }

    public RegisterTaskResultModel(boolean isValid, String displayMessage, String resultData, HxHttpResponse httpResponse) {
        this.isValid = isValid;
        this.displayMessage = displayMessage;
        this.resultData = resultData;
        HttpResponse = httpResponse;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }

    public String getResultData() {
        return resultData;
    }

    public void setResultData(String resultData) {
        this.resultData = resultData;
    }

    public HxHttpResponse getHttpResponse() {
        return HttpResponse;
    }

    public void setHttpResponse(HxHttpResponse httpResponse) {
        HttpResponse = httpResponse;
    }
}
