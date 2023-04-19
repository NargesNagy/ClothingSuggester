package com.example.clothingsuggester.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

class SharedPreferencesUtil(private val context: Context) {

    private val sharedPreference: SharedPreferences = context.getSharedPreferences(
        Constant.SHARED_PREFERINCES_NAME,
        AppCompatActivity.MODE_PRIVATE
    )

     fun saveImageofTheDay(image: Int, date: String) {
        val editor = sharedPreference.edit()
        editor.putInt(Constant.SHARED_CLOTHES_KEY, image)
        editor.putString(Constant.SHARED_DATE_KEY, date)
        editor.apply()
    }

    fun getImageofTheDay(): Pair<Int, String?> {
        val image = sharedPreference.getInt(Constant.SHARED_CLOTHES_KEY, 1)
        val date = sharedPreference.getString(Constant.SHARED_DATE_KEY, "")
        return Pair(image, date)
    }

}