package com.ngockieubao.orderapp.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.ngockieubao.orderapp.data.*
import com.ngockieubao.orderapp.util.TextUtils
import com.ngockieubao.orderapp.util.Utils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Calendar

class OrderViewModel(application: Application) : ViewModel() {

    private val db = Firebase.firestore
    private val auth = Firebase.auth.currentUser
//    private val job = Job()
//    private val scope: CoroutineScope = CoroutineScope(job + Dispatchers.IO)

    private val orderRepository: OrderRepository = OrderRepository(application)

    private val _listCategory = MutableLiveData<List<Category>>()
    val listCategory: LiveData<List<Category>> = _listCategory

    private val _listProduct = MutableLiveData<List<Product>>()
    val listProduct: LiveData<List<Product>> = _listProduct

    private val _listProductCategory = MutableLiveData<List<Product>>()
    val listProductCategory: LiveData<List<Product>> = _listProductCategory

    private val _listProductPopular = MutableLiveData<List<Product>>()
    val listProductPopular: LiveData<List<Product>> = _listProductPopular

    private val _defaultQuantity = 1
    val defaultQuantity = _defaultQuantity

    private val _finalQuantity = MutableLiveData(1)
    val finalQuantity: LiveData<Int> = _finalQuantity

    private val mItemOrder = MutableLiveData<Order>()

    private val _receipt = MutableLiveData<Receipt?>()
    val receipt: LiveData<Receipt?> = _receipt

    private val _itemCartId = MutableLiveData(1)
    val itemCartId: LiveData<Int> = _itemCartId

    private var count = 0
    private var id = 1

    private val ordersFlow: Flow<List<Order>> get() = orderRepository.getAllOrder()

    private val _hasOrder = MutableLiveData<Boolean>()
    val hasOrder: LiveData<Boolean> get() = _hasOrder

    init {
        addCategory()
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
        val query = db.collection("Product").whereGreaterThan("sold", 150).get().await()

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
        if (mItemOrder.value == null) {
            mItemOrder.value = Order("", "", "", 0.0, 0)
            mItemOrder.value!!.id = id
            mItemOrder.value!!.url = url
            mItemOrder.value!!.name = name
            mItemOrder.value!!.description = description
            mItemOrder.value!!.price = price
            mItemOrder.value!!.quantity = quantity
        } else {
            mItemOrder.value!!.id = id
            mItemOrder.value?.url = url
            mItemOrder.value?.name = name
            mItemOrder.value?.description = description
            mItemOrder.value?.price = price
            mItemOrder.value?.quantity = quantity
        }
//        addOrderToCart()
        addItemToCart()
    }

    private fun addItemToCart() {
        if (checkCurrentUser() != null) {
            val item = mItemOrder.value
            val list = mutableListOf<Order>()

            if (item != null) {
                list.add(item)
                insertOrder(item)
            }
        }
    }

    private fun insertOrder(order: Order) = viewModelScope.launch {
        orderRepository.insertOrder(order)
        id++
        count++
        _itemCartId.postValue(id)
    }

    fun deleteOrder(order: Order) = viewModelScope.launch {
        orderRepository.deleteOrder(order)
        id--
        count--
        _itemCartId.postValue(id)
    }

    fun getAllOrderFlow(): Flow<List<Order>> = orderRepository.getAllOrder()

    private suspend fun getAllOrder(): List<Order> = orderRepository.getOrders()

    fun countItemInCart() = flow {
        ordersFlow.collect {
            if (it.isEmpty()) count = 0
            for (item in it) {
                count++
            }
            emit(count)
        }
    }

    // calculating items in cart with flow list
    suspend fun calOrder() = flow {
        // method conflate() get last values avoid slow collector, collector always gets the most recent value emitted.
//        ordersFlow.conflate().collect() {
        ordersFlow.collect {
            var sum = 0.0
            for (i in it.indices) {
                val item = it[i].price * it[i].quantity
                sum += item
            }
            delay(300)
            emit(sum)
        }
    }

    suspend fun makeReceipt(name: String, contact: String, address: String, note: String) {
        if (!TextUtils.checkEdtNull(name) ||
            !TextUtils.checkPhoneNumber(contact) ||
            !TextUtils.checkEdtNull(note)
        ) {
            // calculating receipt
            var sum = 0.0
            val listOrder = getAllOrder()
            for (item in listOrder) {
                sum += item.price * item.quantity
            }
            // make receipt
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

    suspend fun checkItemInCart(item: Product) = flow {
        ordersFlow.collect {
            for (items in it) {
                if (items.name == item.name) {
                    emit(it)
                }
            }
        }
    }

    fun resetCheckCart() {
        _hasOrder.value = false
    }

    private suspend fun clearCart() {
        orderRepository.clear()
    }

    fun checkCurrentUser(): FirebaseUser? {
        return auth
    }

    fun resetMakeReceipt() {
        id = 1
        _receipt.postValue(null)
        _itemCartId.postValue(1)
    }

    override fun onCleared() {
        super.onCleared()
        _receipt.postValue(null)
        _itemCartId.postValue(null)
    }

    companion object {
        private const val TAG = "OrderViewModel"
    }
}