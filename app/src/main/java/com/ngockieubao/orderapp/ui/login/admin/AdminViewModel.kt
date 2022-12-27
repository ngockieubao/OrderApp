package com.ngockieubao.orderapp.ui.login.admin

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.ngockieubao.orderapp.data.Receipt
import kotlinx.coroutines.tasks.await

class AdminViewModel(application: Application) : ViewModel() {

    private val db = Firebase.firestore

    private val _listInvoice = MutableLiveData<List<Receipt?>?>()
    val listInvoice: LiveData<List<Receipt?>?> = _listInvoice

    suspend fun getAllInvoice() {
        val res = db.collection("ReceiptDetail").get().await()
        val toObj = res.toObjects<Receipt>()
        val list = mutableListOf<Receipt>()

        for (item in toObj) {
            list.add(item)
        }
        _listInvoice.value = list
    }

    suspend fun getInvoiceByFilter(status: String) {
        val res = db.collection("ReceiptDetail")
            .whereEqualTo("status", status).get().await()
        val toObj = res.toObjects<Receipt>()
        val list = mutableListOf<Receipt>()

        for (item in toObj) {
            list.add(item)
        }
        _listInvoice.value = list
    }

    override fun onCleared() {
        super.onCleared()

        _listInvoice.value = null
    }
}