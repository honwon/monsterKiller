package com.honwon.monsterkiller

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*
import me.aflak.libraries.FingerprintCallback
import me.aflak.libraries.FingerprintDialog
import java.util.*


class MainActivity : AppCompatActivity() {

    var blood = 1000
    var level = 1
    var mana = 100
    var specialCount = 1
    var randomCount = 1
    var nomalDamage = 1
    var skillDamage = nomalDamage * 5
    var moveDamage = nomalDamage * 10

    var killCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bloodB()

        fingerBtn.setOnClickListener {

            if (Build.VERSION.SDK_INT >= 23) {
                // FingerPrintDiqasdf alog 로 지문인증을 쉽게 사용가능
                // 사용법은 AlertDialog 의 빌더패턴과 유사
                FingerprintDialog.initialize(this)
                    .title("필살기")
                    .message("우오오오오 나에게 힘을!")
                    .callback(object : FingerprintCallback {
                        // 인증성공인 경우의 컬백 함수
                        override fun onAuthenticationSuccess() {
                            Toast.makeText(applicationContext, "지문 공격!", Toast.LENGTH_SHORT).show()
                            attack(moveDamage)

                        }

                        // 인증실패인 경우 컬백함수
                        override fun onAuthenticationCancel() {
                            Toast.makeText(applicationContext, "참고: 재미로 만든 것이며 개인정보와 아무런 관련이 없습니다", Toast.LENGTH_LONG)
                                .show()
                        }
                    })
                    .show()
            } else {
                Toast.makeText(applicationContext, "이 기능을 사용할 수 있는 버전의 기기가 아닙니다", Toast.LENGTH_LONG)
                    .show()
            }
        }

        randomBtn.setOnClickListener {
            var randomDamage = Random().nextInt(nomalDamage * 20 + 1)
            attack(randomDamage)

        }


        nomalBtn.setOnClickListener {
            attack(nomalDamage)

        }


        skillBtn.setOnClickListener {
            attack(skillDamage)

        }

        nextStageBtn.setOnClickListener {
            bossHeal()
        }


    }


    fun attack(damageInt: Int) {
        bloodBar.progress = bloodBar.progress - damageInt
        textDamage.setText(damageInt.toString())
        bossKill()
        randomHeal()
        imageChange()
        bloodB()


    }

    fun bossKill() {
        if (bloodBar.progress == 0 && killCount == 1) {
            Toast.makeText(applicationContext, "퇴치 성공!", Toast.LENGTH_SHORT).show()
            killCount = 0
            nextStageBtn.setVisibility(View.VISIBLE)
        }
    }

    fun imageChange() {
        val nanugi = (bloodBar.progress.toFloat() / bloodBar.max.toFloat() * 100).toInt()
        when (nanugi) {
            in 71..90 -> imageView.setImageResource(R.drawable.boss_2)
            in 51..70 -> imageView.setImageResource(R.drawable.boss_3)
            in 36..50 -> imageView.setImageResource(R.drawable.boss_4)
            in 21..35 -> imageView.setImageResource(R.drawable.boss_5)
            in 11..20 -> imageView.setImageResource(R.drawable.boss_6)
            in 1..10 -> imageView.setImageResource(R.drawable.boss_7)
        }
        if(bloodBar.progress==0){
            imageView.setImageResource(R.drawable.rip)
        }
    }

    fun bossHeal() {
        level += 1
        blood = blood + 100 * level * level
        nomalDamage = nomalDamage * level
        bloodBar.max = blood
        bloodBar.progress = blood
        killCount = 1
        bloodB()

        imageView.setImageResource(R.drawable.boss_1)
        nextStageBtn.setVisibility(View.INVISIBLE)
    }

    fun randomHeal(){
        val nanugi = (bloodBar.progress.toFloat() / bloodBar.max.toFloat() * 100).toInt()

        if(Random().nextInt(30) == 7){
            when (nanugi) {
                in 71..90 -> bloodBar.progress += nomalDamage*2
                in 51..70 -> bloodBar.progress += nomalDamage*4
                in 36..50 -> bloodBar.progress += nomalDamage*6
                in 21..35 -> bloodBar.progress += nomalDamage*9
                in 11..20 -> bloodBar.progress += nomalDamage*12
                in 1..10 -> bloodBar.progress += nomalDamage*20
            }
        }
    }

    fun bloodB() {
        textBlood.text = "${bloodBar.progress} / ${bloodBar.max}"
    }

}

