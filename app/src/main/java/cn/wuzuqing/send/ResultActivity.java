package cn.wuzuqing.send;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import cn.wuzuqing.send.bean.AnswerBean;
import cn.wuzuqing.send.constant.Const;
import cn.wuzuqing.send.dao.DbCodeManager;
import cn.wuzuqing.send.http.Consumer;
import cn.wuzuqing.send.http.HttpHelper;
import cn.wuzuqing.send.http.ListHttpCallBack;

import java.util.List;
import java.util.Locale;
import java.util.Map;


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
    private String link;

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
            final String questionId = intent.getStringExtra("questionId");
            final String tag = intent.getStringExtra("tag");
            textAsk.setText(String.format(Locale.getDefault(), "%d : %s", position, question));
            setTitle(tag);
            List<AnswerBean> beans = CacheManager.getInstance().get(Const.GET_ANSWER + questionId);
//            DbCodeManager.getInstance().getSession().getAnswerBeanDao()
            if (beans == null) {
                HttpHelper.obtain().get(Const.GET_ANSWER, new Consumer<Map<String, Object>>() {
                    @Override
                    public void accept(Map<String, Object> params) {
                        params.put("questionId", questionId);
                    }
                }, new ListHttpCallBack<AnswerBean>() {
                    @Override
                    public void onSuccess(@NonNull List<AnswerBean> result) {
                        showResult(result, question);
                        CacheManager.getInstance().put(Const.GET_ANSWER + questionId, result);
                    }

                    @Override
                    public void emptyData() {
                        textAnswer.setText("暂无答案");
                    }

                    @Override
                    public void onFailed(int what, String msg) {
                        super.onFailed(what, msg);
                        emptyData();
                    }
                });
            }


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

    private void showResult(@NonNull List<AnswerBean> result, final String question) {
        StringBuilder content = new StringBuilder();
        for (AnswerBean answerBean : result) {
            content.append(answerBean.getContent());
            if (answerBean.getUrl() != null) {
                link = answerBean.getUrl();
            }
        }
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
