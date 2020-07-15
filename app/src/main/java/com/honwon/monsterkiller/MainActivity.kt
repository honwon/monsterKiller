package com.honwon.monsterkiller

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.transition.Transition
import android.view.View
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget
import kotlinx.android.synthetic.main.activity_main.*
import me.aflak.libraries.FingerprintCallback
import me.aflak.libraries.FingerprintDialog
import java.util.*


class MainActivity : AppCompatActivity() {

    var blood = 1000
    var level = 1
    var mana = 100

    //특수기술
    var specialCount = 1
    //랜덤기술술
   var randomCount = 1
    var nomalDamage = 1
    var skillDamage = nomalDamage * 5
    var moveDamage = nomalDamage * 10

    //죽었는지 확인
    var killCount = 1
    var imageCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadData()
        bloodB()



        button2.setOnClickListener {
            attack(1000000090)
        }

        button.setOnClickListener {
            Glide.with(this)
                .load(R.raw.heal2)
                .into(GlideDrawableImageViewTarget(imageEffect,1))
        }



        fingerBtn.setOnClickListener {

            if (Build.VERSION.SDK_INT >= 23) {
                FingerprintDialog.initialize(this).title("필살기").message("우오오오오 나에게 힘을!").callback(object : FingerprintCallback {
                        // 인증성공인 경우의 컬백 함수
                        override fun onAuthenticationSuccess() {Toast.makeText(applicationContext, "지문 공격!", Toast.LENGTH_SHORT).show()
                            damageInit()
                            attack(moveDamage)}
                        // 인증실패인 경우 컬백함수
                        override fun onAuthenticationCancel() {Toast.makeText(applicationContext, "참고: 재미로 만든 것이며 개인정보와 아무런 관련이 없습니다", Toast.LENGTH_LONG).show()}}).show()
            } else {Toast.makeText(applicationContext, "이 기능을 사용할 수 있는 버전의 기기가 아닙니다", Toast.LENGTH_LONG).show()}}

        //랜덤데미지
        randomBtn.setOnClickListener {
            damageInit()
            var randomDamage = Random().nextInt(skillDamage * 20 + 1)
            attack(randomDamage)
        }

        //노말데미지
        nomalBtn.setOnClickListener {
            damageInit()
            attack(nomalDamage)
        }

        //스킬데미지
        skillBtn.setOnClickListener {
            damageInit()
            attack(skillDamage)
        }

        //다음스테이지 버튼
        nextStageBtn.setOnClickListener {
            bossHeal()
        }

    }

    //때릴때마다
    fun attack(damageInt: Int) {
        bloodBar.progress = bloodBar.progress - damageInt
        if(killCount==1){
        textDamage.setText("-${damageInt.toString()}")}
        bossKill()
        randomHeal()
        imageChange()
        bloodB()


    }

    // 보스 몬스터가 죽었을 때
    fun bossKill() {
        if (bloodBar.progress == 0 && killCount == 1) {
            Toast.makeText(applicationContext, "퇴치 성공!", Toast.LENGTH_SHORT).show()
            textDamage.text=""
            killCount = 0
            nextStageBtn.setVisibility(View.VISIBLE)
        }
    }

    //보스몬스터 체력에 따라 이미지가 바뀜
    fun imageChange() {
        val nanugi = (bloodBar.progress.toFloat() / bloodBar.max.toFloat() * 100).toInt()
        when (nanugi) {
            in 71..90 -> {imageView.setImageResource(R.drawable.boss_2)
                imageCode = 2}
            in 51..70 -> {imageView.setImageResource(R.drawable.boss_3)
                imageCode = 3}
            in 36..50 -> {imageView.setImageResource(R.drawable.boss_4)
                imageCode = 4}
            in 21..35 -> {imageView.setImageResource(R.drawable.boss_5)
                imageCode = 5}
            in 11..20 -> {imageView.setImageResource(R.drawable.boss_6)
                imageCode = 6}
            in 1..10 -> {imageView.setImageResource(R.drawable.boss_7)
                imageCode = 7}}
        if (bloodBar.progress == 0) {
            imageView.setImageResource(R.drawable.rip)
            imageCode = 0

        }
    }

    fun damageInit(){
        nomalDamage = 1
        nomalDamage =  Random().nextInt(nomalDamage+level)+nomalDamage*level
        skillDamage = Random().nextInt(nomalDamage*5)+nomalDamage*5
        moveDamage = Random().nextInt(nomalDamage*level*10) +nomalDamage * 100
    }


    fun dieMessage(){
        val number = Random().nextInt(1)
        when(number){
        }


    }

    fun revivalMessage(){
        val number = Random().nextInt()


    }


    // 보스 회복
    fun bossHeal() {
        level += 1
        blood += 100 * level * level
        bloodBar.max = blood
        bloodBar.progress = blood
        killCount = 1
        bloodB()

        imageView.setImageResource(R.drawable.boss_1)
        imageCode=1
        nextStageBtn.visibility = View.INVISIBLE
    }

    //보스 랜덤으로 피 회복
    fun randomHeal(){
        val nanugi = (bloodBar.progress.toFloat() / bloodBar.max.toFloat() * 100).toInt()
        //30분의 1 확률
        if(Random().nextInt(50) == 7){
            Glide.with(this)
                .load(R.raw.heal)
                .into(GlideDrawableImageViewTarget(imageEffect,1))
            when (nanugi) {
                in 71..90 -> bloodBar.progress += nomalDamage*1
                in 51..70 -> bloodBar.progress += nomalDamage*2
                in 36..50 -> bloodBar.progress += nomalDamage*4
                in 21..35 -> bloodBar.progress += nomalDamage*8
                in 11..20 -> bloodBar.progress += nomalDamage*16
                in 1..10 -> bloodBar.progress += nomalDamage*32
            }
        }
    }

    //체력과 레벨 표시
    fun bloodB() {
        textBlood.text = "${bloodBar.progress} / ${bloodBar.max}"
        textLevel.text = "LEVEL: ${level}"
    }



    fun loadData() {
        val pref = this.getPreferences(0)
        var a = pref.getInt("_ONE",1)
        var b = pref.getInt("_TWO",1000)
        var k = pref.getInt("_TWOO",1000)
        var c = pref.getInt("_THREE",1)
        var d = pref.getInt("_FOUR",1)
        var e = pref.getInt("_FIVE",1)
        //var n = pref.getInt("_PEOPLE",1)
        var i = pref.getInt("_IMAGECODE",1)


        level = a
        bloodBar.progress  = b
        bloodBar.max  = k
        randomCount = c
        specialCount = d
        killCount = e
        if(killCount==0){
            nextStageBtn.visibility= View.VISIBLE
        }
        //nomalDamage = n

        when(i){
            1 -> imageView.setImageResource(R.drawable.boss_1)
            2 -> imageView.setImageResource(R.drawable.boss_2)
            3 -> imageView.setImageResource(R.drawable.boss_3)
            4 -> imageView.setImageResource(R.drawable.boss_4)
            5 -> imageView.setImageResource(R.drawable.boss_5)
            6 -> imageView.setImageResource(R.drawable.boss_6)
            7 -> imageView.setImageResource(R.drawable.boss_7)
            0 -> imageView.setImageResource(R.drawable.rip)
        }
        bloodB()
    }


    fun saveData(level: Int ,blood:Int,bloodMax:Int,random:Int,spacial:Int,kill:Int, nomaldamage:Int,image :Int) {
        val pref = this.getPreferences(0)
        val editor = pref.edit()
        editor.putInt("_ONE",level)
        editor.putInt("_TWO",blood)
        editor.putInt("_TWOO",bloodMax)
        editor.putInt("_THREE",random)
        editor.putInt("_FOUR",spacial)
        editor.putInt("_FIVE",kill)
        //editor.putInt("_PEOPLE",nomaldamage)
        editor.putInt("_IMAGECODE",image)
        editor.apply()
    }

    override fun onPause() {
        saveData(level, bloodBar.progress,bloodBar.max,randomCount,specialCount,killCount,nomalDamage,imageCode)
        super.onPause()
    }

    override fun onResume() {
        loadData()
        super.onResume()
    }

}

