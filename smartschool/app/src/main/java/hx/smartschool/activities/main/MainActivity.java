package hx.smartschool.activities.main;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import hx.smartschool.R;
import hx.smartschool.activities.BaseAppCompatActivity;
import hx.smartschool.activities.account.login.LoginActivity;
import hx.smartschool.fragments.OnFragmentInteractionListener;
import hx.smartschool.fragments.heartbeat.HeartBeatFragment;
import hx.smartschool.fragments.map.LocationFragment;
import hx.smartschool.fragments.pedo.PedometerFragment;
import hx.smartschool.fragments.phonebook.PhoneBookFragment;
import hx.smartschool.fragments.setting.BindDeviceFragment;
import hx.smartschool.fragments.setting.HwSettingFragment;
import hx.smartschool.fragments.setting.PhoneCallFragment;
import hx.smartschool.fragments.setting.SendMessageFragment;
import hx.smartschool.fragments.sos.SosFragment;
import hx.smartschool.model.AccountStateStoreModel;
import hx.smartschool.services.RabitMQBinder;
import hx.smartschool.services.RabitMQService;
import hx.smartschool.util.SharedPreferencesHelper;
import hx.smartschool.util.ui.BottomNavigationViewHelper;

public class MainActivity extends BaseAppCompatActivity implements OnFragmentInteractionListener {

    // UI 元素
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;

    // 左侧导航菜单中的内容
    private TextView nav_header_phone;
    private TextView nav_header_email;
    //服务相关
    private ServiceConnection rabitServiceConnection;
    private RabitMQService rabitMQService;
    private Intent rabitMQServiceItent;

    // 处理UI的Handler
    private Handler mHandler;


    /**
     * 右上角菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_option_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.option_menus_item_search_button) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void initData() {

    }


    @Override
    public int initLayout() {
        setContentView(R.layout.activity_main);
        return 0;
    }

    @Override
    public void initControls() {

        toolbar = this.find(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = this.find(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle.syncState();

        drawer.addDrawerListener(toggle);

        navigationView = this.find(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        //定义
        nav_header_phone = headerView.findViewById(R.id.ac_main_nav_header_phone);
        nav_header_email = headerView.findViewById(R.id.ac_main_nav_header_email);

        AccountStateStoreModel accountStateStoreModel = this.getAccountStateHelper().getAccountState();
        nav_header_phone.setText(accountStateStoreModel.getPhone());
        nav_header_email.setText(accountStateStoreModel.getEmail());

        //底部导航菜单
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.ac_main_content_buttom_nav);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);


        //loadFragment(2);


    }


    @Override
    public void initListener() {

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        // bottomNavigationView.setOnNavigationItemSelectedListener();


        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {

                    case 2:
                        loadFragment(2);
                        break;
                    default:
                        break;
                }
            }
        };


        BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ac_main_buttom_nav_heartbeat:
                        loadFragment(2);
                        return true;
                    case R.id.ac_main_buttom_nav_pedometer:
                        loadFragment(1);
                        return true;
                    case R.id.ac_main_buttom_nav_location:
                        loadFragment(3);
                        return true;

                    case R.id.ac_main_buttom_nav_mime:
                        loadFragment(5);
                        return true;
                }
                return false;
            }
        };

        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                // Handle buttom_navigation view item clicks here.
                int id = item.getItemId();

                switch (id) {

                    case R.id.ac_main_nav_item_pedometer: {
                        loadFragment(1);
                        break;
                    }

                    case R.id.ac_main_nav_item_heartbeat: {
                        loadFragment(2);
                        break;

                    }

                    case R.id.ac_main_nav_item_location: {
                        loadFragment(3);
                        break;

                    }

                    case R.id.ac_main_nav_item_hwmgr: {
                        loadFragment(4);
                        break;

                    }

                    case R.id.ac_main_nav_item_account: {
                        loadFragment(5);
                        break;

                    }

                    case R.id.ac_main_nav_item_logout: {

                        DialogInterface.OnClickListener onPositiveClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String pn = MainActivity.this.getString(R.string.app_store_preferences_name);
                                SharedPreferencesHelper.clearAll(MainActivity.this, pn);

                                //getAccountStateHelper().clearAccountState();
                                doFinishThenStartActivity(MainActivity.this, LoginActivity.class);
                            }
                        };

                        showAlertDialog(
                                "登出确认",
                                "您是否确认登出帐户",
                                "确认",
                                "取消",
                                onPositiveClickListener,
                                null
                        );
                        break;

                    }

                    default: {
                        break;
                    }
                }


                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


    }


    @Override
    public void doOnStart() {

        Bundle bundle = new Bundle();
        bundle.putString("data", "3G." + getAccountStateHelper().getAccountState().getMainHwCode());

        rabitMQServiceItent = new Intent(MainActivity.this, RabitMQService.class);
        rabitMQServiceItent.putExtras(bundle);

        rabitServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                RabitMQBinder rabitMQBinder = (RabitMQBinder) service;
                rabitMQService = rabitMQBinder.getRabitMQService();
                Log.d("onServiceConnected", "Name:" + name + "> 获取Service实例");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.d("onServiceConnected", "Name:" + name);
            }
        };

        bindService(rabitMQServiceItent, rabitServiceConnection, Service.BIND_AUTO_CREATE);
        Log.d("MainActivity", "Rabit服务已绑定");

        mHandler.sendEmptyMessageDelayed(2,1000);
    }


    @Override
    public void doBackKeyPress() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }


    /**
     * 获取Service实例
     *
     * @return
     */
    public RabitMQService getRabitMQService() {
        return rabitMQService;
    }


    @Override
    protected void onStop() {
        super.onStop();

        unbindService(rabitServiceConnection);
        stopService(rabitMQServiceItent);
        Log.d("--MainActivity", "onStop");

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d("--MainActivity", "onPostResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("--MainActivity", "onDestroy");

    }



    /**
     * 加载和替换Fragment
     *
     * @param fragmentType
     */
    protected void loadFragment(int fragmentType) {

        // 帐户管理
        AccountStateStoreModel accountStateStoreModel = getAccountStateHelper().getAccountState();

        String phone = getAccountStateHelper().getAccountState().getPhone();
        String hwCode = getAccountStateHelper().getAccountState().getMainHwCode();
        String sosPhone = getAccountStateHelper().getAccountState().getSosPhone();

        if (fragmentType == 1) {

            toolbar.setTitle(R.string.fragment_pedometer_mgr_title);

            Log.d("Main加载Fragment计步器", phone + "-" + hwCode);

            // 加载Fragment
            FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            //创建 fragment对象
            PedometerFragment fragment = PedometerFragment.newInstance(phone, hwCode);


            //替换Fragment
            transaction.replace(R.id.ac_main_content_container_frame_layout, fragment);
            transaction.commit();

            return;

        }

        if (fragmentType == 2) {

            toolbar.setTitle(R.string.fragment_heartbeat_mgr_title);

            Log.d("Main加载Fragment心率检查", phone + "-" + hwCode);
            // 加载Fragment
            FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            //创建 fragment对象
            HeartBeatFragment fragment = HeartBeatFragment.newInstance(phone, hwCode);


            //替换Fragment
            transaction.replace(R.id.ac_main_content_container_frame_layout, fragment);
            transaction.commit();
            return;

        }


        if (fragmentType == 3) {
            toolbar.setTitle("远程定位");
            Log.d("Main加载Fragment远程定位", phone + "-" + hwCode);
            // 加载Fragment
            FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            //创建 fragment对象
            LocationFragment fragment = LocationFragment.newInstance(phone, hwCode);

            //替换Fragment
            transaction.replace(R.id.ac_main_content_container_frame_layout, fragment);
            transaction.commit();
            return;
        }


        if (fragmentType == 4) {

            toolbar.setTitle(R.string.fragment_deviceind_mgr_title);

            // 加载Fragment
            FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            //创建 fragment对象
            BindDeviceFragment fragment = BindDeviceFragment.newInstance(phone, hwCode, sosPhone);

            //替换Fragment
            transaction.replace(R.id.ac_main_content_container_frame_layout, fragment);
            transaction.commit();
            return;
        }


        if (fragmentType == 5) {

            toolbar.setTitle("手表设置");

            // 加载Fragment
            FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            //创建 fragment对象
            HwSettingFragment fragment = HwSettingFragment.newInstance(phone, hwCode);

            //替换Fragment
            transaction.replace(R.id.ac_main_content_container_frame_layout, fragment);
            transaction.commit();
            return;
        }


        if (fragmentType == 51) {

            toolbar.setTitle("SOS紧急电话设置");

            // 加载Fragment
            FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            //创建 fragment对象
            SosFragment fragment = SosFragment.newInstance(phone, hwCode);

            //替换Fragment
            transaction.replace(R.id.ac_main_content_container_frame_layout, fragment);
            transaction.commit();
            return;
        }

        if (fragmentType == 52) {

            toolbar.setTitle("电话本设置");

            // 加载Fragment
            FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            //创建 fragment对象
            PhoneBookFragment fragment = PhoneBookFragment.newInstance(phone, hwCode);

            //替换Fragment
            transaction.replace(R.id.ac_main_content_container_frame_layout, fragment);
            transaction.commit();
            return;
        }

        if (fragmentType == 53) {

            toolbar.setTitle("发送消息");

            // 加载Fragment
            FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            //创建 fragment对象
            SendMessageFragment fragment = SendMessageFragment.newInstance(phone, hwCode);

            //替换Fragment
            transaction.replace(R.id.ac_main_content_container_frame_layout, fragment);
            transaction.commit();
            return;
        }

        if (fragmentType == 54) {

            toolbar.setTitle("手表拨打电话");

            // 加载Fragment
            FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            //创建 fragment对象
            PhoneCallFragment fragment = PhoneCallFragment.newInstance(phone, hwCode);

            //替换Fragment
            transaction.replace(R.id.ac_main_content_container_frame_layout, fragment);
            transaction.commit();
            return;
        }

    }


    @Override
    public void onFragmentInteraction(int actionType, String args) {

        Log.d("MainActivity.MESSAGE", actionType + " | " + args);

        switch (actionType) {

            case 1:

                if (args == "BINDING") {
                    loadFragment(4);
                    return;
                }

                if (args == "MESSAGE") {
                    loadFragment(53);
                    return;
                }

                if (args == "CALL") {
                    loadFragment(54);
                    return;
                }

                if (args == "PHONEBOOK") {
                    loadFragment(52);
                    return;
                }

                if (args == "SOS") {
                    loadFragment(51);
                    return;
                }

                break;


            default:
                break;


        }


    }
}
