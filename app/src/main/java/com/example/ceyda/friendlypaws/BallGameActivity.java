package com.example.ceyda.friendlypaws;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.ceyda.friendlypaws.model.Userr;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BallGameActivity extends AppCompatActivity {

    private BouncingBallView ballView;

    private boolean pointsClaimed = false;
    private int currentPoints;

    private int dailyPoint = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_ball_game);

        ballView = new BouncingBallView(this);   //, pointsTextView
        setContentView(ballView);

        fetchUserInformation();
    }

    class BouncingBallView extends View {

        private Bitmap backgroundImage;  // Bitmap for the background image

        private Paint textPaint;
        private String displayText = "Points: 0";

        private float ballX, ballY;
        private float dx = 5, dy = 5; // Initial speed (both positive to move away from corner)
        private int ballRadius = 75; // Adjusted radius for smoother movement
        private Paint ballPaint;

        private int point = 0;

        // Star variables
        private List<Star> stars;

        public BouncingBallView(Context context) {  //, TextView pointsTextView
            super(context);
            //this.pointsTextView = pointsTextView;

            ballPaint = new Paint();
            ballPaint.setColor(Color.argb(255,248,192,192));

            stars = new ArrayList<>();
            // Define initial star positions (adjust as needed)
            stars.add(new Star(400, 600));
            // Add more stars as needed

            // Load the background image from resources
            backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.r_view_bg); // Replace with your image resource

            // Initialize Paint for text
            textPaint = new Paint();
            textPaint.setColor(Color.parseColor("#CFB0B0"));
            textPaint.setTextSize(60);  // Adjust text size as needed

            // Load a bold typeface and set it to the Paint object
            Typeface boldTypeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
            textPaint.setTypeface(boldTypeface);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            // Set your desired background color or draw an image
            //canvas.drawColor(Color.WHITE);

            // Draw background image
            if (backgroundImage != null) {
                // Calculate the scaling factors to fit the image to the canvas
                float scaleX = (float) getWidth() / backgroundImage.getWidth();
                float scaleY = (float) getHeight() / backgroundImage.getHeight();

                // Create a matrix for the scaling operation
                Matrix matrix = new Matrix();
                matrix.postScale(scaleX, scaleY);

                // Apply the scaling operation to the background image
                Bitmap scaledBackground = Bitmap.createBitmap(backgroundImage, 0, 0, backgroundImage.getWidth(), backgroundImage.getHeight(), matrix, true);

                // Draw the scaled background image
                canvas.drawBitmap(scaledBackground, 0, 0, null);

                //canvas.drawBitmap(backgroundImage, 0, 0, null);

            } else {
                // Draw a solid color if no background image is set
                canvas.drawColor(Color.WHITE);
            }

            // Create a copy of the stars list to iterate over
            List<Star> starsCopy = new ArrayList<>(stars);

            // Draw stars
            for (Star star : starsCopy) {
                if (!star.isCollected()) {
                    canvas.drawCircle(star.getX(), star.getY(), star.getRadius(), star.getPaint());
                    //drawStar(canvas, getWidth() / 2, getHeight() / 2, 100);
                } else {
                    stars.remove(star); // Remove collected stars from the original list
                    spawnStar(); // Spawn a new star at a different position
                }
            }

            // Draw the text
            canvas.drawText(displayText, 50, 50, textPaint);

            // Draw the ball
            canvas.drawCircle(ballX, ballY, ballRadius, ballPaint);

            // Move the ball
            ballX += dx;
            ballY += dy;

            // Bounce off the walls with corner checks
            if (ballX + ballRadius > getWidth()) {
                dx = -dx;
                ballX = getWidth() - ballRadius; // Ensure it's pushed out of the corner
            } else if (ballX - ballRadius < 0) {
                dx = Math.abs(dx); // Make sure it moves rightwards
                ballX = ballRadius; // Ensure it's pushed out of the corner
            }
            if (ballY + ballRadius > getHeight()) {
                dy = -dy;
                ballY = getHeight() - ballRadius; // Ensure it's pushed out of the corner
            } else if (ballY - ballRadius < 0) {
                dy = Math.abs(dy); // Make sure it moves downwards
                ballY = ballRadius; // Ensure it's pushed out of the corner
            }

            // Check for collision with stars
            for (Star star : starsCopy) {
                if (!star.isCollected() && isCollision(ballX, ballY, ballRadius, star.getX(), star.getY(), star.getRadius())) {
                    // Handle star collection
                    star.setCollected(true);
                    point++;
                    updateDisplayText();
                    if (point < 10) {
                        // Spawn a new star at a different position
                        spawnStar();
                    }
                    else {  // point == 10
                        claimDailyPoints();
                    }
                }
            }

            invalidate(); // Request a redraw
        }


        private void claimDailyPoints() {
            // Check if the points can be claimed today
            if (!isPointsClaimableToday()) {
                Toast.makeText(BallGameActivity.this, "Points already claimed today!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Claim points
            //int dailyPoint = 10;
            //dailyPoints++;
            pointsClaimed = true;

            //add points to the user (database)
            updateUserInfo();

            // Update UI
            //pointsTextView.setText("Points: " + dailyPoints);
            Toast.makeText(BallGameActivity.this, "Daily Points Claimed!", Toast.LENGTH_SHORT).show();
        }

        private boolean isPointsClaimableToday() {
            // Check if points can be claimed only once per day
            if (!pointsClaimed) {
                return true;
            }

            // Check if it's a new day
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String lastClaimedDate = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                    .getString("lastClaimedDate", "");

            String currentDate = dateFormat.format(new Date());

            if (!currentDate.equals(lastClaimedDate)) {
                // It's a new day, reset points claim status
                pointsClaimed = false;

                // Save the current date
                getSharedPreferences("MyPrefs", MODE_PRIVATE)
                        .edit()
                        .putString("lastClaimedDate", currentDate)
                        .apply();

                return true;
            }

            return false;
        }


        // Modify the method to update the display text
        private void updateDisplayText() {
            displayText = "Points: " + point;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // Change direction based on touch input
                float touchX = event.getX();
                float touchY = event.getY();
                dx = (touchX - ballX) / 50;
                dy = (touchY - ballY) / 50;
            }
            return true;
        }

        private boolean isCollision(float x1, float y1, float radius1, float x2, float y2, float radius2) {
            float dx = x1 - x2;
            float dy = y1 - y2;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);
            return distance < radius1 + radius2;
        }

        private void spawnStar() {
            // Set a random position for the star (adjust as needed)
            float starX = new Random().nextInt(getWidth());
            float starY = new Random().nextInt(getHeight());

            // Remove existing stars
            stars.clear();

            // Add the new star to the list
            stars.add(new Star(starX, starY));
        }
    }

    // Star class to represent stars
    class Star {
        private float x, y;
        private int radius = 30;
        private Paint paint;
        private boolean collected = false;

        public Star(float x, float y) {
            this.x = x;
            this.y = y;
            paint = new Paint();
            paint.setColor(Color.YELLOW);
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public int getRadius() {
            return radius;
        }

        public Paint getPaint() {
            return paint;
        }

        public boolean isCollected() {
            return collected;
        }

        public void setCollected(boolean collected) {
            this.collected = collected;
        }
    }

    private void updateUserInfo() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();

            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users").child(userId);

            // Use a Map to update specific fields
            Map<String, Object> updates = new HashMap<>();
            updates.put("point", (currentPoints+dailyPoint));


            // Update the user information in the database
            userReference.updateChildren(updates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Update successful
                            Toast.makeText(BallGameActivity.this, "User information updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failures
                            Toast.makeText(BallGameActivity.this, "Failed to update user information: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void fetchUserInformation() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();

            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users").child(userId);

            // get currentpoints
            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("FirebaseData", "onDataChange triggered");
                    if (dataSnapshot.exists()) {
                        Log.d("FirebaseData", "Data exists");
                        //User data found
                        Userr user = dataSnapshot.getValue(Userr.class);

                        // Now 'user' contains the information you stored in the database
                        currentPoints = user.getPoint();

                    } else {
                        // User data not found in the database, handle accordingly
                        Log.d("FirebaseData", "Data does not exist");
                        Toast.makeText(BallGameActivity.this, "User data not found.", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors here
                    Toast.makeText(BallGameActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed(); // This line is important to ensure the default behavior
        // Your custom behavior, if any
        Intent intent = new Intent(BallGameActivity.this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }
}