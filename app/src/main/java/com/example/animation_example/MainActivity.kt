package com.example.animation_example

import android.animation.*
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView

class MainActivity : AppCompatActivity() {

    private lateinit var container: FrameLayout
    private lateinit var image: ImageView
    lateinit var btnRotate: Button
    lateinit var btnFade: Button
    lateinit var btnTranslate: Button
    lateinit var btnScale: Button
    lateinit var btnShower: Button
    lateinit var btnColorize: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        image = findViewById(R.id.img)
        btnColorize = findViewById(R.id.btnColor)
        btnFade = findViewById(R.id.btnFade)
        btnRotate = findViewById(R.id.btnRptate)
        btnScale = findViewById(R.id.btnScale)
        btnTranslate = findViewById(R.id.btnTranslate)
        btnShower = findViewById(R.id.btnShower)

        container = findViewById(R.id.container);

        btnRotate.setOnClickListener { rotater() }
        btnTranslate.setOnClickListener { translator() }
        btnScale.setOnClickListener { scaler() }
        btnFade.setOnClickListener { fader() }

        btnColorize.setOnClickListener { colorizer() }

        btnShower.setOnClickListener { shower() }
        container.setOnClickListener { shower() }

        container.setOnLongClickListener {
            startActivity(Intent(this, DragExampleActivity::class.java))
            true
        }

    }

    private fun translator() {
        val animator = ObjectAnimator.ofFloat(image, View.TRANSLATION_Y, -200f)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.start()
    }

    private fun rotater() {
        val animator = ObjectAnimator.ofFloat(image, View.ROTATION, -360f, 0f)
        animator.duration = 1500 // 1.5 sec
        animator.startDelay = 500 // .5 sec
        animator.start()
    }

    private fun scaler() {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 2f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 2f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(image, scaleX, scaleY)
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.repeatCount = 1
        animator.start()
    }

    private fun fader() {
        val animator = ObjectAnimator.ofFloat(image, View.ALPHA, 0f)
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.repeatCount = 1
        animator.start()
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun colorizer() {
        var animator =
            ObjectAnimator.ofArgb(image.parent, "backgroundColor", Color.BLACK, Color.RED)
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.duration = 3000 // 3 sec
        animator.repeatCount = 1
        animator.start()
    }

    private fun shower() {
        val container = image.parent as ViewGroup
        val containerW = container.width
        val containerH = container.height
        var imageW: Float = image.width.toFloat()
        var imageH: Float = image.height.toFloat()

        // Create the new image (an ImageView holding our drawable) and add it to the container
        val newImage = AppCompatImageView(this)
        newImage.setImageResource(R.drawable.wolf_school)
        newImage.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        container.addView(newImage)

        // Scale the view randomly between 10-160% of its default size
        newImage.scaleX = Math.random().toFloat() * 1.5f + .1f
        newImage.scaleY = newImage.scaleX
        imageW *= newImage.scaleX
        imageH *= newImage.scaleY

        // Position the view at a random place between the left and right edges of the container
        newImage.translationX = Math.random().toFloat() * containerW - imageW / 2

        // Create an animator that moves the view from a starting position right about the container
        // to an ending position right below the container. Set an accelerate interpolator to give
        // it a gravity/falling feel
        val mover =
            ObjectAnimator.ofFloat(newImage, View.TRANSLATION_Y, -imageH, containerH + imageH)
        mover.interpolator = AccelerateInterpolator(1f)

        // Create an animator to rotateButton the view around its center up to three times
        val rotator = ObjectAnimator.ofFloat(
            newImage, View.ROTATION,
            (Math.random() * 1080).toFloat()
        )
        rotator.interpolator = LinearInterpolator()

        // Use an AnimatorSet to play the falling and rotating animators in parallel for a duration
        // of a half-second to two seconds
        val set = AnimatorSet()
        set.playTogether(mover, rotator)
        set.duration = (Math.random() * 1500 + 500).toLong()

        // When the animation is done, remove the created view from the container
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                container.removeView(newImage)
            }
        })

        // Start the animation
        set.start()
    }
}