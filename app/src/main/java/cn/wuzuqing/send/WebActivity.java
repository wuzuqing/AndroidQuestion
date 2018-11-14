package cn.wuzuqing.send;

import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 * 问题答案列表
 *
 * @author wuxianglong
 * @date 2016/9/8
 */
public class WebActivity extends BaseActivity {

    private WebView webView;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    protected void initView() {
        webView = findViewById(R.id.webView);
        //页面过度动画
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                Transition slide = TransitionInflater.from(this).inflateTransition(R.transition.slide);
                getWindow().setExitTransition(slide);
                getWindow().setEnterTransition(slide);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            final String link = intent.getStringExtra("link");
            if (!TextUtils.isEmpty(link)) {

                webView.setWebViewClient(new WebViewClient(){
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                        view.loadUrl(link);
                        return true;
                    }
                });
                webView.loadUrl(link);
            }
            setTitle("网页");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            ViewGroup parent = (ViewGroup) webView.getParent();
            parent.removeView(webView);
            webView.destroy();
        }
        super.onDestroy();

    }
}
