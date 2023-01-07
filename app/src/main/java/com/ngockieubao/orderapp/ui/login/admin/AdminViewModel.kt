package com.ngockieubao.orderapp.ui.login.admin

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.ngockieubao.orderapp.data.Receipt
import kotlinx.coroutines.tasks.await

class AdminViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

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

    fun updateInvoice(id: String, status: String) {
        // handle invoice delivery success
        db.collection("ReceiptDetail").document(id).get()
            .addOnSuccessListener { doc ->
                val toObj = doc.toObject<Receipt>()
                if (toObj != null) {
                    when (toObj.status) {
                        "Giao hàng thành công" -> {
                            Toast.makeText(context, "Đơn hàng đã được giao", Toast.LENGTH_SHORT)
                                .show()
                        }
                        "Hủy đơn hàng" -> {
                            Toast.makeText(context, "Đơn hàng đã bị hủy", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            db.collection("ReceiptDetail").document(id).update("status", status)
                                .addOnSuccessListener {
                                    Log.d(TAG, "cancelReceipt: success")
                                    Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT)
                                        .show()
                                }
                                .addOnFailureListener { ex ->
                                    Log.d(TAG, "cancelReceipt: cancel failed - $ex")
                                    Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT)
                                        .show()
                                }
                        }
                    }
                } else {
                    Log.d(TAG, "cancelReceipt: get doc - null")
                }
            }
            .addOnFailureListener { ex ->
                Log.d(TAG, "cancelReceipt: get doc failed - $ex")
            }
    }


    override fun onCleared() {
        super.onCleared()

        _listInvoice.value = null
    }

    companion object {
        private const val TAG = "AdminViewModel"
    }
}