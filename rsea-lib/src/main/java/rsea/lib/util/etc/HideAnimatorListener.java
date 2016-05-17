package rsea.lib.util.etc;

import android.animation.Animator;
import android.view.View;

/**
 * Created by Administrator on 2015-10-16.
 */
public class HideAnimatorListener implements Animator.AnimatorListener {

    private View vv;
    public HideAnimatorListener(View v){
        vv = v;
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        vv.setVisibility(View.GONE);
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
