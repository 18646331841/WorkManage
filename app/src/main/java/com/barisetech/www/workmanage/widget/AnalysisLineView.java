package com.barisetech.www.workmanage.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.barisetech.www.workmanage.R;

public class AnalysisLineView extends LinearLayout {

    public static final String PHONE = "phone";
    public static final String NUMBER = "number";
    public static final String NUMBER_DECIMAL = "numberDecimal";
    private Context mContext;
    private ConstraintLayout rlItem;
    private TextView tvLabel;
    private EditText etContent;
    private View vArrow;
    private View vLine;

    public AnalysisLineView(Context context) {
        super(context);

        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    public AnalysisLineView(Context context, AttributeSet attrs) {

        super(context, attrs);

        mContext = context;

        View view = LayoutInflater.from(context).inflate(R.layout.layout_analysis_line_order, this);

        rlItem = (ConstraintLayout) view.findViewById(R.id.rl_item);
        tvLabel = (TextView) view.findViewById(R.id.tv_label);
        etContent = (EditText) view.findViewById(R.id.et_content);
        vArrow = view.findViewById(R.id.v_arrow);
        vLine = view.findViewById(R.id.v_line);

        rlItem.setOnClickListener(v -> {

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
        });

        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.order);

        String label = typedArray.getString(R.styleable.order_ddd_label);
        String content = typedArray.getString(R.styleable.order_ddd_content);
        int contentColor = typedArray.getColor(R.styleable.order_ddd_content_color, Color.parseColor("#000000"));
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
        } else {
            etContent.setText("");
        }

        if (!TextUtils.isEmpty(contentHint)) {
            etContent.setHint(contentHint);
        } else {
            etContent.setHint("");
        }

        etContent.setTextColor(contentColor);
        etContent.setHintTextColor(hintColor);

        etContent.setEnabled(isContentEnable);
//        etContent.setSingleLine(false);
//        etContent.setHorizontallyScrolling(false);

        // 设置输入类型
//        setInputType(inputType);

        if (isIndicate) {
            vArrow.setVisibility(View.VISIBLE);
//            vArrow.setBackgroundResource(R.drawable.icon_arrow_right);
            etContent.setGravity(Gravity.RIGHT);
        } else {
            vArrow.setBackgroundColor(Color.TRANSPARENT);
//            vArrow.setVisibility(View.GONE);
        }

        if (isBottomLine) {
            vLine.setVisibility(View.VISIBLE);
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
        } else if (NUMBER.equals(inputType)) {
//            etContent.addTextChangedListener(new NumberDicemalTextWatcher(etContent));
            etContent.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        } else if (NUMBER_DECIMAL.equals(inputType)) {
//            etContent.addTextChangedListener(new NumberDicemalTextWatcher(etContent));
            etContent.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType
                    .TYPE_NUMBER_VARIATION_NORMAL);
        } else {
            etContent.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
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
