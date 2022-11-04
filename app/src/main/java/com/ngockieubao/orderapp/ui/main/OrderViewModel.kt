package com.ngockieubao.orderapp.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.ngockieubao.orderapp.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class OrderViewModel(application: Application) : ViewModel() {

    private val db = Firebase.firestore
    private val auth = Firebase.auth.currentUser
    private val job = Job()
    private val scope: CoroutineScope = CoroutineScope(job + Dispatchers.IO)

    private val orderRepository: OrderRepository = OrderRepository(application)

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

    private val _itemOrder = MutableLiveData<Order>()

    private val _itemInCart = MutableLiveData(0)
    val itemInCart: LiveData<Int> = _itemInCart
    private var count = 0

    private val _sumOrder = MutableLiveData(0.0)
    val sumOrder: LiveData<Double> = _sumOrder

    private val _receipt = MutableLiveData<Receipt?>()
    val receipt: MutableLiveData<Receipt?> = _receipt

    init {
        addCategory()
        countOrder()
    }

    private fun addCategory() {
        val list = mutableListOf<Category>()

        val item1 = Category("default", "Cơm")
        list.add(item1)
        val item2 = Category("default", "Phở")
        list.add(item2)
        val item3 = Category("default", "Lẩu")
        list.add(item3)
        val item4 = Category("default", "Đồ ăn vặt")
        list.add(item4)
        val item5 = Category("default", "Đồ ăn nhanh")
        list.add(item5)
        val item6 = Category("default", "Đồ uống")
        list.add(item6)

        _listCategory.value = list
    }

    suspend fun getProduct() {
        val listToObj = mutableListOf<Product>()
        val query = db.collection("Product").get().await()

        if (query.documents.isNotEmpty()) {
            val queryToObj = query.toObjects<Product>()
            for (item in queryToObj) {
                listToObj.add(item)
            }
            _listProduct.value = listToObj
        }
    }

    suspend fun getProductPopular() {
        val listToObj = mutableListOf<Product>()
        val query = db.collection("Product").whereGreaterThan("rating", 4.5).get().await()

        if (query.documents.isNotEmpty()) {
            val queryToObj = query.toObjects<Product>()
            for (item in queryToObj) {
                listToObj.add(item)
            }
            _listProductPopular.value = listToObj
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

    fun createOrder(url: String, name: String, description: String, price: Double, quantity: Int) {
        if (_itemOrder.value == null) {
            _itemOrder.value = Order("", "", "", 0.0, 0)
            _itemOrder.value!!.url = url
            _itemOrder.value!!.name = name
            _itemOrder.value!!.description = description
            _itemOrder.value!!.price = price
            _itemOrder.value!!.quantity = quantity
        } else {
            _itemOrder.value?.url = url
            _itemOrder.value?.name = name
            _itemOrder.value?.description = description
            _itemOrder.value?.price = price
            _itemOrder.value?.quantity = quantity
        }

        addListOrder()
    }

    private fun addListOrder() {
        val item = _itemOrder.value
        val list = mutableListOf<Order>()

        if (item != null) {
            list.add(item)
            insertOrder(item)
        }
    }

    private fun insertOrder(order: Order) = viewModelScope.launch {
        orderRepository.insertOrder(order)
        count++
        _itemInCart.postValue(count)
    }

    fun deleteOrder(order: Order) = viewModelScope.launch {
        orderRepository.deleteOrder(order)
        count--
        _itemInCart.postValue(count)
    }

    private fun countOrder() {
        viewModelScope.launch {
            val list = orderRepository.fullOrder()
            if (list.isEmpty()) _itemInCart.value = 0
            else {
                for (item in list) {
                    count++
                }
                _itemInCart.value = count
            }
        }
    }

    fun getAllOrder(): Flow<List<Order>> = orderRepository.getAllOrder()

    fun calOrder(
        order: List<Order>
    ) {
        val size = order.size
        var sum = 0.0

        for (i in 0 until size) {
            sum += order[i].price * order[i].quantity
        }
        _sumOrder.postValue(sum)
    }

    suspend fun fullOrder(): List<Order> = orderRepository.fullOrder()

    fun makeReceipt(list: List<Order>, name: String, contact: String, address: String, note: String) {
        var sum = 0.0
        for (item in list) {
            sum += item.price * item.quantity
        }
        val receipt = Receipt(name, contact, note, sum, address)
        checkCurrentUser()?.uid.let {
            db.collection("Receipt")
                .add(receipt.toHashMap())
                .addOnSuccessListener {
                    _receipt.value = receipt
                    for (item in list) {
                        deleteOrder(item)
                    }
                    Log.d(TAG, "makeReceipt: success")
                }
                .addOnFailureListener { ex ->
                    Log.d(TAG, "makeReceipt: ${ex.message} - failed")
                }
        }
    }

    fun checkCurrentUser(): FirebaseUser? {
        val user = auth
        if (user != null) {
            return user
        } else {
            return null
        }
    }

    fun resetMakeReceipt() {
        _receipt.value = null
    }

    companion object {
        private const val TAG = "OrderViewModel"
    }
}