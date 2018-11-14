package cn.wuzuqing.send

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

class MainActivity : AppCompatActivity() {
    companion object {
        private const val ACTION = "cn.wuzuqing.dingding.broadcast.permission.TestReceiver"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.btn).setOnClickListener {
            startActivity(Intent(this@MainActivity, TagActivity::class.java))
//            val builder = NotificationCompat.Builder(this,"1")
//            builder.setContentTitle("标题")
//                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
//                .setContentText("内容")
//                .setNumber(10)
//                .setSmallIcon(R.mipmap.ic_launcher)
//            NotificationManagerCompat.from(applicationContext).notify(1, builder.build())
//            val intent = Intent(ACTION)
//            intent.component = ComponentName("cn.wuzuqing.dingding","cn.wuzuqing.dingding.receiver.TestReceiver")
//            sendBroadcast(intent)
        }
    }
}
