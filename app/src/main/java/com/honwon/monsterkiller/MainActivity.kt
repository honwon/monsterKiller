package com.honwon.monsterkiller

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import me.aflak.libraries.FingerprintCallback
import me.aflak.libraries.FingerprintDialog
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    var blood = 10000
    var level: Double = 1.0
    var mana = 100
    var specialCount = 1
    var randomCount = 1

    var nomalDamage = 20
    var skillDamage = nomalDamage*5
    var moveDamage = nomalDamage*100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)





        fingerDamage.setOnClickListener {

            if (Build.VERSION.SDK_INT >= 23) {
                // FingerPrintDiqasdf alog 로 지문인증을 쉽게 사용가능
                // 사용법은 AlertDialog 의 빌더패턴과 유사
                FingerprintDialog.initialize(this)
                    .title("필살기")
                    .message("지문으로 인증하면?")
                    .callback(object : FingerprintCallback {
                        // 인증성공인 경우의 컬백 함수
                        override fun onAuthenticationSuccess() {
                            Toast.makeText(applicationContext, "인증", Toast.LENGTH_SHORT).show()
                        }

                        // 인증실패인 경우 컬백함수
                        override fun onAuthenticationCancel() {
                            Toast.makeText(applicationContext, "아직 자격이 되지 않았다", Toast.LENGTH_SHORT)
                                .show()
                        }
                    })
                    .show()
            } else {
                Toast.makeText(applicationContext, "이 기능을 사용할 수 있는 버전의 기기가 아닙니다", Toast.LENGTH_LONG)
                    .show()
            }
        }











        nomal.setOnClickListener {
        }


        skill.setOnClickListener {}


    }



    fun attack(damageInt: Int) {
        bloodBar.progress = bloodBar.progress - damageInt
        damage.setText(damageInt.toString())
    }

}

