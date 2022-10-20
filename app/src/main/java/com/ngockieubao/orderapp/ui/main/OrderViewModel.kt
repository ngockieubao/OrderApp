package com.ngockieubao.orderapp.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ngockieubao.orderapp.data.Category
import com.ngockieubao.orderapp.data.Product

class OrderViewModel(application: Application) : ViewModel() {

    private val _listCategory = MutableLiveData<List<Category>>()
    val listCategory: LiveData<List<Category>> = _listCategory

    private val _listProduct = MutableLiveData<List<Product>>()
    val listProduct: LiveData<List<Product>> = _listProduct

    private val _listProductPopular = MutableLiveData<List<Product>>()
    val listProductPopular: LiveData<List<Product>> = _listProductPopular

    init {
        addCategory()
        addProduct()
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

    private fun addProduct() {
        val list = mutableListOf<Product>()
        val item1 = Product(
            "Khô gà cháy tỏi 32gram",
            "12 hours",
            "Chân gà cay Tứ Xuyên",
            38000.0,
            7,
            4.8,
            "Lẩu",
            "https://i.ibb.co/G332np3/depositphotos-20136185-stock-photo-delicious-italian-pizza.jpg",
            "200 grams"
        )
        list.add(item1)
        val item2 = Product(
            "Khô gà cháy tỏi 32gram",
            "12 hours",
            "Chân gà cay Tứ Xuyên",
            38000.0,
            7,
            4.8,
            "Lẩu",
            "https://i.ibb.co/G332np3/depositphotos-20136185-stock-photo-delicious-italian-pizza.jpg",
            "200 grams"
        )
        list.add(item2)
        val item3 = Product(
            "Khô gà cháy tỏi 32gram",
            "12 hours",
            "Chân gà cay Tứ Xuyên",
            38000.0,
            7,
            4.8,
            "Lẩu",
            "https://i.ibb.co/G332np3/depositphotos-20136185-stock-photo-delicious-italian-pizza.jpg",
            "200 grams"
        )
        list.add(item3)
        val item4 = Product(
            "Khô gà cháy tỏi 32gram",
            "12 hours",
            "Chân gà cay Tứ Xuyên",
            38000.0,
            7,
            4.8,
            "Lẩu",
            "https://i.ibb.co/G332np3/depositphotos-20136185-stock-photo-delicious-italian-pizza.jpg",
            "200 grams"
        )
        list.add(item4)
        val item5 = Product(
            "Khô gà cháy tỏi 32gram",
            "12 hours",
            "Chân gà cay Tứ Xuyên",
            38000.0,
            7,
            4.8,
            "Lẩu",
            "https://i.ibb.co/G332np3/depositphotos-20136185-stock-photo-delicious-italian-pizza.jpg",
            "200 grams"
        )
        list.add(item5)
        val item6 = Product(
            "Khô gà cháy tỏi 32gram",
            "12 hours",
            "Chân gà cay Tứ Xuyên",
            38000.0,
            7,
            4.8,
            "Lẩu",
            "https://i.ibb.co/G332np3/depositphotos-20136185-stock-photo-delicious-italian-pizza.jpg",
            "200 grams"
        )
        list.add(item6)

        _listProduct.value = list
        _listProductPopular.value = list
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
}