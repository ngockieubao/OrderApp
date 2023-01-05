package com.ngockieubao.orderapp.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
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
import java.util.*


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

    private val _listBanner = MutableLiveData<List<Banner>>()
    val listBanner: LiveData<List<Banner>> get() = _listBanner

    private val _defaultQuantity = 1
    val defaultQuantity = _defaultQuantity

    private val _finalQuantity = MutableLiveData(1)
    val finalQuantity: LiveData<Int> = _finalQuantity

    private val mItemOrder = MutableLiveData<Order>()

    private val _receipt = MutableLiveData<Receipt?>()
    val receipt: LiveData<Receipt?> = _receipt

    private val _itemCartId = MutableLiveData(1)
    val itemCartId: LiveData<Int> = _itemCartId

    private val _listHistory = MutableLiveData<List<Receipt>>()
    val listHistory: LiveData<List<Receipt>> get() = _listHistory

    private var count = 0
    private var id = 1

    private val ordersFlow: Flow<List<Order>> get() = orderRepository.getAllOrder()

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    private val _admin = MutableLiveData<Admin?>()
    val admin: LiveData<Admin?> = _admin

    private val _hasAdmin = MutableLiveData<Boolean?>()
    val hasAdmin: LiveData<Boolean?> = _hasAdmin

    private val _isCancel = MutableLiveData<Boolean?>()
    val isCancel: LiveData<Boolean?> = _isCancel

    private val _banner = MutableLiveData<Int>()
    val banner: LiveData<Int> = _banner

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

    // Handles adjust quantity order in order fragment
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

    // Handles adjust quantity order in cart
    suspend fun decreasingCart(name: String, quantity: Int) {
        val dbLocal = getAllOrder()
        for (item in dbLocal) {
            if (name == item.name) {
                orderRepository.decreasing(name, quantity.minus(1))
            }
        }
    }

    suspend fun increasingCart(name: String, quantity: Int) {
        val dbLocal = getAllOrder()
        for (item in dbLocal) {
            if (name == item.name) {
                orderRepository.increasing(name, quantity.plus(1))
            }
        }
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
        addItemToCart()
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

    suspend fun getAllOrder(): List<Order> = orderRepository.getOrders()

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

    fun countItemInCart() = flow {
        ordersFlow.collect {
            if (it.isEmpty()) count = 0
            for (item in it) {
                count++
            }
            emit(count)
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

    suspend fun clearCart() {
        orderRepository.clear()
    }

    suspend fun makeReceipt(
        address: String,
        contact: String,
        name: String,
        note: String,
        type: String
    ) {
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
            // init receipt
            val time = Calendar.getInstance().time
            val receipt = Receipt(
                address = address,
                code = "",
                contact = contact,
                name = name,
                note = note,
                receipts = listOrder,
                status = "Đang xử lý",
                time = "${Utils.formatDate(time)}, ${Utils.formatTime(time)}",
                total = sum,
                type = type,
                userID = checkCurrentUser()!!.uid
            )
            // add receipt to firestore
            checkCurrentUser()?.uid.let {
                // no user
                if (it == null) Log.d(TAG, "makeReceipt: Unknown user")
                // has user
                else {
                    db.collection("ReceiptDetail").add(receipt.toHashMap())
                        .addOnSuccessListener { docRef ->
                            Log.d(TAG, "makeReceipt: ${docRef.id}")
                            // update code receipt by doc id generated above
                            db.collection("ReceiptDetail").document(docRef.id)
                                .update("code", docRef.id)
                            // assign value
                            _receipt.value = receipt
                        }
                        .addOnFailureListener {
                            Log.d(TAG, "makeReceipt: failed")
                        }
                }
            }
        }
    }

    fun resetMakeReceipt() {
        id = 1
        _receipt.postValue(null)
        _itemCartId.postValue(1)
    }

    suspend fun getHistory() {
        checkCurrentUser()?.uid.let {
            // no user
            if (it == null) Log.d(TAG, "makeReceipt: Unknown user")
            // has user
            else {
                val listReceipt = db.collection("ReceiptDetail")
                    .whereEqualTo("userID", checkCurrentUser()!!.uid).get().await()
                val listToObj = listReceipt.toObjects<Receipt>()

                if (listToObj.isEmpty()) {
                    Log.d(TAG, "getHistory: null")
                } else {
                    _listHistory.postValue(listToObj)
                }
            }
        }
    }

    suspend fun getProductOrderByPopular() = flow {
        val listBanner = db.collection("Product")
            .whereGreaterThanOrEqualTo("sold", 180).get().await()
        val listToObj = listBanner.toObjects<Banner>()

        while (true) {
            for (item in listToObj) {
                emit(item.url)
                delay(5000)
            }
        }
    }

    suspend fun getBanner() {
//        val listBanner = db.collection("Banner").get().await()
//        val listToObj = listBanner.toObjects<Banner>()

        val listBanner = db.collection("Product")
            .whereGreaterThanOrEqualTo("sold", 180).get().await()
        val listToObj = listBanner.toObjects<Banner>()

        if (listToObj.isEmpty()) {
            Log.d(TAG, "getBanner: null")
        } else {
            var res = 0
            for (item in listBanner) res++
            _banner.postValue(res)
            _listBanner.postValue(listToObj)
        }
    }

    suspend fun getSizeListBanner(): Int {
        val listBanner = db.collection("Banner").get().await()
        var res = 0
        for (item in listBanner) res++
        return res
    }

    fun cancelReceipt(id: String, status: String) {
        checkCurrentUser()?.uid.let {
            // no user
            if (it == null) Log.d(TAG, "makeReceipt: Unknown user")
            // has user
            else {
                db.collection("ReceiptDetail").document(id).update("status", status)
                    .addOnSuccessListener {
                        Log.d(TAG, "cancelReceipt: success")
                        _isCancel.value = true
                    }
                    .addOnFailureListener {
                        Log.d(TAG, "cancelReceipt: failed")
                        _isCancel.value = false
                    }
            }
        }
    }

    suspend fun getUserInfo() {
        checkCurrentUser()?.uid?.let {
            val user = db.collection("UserInfo").document(it).get().await()
            val toObj = user.toObject<User>()
            // assign value to observe change data side view
            _user.postValue(toObj)
        }
    }

    fun sendFeedback(name: String, contact: String, email: String, message: String) {
        val feedback = Feedback(name, contact, email, message)
        db.collection("Feedback").add(feedback)
            .addOnSuccessListener {
                Log.d(TAG, "sendFeedback: success")
            }
            .addOnFailureListener { ex ->
                Log.d(TAG, "sendFeedback: failed - $ex")
            }
    }

    fun signInAdmin(username: String, passwd: String) {
        db.collection("Admin").get()
            .addOnSuccessListener {
                val toObj = it.toObjects<Admin>()
                val size = toObj.size
                for (doc in 0 until size) {
                    if (
                        username == toObj[doc].username &&
                        passwd == toObj[doc].passwd
                    ) {
                        // assign value to observe change data side view
                        _admin.value = toObj[doc]
                    }
                }
            }
            .addOnFailureListener { ex ->
                Log.d(TAG, "signInAdmin: $ex")
            }
    }

    fun signOutAdmin() = _hasAdmin.postValue(false)

    fun checkCurrentUser(): FirebaseUser? {
        return auth
    }

    override fun onCleared() {
        super.onCleared()
        _receipt.postValue(null)
        _itemCartId.postValue(null)
        _isCancel.postValue(null)
    }

    companion object {
        private const val TAG = "OrderViewModel"
    }
}