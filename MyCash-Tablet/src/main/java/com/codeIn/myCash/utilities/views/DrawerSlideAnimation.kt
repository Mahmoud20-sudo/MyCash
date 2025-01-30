package com.codeIn.myCash.utilities.views

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.drawerlayout.widget.DrawerLayout
import com.codeIn.myCash.R
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible

class DrawerSlideAnimation(private val activity: AppCompatActivity) {

    /**
     *  function to enable the drawer slide animation
     *  it takes the main layout, the drawer layout, the block view and the toolbar
     *  Block view is the view that will be visible when the drawer is open to bock the user from clicking on the main layout
     */
    fun enableDrawerSlideAnimation(
        mainLayout: CardView,
        drawerLayout: DrawerLayout,
        blockView: View?,
        toolbar: Toolbar?
    ) {
        var actionBarDrawerToggle: ActionBarDrawerToggle? = null

        blockView?.gone()

        mainLayout.radius = 0f

        // get the layout direction of the activity to reverse the animation if the layout direction is RTL
        val layoutDirection =
            if (activity.resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_LTR) 1 else -1

        actionBarDrawerToggle = object : ActionBarDrawerToggle(
            activity,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
            private val translateStart = -200
            private val scaleFactorX = 8f
            private val scaleFactorY = 8f
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)

                //get the width of the drawer layout and multiply it by the slide offset to get the translationX value
                val slideX = (drawerView.width + translateStart) * slideOffset

                //set the translationX and the scale of the main layout
                mainLayout.translationX = layoutDirection * slideX

                //set the scale of the main layout
                mainLayout.scaleX = 1 - slideOffset / scaleFactorX
                mainLayout.scaleY = 1 - slideOffset / scaleFactorY

                //set the radius of the main layout
                mainLayout.radius = 50f

                // If you want a fading effect uncomment the next line:
                // mainLayout.alpha = 0.25f + (1 - abs(slideOffset))

            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                mainLayout.radius = 0f

                // enable the main layout when the drawer is closed
                blockView?.gone()
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                mainLayout.radius = 50f

                // disable the main layout when the drawer is open
                blockView?.visible()
            }

            override fun onDrawerStateChanged(newState: Int) {
                super.onDrawerStateChanged(newState)

                // hide the keyboard when the drawer is open
                (activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    activity.currentFocus?.windowToken,
                    0
                )

                if (drawerLayout.isOpen) {
                    mainLayout.radius = 50f
                    (actionBarDrawerToggle as ActionBarDrawerToggle).syncState()
                } else {
                    mainLayout.radius = 0f
                }

            }
        }

//        if (activity.supportActionBar != null) {
//            activity.supportActionBar!!.setHomeButtonEnabled(true)
//            activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//        }

        drawerLayout.setScrimColor(Color.TRANSPARENT)
        drawerLayout.drawerElevation = 0f
        drawerLayout.addDrawerListener(actionBarDrawerToggle as ActionBarDrawerToggle)
        (actionBarDrawerToggle as ActionBarDrawerToggle).isDrawerSlideAnimationEnabled =
            true //disable "hamburger to arrow" drawable
        (actionBarDrawerToggle as ActionBarDrawerToggle).isDrawerIndicatorEnabled =
            true //disable "hamburger to arrow" drawable
        (actionBarDrawerToggle as ActionBarDrawerToggle).syncState()


        blockView?.setOnClickListener {
            if (drawerLayout.isOpen)
                drawerLayout.close()
        }

    }
}