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

    ArrayList<Integer> arrayValues = new ArrayList<>();
    ArrayList<ImageView> arrayImages = new ArrayList<>();

    private ImageView image1, image2, image3, image4,
            image5, image6, image7, image8,
            image9, image10, image11, image12;

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

        fillInTheList(arrayValues);
        fillInTheImageList();


    }
    //заполнить лист со значениями
    private void fillInTheList(ArrayList<Integer> arr){
        //заполнить лист значениями
        int i = 0;
        for (i = 0; i < 12 ; i++){
            arr.add(i);
        }
        //перемешать
        Collections.shuffle(arrayValues);
    }

    private void fillInTheImageList(){
        arrayImages.add(image1);
        arrayImages.add(image2);
        arrayImages.add(image3);
        arrayImages.add(image4);
        arrayImages.add(image5);
        arrayImages.add(image6);
        arrayImages.add(image7);
        arrayImages.add(image8);
        arrayImages.add(image9);
        arrayImages.add(image10);
        arrayImages.add(image11);
        arrayImages.add(image12);
        //определить слушатель нажатий для каждой имаджвью
        for (ImageView img : arrayImages){
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //проверить какая кнопка по Таг нажата была
                    //запуск метода открыть ячейку
                    openTheImage();
                }
            });
        }
    }

    //метод открыть ячейку
    private void openTheImage(){
        switch (arrayValues.get(0)){
            case 1:
                image1.setImageResource(R.drawable.token5);
                break;
            case 2:
                image1.setImageResource(R.drawable.token10);
                break;
            case 3:
                image1.setImageResource(R.drawable.token25);
                break;
            case 4:
                image1.setImageResource(R.drawable.token50);
                break;
            case 5:
                image1.setImageResource(R.drawable.token100);
                break;
            case 6:
                image1.setImageResource(R.drawable.token250);
                break;
            case 7:
                image1.setImageResource(R.drawable.token5);
                break;
            case 8:
                image1.setImageResource(R.drawable.token10);
                break;
            case 9:
                image1.setImageResource(R.drawable.token25);
                break;
            case 10:
                image1.setImageResource(R.drawable.token50);
                break;
            case 11:
                image1.setImageResource(R.drawable.token100);
                break;
            case 12:
                image1.setImageResource(R.drawable.token250);
                break;
        }
    }





}



