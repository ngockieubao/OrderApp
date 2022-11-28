package com.ngockieubao.orderapp.ui.login

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.IntentSender
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.ngockieubao.orderapp.BuildConfig
import com.ngockieubao.orderapp.data.Login
import com.ngockieubao.orderapp.data.User
import com.ngockieubao.orderapp.util.Constants
import com.ngockieubao.orderapp.util.TextUtils
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Suppress("KotlinConstantConditions")
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    private val auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore

    private var _oneTapClient: SignInClient? = null
    val oneTapClient get() = _oneTapClient
    private var _signUpRequest: BeginSignInRequest? = null
    val signUpRequest get() = _signUpRequest
    private var _signInRequest: BeginSignInRequest? = null
    val signInRequest get() = _signInRequest

    private val _signUp = MutableLiveData<Login?>()
    val signUp: LiveData<Login?>
        get() = _signUp

    private val _login = MutableLiveData<Login?>()
    val login: LiveData<Login?>
        get() = _login

    private val _hasGoogleSignIn = MutableLiveData<Boolean?>()
    val hasGoogleSignIn: LiveData<Boolean?>
        get() = _hasGoogleSignIn

    fun createAccount(email: String?, passwd: String?) {
        if (!TextUtils.checkEmailPasswdNull(email, passwd)) {
            if (TextUtils.isValidEmail(email)) {
                if (TextUtils.checkPassword(passwd)) {
                    auth.createUserWithEmailAndPassword(email!!, passwd!!)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val getSignUp = Login(email, passwd)
                                _signUp.value = getSignUp
                                addUserData()
                                // Cause sign-up success then user logged so need to sign out & login again
                                auth.signOut()
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
        if (!TextUtils.checkEmailPasswdNull(email, passwd)) {
            if (TextUtils.isValidEmail(email)) {
                if (TextUtils.checkPassword(passwd)) {
                    auth.signInWithEmailAndPassword(email!!, passwd!!)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                if (email == auth.currentUser?.email) {
                                    val getLogin = Login(email, passwd)
                                    _login.value = getLogin
                                }
                                Toast.makeText(context, "Login success", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener() { exception ->
                            val strErr = exception
                            if (strErr == exception)
                                Toast.makeText(context, "Your email is not exist", Toast.LENGTH_SHORT).show()
                            else
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

    fun checkCurrentUser(): FirebaseUser? {
        return Firebase.auth.currentUser
    }

    private fun addUserData() {
        val user = User(
            checkCurrentUser()?.uid,
            Constants.getUsernameFromEmail(checkCurrentUser()?.email),
            checkCurrentUser()?.email,
            "default",
            null,
            null
        )
        checkCurrentUser()?.uid.let {
            // Create user
            db.collection("UserInfo")
                .document(it!!)
                .set(user.toHashMap())
                .addOnSuccessListener {
                    Log.d(TAG, "addUserData: success")
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "addUserData: $exception - failed")
                }
            // Create cart
            db.collection("Cart")
                .document(it).set(user.toHashMap())
                .addOnSuccessListener {
                    Log.d(TAG, "addUserData: success")
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "addUserData: $exception - failed")
                }
        }
    }

    fun initParams(context: Context) {
        _oneTapClient = Identity.getSignInClient(context)
        _signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(BuildConfig.WEB_CLIENT_ID)
                    // Show all accounts on the device.
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()
        _signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(BuildConfig.WEB_CLIENT_ID)
                    // Show all accounts on the device.
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .build()
    }

    fun initOneTapResult(result: ActivityResult, activity: Activity) {
        try {
            val credential = oneTapClient?.getSignInCredentialFromIntent(result.data)
            val idToken = credential?.googleIdToken
            when {
                idToken != null -> {
                    // Got an ID token from Google. Use it to authenticate with your backend.
                    val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                    auth.signInWithCredential(firebaseCredential)
                        .addOnCompleteListener(activity) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                val currentUserId = auth.currentUser?.uid
                                val msg = "user ${auth.currentUser?.email} signed in"
                                _hasGoogleSignIn.value = true
                                viewModelScope.launch { checkUserSignInGoogle(currentUserId) }
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithCredential: failure", task.exception)
//                                updateUI(null)
                            }
                        }
                }
                else -> {
                    // Shouldn't happen.
                    Log.d("one tap", "No ID token!")
//                    Snackbar.make(binding.root, "No ID token!", Snackbar.LENGTH_SHORT).show()
                }
            }
        } catch (e: ApiException) {
            when (e.statusCode) {
                CommonStatusCodes.CANCELED -> {
                    Log.d("one tap", "One-tap dialog was closed.")
                    // Don't re-prompt the user.
//                    Snackbar.make(binding.root, "One-tap dialog was closed.", Snackbar.LENGTH_SHORT).show()
                }
                CommonStatusCodes.NETWORK_ERROR -> {
                    Log.d("one tap", "One-tap encountered a network error.")
                    // Try again or just ignore.
//                    Snackbar.make(binding.root, "One-tap encountered a network error.", Snackbar.LENGTH_SHORT).show()
                }
                else -> {
                    Log.d("one tap", "Couldn't get credential from result." + " (${e.localizedMessage})")
//                    Snackbar.make( binding.root, "Couldn't get credential from result.\" +\n" +
//                                " (${e.localizedMessage})", Snackbar.LENGTH_SHORT ).show()
                }
            }
        }
    }

    private suspend fun checkUserSignInGoogle(currentUserId: String?) {
        val query = db.collection("UserInfo").get().await()
        if (query.documents.isNotEmpty()) {
            val listUserId = mutableListOf<String>()
            val queryToObj = query.toObjects<User>()
            for (user in queryToObj) {
                listUserId.add(user.userID!!)
            }
            if (!listUserId.contains(currentUserId)) {
                addUserData()
                Toast.makeText(context, "addUserData done", Toast.LENGTH_SHORT).show()
            } else {
                Log.d(TAG, "initOneTapResult: user existed")
            }
        }
    }

    fun resetSignInGoogle() {
        _hasGoogleSignIn.value = false
    }

    fun displaySignIn(
        activity: Activity,
        oneTapClient: SignInClient?,
        signInRequest: BeginSignInRequest?,
        signUpRequest: BeginSignInRequest?,
        oneTapResult: ActivityResultLauncher<IntentSenderRequest>
    ) {
        oneTapClient?.beginSignIn(signInRequest!!)
            ?.addOnSuccessListener(activity) { result ->
                try {
                    val ib = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    oneTapResult.launch(ib)
                } catch (e: IntentSender.SendIntentException) {
                    Log.e("btn click", "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            ?.addOnFailureListener(activity) { e ->
                // No Google Accounts found. Just continue presenting the signed-out UI.
                displaySignUp(activity, oneTapClient, signUpRequest, oneTapResult)
                Log.d("btn click", e.localizedMessage!!)
            }
    }

    private fun displaySignUp(
        activity: Activity,
        oneTapClient: SignInClient?,
        signUpRequest: BeginSignInRequest?,
        oneTapResult: ActivityResultLauncher<IntentSenderRequest>
    ) {
        oneTapClient?.beginSignIn(signUpRequest!!)
            ?.addOnSuccessListener(activity) { result ->
                try {
                    val ib = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    oneTapResult.launch(ib)
                } catch (e: IntentSender.SendIntentException) {
                    Log.e("btn click", "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            ?.addOnFailureListener(activity) { e ->
                // No Google Accounts found. Just continue presenting the signed-out UI.
                Log.d("btn click", e.localizedMessage!!)
                Toast.makeText(
                    context, "No google account found!" +
                            "\nPlease sign in your to use.", Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun onCleared() {
        super.onCleared()
        _signUp.value = null
        _login.value = null
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}