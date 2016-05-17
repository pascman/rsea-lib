package rsea.lib.util.adapter;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONObject;

import java.util.List;

import rsea.lib.util.etc.PageScrollListener;


/**
 * Created by RedSea on 2015. 10. 8..
 */
public abstract class RecyclerAdapterImpl extends RecyclerView.Adapter<RecyclerViewHolder>{

    protected Object datas;
    private RecyclerViewHolder[] headers;
    private int row_layout;
    private PageScrollListener pageination;
    private OnPageRequestListener page_request_listener;
    public static interface OnPageRequestListener{
        public void requestPage(int page);
    }
    public RecyclerAdapterImpl(int res_id){
        super();
        this.row_layout = res_id;
    }


    public RecyclerViewHolder header(int position){
        return headers[position];
    }

    public void initHeaderSize(int headeSize){
        headers = new RecyclerViewHolder[headeSize];
    }
    public void setHeaders(int position,RecyclerViewHolder holder){
        headers[position] = holder;
    }

    public void setRowLayout(int row_layout) {
        this.row_layout = row_layout;
    }

    public int getRowLayout() {
        return row_layout;
    }

    public void setMappingData(Object object){
        datas = object;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setOnPageRequestListener(RecyclerView recylcer_view ,OnPageRequestListener listener){
        pageination = new PageScrollListener((LinearLayoutManager) recylcer_view.getLayoutManager()) {
            @Override
            public void loadPage(int page) {
                page_request_listener.requestPage(page);
            }
        };
        page_request_listener = listener;
        recylcer_view.addOnScrollListener(pageination);

    }

    public void clear(RecyclerView recycler){
        if(datas instanceof JSONObject){
            datas = new JSONObject();
        }else{
            ((List<Bundle>)datas).clear();
            if(pageination != null)
                pageination.init((List<Bundle>) datas);
        }
        recycler.setAdapter(this);
    }
    public void pageComplate(){
        pageination.complete();
    }
    public void pageDisabled(){
        pageination.disabled();
    }
    public void pageFail(){
        pageination.fail();
    }

    public void changeRecyclerManager(RecyclerView.LayoutManager manager){
        pageination.setLayoutManager((LinearLayoutManager) manager);
    }
}
