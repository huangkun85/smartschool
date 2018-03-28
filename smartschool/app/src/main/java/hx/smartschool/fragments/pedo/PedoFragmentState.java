package hx.smartschool.fragments.pedo;

/**
 * Created by DEV on 2018/3/24.
 */

public class PedoFragmentState {
    private boolean SwitchOn;
    private String time_start;
    private String time_end;

    public boolean isSwitchOn() {
        return SwitchOn;
    }

    public void setSwitchOn(boolean switchOn) {
        SwitchOn = switchOn;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }


}
