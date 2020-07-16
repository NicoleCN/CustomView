package com.example.customview.showalltextview;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.example.customview.R;

/***
 * @date 2020-07-16 09:47
 * @author BoXun.Zhao
 * @description maxLines根据需求自行设置
 */
public class ShowAllTextView extends AppCompatTextView {
    private static final String STR_SUFFIX = "...全文";
    private static final int SUFFIX_LENGTH = STR_SUFFIX.length();
    private SpannableStringBuilder spanBuilder = new SpannableStringBuilder();
    private ClickSpan clickSpan = new ClickSpan(getContext());

    public ShowAllTextView(Context context) {
        this(context, null);
    }

    public ShowAllTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShowAllTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        setMovementMethod(LinkMovementMethod.getInstance());
        setHighlightColor(ContextCompat.getColor(context, R.color.transparent));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final Layout layout = getLayout();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (layout.getLineCount() >= getMaxLines()) {
                CharSequence charSequence = getText();
                int lastCharDown = layout.getLineVisibleEnd(getMaxLines() - 1);
                if (lastCharDown >= SUFFIX_LENGTH && charSequence.length() > lastCharDown) {
                    String tempCharSeq = charSequence.toString().substring(0, lastCharDown);
                    if ((tempCharSeq).contains("\n")) {
                        spanBuilder.append(charSequence.subSequence(0, lastCharDown))
                                .append(STR_SUFFIX);
                    } else {
                        spanBuilder
                                //省略号有点短 算1个文字
                                .append(charSequence.subSequence(0, lastCharDown + 2 - SUFFIX_LENGTH))
                                .append(STR_SUFFIX);
                    }
                    int end = spanBuilder.length();
                    int start = end - SUFFIX_LENGTH;
                    if (end > 0 && start > 0) {
                        spanBuilder.setSpan(clickSpan, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                        setText(spanBuilder);
                    }
                }
            }
        }
        super.onDraw(canvas);
    }

    private static class ClickSpan extends ClickableSpan {
        private Context mContext;

        public ClickSpan(Context context) {
            mContext = context;
        }

        @Override
        public void onClick(View widget) {
            if (widget instanceof TextView) {
                ((TextView) widget).setHighlightColor(ContextCompat.getColor(mContext, R.color.transparent));
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
            ds.setColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        }
    }
}
