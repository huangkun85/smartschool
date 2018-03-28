package hx.smartschool.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import hx.smartschool.R;
import hx.smartschool.activities.main.MainActivity;


public abstract class BaseFragment extends Fragment implements IBaseFragment {

    protected String preferencesName;
    protected MainActivity mainActivity;

    //和主Activity 交互的监听器
    protected OnFragmentInteractionListener mListener;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesName = this.getContext().getString(R.string.app_store_preferences_name);

        initParameter(this.getArguments());
        LoadFragmentState();


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = initLayout(inflater, container, savedInstanceState);

        //加载视图后初始化控制器监听器，继承类重写
        initListener(view);

        return view;

    }


    /**
     * 长时间显示Toast提示(来自String)
     **/
    public void showLongToast(String text) {
        Toast.makeText(this.getContext(), text, Toast.LENGTH_LONG).show();
    }


    /**
     * 含有标题和内容的对话框
     **/
    public AlertDialog showAlertDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).setTitle(title)
                .setMessage(message).show();
        return alertDialog;
    }

    /**
     * 含有标题、内容、两个按钮的对话框
     *
     * @param title
     * @param message
     * @param positiveText
     * @param onPositiveClickListener
     * @param negativeText
     * @param onNegativeClickListener
     * @return
     */
    public AlertDialog showAlertDialog(String title,
                                       String message,
                                       String positiveText,
                                       String negativeText,
                                       DialogInterface.OnClickListener onPositiveClickListener,
                                       DialogInterface.OnClickListener onNegativeClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveText, onPositiveClickListener)
                .setNegativeButton(negativeText, onNegativeClickListener)
                .show();

        return alertDialog;
    }

    /**
     * 含有标题、内容、图标、两个按钮的对话框
     **/
    public AlertDialog showAlertDialog(String title, String message,
                                       int icon, String positiveText,
                                       DialogInterface.OnClickListener onPositiveClickListener,
                                       String negativeText,
                                       DialogInterface.OnClickListener onNegativeClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).setTitle(title)
                .setMessage(message).setIcon(icon)
                .setPositiveButton(positiveText, onPositiveClickListener)
                .setNegativeButton(negativeText, onNegativeClickListener)
                .show();
        return alertDialog;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.d("BaseFragment.onAttach", "OnAttach");

        mainActivity = (MainActivity) context;

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        Log.d("BaseFragment.onDetach", "onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
        SaveFragmentState();

        Log.d("BaseFragment.onDetach", "onDetach");
    }


}

