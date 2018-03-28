package hx.smartschool.model.hardware;


/**
 *
 */
public class HwModel {

    private String id;
    private String hwCode;
    private String phoneNumber;
    private String studentId;

    /**
     * 构造函数
     *
     * @param id
     * @param hwCode
     * @param phoneNumber
     * @param studentId
     */
    public HwModel(String id, String hwCode, String phoneNumber, String studentId) {
        this.id = id;

        this.hwCode = hwCode;
        this.phoneNumber = phoneNumber;
        this.studentId = studentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHwCode() {
        return hwCode;
    }

    public void setHwCode(String hwCode) {
        this.hwCode = hwCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
