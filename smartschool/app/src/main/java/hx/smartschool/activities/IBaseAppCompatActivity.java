package hx.smartschool.activities;

/**
 * Created by DEV on 2018/2/28.
 */

public interface IBaseAppCompatActivity {

    //1、初始化布局
    int initLayout();

    //2、初始化控件
    void initControls();

    //3、初始化数据
    void initData();

    //4、初始化点击事件
    void initListener();

    //5. 可见时候加载
    void doOnStart();

    // 后退按钮实现
    void doBackKeyPress();
}
