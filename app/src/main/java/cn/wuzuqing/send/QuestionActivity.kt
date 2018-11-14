package cn.wuzuqing.send

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import cn.wuzuqing.send.adapter.QuestionAdapter
import cn.wuzuqing.send.bean.QuestionBean
import cn.wuzuqing.send.constant.Const
import cn.wuzuqing.send.dao.DbCodeManager
import cn.wuzuqing.send.http.HttpHelper
import cn.wuzuqing.send.http.ListHttpCallBack
import kotlinx.android.synthetic.main.activity_question.*

class QuestionActivity : BaseActivity() {
    override fun initEvent() {
        mAdapter.setOnItemClickListener { _, view, position ->
            toAnswer(mAdapter.getItem(position)!!, position, view.findViewById(R.id.layoutAsk))
        }
        srf.setOnRefreshListener {
            lastId = 0
            queryData()
        }
        mAdapter.setOnLoadMoreListener({
            queryData()
        }, recyclerView)
    }

    override fun initData() {
        if (intent != null) {
            tag = intent.getStringExtra("tag")
            setTitle(tag)
        }
        queryData()
    }

    override fun initView() {
        mAdapter = QuestionAdapter()
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.adapter = mAdapter

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_question
    }

    var tag = "activity"
    lateinit var mAdapter: QuestionAdapter

    private fun toAnswer(item: QuestionBean, position: Int, view: View) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("tag", tag)
        intent.putExtra("question", item.question)
        intent.putExtra("questionId", item.id)
        intent.putExtra("position", position + 1)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val options = ActivityOptions.makeSceneTransitionAnimation(this, view, "title")
            startActivity(intent, options.toBundle())
        } else {
            startActivity(intent)
        }
    }

    private var lastId = 0L
    private val pageSize = 10
    /**
     * 查询数据
     */
    private fun queryData() {
        if (lastId == -1L) {
            return
        }
        val list = DbCodeManager.getInstance().session.questionBeanDao.loadPage("id >= $lastId and questionTag = '$tag'", pageSize)
        if (list != null && list.isNotEmpty()) {
            showList(list)
            return
        }
        HttpHelper.obtain().get(Const.GET_QUESTION_LIST, {
            it["tag"] = tag
            it["lastId"] = lastId
        }, object : ListHttpCallBack<QuestionBean>() {
            override fun onSuccess(result: List<QuestionBean>) {
                showList(result)
                DbCodeManager.getInstance().session.questionBeanDao.insertIx(result)
            }

            override fun onFinish() {
                super.onFinish()
                if (srf.isRefreshing) {
                    srf.isRefreshing = false
                } else {
                    mAdapter.loadMoreComplete()
                }
            }

            override fun emptyData() {
                if (!srf.isRefreshing) {
                    mAdapter.loadMoreEnd()
                }
            }
        })

    }

    private fun showList(result: List<QuestionBean>) {
        if (lastId == 0L) {
            mAdapter.setNewData(result)
        } else {
            mAdapter.addData(result)
        }
        lastId = result[result.size - 1].id
    }
}
