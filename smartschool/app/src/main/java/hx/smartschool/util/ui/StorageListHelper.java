package hx.smartschool.util.ui;

import java.util.ArrayList;

import hx.smartschool.util.hw.HwCmdModel;

/**
 * Created by DEV on 2018/3/23.
 */

public class StorageListHelper {

    public static int addItemToList(ArrayList<HwCmdModel> list, HwCmdModel item) {

        list.add(item);

        //达到200条的的时候，删除前100条
        if (list.size() > 199) {
            //添加前到列表尾部

            //删除前100条
            for (int i = 0; i < 100; i++) {
                list.remove(0);
            }
        }

        return list.size();

    }


}
