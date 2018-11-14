package cn.wuzuqing.send

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import cn.wuzuqing.send.adapter.TagAdapter
import cn.wuzuqing.send.bean.QuestionTagBean
import cn.wuzuqing.send.constant.Const
import cn.wuzuqing.send.http.HttpHelper
import cn.wuzuqing.send.http.ListHttpCallBack
import kotlinx.android.synthetic.main.activity_tag.*

class TagActivity : BaseActivity() {
    lateinit var mAdapter: TagAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_tag
    }

    override fun initEvent() {
        mAdapter.setOnItemClickListener { _, _, position ->
            toQuestionList(mAdapter.getItem(position)!!)
        }
    }

    override fun initData() {
        loadData()
    }

    override fun initView() {
        setTitle("android面试", false)
        mAdapter = TagAdapter()
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)!!
        recyclerView.adapter = mAdapter
    }


    private fun toQuestionList(tag: QuestionTagBean) {
        val intent = Intent(this, QuestionActivity::class.java)
        intent.putExtra("tag", tag.tag)
        intent.putExtra("tagId", tag.id)
        startActivity(intent)
    }

    private fun loadData() {
        var value: List<QuestionTagBean>? = CacheManager.getInstance().get(Const.GET_ALL_TAG)
        if (value != null) {
            mAdapter.setNewData(value)
            return
        }
        HttpHelper.obtain().get(Const.GET_ALL_TAG, null, object : ListHttpCallBack<QuestionTagBean>() {
            override fun onSuccess(result: List<QuestionTagBean>) {
                mAdapter.setNewData(result)
                CacheManager.getInstance().put(Const.GET_ALL_TAG, result)
            }
        })
    }
}
