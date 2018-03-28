package hx.smartschool.util.net;

/**
 * Http 响应内容
 */
public class HxHttpResponse {

    private int statusCode;
    private String content;

    public HxHttpResponse(int statusCode, String content) {
        this.statusCode = statusCode;
        this.content = content;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public String toString() {

        return "StatusCode=" + this.statusCode + ",Content=" + this.content;
    }
}
