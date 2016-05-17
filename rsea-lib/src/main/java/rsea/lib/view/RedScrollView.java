package rsea.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2015-10-22.
 */
public class RedScrollView extends ScrollView{

    private long lastScrollUpdate = -1;
    private boolean isScrolling = false;
    public static interface OnRedScrollListener{
        public void onScrollChanged(int x, int y, int oldx, int oldy);
    }

    private OnRedScrollListener onRedListener;

    public RedScrollView(Context context) {
        super(context);
    }

    public RedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnRedScrollListener(OnRedScrollListener listener){
        onRedListener = listener;
    }

    public boolean isScrolling(){
        return isScrolling;
    }


    private class ScrollStateHandler implements Runnable {

        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastScrollUpdate) > 100) {
                lastScrollUpdate = -1;
                isScrolling = false;
            } else {
                postDelayed(this, 100);
            }
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (lastScrollUpdate == -1) {
            isScrolling = true;
            postDelayed(new ScrollStateHandler(), 100);
        }
        lastScrollUpdate = System.currentTimeMillis();

        if(onRedListener != null){
            onRedListener.onScrollChanged(l,t,oldl,oldt);
        }
    }


}
