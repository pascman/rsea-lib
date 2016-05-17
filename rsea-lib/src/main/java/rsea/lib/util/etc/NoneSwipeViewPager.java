package rsea.lib.util.etc;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2015-10-07.
 */
public class NoneSwipeViewPager extends ViewPager{

    public NoneSwipeViewPager(Context context) {
        super(context);
    }
    public NoneSwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean canScrollHorizontally(int direction) {
        return false;
    }
}
