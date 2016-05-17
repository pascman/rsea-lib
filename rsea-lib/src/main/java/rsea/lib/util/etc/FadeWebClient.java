package rsea.lib.util.etc;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Administrator on 2015-11-11.
 */
public class FadeWebClient extends WebViewClient{


    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        view.setVisibility(View.GONE);
    }


    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.ALPHA, 0.0f,1.1f);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(250);
        animator.start();
        view.setVisibility(View.VISIBLE);
    }
}
