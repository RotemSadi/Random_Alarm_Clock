package com.example.randomalarmclock.animalsDatabase

import com.example.randomalarmclock.R

object AnimalList {

    val animalList = ArrayList<AnimalInfo>().apply {
        add(AnimalInfo(0, R.drawable.baboon, "Baboon", R.raw.baboon1))
        add(AnimalInfo(1, R.drawable.cow, "Cow", R.raw.cow))
        add(AnimalInfo(2, R.drawable.donkey, "Donkey", R.raw.donkey))
        add(AnimalInfo(3, R.drawable.elephants, "Elephant", R.raw.elephant9))
        add(AnimalInfo(4, R.drawable.elk, "Elk", R.raw.elk))
        add(AnimalInfo(5, R.drawable.eurasian_wolf, "Eurasian Wolf", R.raw.wolf3))
        add(AnimalInfo(6, R.drawable.goose, "Goose", R.raw.goose))
        add(AnimalInfo(7, R.drawable.lion, "Lion", R.raw.lion))
        add(AnimalInfo(8, R.drawable.magpie, "Magpie", R.raw.magpie))
        add(AnimalInfo(9, R.drawable.peacock, "Peacock", R.raw.peacock))
        add(AnimalInfo(10, R.drawable.sheep, "Sheep", R.raw.sheep))
        add(AnimalInfo(11, R.drawable.rooster, "Rooster", R.raw.rooster))
    }
}