package com.vhenri.cachew.ui

import androidx.lifecycle.ViewModel
import com.vhenri.cachew.core.lifecycle.SingleLiveEvent
import com.vhenri.cachew.data.LightbulbBucketParameter
import com.vhenri.cachew.data.LightbulbBucketParameter.TOTAL_AVAILABLE_BULBS
import com.vhenri.cachew.data.LightbulbBucketParameter.NUM_BULB_COLORS
import com.vhenri.cachew.data.LightbulbBucketParameter.NUM_EACH_COLOR
import com.vhenri.cachew.data.LightbulbItem
import org.koin.core.component.KoinComponent
import java.lang.Exception
import java.math.RoundingMode
import kotlin.random.Random

class MainViewModel() : ViewModel(), KoinComponent {
    var totalAvailableBulbs = SingleLiveEvent<Int>().apply { value = 0 } // i
    var numBulbColors = SingleLiveEvent<Int>().apply { value = 0 } // j
    var numEachColor = SingleLiveEvent<Int>().apply { value = 0 } // k
    var numBulbsPicked = SingleLiveEvent<Int>().apply { value = 0 } // m
    private var numSims = SingleLiveEvent<Int>().apply { value = 1 } // n
    var totalUniqueColors = SingleLiveEvent<Int>()
    var averageUniqueColors = SingleLiveEvent<Float>()


    fun onTotalAvailableChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
        val value = s.toString()
        totalAvailableBulbs.postValue(intFromValue(value))
    }
    fun onNumBulbColorsChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
        val value = s.toString()
        numBulbColors.postValue(intFromValue(value))
    }
    fun onNumEachColorChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
        val value = s.toString()
        numEachColor.postValue(intFromValue(value))
    }
    fun onNumBulbsPickedChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
        val value = s.toString()
        numBulbsPicked.postValue(intFromValue(value))
    }
    fun onNumSimChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
        val value = s.toString()
        numSims.postValue(intFromValue(value))
    }

    private fun intFromValue(stringValue: String): Int {
        return try {
            stringValue.toInt()
        } catch (e: Exception){
            0
        }
    }

    fun runSimulation(){
        val numRuns = numSims.value ?: 1
        var sumRuns = 0
        for (run in 0 until numRuns){
            val numberOfUniqueColors = getNumberOfUniqueColors()
            if (run == 0){
                totalUniqueColors.postValue(numberOfUniqueColors)
            }
            sumRuns += numberOfUniqueColors
        }
        if (numRuns != 0){
            averageUniqueColors.postValue(sumRuns.toFloat()/numRuns.toFloat())
        } else {
            averageUniqueColors.postValue(0.toFloat())
        }
    }
    private fun getNumberOfUniqueColors(): Int {
        val bucket = arrayListOf<LightbulbItem>()
        val numColors = numBulbColors.value ?: 0
        val numOfEachColor = numEachColor.value ?: 0
        var bulbsToPick = numBulbsPicked.value?: 0

        // Add the bulbs to the bucket
        for (j in 0 until numColors){
            for (k in 0 until numOfEachColor){
                bucket.add(LightbulbItem(j))
            }
        }
        val randomSeed = Random.nextInt()
        // Using a seeded random generator to make testing/debugging more repeatable.
        val randomGenerator = Random(randomSeed)
        for (i in 0 until bulbsToPick){
            val randomIndex = randomGenerator.nextInt(0, bucket.size)
            val pickedBulb = bucket[randomIndex]
            if (!pickedBulb.picked){
                pickedBulb.picked = true
            } else {
                // if we've already picked the bulb, add another iteration
                bulbsToPick ++
            }
        }
        val distinctArray = bucket.filter{bulb -> bulb.picked }.distinct()

        return distinctArray.size
    }
}