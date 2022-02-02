package com.example.androidschool.andersencoursework.ui.core

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import java.lang.IllegalStateException
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentBindingDelegate<T : ViewBinding>(
    val fragment: Fragment,
    val bindingFactory: (View) -> T
): ReadOnlyProperty<Fragment, T> {

    private var binding: T? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        val binding = binding
        if (binding != null) return binding

        val lifecycle = fragment.viewLifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            throw IllegalStateException("Should not initialize binding when fragment views are destroyed")
        }

        return bindingFactory.invoke(thisRef.requireView())
            .also { this@FragmentBindingDelegate.binding = it }
    }
}

fun <T: ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> T) =
    FragmentBindingDelegate(this, viewBindingFactory)