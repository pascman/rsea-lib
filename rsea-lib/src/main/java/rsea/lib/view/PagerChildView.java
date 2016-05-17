package rsea.lib.view;

import android.content.Context;
import android.content.Intent;
import android.widget.FrameLayout;
import android.widget.Toast;

public abstract class PagerChildView extends FrameLayout {
	protected boolean isRefreshed = false;
	private Toast mToast;
	public PagerChildView(Context context) {
		super(context);
	}
	
	public abstract void onRefreshView();
	protected void onDestory() {
		
	}
	public void onRefreshRawView(){
		if(!isRefreshed){
			onRefreshView();
			isRefreshed = true;
		}
	}

	public void onActivityResultForView(int requestCode, int resultCode, Intent data){

	}
	
	public void showToast(String msg){
		if(mToast == null){
			mToast = Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT);
			mToast.show();
		}else{
			mToast.setText(msg);
			mToast.show();
		}
	}

}
