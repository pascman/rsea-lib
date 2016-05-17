package rsea.lib.util.etc;

import android.util.SparseArray;
import android.view.View;

public class RedHolder {


	private SparseArray<View> mMap = new SparseArray<View>();
	private Object tag;
	
	public View findViewById(Integer id){
		return mMap.get(id);
	}
	
	public void saveID(Integer id,View v){
		mMap.put(id, v.findViewById(id));
	}

	public Object getTag() {
		return tag;
	}

	public void setTag(Object tag) {
		this.tag = tag;
	}
}