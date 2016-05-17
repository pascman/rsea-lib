package rsea.lib.util.etc;

import android.animation.Animator;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2015-10-16.
 */
public class MarginAnimatorListener implements Animator.AnimatorListener {

    private View vv;
    private int x;
    private int y;
    public MarginAnimatorListener(View v,int x,int y){
        vv = v;
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if(vv.getLayoutParams() instanceof ViewGroup.MarginLayoutParams){
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) vv.getLayoutParams();
            params.topMargin = y;
            params.leftMargin = x;
            vv.setLayoutParams(params);
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
