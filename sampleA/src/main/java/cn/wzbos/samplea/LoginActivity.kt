package cn.wzbos.samplea

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import cn.wzbos.android.rudolph.Rudolph
import cn.wzbos.android.rudolph.annotations.Extra
import cn.wzbos.android.rudolph.annotations.Route
import kotlinx.android.synthetic.main.activity_login.*

@Route("/kotlin/login")
class LoginActivity : AppCompatActivity() {

    @Extra
    var to: String? = null

    @Extra
    var toExtras: Bundle? = null

    @Extra
    var toEn: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Rudolph.bind(this)

        btnLogin.setOnClickListener {
            AppUserRouter.get().isLogin = true
            Toast.makeText(this, "login success!", Toast.LENGTH_SHORT).show()
            to?.also {
                Rudolph.builder(to).putExtra(toExtras).build().open(this)
            }
            finish()
        }
    }

}
