package com.example.workmanagerdemo

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Observer
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.workmanagerdemo.ui.theme.WorkManagerDemoTheme
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    companion object{
        const val KEY_COUNT_VALUE = "key_count"
        const val TAG = "MainActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val startButton = findViewById<Button>(R.id.start_button)

        startButton.setOnClickListener {
           // oneTimeWorkRequest()
            setPeriodicWorkRequest()
        }
    }

    private fun setPeriodicWorkRequest() {
        val periodicWorkRequest = PeriodicWorkRequest.Builder(UploadWorker::class.java, 15, TimeUnit.MINUTES).build()
        val workManager = WorkManager.getInstance()
        workManager.enqueue(periodicWorkRequest)
    }

    private fun oneTimeWorkRequest() {
        val workText = findViewById<TextView>(R.id.work_result)
        val workManager = WorkManager.getInstance()
        val constraints = Constraints.Builder().setRequiresCharging(true).build()
        val data = Data.Builder().putInt(KEY_COUNT_VALUE, 125).build()
        val uploadRequest:OneTimeWorkRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java)
         //   .setConstraints(constraints)
            .setInputData(data)
            .build()

        val uploadRequest1:OneTimeWorkRequest = OneTimeWorkRequest.Builder(UploadWorker1::class.java)
            .setInputData(data)
            .build()

        val uploadRequest2:OneTimeWorkRequest = OneTimeWorkRequest.Builder(UploadWorker2::class.java)
            .setInputData(data)
            .build()

        workManager.beginWith(uploadRequest).then(uploadRequest1).then(uploadRequest2).enqueue()
        workManager.getWorkInfoByIdLiveData(uploadRequest.id).observe(this, Observer {
            if (it != null) {
                workText.text =  it.state.name
                if(it.state.isFinished){
                    val data = it.outputData
                    val message = data.getString(UploadWorker.KEY_WORKER)
                    Log.d(TAG, "data + $data")
                }
            }
        })
    }

}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WorkManagerDemoTheme {
        Greeting("Android")
    }
}