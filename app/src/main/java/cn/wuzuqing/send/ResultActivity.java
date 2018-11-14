package cn.wuzuqing.send;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import java.util.Locale;


/**
 * 问题答案列表
 *
 * @author wuxianglong
 * @date 2016/9/8
 */
public class ResultActivity extends BaseActivity {

    private TextView textAsk;
    private TextView textAnswer;
    private TextView textLink;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_result;
    }

    @Override
    protected void initView() {
        textAnswer = findViewById(R.id.text_answer);
        textAsk = findViewById(R.id.text_ask);
        textLink = findViewById(R.id.text_link);
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
            int position = intent.getIntExtra("position", 0);
            final String question = intent.getStringExtra("question");
            final String tag = intent.getStringExtra("tag");
            String content = intent.getStringExtra("content");
            final String link = intent.getStringExtra("link");
            textAsk.setText(String.format(Locale.getDefault(), "%d : %s", position, question));
            textAnswer.setText(content);
            if (!TextUtils.isEmpty(link)) {
                textLink.setText(link);
                textLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toWebView(link, question);
                    }
                });
            }
            setTitle(tag);
        }

        //启动动画
        textAsk.post(new Runnable() {
            @Override
            public void run() {
                startTextAnim(textAsk);
            }
        });
        //启动动画
        textLink.post(new Runnable() {
            @Override
            public void run() {
                startTextAnim(textLink);
            }
        });
        textAnswer.post(new Runnable() {
            @Override
            public void run() {
                startTextAnim(textAnswer);
            }
        });
    }

    private void toWebView(String link, String question) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra("link", link);
        intent.putExtra("question", question);
        startActivity(intent);
    }

    /**
     * TextView展开动画（5.0以上）
     *
     * @param view
     */
    private void startTextAnim(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Animator animator = ViewAnimationUtils.createCircularReveal(
                    view, 0, 0, 0,
                    (float) Math.hypot(view.getWidth(), view.getHeight()));
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(700);
            animator.start();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
    }
}
