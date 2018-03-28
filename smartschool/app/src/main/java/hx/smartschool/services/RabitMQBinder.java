package hx.smartschool.services;

import android.os.Binder;

/**
 * 对应后台服务的Binder
 */

public class RabitMQBinder extends Binder {


    private RabitMQService rabitMQService;


    public RabitMQBinder(RabitMQService rabitMQService) {
        this.rabitMQService = rabitMQService;
    }


    public RabitMQService getRabitMQService() {
        return rabitMQService;
    }


    public void setRabitMQService(RabitMQService rabitMQService) {
        this.rabitMQService = rabitMQService;
    }
}
