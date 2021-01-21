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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    int[] array = new int[16];
    List<ImageView> listImageView = new ArrayList<>();
    Map<Integer, ImageView> map1 = new HashMap<>();

    private ImageView image1, image2, image3, image4,
            image5, image6, image7, image8,
            image9, image10, image11, image12;

    Animation animation1 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        imagesOnClickListener();


    }
    //инициализация
    private void init(){
        //анимация
        animation1 = AnimationUtils.loadAnimation(this, R.anim.anim_one);
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
        //присвоить ресурсы вьюшкам
        image1.setImageResource(R.drawable.token5);
        image2.setImageResource(R.drawable.token5d);
        image3.setImageResource(R.drawable.token10);
        image4.setImageResource(R.drawable.token10d);
        image5.setImageResource(R.drawable.token25);
        image6.setImageResource(R.drawable.token25d);
        image7.setImageResource(R.drawable.token50);
        image8.setImageResource(R.drawable.token50d);
        image9.setImageResource(R.drawable.token100);
        image10.setImageResource(R.drawable.token100d);
        image11.setImageResource(R.drawable.token250);
        image12.setImageResource(R.drawable.token250d);
        //заполнение аррайлиста
        listImageView.add(image1);
        listImageView.add(image2);
        listImageView.add(image3);
        listImageView.add(image4);
        listImageView.add(image5);
        listImageView.add(image6);
        listImageView.add(image7);
        listImageView.add(image8);
        listImageView.add(image9);
        listImageView.add(image10);
        listImageView.add(image11);
        listImageView.add(image12);
        //перемешать
        Collections.shuffle(listImageView);
        //добавить картинки в мапу
        map1.put(1, listImageView.get(0));
        map1.put(2, listImageView.get(1));
        map1.put(3, listImageView.get(2));
        map1.put(4, listImageView.get(3));
        map1.put(5, listImageView.get(4));
        map1.put(6, listImageView.get(5));
        map1.put(7, listImageView.get(6));
        map1.put(8, listImageView.get(7));
        map1.put(9, listImageView.get(8));
        map1.put(10, listImageView.get(9));
        map1.put(11, listImageView.get(10));
        map1.put(12, listImageView.get(11));






    }
    //random res
    private void randomRes() {
        int i = (int) (3 * Math.random() + 1);
        String RandomS = "token" + i;
        int resID = getResources().getIdentifier(RandomS, "drawable", getApplicationContext().getPackageName());
        image12.setImageResource(resID);
    }



    //установить слушатель нажатий для всех картинок
    public void imagesOnClickListener(){
        // Получаем набор элементов
        Set<Map.Entry<Integer, ImageView>> set = map1.entrySet();
        //Отобразить набор
        for (final Map.Entry<Integer, ImageView> pair : set) {
            Log.d("Map",(pair.getKey() + ": "));
            Log.d("Map",(pair.getValue() + ": "));
            pair.getValue().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    map1.get(pair.getKey()).startAnimation(animation1);
                    animation1.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
//                            map1.get(pair.getKey()).setImageResource(R.drawable.);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            map1.get(pair.getKey()).setImageResource(R.drawable.ic_launcher_foreground);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
            });
        }

    }




}