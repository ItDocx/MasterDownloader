package com.brainmedia.masterdownloader.Dialogues;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.brainmedia.masterdownloader.R;

public class RateUsDialoge extends Dialog {

    private float userRate = 0;

    public RateUsDialoge(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_rating);

        final Button btn_later, btn_rateNow;
        final ImageView emojies;
        final RatingBar ratingBar;

        btn_later = findViewById(R.id.btn_later);
        btn_rateNow = findViewById(R.id.btn_rateNow);
        emojies = findViewById(R.id.image_rating);
        ratingBar = findViewById(R.id.rating_bar);

        btn_rateNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "Rate Now", Toast.LENGTH_SHORT).show();

            }
        });
        btn_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating<=1){
                    emojies.setImageResource(R.drawable.rating1);
                }
                else  if (rating<=2){

                    emojies.setImageResource(R.drawable.rating2);

                }
                else if (rating<=3){
                    emojies.setImageResource(R.drawable.rating3);
                }
                else if (rating<=4){
                    emojies.setImageResource(R.drawable.rating4);
                }
                else if (rating<=5){
                    emojies.setImageResource(R.drawable.rating5);
                }

                animateImage(emojies);
                userRate = rating;
            }
        });
    }

    private void animateImage(ImageView image){

        ScaleAnimation scaleAnimation = new ScaleAnimation(0,1f,0,1f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(200);
        image.startAnimation(scaleAnimation);

    }

}
