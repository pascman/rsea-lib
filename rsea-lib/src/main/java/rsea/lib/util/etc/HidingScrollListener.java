package rsea.lib.util.etc;

import android.support.v7.widget.RecyclerView;

/**
 * Created by RedSea on 2015. 10. 3..
 */
public abstract class HidingScrollListener extends RecyclerView.OnScrollListener {
    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlToggle = true;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (scrolledDistance > HIDE_THRESHOLD && controlToggle) {
            onScollUp();
            controlToggle = false;
            scrolledDistance = 0;
        } else if (scrolledDistance < -HIDE_THRESHOLD && !controlToggle) {
            onScollDown();
            controlToggle = true;
            scrolledDistance = 0;
        }

        if((controlToggle && dy>0) || (!controlToggle && dy<0)) {
            scrolledDistance += dy;
        }
    }

    public boolean getControlToggle(){
        return controlToggle;
    }

    public abstract void onScollUp();
    public abstract void onScollDown();

}