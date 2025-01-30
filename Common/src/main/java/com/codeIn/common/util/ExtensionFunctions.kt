package com.codeIn.common.util

import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import kotlin.math.roundToInt


fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

fun Double.toTwoDigits(): Double = (this * 100.0).roundToInt() / 100.0

fun Double.toDecimalFormat(): String = DecimalFormat("###.#").format(this)


fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}


fun View.enable() {
    this.isEnabled = true
}

fun View.disable() {
    this.isEnabled = false
}


fun ViewModel.launchIO(block: suspend () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
    block()
}
fun ViewModel.asyncIO(block: suspend () -> Unit) = viewModelScope.async(Dispatchers.IO) {
    block()
}

fun ViewModel.launchMain(block: suspend () -> Unit) = viewModelScope.launch(Dispatchers.Main) {
    block()
}

fun ViewModel.launchDefault(block: suspend () -> Unit) =
    viewModelScope.launch(Dispatchers.Default) {
        block()
    }

fun ViewModel.launch(block: suspend () -> Unit) = viewModelScope.launch {
    block()
}


/**
 * Collects the values of the [StateFlow] and calls the [block] every time a new value is emitted.
 *
 * @param scope The [CoroutineScope] to launch the collection coroutine in.
 * @param owner The [LifecycleOwner] to repeat the collection until the lifecycle is destroyed.
 * @param block The block to execute every time a new value is emitted.
 * @param T The type of the [StateFlow].
 * @return Unit
 */
fun <T> StateFlow<T>.collectOnLifecycle(
    scope: CoroutineScope,
    owner: LifecycleOwner,
    block: suspend (T) -> Unit
) {
    // Start a coroutine in the lifecycle scope
    scope.launch {
        // repeatOnLifecycle launches the block in a new coroutine every time the
        // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
        owner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            // Trigger the flow and start listening for values.
            // Note that this happens when lifecycle is STARTED and stops
            // collecting when the lifecycle is STOPPED
            this@collectOnLifecycle.collect {
                block(it)
            }
        }
    }
}

fun SearchView.getQueryTextChangeStateFlow(): StateFlow<String> {
    val query = MutableStateFlow("")
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
            query.value = newText
            return true
        }
    })
    return query
}
infix fun <T> T.isSame(item: T): Boolean = this == item

fun RecyclerView.resetScrollState() =try {
    Handler(Looper.myLooper()!!).postDelayed({
        this.scrollToPosition(0)
    }, 200)
} catch (e: Exception) {
    e.printStackTrace()
}


fun <T> ArrayList<T>?.appendList(list: ArrayList<T>?): ArrayList<T>{
    val newList = ArrayList(this ?: arrayListOf())
    newList.addAll(list ?: arrayListOf())
    return newList
}
fun Context.isTablet(): Boolean {
    return ((resources.configuration.screenLayout
            and Configuration.SCREENLAYOUT_SIZE_MASK)
            >= Configuration.SCREENLAYOUT_SIZE_LARGE)
}

fun Context.isOnline(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            return true
        }
    }
    return false
}