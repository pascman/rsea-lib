package rsea.lib.util.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by RedSea on 2015. 10. 8..
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public static interface ViewHolderListener{
        public void onViewHolderItemClick(View v, int position);
    }
    public RecyclerViewHolder(View itemView) {
        super(itemView);
//        Field[] fields = getClass().getFields();
//        try{
//            for(Field f : fields){
//                if(f.isAnnotationPresent(RedView.class)){
//                    RedView redView = f.getAnnotation(RedView.class);
//                    View view = itemView.findViewById(redView.value());
//                    if(redView.type().equals("currency")){
//                        ((EditText)view).addTextChangedListener(new CurrencyWatcher((EditText) view));
//                    }
//                    f.set(this,view);
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//            throw new AndroidRuntimeException(e);
//        }
    }

    public void setOnItemClickListener(final int pos, final ViewHolderListener listener){
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onViewHolderItemClick(v, pos);
            }
        });
    }

    public void setOnItemButtonClickListener(final int pos, final ViewHolderListener listener,View view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onViewHolderItemClick(v, pos);
            }
        });
    }


}