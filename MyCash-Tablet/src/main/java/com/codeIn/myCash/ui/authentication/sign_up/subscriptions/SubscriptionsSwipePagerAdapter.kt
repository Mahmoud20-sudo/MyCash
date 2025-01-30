package com.codeIn.myCash.ui.authentication.sign_up.subscriptions

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.gson.Gson
import com.codeIn.myCash.features.user.data.settings.remote.response.subscription.Package
/**
 * Swipe pager adapter for the subscriptions fragment to show the subscriptions in a swipe-able view.
 * @param fragment [Fragment] the fragment that will hold the view pager. it must implement the [Communicator] interface. to be able to communicate with it.
 * otherwise it will throw a [ClassCastException] exception. Catch It Or Crash :D. (just kidding, it will crash anyway)
 * @see SubscriptionsFragment
 */
class SubscriptionsSwipePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private var packages: List<Package> = ArrayList()

    // make sure that the fragment implements the Communicator interface to be able to communicate with it.
    init {
        if (fragment !is Communicator) {
            throw ClassCastException("Calling fragment must implement SubscriptionsSwipePagerAdapter.Communicator interface")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(packages: ArrayList<Package>?) {
        this.packages = packages!!
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return packages.size
    }

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        val fragment = SubscriptionItemFragment()
        fragment.arguments = Bundle().apply {
            putString(SubscriptionItemFragment.PACKAGE, Gson().toJson(packages[position]))
        }
        return fragment
    }

    interface Communicator {

        var showImage: Boolean
        fun startPackage(newPackage: Package)
    }
}