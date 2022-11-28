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
import com.ngockieubao.orderapp.util.TextUtils
import com.ngockieubao.orderapp.util.Utils
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Calendar

class OrderViewModel(application: Application) : ViewModel() {

    private val db = Firebase.firestore
    private val auth = Firebase.auth.currentUser
//    private val job = Job()
//    private val scope: CoroutineScope = CoroutineScope(job + Dispatchers.IO)

    private val itemInCartCurUser = db.collection("Cart")
        .document(checkCurrentUser()!!.uid).collection("ItemInCart")

    private val orderRepository: OrderRepository = OrderRepository(application)

    private val _listCategory = MutableLiveData<List<Category>>()
    val listCategory: LiveData<List<Category>> = _listCategory

    private val _listProduct = MutableLiveData<List<Product>>()
    val listProduct: LiveData<List<Product>> = _listProduct

    private val _listProductCategory = MutableLiveData<List<Product>>()
    val listProductCategory: LiveData<List<Product>> = _listProductCategory

    private val _listProductPopular = MutableLiveData<List<Product>>()
    val listProductPopular: LiveData<List<Product>> = _listProductPopular

    private var _listCart = MutableLiveData<List<Order>?>()
    val listCart: LiveData<List<Order>?> = _listCart

    private val _defaultQuantity = 1
    val defaultQuantity = _defaultQuantity

    private val _finalQuantity = MutableLiveData<Int>(1)
    val finalQuantity: LiveData<Int> = _finalQuantity

    private val _itemOrder = MutableLiveData<Order>()

    private val _itemInCart = MutableLiveData<Int>(0)
    val itemInCart: LiveData<Int> = _itemInCart
    private var count = 0

    private val _sumOrder = MutableLiveData(0.0)
    val sumOrder: LiveData<Double> = _sumOrder

    private val _receipt = MutableLiveData<Receipt?>()
    val receipt: MutableLiveData<Receipt?> = _receipt

    private val _itemCartId = MutableLiveData(1)
    val itemCartId: LiveData<Int> = _itemCartId
    private var id = 1

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

    suspend fun getProductByCategory(type: String) {
        val listToObj = mutableListOf<Product>()
        val query = db.collection("Product").whereEqualTo("type", type).get().await()

        if (query.documents.isNotEmpty()) {
            val queryToObj = query.toObjects<Product>()
            for (item in queryToObj) {
                listToObj.add(item)
            }
            _listProductCategory.value = listToObj
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

    fun createOrder(
        id: Int, url: String, name: String, description: String, price: Double, quantity: Int
    ) {
        if (_itemOrder.value == null) {
            _itemOrder.value = Order(1, "", "", "", 0.0, 0)
            _itemOrder.value!!.id = id
            _itemOrder.value!!.url = url
            _itemOrder.value!!.name = name
            _itemOrder.value!!.description = description
            _itemOrder.value!!.price = price
            _itemOrder.value!!.quantity = quantity
        } else {
            _itemOrder.value!!.id = id
            _itemOrder.value?.url = url
            _itemOrder.value?.name = name
            _itemOrder.value?.description = description
            _itemOrder.value?.price = price
            _itemOrder.value?.quantity = quantity
        }
        addOrderToCart()
    }

    private fun addOrderToCart() {
        val item = _itemOrder.value
        item?.toHashMap()
        if (item != null) {
            itemInCartCurUser.add(item)
                .addOnSuccessListener {
                    id++
                    count++
                    _itemCartId.postValue(id)
                    _itemInCart.postValue(count)
                    Log.d(TAG, "addOrderToCart: success")
                }
                .addOnFailureListener {
                    Log.d(TAG, "addOrderToCart: failed")
                }
        }
    }

    fun deleteOrder(order: Order) = viewModelScope.launch {
        orderRepository.deleteOrder(order)
        count--
        _itemInCart.postValue(count)
    }

    private fun countOrder() {
        viewModelScope.launch {
            val list = getUserCart()
            if (list.isEmpty()) _itemInCart.value = 0
            else {
                for (item in list) {
                    count++
                }
                _itemInCart.postValue(count)
            }
        }
    }

    private suspend fun getUserCart(): List<Order> {
        val listCart = mutableListOf<Order>()
        val queryCart = itemInCartCurUser.get().await()

        if (queryCart.documents.isNotEmpty()) {
            val queryToObj = queryCart.toObjects<Order>()
            for (item in queryToObj) {
                listCart.add(item)
            }
        }
        return listCart
    }

    suspend fun populateCart() {
        val list = getUserCart()
        _listCart.value = list
    }

    suspend fun calOrder(
    ): Double {
        var sum = 0.0
        val list = getUserCart()
        val size = list.size

        for (i in 0 until size) {
            sum += list[i].price * list[i].quantity
        }
        _sumOrder.postValue(sum)
        return sum
    }

    suspend fun makeReceipt(name: String, contact: String, address: String, note: String) {
        if (!TextUtils.checkEdtNull(name) ||
            !TextUtils.checkPhoneNumber(contact) ||
            !TextUtils.checkEdtNull(note)
        ) {
            var sum = 0.0
            val listOrder = getUserCart()
            for (item in listOrder) {
                sum += item.price * item.quantity
            }

            val time = Calendar.getInstance().time
            val receipt = Receipt(name, contact, note, sum, address, listOrder, "${Utils.formatTime(time)} GMT+7, ${Utils.formatDate(time)}")
            checkCurrentUser()?.uid.let {
                db.collection("Receipt")
                    .add(receipt.toHashMap()).await()
                _receipt.value = receipt
                // create receipt success -> clear cart
                clearCart()
            }
        }
    }

    private suspend fun clearCart() {
        val listId = mutableListOf<String>()
//        val listInCart =
        itemInCartCurUser.get()
            .addOnSuccessListener { listInCart ->
                for (item in listInCart) {
                    listId.add(item.id)
                }
                for (item in 0 until listInCart.size()) {
                    if (listId[item] == listInCart.documents[item].id) {
                        itemInCartCurUser.document(listInCart.documents[item].id).delete()
                            .addOnSuccessListener {
                                count--
                                Log.d(TAG, "clearCart: success")
                            }.addOnFailureListener {
                                Log.d(TAG, "clearCart: failed")
                            }
                        _itemInCart.postValue(0)
                    }
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "clearCart: null")
            }
    }

    fun checkCurrentUser(): FirebaseUser? {
        return auth
    }

    fun resetMakeReceipt() {
        _receipt.postValue(null)
        id = 1
        _itemCartId.postValue(1)
    }

    override fun onCleared() {
        super.onCleared()
//        _listCart.value = null
        _receipt.postValue(null)
        _itemCartId.postValue(null)
    }

    companion object {
        private const val TAG = "OrderViewModel"
    }
}