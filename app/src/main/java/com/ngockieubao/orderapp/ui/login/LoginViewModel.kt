package com.ngockieubao.orderapp.ui.login

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ngockieubao.orderapp.data.Login
import com.ngockieubao.orderapp.data.User
import com.ngockieubao.orderapp.util.Constants
import com.ngockieubao.orderapp.util.TextUtils

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    private val auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore

    private val _signUp = MutableLiveData<Login>()
    val signUp: LiveData<Login>
        get() = _signUp

    private val _login = MutableLiveData<Login?>()
    val login: LiveData<Login?>
        get() = _login

    fun createAccount(email: String?, passwd: String?) {
        if (!TextUtils.checkNull(email, passwd)) {
            if (TextUtils.isValidEmail(email)) {
                if (TextUtils.checkPassword(passwd)) {
                    auth.createUserWithEmailAndPassword(email!!, passwd!!)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val getSignUp = Login(email, passwd)
                                _signUp.value = getSignUp
//                                addUserData()
                            }
                        }
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Sign-in success, update UI with user's info signed-in success
                                Toast.makeText(context, "Register success", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener { task ->
                            Log.d(TAG, "${task.message}")
                            val emailExistsException = task.message
                            if (task.message == emailExistsException)
                                Toast.makeText(context, "The email address is already in use by another account.", Toast.LENGTH_SHORT).show()
                            else
                                Toast.makeText(context, "Register failed", Toast.LENGTH_SHORT).show()
                        }
                } else Toast.makeText(context, "Password has length more 6 character", Toast.LENGTH_SHORT).show()
            } else Toast.makeText(context, "Invalid email format", Toast.LENGTH_SHORT).show()
        } else Toast.makeText(context, "Email or Password null", Toast.LENGTH_SHORT).show()
    }

    fun signIn(email: String?, passwd: String?) {
        if (!TextUtils.checkNull(email, passwd)) {
            if (TextUtils.isValidEmail(email)) {
                if (TextUtils.checkPassword(passwd)) {
                    auth.signInWithEmailAndPassword(email!!, passwd!!)
                        .addOnCompleteListener { task ->
                            if (email == auth.currentUser?.email) {
                                val getLogin = Login(email, passwd)
                                _login.value = getLogin
                            }
                        }
                        .addOnCompleteListener() { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Login success", Toast.LENGTH_SHORT).show()
                            }
                        }.addOnFailureListener() { exception ->
                            Toast.makeText(context, "The email or password is not correct", Toast.LENGTH_SHORT).show()
                        }
                } else Toast.makeText(context, "Password has length more 6 character", Toast.LENGTH_SHORT).show()
            } else Toast.makeText(context, "Invalid email format", Toast.LENGTH_SHORT).show()
        } else Toast.makeText(context, "Email or Password null", Toast.LENGTH_SHORT).show()
    }

    fun signOut() {
        Firebase.auth.signOut()
        _login.value = null
    }

    private fun checkCurrentUser(): FirebaseUser? {
        val user = Firebase.auth.currentUser
        if (user != null) return user
        else return null
    }

    private fun addUserData() {
        val user = User(
            checkCurrentUser()?.uid,
            Constants.getUsernameFromEmail(checkCurrentUser()?.email),
            checkCurrentUser()?.email,
            "default",
            "default",
            0
        )
        checkCurrentUser()?.uid.let {
//            db.collection("user")
//                .document(it).set(user.toHashMap())
//                .addOnSuccessListener {
//                    Log.d(TAG, "addUserData: success")
//                }
//                .addOnFailureListener { exception ->
//                    Log.d(TAG, "addUserData: failed")
//                }
        }
    }

    class LoginViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}