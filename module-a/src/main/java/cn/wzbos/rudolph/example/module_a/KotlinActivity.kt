package cn.wzbos.rudolph.example.module_a

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cn.wzbos.rudolph.Rudolph
import cn.wzbos.rudolph.Consts.RAW_URI
import cn.wzbos.rudolph.annotations.Arg
import cn.wzbos.rudolph.annotations.Route

@Route("/kotlin/test")
class KotlinActivity : AppCompatActivity() {

    @Arg(RAW_URI)
    var routeUri: String? = null

    @Arg("userId")
    var userId: Int = 0

    @Arg("userName")
    var userName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        Rudolph.bind(this)
    }

}
