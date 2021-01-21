package com.example.searchpair;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {


    private ImageView image1, image2, image3, image4,
            image5, image6, image7, image8,
            image9, image10, image11, image12, imageCloser1;

    Animation animation1 = null;
    Animation animation2 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();



    }
    //инициализация
    private void init() {
        //анимация
        animation1 = AnimationUtils.loadAnimation(this, R.anim.anim_one);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.anim_two);
        //айдишники картинок
        image1 = findViewById(R.id.imageView1);
        image2 = findViewById(R.id.imageView2);
        image3 = findViewById(R.id.imageView3);
        image4 = findViewById(R.id.imageView4);
        image5 = findViewById(R.id.imageView5);
        image6 = findViewById(R.id.imageView6);
        image7 = findViewById(R.id.imageView7);
        image8 = findViewById(R.id.imageView8);
        image9 = findViewById(R.id.imageView9);
        image10 = findViewById(R.id.imageView10);
        image11 = findViewById(R.id.imageView11);
        image12 = findViewById(R.id.imageView12);
        imageCloser1 = findViewById(R.id.imageViewCloser1);
        onClickFirstCard();
        onClickFirstImg();
    }


    public void onClickFirstCard(){
        imageCloser1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageCloser1.startAnimation(animation1);
                animation1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        imageCloser1.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        image1.setImageResource(R.drawable.token5);
                        image1.startAnimation(animation2);
                        image1.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });
    }

    public void onClickFirstImg(){
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image1.startAnimation(animation1);
                animation1.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        imageCloser1.setVisibility(View.VISIBLE);
                        imageCloser1.startAnimation(animation2);
                        image1.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });
    }

}



