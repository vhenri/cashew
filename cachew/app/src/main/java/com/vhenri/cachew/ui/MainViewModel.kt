package com.vhenri.cachew.ui

import androidx.lifecycle.ViewModel
import com.vhenri.cachew.core.lifecycle.SingleLiveEvent
import com.vhenri.cachew.data.LightbulbBucketParameter
import com.vhenri.cachew.data.LightbulbBucketParameter.TOTAL_AVAILABLE_BULBS
import com.vhenri.cachew.data.LightbulbBucketParameter.NUM_BULB_COLORS
import com.vhenri.cachew.data.LightbulbBucketParameter.NUM_EACH_COLOR
import com.vhenri.cachew.data.LightbulbItem
import org.koin.core.component.KoinComponent

class MainViewModel() : ViewModel(), KoinComponent {
    var totalAvailableBulbs = SingleLiveEvent<Int>().apply { value = 0 } // i
    var numBulbColors = SingleLiveEvent<Int>().apply { value = 0 } // j
    var numEachColor = SingleLiveEvent<Int>().apply { value = 0 } // k
    var numBulbsPicked = SingleLiveEvent<Int>().apply { value = 0 } // m
    var numSims = SingleLiveEvent<Int>().apply { value = 1 } // n


    fun onTotalAvailableChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
        val value = s.toString()
        totalAvailableBulbs.postValue(value.toInt())
    }
    fun onNumBulbColorsChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
        val value = s.toString()
        numBulbColors.postValue(value.toInt())
    }
    fun onNumEachColorChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
        val value = s.toString()
        numEachColor.postValue(value.toInt())
    }
    fun onNumBulbsPickedChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
        val value = s.toString()
        numBulbsPicked.postValue(value.toInt())
    }
    fun onNumSimChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
        val value = s.toString()
        numSims.postValue(value.toInt())
    }

    fun updateBucketParameters(parameterChanged: LightbulbBucketParameter) {
        // i = j*k
        val i = totalAvailableBulbs.value
        val j = numBulbColors.value
        val k = numEachColor.value
        when(parameterChanged){
            TOTAL_AVAILABLE_BULBS -> {
                if (j == null && k != null){
                    val newj = i?.div(k)
                    // newj.let { numBulbColors.postValue(newj) }
                }
            }
        }
    }

    fun createBucketOfBulbs() {
        val bucket = arrayListOf<LightbulbItem>()
        val totalBulbs = totalAvailableBulbs.value ?: 0
        val numColors = numBulbColors.value ?: 0
        val numOfEachColor = numEachColor.value ?: 0
        for (j in 0 until numColors){
            for (k in 0 until numOfEachColor){
                bucket.add(
                    LightbulbItem(
                        java.util.UUID.randomUUID().toString(),
                        j
                    )
                )
            }
            println("bucketTotal - ${bucket.size} vs totalBulbs - $totalBulbs")
        }
    }

    fun createBulbs(numOfEach: Int) {

    }
}