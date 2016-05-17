package rsea.lib.util.etc;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import java.util.ArrayList;

public class AnimationManager implements AnimationListener{

	public interface onAllAnimationFinishListener{
		public void animationStarted(AnimationManager manager);
		public void animationAllFinished(AnimationManager manager);
	}

	private ArrayList<Animation> animationList = new ArrayList<Animation>();
	private onAllAnimationFinishListener mListener;
	private boolean isStarted = false;
	private Object tag;
	private int id;
	public Object getTag() {
		return tag;
	}
	public void setTag(Object tag) {
		this.tag = tag;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public AnimationManager(onAllAnimationFinishListener listener){
		mListener = listener;
	}
	public void addAnimation(Animation anim){
		anim.setAnimationListener(this);
		animationList.add(anim);
	}
	@Override
	public void onAnimationEnd(Animation animation) {
		animationList.remove(animation);
		if(animationList.size()==0){
			if(mListener!=null)
				mListener.animationAllFinished(this);
		}
	}
	
	@Override
	public void onAnimationRepeat(Animation animation) {
		
	}
	@Override
	public void onAnimationStart(Animation animation) {
		if(!isStarted && mListener!=null) mListener.animationStarted(this);
	}
	
}
