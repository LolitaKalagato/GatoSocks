package net.typeblog.socks

import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import net.typeblog.socks.util.Constants
import net.typeblog.socks.util.KalaSocks

class MainActivity : AppCompatActivity() {
    lateinit var kalaSocks : KalaSocks
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        kalaSocks = KalaSocks.getInstance()
        kalaSocks.set("18.223.222.37", 443, this)
        (findViewById<Button>(R.id.button)).setOnClickListener {
            startVpn();
        }
        (findViewById<Button>(R.id.button2)).setOnClickListener {
            kalaSocks.stopVpn(this);
        }
    }

    private fun startVpn() {

        val i = VpnService.prepare(this)
        if (i != null) {
            startActivityForResult(i, 0)
        } else {
            onActivityResult(0, RESULT_OK, null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            kalaSocks.startVpn(this);
        }
    }
}