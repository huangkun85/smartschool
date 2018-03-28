package hx.smartschool.util.hw;






public class HwContactModel {

    private String Phone;

    private String Name;

    public HwContactModel(String phone, String name) {
        Phone = phone;
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
