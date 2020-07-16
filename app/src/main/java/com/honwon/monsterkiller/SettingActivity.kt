package com.honwon.monsterkiller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.setting.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.browse
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

class SettingActivity : AppCompatActivity() {

    // 뒷 앱바 네비게이션 화살표 눌렀을 때 작동시킬 것
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting)

        var resetCount = intent.getIntExtra("id",0)
        // 아이콘 눌렀을 때 실행할 것
        toolbar2.setNavigationOnClickListener {
            onBackPressed()
        }

        inButton.setOnClickListener {

            alert("게임의 모든 데이터가 초기화됩니다\n정말 초기화하시겠습니까?"){
                positiveButton("예"){
                    alert("정말로 초기화하시겠습니까?"){
                        positiveButton("예"){
                            resetCount= 1

                            val intent = Intent(applicationContext, MainActivity::class.java)
                            intent.putExtra("idd", resetCount)
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)

                            longToast("초기화되었습니다")
                        }
                        negativeButton("아니오"){
                        }
                    }.show()
                }
                negativeButton("아니오"){
                }
            }.show()
        }



        googleButton.setOnClickListener {
            alert("칭찬해줘요 뿌잉뿌잉 ლ( > ◡ < ლ)"){
                positiveButton("그래"){
                    browse(url = "https://play.google.com/store/apps/details?id=com.honwon.monsterkiller")
                }
                negativeButton("장난하나.."){
                    longToast("시무룩....")
                }
            }.show()
        }





    }
}