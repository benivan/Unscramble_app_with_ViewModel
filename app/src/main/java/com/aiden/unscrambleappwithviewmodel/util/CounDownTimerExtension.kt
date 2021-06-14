package com.aiden.unscrambleappwithviewmodel.util

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

abstract class CountDownTimerExt(private var mMillisInFuture: Long, var mInterval: Long) :
    DefaultLifecycleObserver {

    private lateinit var countDownTimer: CountDownTimer
    private var remainingTime: Long = 0
    private var isTimerPaused: Boolean = true

    init {
        this.remainingTime = mMillisInFuture
    }

    @Synchronized
    fun start() {
        Log.d(TAG, "start: called")
        if (isTimerPaused) {
            countDownTimer = object : CountDownTimer(remainingTime, mInterval) {
                override fun onFinish() {
                    onTimerFinish()
                    restart()
                }

                override fun onTick(millisUntilFinished: Long) {
                    remainingTime = millisUntilFinished
                    onTimerTick(millisUntilFinished)
                }

            }.apply {
                start()
            }
            isTimerPaused = false
        }
    }

    fun pause() {
        Log.d(TAG, "pause: timer")
        if (!isTimerPaused) {
            countDownTimer.cancel()
        }
        isTimerPaused = true
    }

    fun restart() {
        countDownTimer.cancel()
        remainingTime = mMillisInFuture
        isTimerPaused = true
        start()
    }

    abstract fun onTimerTick(millisUntilFinished: Long)
    abstract fun onTimerFinish()

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        Log.d(TAG, "onPause: fragment paused")
        pause()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Log.d(TAG, "onResume: fragment resumed")
        start()
    }

    companion object {
        private const val TAG = "CounDownTimerExtension"
    }
}