package edu.umsl.duc_ngo.shoppinglist.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment : Fragment(), CoroutineScope {
    //Job is a background task in coroutines
    private lateinit var job: Job

    //Dispatcher defines our threads, such as where we want to execute the job.
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    //Create an instance when fragment come alive
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
    }

    //When fragment lifetime is over, cancel all job
    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}