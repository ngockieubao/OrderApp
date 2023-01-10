package com.ngockieubao.orderapp.ui.login.admin

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.ngockieubao.orderapp.data.Product
import com.ngockieubao.orderapp.data.Receipt
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

class AdminViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    private val db = Firebase.firestore
    private val storage = Firebase.storage

    private val _listInvoice = MutableLiveData<List<Receipt?>?>()
    val listInvoice: LiveData<List<Receipt?>?> = _listInvoice

    private val _listProduct = MutableLiveData<List<Product?>?>()
    val listProduct: LiveData<List<Product?>?> = _listProduct

    private val _isSuccess = MutableLiveData<Boolean?>()
    val isSuccess: MutableLiveData<Boolean?> = _isSuccess

    private val _isDelete = MutableLiveData<Boolean?>()
    val isDelete: MutableLiveData<Boolean?> = _isDelete

    private val _isAdded = MutableLiveData<Boolean?>()
    val isAdded: MutableLiveData<Boolean?> = _isAdded

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
                                    Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { ex ->
                                    Log.d(TAG, "cancelReceipt: cancel failed - $ex")
                                    Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show()
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

    suspend fun getAllProduct() {
        val docs = db.collection("Product").get().await()
        val docsToObj = docs.toObjects<Product>()
        val list = mutableListOf<Product>()

        for (item in docsToObj) {
            list.add(item)
        }
        _listProduct.postValue(list)
    }

    suspend fun getProductByFilter(type: String) {
        val docs = db.collection("Product")
            .whereEqualTo("type", type).get().await()
        val docsToObj = docs.toObjects<Product>()
        val list = mutableListOf<Product>()

        for (item in docsToObj) {
            list.add(item)
        }
        _listProduct.postValue(list)
    }

    fun edtProduct(
        docID: String, name: String, price: String, category: Int,
        expiry: String, type: String, weight: String, delivery: String, description: String
    ) {
        // use product docID to update
        db.collection("Product").document(docID)
            .update(
                "name", name,
                "price", price.toDouble(),
                "category", category,
                "expiry", expiry,
                "type", type,
                "weight", weight,
                "delivery", delivery,
                "description", description
            )
            .addOnSuccessListener {
                _isSuccess.value = true
                Toast.makeText(context, "Đã cập nhật sản phẩm", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { ex ->
                Log.d(TAG, "edtProduct: failed - $ex")
                Toast.makeText(context, "Lỗi cập nhật sản phẩm", Toast.LENGTH_SHORT).show()
            }
    }

    fun reset() {
        _isSuccess.value = false
    }

    fun delProduct(
        docID: String
    ) {
        db.collection("Product").document(docID).delete()
            .addOnSuccessListener {
                _isDelete.value = true
                Toast.makeText(context, "Xoá thành công", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { ex ->
                Log.d(TAG, "delProduct: failed - $ex")
                Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show()
            }
    }

    fun resetDel() {
        _isDelete.value = false
    }

    fun addProduct(
        name: String,
        price: String,
        expiry: String,
        weight: String,
        delivery: String,
        description: String,
        category: Int,
        type: String,
        bitmap: Bitmap
    ) {
        val product = Product(0, "", "", "", "", "", "", 0.0, 0.0, 0, "", "", "")
        db.collection("Product").add(product.toHashMap())
            .addOnSuccessListener { doc ->
                db.collection("Product").document(doc.id)
                    .update(
                        "name", name,
                        "price", price.toDouble(),
                        "category", category,
                        "expiry", expiry,
                        "type", type,
                        "weight", weight,
                        "delivery", delivery,
                        "description", description,
                        "docID", doc.id
                    )
                    .addOnSuccessListener {
                        addImageUrl(bitmap, doc.id)
                        _isAdded.value = true
                        Toast.makeText(context, "Đã thêm sản phẩm", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { ex ->
                        Log.d(TAG, "addProduct: failed - $ex")
                        Toast.makeText(context, "Lỗi thêm sản phẩm", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener { ex ->
                Log.d(TAG, "addProduct: failed - $ex")
                Toast.makeText(context, "Thêm thất bại", Toast.LENGTH_SHORT).show()
            }
    }

    fun resetAdded() {
        _isAdded.value = false
    }

    private fun updateUrl(docID: String, newUrl: String) {
        db.collection("Product").document(docID).update("url", newUrl)
            .addOnSuccessListener {
                Log.d(TAG, "updateUrl: success")
            }
            .addOnFailureListener { ex ->
                Log.d(TAG, "updateUrl: failed -$ex")
            }
    }

    private fun addImageUrl(bitmap: Bitmap, docID: String) {
        val storageRef = storage.reference
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val filename = System.currentTimeMillis().toString()
        val data = baos.toByteArray()

        val productsRef = storageRef.child("products").child("${filename}.jpg")
        productsRef.putBytes(data)
            .addOnSuccessListener {
                productsRef.downloadUrl
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val downloadUri = task.result.toString()
                            updateUrl(docID, downloadUri)
                            Log.d(TAG, "addImageUrl: upload success")
                        } else {
                            Log.d(TAG, "addImageUrl: downloadUrl failed")
                        }
                    }
                    .addOnFailureListener { ex ->
                        Log.d(TAG, "addImageUrl: downloadUrl failed - $ex")
                    }
            }
            .addOnFailureListener { ex ->
                Log.d(TAG, "addImageUrl: upload failed - $ex")
            }
    }

    override fun onCleared() {
        super.onCleared()

        _listInvoice.postValue(null)
        _listProduct.postValue(null)
        _isSuccess.value = null
        _isDelete.value = null
        _isAdded.value = null
    }

    companion object {
        private const val TAG = "AdminViewModel"
    }
}