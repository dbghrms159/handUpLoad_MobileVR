package kr.ac.hallym.skeleton;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by yuhogeun on 2017. 11. 26..
 */

public class OverlayEye extends ViewGroup{

    private Context context;
    private AttributeSet attrs;
    private TextView textView;
    private int textColor;
    private int depthOffset;
    private int viewW;

    public OverlayEye(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
    }

    public void setColor(int color){
        this.textColor = color;
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int w = r - l;
        final int h = b - t;
        viewW = w;

        final float verticalTextPos = 0.2f;

        float topMargin = h * verticalTextPos;
        textView.layout(0,(int) topMargin, w,b);
    }

    public void setDepthFactor(float factor){
        this.depthOffset = (int)(factor * viewW);
    }

    public void setContext(String text){
        removeAllViews();
        textView = new TextView(context, attrs);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(textColor);
        textView.setText(text);
        textView.setX(depthOffset);
        addView(textView);
    }
}
