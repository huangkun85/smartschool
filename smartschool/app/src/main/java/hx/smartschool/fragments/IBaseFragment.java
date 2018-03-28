package hx.smartschool.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment重载的重写方法
 */

public interface IBaseFragment {

    /**
     * 初始化参数
     * @param argBundle
     */
    void initParameter(Bundle argBundle);

    /**
     * 初始化页面
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 初始化监听器
     * @param view
     */
    void initListener(View view);


    /**
     * 保存Fragment 状态，在Detatch上执行
     */
    void SaveFragmentState();

    /**
     * 加载Fragment状态，在Attache上执行
     */
    void LoadFragmentState();
}
