package cn.wuzuqing.send;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;

/**
 * @author: 吴祖清
 * @createDate: 2018/11/7 17:12
 * @description: java类作用描述
 * @updateUser: 吴祖清
 * @updateDate: 2018/11/7 17:12
 * @updateRemark: 更新说明
 * @version: 1.0
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        initEvent();
        initData();
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initEvent();

    protected abstract void initData();


    protected void setTitle(String title) {
        setTitle(title, true);
    }

    protected void setTitle(String title, boolean showBack) {
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            if (TextUtils.isEmpty(title)) {

            } else {
                actionBar.setTitle(title);

                actionBar.setDisplayHomeAsUpEnabled(showBack);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //返回
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition();
            } else {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
