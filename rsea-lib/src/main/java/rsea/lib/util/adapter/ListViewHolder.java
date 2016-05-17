package rsea.lib.util.adapter;

import android.util.AndroidRuntimeException;
import android.view.View;
import android.widget.EditText;

import java.lang.reflect.Field;

import rsea.lib.util.anontation.RedView;
import rsea.lib.util.etc.CurrencyWatcher;


/**
 * Created by RedSea on 2015. 10. 8..
 */

public class ListViewHolder{

    public static interface ViewHolderListener{
        public void onViewHolderItemClick(View v, int position);
    }
    public View itemView;
    public ListViewHolder(View itemView) {
        super();
        this.itemView = itemView;
        Field[] fields = getClass().getFields();
        for(Field f : fields){
            if(f.isAnnotationPresent(RedView.class)){
                RedView redView = f.getAnnotation(RedView.class);
                View view = itemView.findViewById(redView.value());
                if(redView.type().equals("currency")){
                    ((EditText)view).addTextChangedListener(new CurrencyWatcher((EditText) view));
                }
                try {
                    f.set(this,view);
                } catch (IllegalAccessException e) {
                    throw new AndroidRuntimeException(e);
                }
            }
        }
    }

}