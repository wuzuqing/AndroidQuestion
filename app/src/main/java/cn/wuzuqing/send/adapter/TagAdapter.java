package cn.wuzuqing.send.adapter;

import cn.wuzuqing.send.R;
import cn.wuzuqing.send.bean.QuestionTagBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * @author: 吴祖清
 * @createDate: 2018/11/7 16:22
 * @description: java类作用描述
 * @updateUser: 吴祖清
 * @updateDate: 2018/11/7 16:22
 * @updateRemark: 更新说明
 * @version: 1.0
 */
public class TagAdapter extends BaseQuickAdapter<QuestionTagBean, BaseViewHolder> {
    public TagAdapter() {
        super(R.layout.item_question);
    }

    @Override
    protected void convert(BaseViewHolder helper, QuestionTagBean item) {
        helper.setText(R.id.tv_tag, item.getTag());
    }
}
