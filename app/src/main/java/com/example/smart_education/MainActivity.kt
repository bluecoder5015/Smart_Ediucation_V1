package com.example.smart_education

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var docpreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
        val email = docpreferences.getString("EMAIL","")

        val homeFragment = TodoFragment()
        val chatFragment = MessageFragment()
        val profileFragment = ProfileFragment()
        val subjectFragment = SubjectFragment()

        setCurrentfragment(homeFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.todos_item -> setCurrentfragment(homeFragment)
                R.id.chat_item -> setCurrentfragment(chatFragment)
                R.id.subject_item -> setCurrentfragment(subjectFragment)
                R.id.profile_item -> setCurrentfragment(profileFragment)
            }
            true
        }
        bottomNavigationView.getOrCreateBadge(R.id.chat_item).apply {
            isVisible =true
        }


    }

    private fun setCurrentfragment(fragment :Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.framelayout,fragment)
            commit()
        }
    }
}