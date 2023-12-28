package com.example.ceyda.friendlypaws;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Load the animation
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        // Apply the animation to the layout
        //findViewById(R.id.activity_splash).startAnimation(fadeIn);

        // Apply the animation to the ImageView
        ImageView gifImageView = findViewById(R.id.gifImageView);
        //gifImageView.startAnimation(fadeIn);
        Glide.with(this).asGif().load(R.drawable.r_dogloveanim).into(gifImageView);

        // Start the animation on the ImageView
        gifImageView.startAnimation(fadeIn);

        // Set a listener to go to the main activity when the animation ends
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Not needed for this example
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Start the main activity
                Intent intent = new Intent(SplashActivity.this, SignupLoginActivity.class);
                startActivity(intent);

                // Finish the splash activity so it's not in the back stack
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Not needed for this example
            }
        });
    }
}