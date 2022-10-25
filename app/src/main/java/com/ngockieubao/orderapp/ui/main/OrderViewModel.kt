package com.ngockieubao.orderapp.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.ngockieubao.orderapp.data.Category
import com.ngockieubao.orderapp.data.Order
import com.ngockieubao.orderapp.data.Product

class OrderViewModel(application: Application) : ViewModel() {

    private val db = Firebase.firestore
    private val auth = Firebase.auth.currentUser

    private val _listCategory = MutableLiveData<List<Category>>()
    val listCategory: LiveData<List<Category>> = _listCategory

    private val _listProduct = MutableLiveData<List<Product>>()
    val listProduct: LiveData<List<Product>> = _listProduct

    private val _listProductPopular = MutableLiveData<List<Product>>()
    val listProductPopular: LiveData<List<Product>> = _listProductPopular

    private val _defaultQuantity = 1
    val defaultQuantity = _defaultQuantity

    private val _finalQuantity = MutableLiveData<Int>(1)
    val finalQuantity: LiveData<Int> = _finalQuantity

    private val _listOrder = MutableLiveData<List<Order>>()
    val listOrder: LiveData<List<Order>> = _listOrder

    init {
        addCategory()
        loadProduct()
        loadProductPopular()
    }

    private fun addCategory() {
        val list = mutableListOf<Category>()

        val item1 =
            Category("default", "Cơm")
        list.add(item1)
        val item2 =
            Category("default", "Phở")
        list.add(item2)
        val item3 =
            Category("default", "Lẩu")
        list.add(item3)
        val item4 =
            Category("default", "Đồ ăn vặt")
        list.add(item4)
        val item5 =
            Category("default", "Đồ ăn nhanh")
        list.add(item5)
        val item6 =
            Category("default", "Đồ uống")
        list.add(item6)

        _listCategory.value = list
    }

    private fun loadProduct() {
        val list = mutableListOf<String>()
        val listToObj = mutableListOf<Product>()

        db.collection("Product").get()
            .addOnSuccessListener { result ->
//                Log.d(TAG, "loadProduct: ${result.isEmpty}")
//                Log.d(TAG, "loadProduct: ${result.documents}")
                for (document in result) {
//                    Log.d(TAG, "loadProduct: ${document.id} => ${document.data}")
                    list.add(document.id)
                }
                for (item in list) {
                    db.collection("Product").document(item).get()
                        .addOnSuccessListener { documentSnapshot ->
                            val itemToObj = documentSnapshot.toObject<Product>()
//                            Log.d(TAG, "loadProduct: $itemToObj")
                            if (itemToObj != null) {
                                listToObj.add(itemToObj)
                            }
                            _listProduct.value = listToObj
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents.", exception)
            }
    }

    private fun loadProductPopular() {
        val list = mutableListOf<String>()
        val listToObj = mutableListOf<Product>()

        db.collection("Product").whereGreaterThan("rating", 4.5).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
//                    Log.d(TAG, "${document.id} => ${document.data}")
                    list.add(document.id)
                }
                for (item in list) {
                    db.collection("Product").document(item).get()
                        .addOnSuccessListener { documentSnapshot ->
                            val itemToObj = documentSnapshot.toObject<Product>()
                            if (itemToObj != null) {
                                listToObj.add(itemToObj)
                            }
                            _listProductPopular.value = listToObj
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    // Handles adjust quantity order
    fun decreasing() {
        val adjust = _finalQuantity
        val adjustDe = adjust.value?.minus(1)
        if (adjust.value!! <= 1) {
            _finalQuantity.value = 1
        } else {
            adjust.value = adjustDe
            _finalQuantity.value = adjust.value
        }
    }

    fun increasing() {
        val adjust = _finalQuantity
        val adjustIn = adjust.value?.plus(1)
        adjust.value = adjustIn
        _finalQuantity.value = adjust.value
    }

    fun resetOrder() {
        _finalQuantity.value = 1
    }

    fun createOrder(url: String, name: String, description: String, price: Double, quantity: Int = 1) {
        val list = mutableListOf<Order>()

        val order = Order(url, name, description, price, quantity)
        list.add(order)

        _listOrder.value = list
    }

    fun checkCurrentUser(): FirebaseUser? {
        val user = auth
        if (user != null) {
            return user
        } else {
            return null
        }
    }

    class OrderViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return OrderViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    companion object {
        private const val TAG = "OrderViewModel"
    }
}