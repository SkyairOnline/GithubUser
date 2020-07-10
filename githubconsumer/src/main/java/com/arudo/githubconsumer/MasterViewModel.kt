package com.arudo.githubconsumer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arudo.githubconsumer.model.Lists
import com.arudo.githubconsumer.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class MasterViewModel : ViewModel() {
    val listSearchUsers = MutableLiveData<ArrayList<Lists>>()
    val listUser = MutableLiveData<User>()
    var errorStatus = MutableLiveData<String>()
    private val apiKey = BuildConfig.API_KEY
    private val client = AsyncHttpClient()
    var list = Lists()
    var user = User()
    var userObjectList = JSONObject()
    var listItem = JSONArray()
    var responseObject = JSONObject()
    val userSearchItems = ArrayList<Lists>()

    fun setViewModel(urlApi: String, key: String) {
        client.addHeader("Authorization", "token $apiKey")
        client.addHeader("User-Agent", "request")
        client.get(urlApi, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    if (key == "user") {
                        responseObject = JSONObject(result)
                        user = User()
                        user.username = responseObject.getString("login")
                        user.name = responseObject.getString("name")
                        user.avatar = responseObject.getString("avatar_url")
                        user.company = responseObject.getString("company")
                        user.location = responseObject.getString("location")
                        user.repository = responseObject.getInt("public_repos")
                        user.follower = responseObject.getInt("followers")
                        user.following = responseObject.getInt("following")
                        user.bio = responseObject.getString("bio")
                        user.url = responseObject.getString("html_url")
                        user.urlApi = responseObject.getString("url")
                        user.gist = responseObject.getInt("public_gists")
                        listUser.postValue(user)
                    } else {
                        userSearchItems.clear()
                        if (key == "list") {
                            responseObject = JSONObject(result)
                            listItem = responseObject.getJSONArray("items")
                            for (i in 0 until listItem.length()) {
                                userObjectList = listItem.getJSONObject(i)
                                list = Lists()
                                list.username = userObjectList.getString("login")
                                list.avatar = userObjectList.getString("avatar_url")
                                list.url = userObjectList.getString("html_url")
                                list.urlApi = userObjectList.getString("url")
                                userSearchItems.add(list)
                            }
                        } else if (key == "follow") {
                            listItem = JSONArray(result)
                            for (i in 0 until listItem.length()) {
                                userObjectList = listItem.getJSONObject(i)
                                list = Lists()
                                list.username = userObjectList.getString("login")
                                list.avatar = userObjectList.getString("avatar_url")
                                list.url = userObjectList.getString("html_url")
                                list.urlApi = userObjectList.getString("url")
                                userSearchItems.add(list)
                            }
                        }
                        listSearchUsers.postValue(userSearchItems)
                    }
                } catch (ex: Exception) {
                    Log.d("ExceptionOnSuccess", "$statusCode : ${ex.message}")
                    errorStatus.postValue(ex.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                Log.d("ExceptionOnFailure", "$statusCode : ${error.message}")
                errorStatus.postValue(error.message.toString())
            }

        })
    }

    fun getList(): LiveData<ArrayList<Lists>> {
        return listSearchUsers
    }

    fun getUser(): LiveData<User> {
        return listUser
    }

    fun getStatusApp(): LiveData<String> {
        return errorStatus
    }
}