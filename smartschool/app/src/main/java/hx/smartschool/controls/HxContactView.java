package hx.smartschool.controls;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * KUN
 */

public class HxContactView extends LinearLayout {

    private TextView mTextView;
    private EditText mContactName;
    private EditText mContactPhone;


    /**
     *
     * @param context
     */
    public HxContactView(Context context) {
        super(context);

    }

    public HxContactView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public String getPhone() {
        return this.mContactPhone.getText().toString();
    }


}
