package rsea.lib.http;

import android.app.Dialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import rsea.lib.util.DipUtil;

public class RSHttpProgressDialog extends Dialog{

    private Context mContext;
    private TextView mTitleView;
    public RSHttpProgressDialog(Context context) {
        super(context);
        mContext = context;
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(createDialogLayout());
        setCancelable(false);
    }

    private View createDialogLayout(){
        FrameLayout rootView = new FrameLayout(mContext);
        rootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        rootView.setBackgroundResource(android.R.color.transparent);
        LinearLayout lLayout = new LinearLayout(mContext);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        lLayout.setLayoutParams(params);
        lLayout.setOrientation(LinearLayout.VERTICAL);

        ProgressBar pBar = new ProgressBar(mContext);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(DipUtil.pixel(29.3333f), DipUtil.pixel(29.6667f));
        params2.gravity = Gravity.CENTER;
        pBar.setLayoutParams(params2);
        TextView tv = new TextView(mContext);
        params2 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params2.gravity = Gravity.CENTER;
        tv.setLayoutParams(params2);
        tv.setTextColor(0xFF525252);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
        tv.setPadding(0, 4, 0, 0);
        tv.setText("");

        lLayout.addView(pBar);
        lLayout.addView(tv);

        rootView.addView(lLayout);

        mTitleView = tv;

        return rootView;
    }
    public void setText(String title){
        mTitleView.setText(title);
    }
    @Override
    public void show() {
        getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;
//        getWindow().getAttributes().height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setWindowAnimations(android.R.style.Animation_Toast);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().getAttributes().dimAmount = 0.0f;
        super.show();
    }

}
