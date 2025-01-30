package com.codeIn.myCash.utilities

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import com.codeIn.common.util.gone
import com.codeIn.common.util.visible
import com.codeIn.myCash.R

class MyAnimator {

    private var animator: ObjectAnimator? = null

    fun animateShakeX(
        view: View,
        startPosition: Float = -30f,
        endPosition: Float = 30f
    ) {

        val duration = 150L

        view.visibility = View.VISIBLE

        animator?.cancel()
        animator =
            ObjectAnimator.ofFloat(view, View.TRANSLATION_X, startPosition * 2, endPosition * 2)

        animator?.duration = duration
        animator?.repeatCount = 5
        animator?.repeatMode = ValueAnimator.REVERSE
        animator?.interpolator = AccelerateDecelerateInterpolator()

        animator?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {
            }

            override fun onAnimationEnd(p0: Animator) {
                val anime = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, view.translationX, 0f)
                anime.duration = duration
                anime.interpolator = AccelerateDecelerateInterpolator()
                anime.start()
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationRepeat(p0: Animator) {
            }

        })

        animator?.start()
    }


    fun animateTranslationYHide(view: View, direction: Int = 1, duration: Long = 500) {
        animator?.cancel()
        if (view.visibility == View.VISIBLE) {
            val startPosition = 0f
            val endPosition = direction * 300f
            animator =
                ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, startPosition, endPosition)
            animator?.duration = duration
            animator?.interpolator = AccelerateInterpolator()
            animator?.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {
                }

                override fun onAnimationEnd(p0: Animator) {
                    view.gone()
                }

                override fun onAnimationCancel(p0: Animator) {
                    view.gone()
                }

                override fun onAnimationRepeat(p0: Animator) {
                }

            })
            animator?.start()
        }
    }

    fun animateTranslationYShow(view: View, direction: Int = 1, duration: Long = 500) {
        animator?.cancel()

        if (view.visibility != View.VISIBLE) {
            val startPosition = direction * 300f
            val endPosition = 0f
            animator =
                ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, startPosition, endPosition)
            animator?.duration = duration
            animator?.interpolator = DecelerateInterpolator()
            animator?.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {
                    view.visible()
                }

                override fun onAnimationEnd(p0: Animator) {
                }

                override fun onAnimationCancel(p0: Animator) {
                    view.visible()
                    view.translationY = 0f
                }

                override fun onAnimationRepeat(p0: Animator) {
                }

            })
            animator?.start()
        }

    }

    fun animateImageTransition(imageView: ImageView, isToggled: Boolean) {

        val animation = AnimationUtils.loadAnimation(imageView.context, R.anim.toggled_in)
        animation.duration = 250

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {   }

            override fun onAnimationEnd(p0: Animation?) {
                if (isToggled) {
                    imageView.setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
                } else {
                    imageView.setImageResource(R.drawable.ic_menu)
                }

                val animation2 = AnimationUtils.loadAnimation(imageView.context, R.anim.toggled_out)
                animation2.duration = 250
                imageView.startAnimation(animation2)

            }

            override fun onAnimationRepeat(p0: Animation?) {

            }

        })
        imageView.startAnimation(animation)
    }

}