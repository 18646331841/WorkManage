package com.barisetech.www.workmanage.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.barisetech.www.workmanage.R;

public class FormView extends LinearLayout{



    public static final String PHONE = "phone";
    public static final String NUMBER = "number";
    public static final String NUMBER_DECIMAL = "numberDecimal";
    private Context mContext;
    private RelativeLayout rlItem;
    private TextView tvLabel;
    private EditText etContent;
    private TextView tvContent;
    private android.view.View vArrow;
    private android.view.View vLine;
//    private NumberDicemalTextWatcher mNumberDicemalTextWatcher;
    private TextWatcher mTextWatcher;

    public FormView(Context context) {
        super(context);

        mContext = context;
    }

    public FormView(Context context, AttributeSet attrs) {

        super(context, attrs);

        mContext = context;

//        mNumberDicemalTextWatcher = new NumberDicemalTextWatcher();
        mTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        android.view.View view = LayoutInflater.from(context).inflate(R.layout.layout_form_order, this);

        rlItem = (RelativeLayout) view.findViewById(R.id.rl_item);
        tvLabel = (TextView) view.findViewById(R.id.tv_label);
        etContent = (EditText) view.findViewById(R.id.et_content);
        tvContent = (TextView) view.findViewById(R.id.tv_content);
        vArrow = view.findViewById(R.id.v_arrow);
        vLine = view.findViewById(R.id.v_line);

        rlItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                if (mOnItemClickListener != null) {

                    rlItem.setEnabled(false);
                    mOnItemClickListener.onClick();

                    CountDownTimer countDownTimer = new CountDownTimer(1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {

                            rlItem.setEnabled(true);
                        }
                    };
                    countDownTimer.start();
                }
            }
        });

        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.order);

        String label = typedArray.getString(R.styleable.order_ddd_label);
        String content = typedArray.getString(R.styleable.order_ddd_content);
        int contentColor = typedArray.getColor(R.styleable.order_ddd_content_color, Color.parseColor("#262626"));
        String contentHint = typedArray.getString(R.styleable.order_ddd_content_hint);
        int hintColor = typedArray.getColor(R.styleable.order_ddd_content_hint_color, Color.parseColor("#ABABAB"));
        boolean isContentEnable = typedArray.getBoolean(R.styleable.order_ddd_content_enable, false);
        boolean isIndicate = typedArray.getBoolean(R.styleable.order_ddd_is_indicate, false);
        boolean isBottomLine = typedArray.getBoolean(R.styleable.order_ddd_is_bottom_line, false);
        String inputType = typedArray.getString(R.styleable.order_dddInputType);

        tvLabel.setText(label);

        if (!TextUtils.isEmpty(content)) {
            etContent.setText(content);
            etContent.setSelection(etContent.getText().toString().length());

            tvContent.setText(content);
        } else {
            etContent.setText("");
            tvContent.setText("");
        }

        if (!TextUtils.isEmpty(contentHint)) {
            etContent.setHint(contentHint);
            tvContent.setHint(contentHint);
        } else {
            etContent.setHint("");
            tvContent.setHint("");
        }

        etContent.setTextColor(contentColor);
        tvContent.setTextColor(contentColor);
        etContent.setHintTextColor(hintColor);
        tvContent.setHintTextColor(hintColor);

        etContent.setEnabled(isContentEnable);

        // 设置输入类型
        setInputType(inputType);

        if (isContentEnable) {
            etContent.setVisibility(android.view.View.VISIBLE);
            tvContent.setVisibility(android.view.View.GONE);
        } else {
            etContent.setVisibility(android.view.View.GONE);
            tvContent.setVisibility(android.view.View.VISIBLE);
        }

        if (isIndicate) {
            vArrow.setVisibility(android.view.View.VISIBLE);
        } else {
            vArrow.setVisibility(android.view.View.GONE);
        }

        if (isBottomLine) {
            vLine.setVisibility(android.view.View.VISIBLE);
        } else {
            vLine.setVisibility(View.GONE);
        }

        typedArray.recycle();
    }

    /**
     * 获取编辑文本内容
     *
     * @return
     */
    public String getText() {

        return etContent.getText().toString();
    }

    /**
     * 设置内容
     *
     * @param content
     */
    public void setText(String content) {
        if (TextUtils.isEmpty(content)) {
            content = "";
        }
        etContent.setText(content);
        tvContent.setText(content);
    }

    /**
     * 设置光标所在的位置
     *
     * @param length
     */
    public void setSelection(int length) {
        etContent.setSelection(length);
    }

    /**
     * 设置输入类型
     *
     * @param inputType
     */
    public void setInputType(String inputType) {

        if (PHONE.equals(inputType)) {
            etContent.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_TEXT_VARIATION_PHONETIC);
            tvContent.setInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_TEXT_VARIATION_PHONETIC);
        } else if (NUMBER.equals(inputType)) {
//            etContent.addTextChangedListener(new NumberDicemalTextWatcher(etContent));
            etContent.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
            tvContent.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        } else if (NUMBER_DECIMAL.equals(inputType)) {
//            etContent.addTextChangedListener(new NumberDicemalTextWatcher(etContent));
            etContent.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_VARIATION_NORMAL);
            tvContent.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        } else {
            etContent.setInputType(InputType.TYPE_CLASS_TEXT);
            tvContent.setInputType(InputType.TYPE_CLASS_TEXT);
        }
    }

    /**
     * 设置内容
     *
     * @param content
     */
    public void setText(int content) {
        etContent.setText(content);
    }

    public void contentFocus() {
        etContent.requestFocus();
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {

        void onClick();
    }

}
