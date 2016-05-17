package rsea.lib.util.adapter;

import android.widget.BaseAdapter;

/**
 * Created by RedSea on 2015. 10. 8..
 */
public abstract class BaseAdapterImpl extends BaseAdapter{
    protected Object datas;

    public void setMappingData(Object object){
        datas = object;
    }

}
