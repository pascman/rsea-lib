package rsea.lib.util.etc;

import android.support.v7.widget.RecyclerView;

/**
 * Created by RedSea on 2015. 10. 3..
 */
public abstract class ScrollDetectListener<T> extends RecyclerView.OnScrollListener {

    private int scrollY = 0;
    private int scrollX = 0;
    public ScrollDetectListener(){
        init();
    }
    public void init(){
        scrollY =0;

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        scrollY = scrollY + dy;
        scrollX = scrollX + dx;
    }

    public int getScrollY(){
        return scrollY;
    }
    public int getScrollX(){
        return scrollX;
    }
}