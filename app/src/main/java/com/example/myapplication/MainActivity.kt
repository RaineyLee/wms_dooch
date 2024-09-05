package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {

    private lateinit var mainbinding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // inflate(layoutInflater)로 초기화 한다
        mainbinding = ActivityMainBinding.inflate(layoutInflater)
        // binding.root 뷰를 화면에 표시하도록 설정
        setContentView(mainbinding.root)

        mainbinding.btnExWare.setOnClickListener {
            val intent = Intent(this, W_ItemLoc::class.java)
            startActivity(intent)
        }
    }

}