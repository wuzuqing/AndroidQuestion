package cn.wuzuqing.send

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_answer.*

class AnswerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer)

        if (intent != null) {
//            val content = intent.getStringExtra("content")
//            tv_content.text = content
//            tv_link.text = intent.getStringExtra("link")
        }
    }
}
