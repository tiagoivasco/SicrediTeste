package com.ivasco.sicrediteste.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivasco.sicrediteste.data.repository.SicrediRepository
import com.ivasco.sicrediteste.model.CheckIn
import com.ivasco.sicrediteste.model.Events
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class HomeViewModel : ViewModel() {
    private val apiRepository = SicrediRepository().makeRequest()
    val eventsResponse: MutableLiveData<ArrayList<Events>> = MutableLiveData()
    val eventPostResponse: MutableLiveData<Any> = MutableLiveData()

    fun getEvents() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val events = apiRepository.getEvents()

                eventsResponse.postValue(events)
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }

    fun postEvent(checkInEvent: CheckIn) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiRepository.checkInEvent(checkInEvent)

                if (response.code() != 200 || response.code() != 204) {
                    eventPostResponse.postValue(false)
                } else {
                    eventPostResponse.postValue(true)
                }
            } catch (exception: Exception) {
                when (exception) {
                    is HttpException -> {
                        val jsonParsed = JSONObject(exception.response()?.errorBody()!!.toString())
                        eventPostResponse.postValue(jsonParsed)
                    }
                }
            }
        }
    }
}