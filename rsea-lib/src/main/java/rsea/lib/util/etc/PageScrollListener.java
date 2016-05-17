package rsea.lib.util.etc;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by RedSea on 2015. 10. 3..
 */
public abstract class PageScrollListener<T> extends RecyclerView.OnScrollListener {

    private RecyclerView.LayoutManager manager;
    private T datas;
    private boolean isLoading = false;
    private boolean isEnded = false;
    private int start_page = 0;
    public PageScrollListener(LinearLayoutManager manager){
        this.manager = manager;
    }
    public PageScrollListener(){
    }
    public void setLayoutManager(LinearLayoutManager manager){
        this.manager = manager;
    }
    public void init(T datas){
        this.datas = datas;
        start_page = 0;
        isLoading =false;
        isEnded= false;

    }

    public void enabled(){
        isEnded = false;
    }
    public void disabled(){
        isEnded = true;
    }
    public void complete(){
        start_page++;
        isLoading = false;
    }
    public void fail(){
        isLoading = false;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if(isLoading || isEnded || datas == null){
            return;
        }
        int visibleItemCount = 0;
        int pastVisibles = 2;
        if(manager instanceof  GridLayoutManager || manager instanceof LinearLayoutManager){
            visibleItemCount = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
        }
        int dateSize = datas instanceof JSONArray ? ((JSONArray)datas).length() : ((List)datas).size();
        if(visibleItemCount + pastVisibles>= dateSize){
            isLoading = true;
            loadPage(start_page+1);
        }
    }
    public abstract void loadPage(int page);

}