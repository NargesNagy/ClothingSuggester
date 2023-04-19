package com.example.clothingsuggester.data

import com.example.clothingsuggester.R

class ClothesImages(val weather: Int) {

    fun getListOfClothesAccordingToTempreture(): List<Int>{
        val list = mutableListOf<Int>()
        when(weather){
            in MIN_COLD_TEMP..MAX_COLD_TEMP -> {
                list.add(R.drawable.coat1)
                list.add(R.drawable.c2)
                list.add(R.drawable.c3)
                list.add(R.drawable.coat5)
                list.add(R.drawable.coat4)
                list.add(R.drawable.coat6)
            }

            in MAX_COLD_TEMP..MEDIUM_TEMP -> {
                list.add(R.drawable.swee0)
                list.add(R.drawable.sweet1)
                list.add(R.drawable.sweet2)
                list.add(R.drawable.sweet5)
                list.add(R.drawable.sweet7)
                list.add(R.drawable.sweet8)
                list.add(R.drawable.sweet9)
            }

             in MEDIUM_TEMP..HIGH_TEMP -> {
                 list.add(R.drawable.dress)
                 list.add(R.drawable.dress2)
                 list.add(R.drawable.dress4)
                 list.add(R.drawable.dress5)
                 list.add(R.drawable.dress6)
                 list.add(R.drawable.dress7)
             }

        }

        return list
    }

    companion object{
        const val MIN_COLD_TEMP = 0
        const val MAX_COLD_TEMP = 15
        const val MEDIUM_TEMP = 25
        const val HIGH_TEMP = 40



    }



}