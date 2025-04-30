package com.example.workmanagerdemo

import android.content.Context
import android.util.Log
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.text.SimpleDateFormat
import java.util.Date

class UploadWorker1(context:Context, parameters: WorkerParameters): Worker(context, parameters) {
    companion object{
        val TAG = UploadWorker1.javaClass.name
        const val KEY_WORKER = "key_worker"
    }
    override fun doWork(): Result {
        val count = inputData.getInt(MainActivity.KEY_COUNT_VALUE, 0)
        val time = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        val currentDate = time.format(Date())
        val outputData =Data.Builder().putString(KEY_WORKER, currentDate).build()
        Log.d(TAG, "COUNT : $count")
        try {
            for(i in 0..60){
                Log.d(TAG, "i value in uploadworker1 is  $i")
            }
            return (
                Result.success(outputData)
            )
        }catch (e:Exception) {
            return Result.failure()
        }

    }
}