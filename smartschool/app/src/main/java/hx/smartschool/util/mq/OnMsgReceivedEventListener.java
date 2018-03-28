package hx.smartschool.util.mq;

import hx.smartschool.util.hw.HwCmdModel;

/**
 * Created by DEV on 2018/2/28.
 */

public interface OnMsgReceivedEventListener {

    void newMqMsg(HwCmdModel hwCmdModel);
}
