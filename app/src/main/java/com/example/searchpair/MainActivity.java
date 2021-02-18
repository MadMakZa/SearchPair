package com.example.searchpair;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends AppCompatActivity {

    ArrayList<Integer> arrayResourcesImages = new ArrayList<>(); //лист с картинками (будут перемешиваться)
    ArrayList<ImageView> arrayImageViewsButtons = new ArrayList<>(); //лист с кнопками
    ArrayList<Card> arrayCards = new ArrayList<>(); //лист с экземплярами карточек

    private ImageView imageView1, imageView2, imageView3, imageView4,
            imageView5, imageView6, imageView7, imageView8,
            imageView9, imageView10, imageView11, imageView12,
            imageView13, imageView14, imageView15, imageView16;
    Animation animation1 = null;
    Animation animation2 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }
    //инициализации блок
    private void init() {
        animation1 = AnimationUtils.loadAnimation(this, R.anim.flip_to_middle);
        animation2 = AnimationUtils.loadAnimation(this, R.anim.flip_from_middle);

        imageView1 = findViewById(R.id.idImage1);
        imageView2 = findViewById(R.id.idImage2);
        imageView3 = findViewById(R.id.idImage3);
        imageView4 = findViewById(R.id.idImage4);
        imageView5 = findViewById(R.id.idImage5);
        imageView6 = findViewById(R.id.idImage6);
        imageView7 = findViewById(R.id.idImage7);
        imageView8 = findViewById(R.id.idImage8);
        imageView9 = findViewById(R.id.idImage9);
        imageView10 = findViewById(R.id.idImage10);
        imageView11 = findViewById(R.id.idImage11);
        imageView12 = findViewById(R.id.idImage12);
        imageView13 = findViewById(R.id.idImage13);
        imageView14 = findViewById(R.id.idImage14);
        imageView15 = findViewById(R.id.idImage15);
        imageView16 = findViewById(R.id.idImage16);
    }
    //заполнить лист картинок из drawable
    private void addResourcesImagesToArray(){
        arrayResourcesImages.add(R.drawable.image1);
        arrayResourcesImages.add(R.drawable.image2);
        arrayResourcesImages.add(R.drawable.image3);
        arrayResourcesImages.add(R.drawable.image4);
        arrayResourcesImages.add(R.drawable.image5);
        arrayResourcesImages.add(R.drawable.image6);
        arrayResourcesImages.add(R.drawable.image7);
        arrayResourcesImages.add(R.drawable.image8);
        arrayResourcesImages.add(R.drawable.image1);
        arrayResourcesImages.add(R.drawable.image2);
        arrayResourcesImages.add(R.drawable.image3);
        arrayResourcesImages.add(R.drawable.image4);
        arrayResourcesImages.add(R.drawable.image5);
        arrayResourcesImages.add(R.drawable.image6);
        arrayResourcesImages.add(R.drawable.image7);
        arrayResourcesImages.add(R.drawable.image8);
    }
}









