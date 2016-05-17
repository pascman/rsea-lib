/*******************************************************************************
 * Copyright (c) 2011  P&K 
 * Contributors:
 *     RedSea - initial API and implementation
 *******************************************************************************/
package rsea.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import rsea.lib.R;
import rsea.lib.util.DipUtil;


public class PageController extends LinearLayout{

	
	private Drawable activeRes;
	private Drawable deActiveRes;
	private int nTotalPage = 0;
	private int nCurrentPage = 0;
	private int defaultGap = 0;
	private Context mContext;
	public PageController(Context context) {
		super(context);
		init(context);
	}

	public PageController(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PageController);
		nTotalPage = a.getInteger(R.styleable.PageController_totalpage, 0);
		nCurrentPage = a.getInteger(R.styleable.PageController_currentpage,0);
		activeRes = a.getDrawable(R.styleable.PageController_active);
		deActiveRes = a.getDrawable(R.styleable.PageController_deactive);
		defaultGap =  a.getDimensionPixelSize(R.styleable.PageController_gap, DipUtil.pixel(3));
		setTotalPage(nTotalPage);
		setCurrentPage(nCurrentPage);
	}

	public void init(Context c){
		mContext = c;
		setOrientation(LinearLayout.HORIZONTAL);
		setGravity(Gravity.CENTER);
	}
	
	public void setTotalPage(int page){
		removeAllViews();
		

		nTotalPage = page;
		for(int i=0;i<nTotalPage;i++){
			ImageView img = new ImageView(mContext);
			img.setImageDrawable(deActiveRes);
			if(i!=0){
				LayoutParams param = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				param.setMargins(defaultGap, 0, 0, 0);
				img.setLayoutParams(param);
			}
			addView(img);
		}
		setCurrentPage(0);
	}
	
	public void setCurrentPage(int page){
		nCurrentPage = page;
		if(page>= nTotalPage){
			return;
		}
		for(int i=0;i<getChildCount();i++){
			ImageView img = (ImageView) getChildAt(i);
			if(nCurrentPage == i){
				img.setImageDrawable(activeRes);
			}else{
				img.setImageDrawable(deActiveRes);
			}
		}
	}
}
