package com.vhenri.cachew.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vhenri.cachew.core.lifecycle.SingleLiveEvent
import com.vhenri.cachew.data.LightbulbItem
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import java.lang.Exception
import kotlin.random.Random

class MainViewModel() : ViewModel(), KoinComponent {
    var oomError = false

    private val totalAvailableBulbs = SingleLiveEvent<Int>().apply { value = 0 } // i
    private val numBulbColors = SingleLiveEvent<Int>().apply { value = 0 } // j
    private val numEachColor = SingleLiveEvent<Int>().apply { value = 0 } // k
    private val numBulbsPicked = SingleLiveEvent<Int>().apply { value = 0 } // m
    private var numSims = SingleLiveEvent<Int>().apply { value = 1 } // n
    val totalUniqueColors = SingleLiveEvent<Int>()
    val averageUniqueColors = SingleLiveEvent<Float>()
    val showError = SingleLiveEvent<Boolean>().apply { value = false }

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
            kotlin.math.abs(stringValue.toInt())
        } catch (e: Exception) {
            0
        }
    }

    fun runSimulation() {
        viewModelScope.launch {
            val numRuns = numSims.value ?: 1
            var sumRuns = 0
            simLoop@ for (run in 0 until numRuns) {
                if (oomError) {
                    break@simLoop
                }
                val numberOfUniqueColors = getNumberOfUniqueColors()
                if (run == 0) {
                    totalUniqueColors.postValue(numberOfUniqueColors)
                }
                sumRuns += numberOfUniqueColors
            }
            // Check for divide by zero
            if (numRuns != 0) {
                averageUniqueColors.postValue(sumRuns.toFloat() / numRuns.toFloat())
            } else {
                averageUniqueColors.postValue(0.toFloat())
            }
            if (oomError) {
                showError.postValue(true)
            }
        }
    }

    private fun getNumberOfUniqueColors(): Int {
        val bucket = createBulbBucket()
        return if (!oomError) {
            val pickedBulbs = pickBulbs(bucket)
            pickedBulbs.distinct().size
        } else {
            0
        }
    }

    private fun createBulbBucket(): ArrayList<LightbulbItem> {
        val bucket = arrayListOf<LightbulbItem>()
        val numColors = numBulbColors.value ?: 0
        val numOfEachColor = numEachColor.value ?: 0

        // Add the bulbs to the bucket
        loop@ for (j in 0 until numColors) {
            for (k in 0 until numOfEachColor) {
                try {
                    bucket.add(LightbulbItem(j))
                } catch (e: OutOfMemoryError) {
                    Log.d("###", "Error occurred! $e")
                    oomError = true
                    break@loop
                }
            }
        }

        return bucket
    }

    private fun pickBulbs(bucket: ArrayList<LightbulbItem>): List<LightbulbItem> {
        var bulbsToPick = numBulbsPicked.value ?: 0
        // Using a seeded random generator to make testing/debugging more repeatable.
        val randomSeed = Random.nextInt()
        val randomGenerator = Random(randomSeed)

        for (i in 0 until bulbsToPick) {
            val randomIndex = randomGenerator.nextInt(0, bucket.size)
            val pickedBulb = bucket[randomIndex]
            if (!pickedBulb.picked) {
                pickedBulb.picked = true
            } else {
                // if we've already picked the bulb, add another iteration
                bulbsToPick++
            }
        }
        return bucket.filter { bulb -> bulb.picked }
    }
}
