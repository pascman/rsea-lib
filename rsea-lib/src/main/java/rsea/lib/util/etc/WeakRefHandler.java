package rsea.lib.util.etc;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2015-10-01.
 */
public class WeakRefHandler extends Handler{

    private final WeakReference<IWeakRefMessage> targets;

    public WeakRefHandler(IWeakRefMessage _target){
        targets = new WeakReference<IWeakRefMessage>(_target);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        IWeakRefMessage target =  targets.get();
        if(target == null) return;
        target.handleMessage(msg);
    }
}
