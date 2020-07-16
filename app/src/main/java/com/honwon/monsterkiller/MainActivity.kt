package com.honwon.monsterkiller

import android.content.Context
import android.content.Intent
import android.graphics.Color.*
import android.graphics.drawable.Drawable
import android.os.*
import android.transition.Transition
import android.view.*
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
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.Appcompat
import java.util.*


class MainActivity : AppCompatActivity() {

    var blood = 3000
    var level = 1
    var mana = 100

    //특수기술
    var fingerCount = 1
    //랜덤기술술
   var randomCount = 1

    var nomalDamage = 1
    var skillDamage = 1
    var moveDamage = 1
    var randomDamage = 1


    //죽었는지 확인
    var killCount = 1
    var imageCode = 1

    var esterEgg =0

    var resetCount = 0

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.setting -> {
            val intent = Intent(this, SettingActivity::class.java)
            intent.putExtra("id", resetCount)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            true
        }


        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        damageInit()

        loadData()

        setSupportActionBar(findViewById(R.id.toolbar))
        // 앱바 이름 아이콘

        val ab = supportActionBar
        loadData()
        bloodB()


        bloodBar.setOnClickListener {
            if(esterEgg==15){
                attack(2147483647)
            }else{
                esterEgg+=1
            }

        }

        fingerBtn.setOnClickListener {
            if(fingerCount==0){
                alert(Appcompat, "다음 레벨에서 사용할 수 있습니다"){positiveButton("확인"){} }.show()
            }else {
                fingerCount -= 1
                countB()
            if (Build.VERSION.SDK_INT >= 23) {
                FingerprintDialog.initialize(this).title("필살기").message("우오오오오 나에게 힘을!").callback(object : FingerprintCallback {
                        // 인증성공인 경우의 컬백 함수
                        override fun onAuthenticationSuccess() {
                            damageInit()
                            attack(moveDamage)
                            Glide.with(applicationContext)
                                .load(R.raw.windexplosion)
                                .into(GlideDrawableImageViewTarget(imageEffect,1))}
                        // 인증실패인 경우 컬백함수
                        override fun onAuthenticationCancel() {Toast.makeText(applicationContext, "참고: 재미로 만든 것이며 개인정보와 아무런 관련이 없습니다", Toast.LENGTH_LONG).show()}}).show()
            } else {Toast.makeText(applicationContext, "이 기능을 사용할 수 있는 버전의 기기가 아닙니다", Toast.LENGTH_LONG).show()}
            }
        }

        //랜덤데미지
        randomBtn.setOnClickListener {
            if(randomCount==0){
                alert(Appcompat, "다음 레벨에서 사용할 수 있습니다"){positiveButton("확인"){} }.show()
            }else {
                window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                randomCount -= 1
                countB()
            damageInit()
                Glide.with(applicationContext)
                    .load(R.raw.skill)
                    .into(GlideDrawableImageViewTarget(imageEffect,1))
                damageInit()
            attack(randomDamage)}
            Handler().postDelayed({
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            },3000)
        }

        //노말데미지
        nomalBtn.setOnClickListener {
            damageInit()

            Glide.with(applicationContext)
                .load(R.raw.attack)
                .into(GlideDrawableImageViewTarget(imageEffect,1))

            attack(nomalDamage)
        }

        //스킬데미지
        skillBtn.setOnClickListener {
            window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            damageInit()
            Glide.with(applicationContext)
                .load(R.raw.explosion)
                .into(GlideDrawableImageViewTarget(imageEffect,1))
            attack(skillDamage)
            Handler().postDelayed({
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            },700)

        }

        //다음스테이지 버튼
        nextStageBtn.setOnClickListener {
            window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            Handler().postDelayed({
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            },5000)
            Glide.with(applicationContext)
                .load(R.raw.epicbeam)
                .into(GlideDrawableImageViewTarget(imageEffect,1))
            bossHeal()
            revibalMessage()


        }


    }






    //때릴때마다
    fun attack(damageInt: Int) {
        bloodBar.progress = bloodBar.progress - damageInt
        if(killCount==1){
            textDamage.setTextColor(RED)
        textDamage.setText("-${damageInt.toString()}")}
        bossKill()
        randomHeal()
        imageChange()
        bloodB()
    }

    // 보스 몬스터가 죽었을 때
    fun bossKill() {
        if (bloodBar.progress == 0 && killCount == 1) {
            imageView.setImageResource(R.drawable.rip)
            textDamage.text=""
            killCount = 0
            imageCode = 0
            dieMessage()
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= 26) {
                // 1초동안 100의 세기(최고 255) 로 1회 진동
                vibrator.vibrate(VibrationEffect.createOneShot(500, 50))
            } else {
                // 1초동안 진동
                vibrator.vibrate(500)
            }
            Handler().postDelayed({
                nextStageBtn.setVisibility(View.VISIBLE)
            }, 5000)

        }
    }

    //보스몬스터 체력에 따라 이미지가 바뀜
    fun imageChange() {
        val nanugi = (bloodBar.progress.toFloat() / bloodBar.max.toFloat() * 100).toInt()
        when (nanugi) {
            in 91..100 -> {imageView.setImageResource(R.drawable.boss_1)
                imageCode = 1}
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
        if (nanugi ==0 && killCount == 1){
            imageView.setImageResource(R.drawable.boss_7)
            imageCode = 7
        }
    }


    fun damageInit(){
        nomalDamage = 1+level
        nomalDamage =  Random().nextInt(nomalDamage)+nomalDamage*5
        skillDamage = Random().nextInt(nomalDamage*5)+nomalDamage*10
        moveDamage = Random().nextInt(nomalDamage*10) +nomalDamage *50
        randomDamage=Random().nextInt(nomalDamage*15) +nomalDamage *75

    }


    fun dieMessage(){
        val number = Random().nextInt(4)
        when(number){
            0 -> longToast("밍구.. 이 벌레녀석..").apply{
                setGravity(Gravity.CENTER_HORIZONTAL,0,0)
                setGravity(Gravity.CENTER_VERTICAL,0,300)}
            1 -> longToast("윽.. 다시 돌아오겠다..").apply{
                setGravity(Gravity.CENTER_HORIZONTAL,0,0)
                setGravity(Gravity.CENTER_VERTICAL,0,300)}
                2 -> longToast("복수하겠다...").apply{
                setGravity(Gravity.CENTER_HORIZONTAL,0,0)
                setGravity(Gravity.CENTER_VERTICAL,0,300)}
            3 -> longToast("내 망령이 너를 쫒아다닐 것이다").apply{
                setGravity(Gravity.CENTER_HORIZONTAL,0,0)
                setGravity(Gravity.CENTER_VERTICAL,0,300)  }
            }


    }
    fun revibalMessage(){
        val number = Random().nextInt(4)

            when(number){
                0 -> longToast("크하하하하하하핫 너는 날 죽일 수 없다!").apply{
                    setGravity(Gravity.CENTER_HORIZONTAL,0,0)
                    setGravity(Gravity.CENTER_VERTICAL,0,300)            }
                1 -> longToast("포끝갈사람!?").apply{
                    setGravity(Gravity.CENTER_HORIZONTAL,0,0)
                    setGravity(Gravity.CENTER_VERTICAL,0,300)  }
                2 -> longToast("으오오! 등.장!").apply{
                    setGravity(Gravity.CENTER_HORIZONTAL,0,0)
                    setGravity(Gravity.CENTER_VERTICAL,0,300)  }
                3 -> longToast("과연 나를 죽일 수 있을까?").apply{
                    setGravity(Gravity.CENTER_HORIZONTAL,0,0)
                    setGravity(Gravity.CENTER_VERTICAL,0,300)  }
    }}

    // 보스 회복
    fun bossHeal() {
        esterEgg=0
        level += 1
        blood += 100*level*level
        bloodBar.max = blood
        bloodBar.progress = blood
        killCount = 1
        randomCount = 1 + (level / 5.0).toInt()
        fingerCount = 1 + (level / 3.0).toInt()
        bloodB()
        countB()
        imageView.setImageResource(R.drawable.boss_1)
        imageCode=1
        nextStageBtn.visibility = View.INVISIBLE
    }

    //보스 랜덤으로 피 회복
    fun randomHeal() {
        if(killCount==1){
        val nanugi = (bloodBar.progress.toFloat() / bloodBar.max.toFloat() * 100).toInt()
        //30분의 1 확률
        if (Random().nextInt(30) == 7) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            textDamage.setTextColor(GREEN)
            Glide.with(this)
                .load(R.raw.heal)
                .into(GlideDrawableImageViewTarget(imageEffect, 1))
            val beforeProgress = bloodBar.progress
            when (nanugi) {
                in 71..90 -> bloodBar.progress += (Random().nextInt(nomalDamage*5)+nomalDamage*1)
                in 51..70 -> bloodBar.progress += (Random().nextInt(nomalDamage*10)+nomalDamage*2)
                in 36..50 -> bloodBar.progress += (Random().nextInt(nomalDamage*15)+nomalDamage*3)
                in 21..35 -> bloodBar.progress += (Random().nextInt(nomalDamage*20)+nomalDamage*4)
                in 11..20 -> bloodBar.progress += (Random().nextInt(nomalDamage*25)+nomalDamage*5)
                in 1..10 -> bloodBar.progress += (Random().nextInt(nomalDamage*30)+nomalDamage*6)
            }

            val afterProgress = bloodBar.progress
            textDamage.setText("+${afterProgress-beforeProgress}")
            Handler().postDelayed({
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            },750)
            }}

        }



    //체력과 레벨 표시
    fun bloodB() {
        textBlood.text = "${bloodBar.progress} / ${bloodBar.max}"
        textLevel.text = "LEVEL: ${level}"
    }

    fun countB(){
        fingerText.text = "남은 횟수: ${fingerCount}"
        magicText.text = "남은 횟수: ${randomCount}"
    }



    open fun loadData() {
        val pref = this.getPreferences(0)
        val a = pref.getInt("_ONE",1)
        val b = pref.getInt("_TWO",1000)
        val k = pref.getInt("_TWOO",1000)
        val c = pref.getInt("_THREE",1)
        val d = pref.getInt("_FOUR",1)
        val e = pref.getInt("_FIVE",1)
        //  n = pref.getInt("_PEOPLE",1)
        val i = pref.getInt("_IMAGECODE",1)


        level = a
        bloodBar.max  = k
        bloodBar.progress  = b

        randomCount = c
        fingerCount = d
        killCount = e
        if(killCount==0){
            nextStageBtn.visibility= View.VISIBLE
        }
        countB()
        imageCode=i
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


    open fun saveData(level: Int ,blood:Int,bloodMax:Int,random:Int,spacial:Int,kill:Int, nomaldamage:Int,image :Int) {
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

    fun init(){

        blood = 1000
        level = 1
        mana = 100
        fingerCount = 1
        randomCount = 1
        nomalDamage = 1
        skillDamage = nomalDamage * 5
        moveDamage = nomalDamage * 10
        killCount = 1
        imageCode = 1
        bloodBar.max = blood
        bloodBar.progress = blood
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        Handler().postDelayed({
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        },1200)
        Glide.with(applicationContext)
            .load(R.raw.epicbeam)
            .into(GlideDrawableImageViewTarget(imageEffect,1))
        bloodB()
        countB()
        imageChange()

    }

    override fun onStop() {
        saveData(level, bloodBar.progress,bloodBar.max,randomCount,fingerCount,killCount,nomalDamage,imageCode)
        super.onStop()
    }

    override fun onResume() {

        resetCount = intent.getIntExtra("idd", 0)

        if (resetCount == 1) {
            init()
        }
        super.onResume()
    }

    override fun onRestart() {

        loadData()


        super.onRestart()
    }

}

