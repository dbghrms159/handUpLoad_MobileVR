package kr.ac.hallym.skeleton;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by yuhogeun on 2017. 11. 26..
 */

public class OverlayView extends LinearLayout {
    private OverlayEye leftEye;
    private OverlayEye rightEye;


    public OverlayView(Context context, AttributeSet attre) {
        super(context, attre);
        Thread thread = new Thread(){};
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT,1.0f
        );
        params.setMargins(0,    0,  0,  0);

        leftEye = new OverlayEye(context,attre);
        leftEye.setLayoutParams(params);
        addView(leftEye);

        rightEye = new OverlayEye(context,attre);
        rightEye.setLayoutParams(params);
        addView(rightEye);

        setDepthFactor(0.01f);
        setColor(Color.rgb(0,0,0));
        addContext("0");
        setVisibility(View.VISIBLE);
    }

    public void setDepthFactor(float factor){
        leftEye.setDepthFactor(factor);
        rightEye.setDepthFactor(-factor);
    }

    public void setColor(int color){
        leftEye.setColor(color);
        rightEye.setColor(color);
    }

    public void addContext(String text){
        leftEye.setContext(text);
        rightEye.setContext(text);
    }
}
