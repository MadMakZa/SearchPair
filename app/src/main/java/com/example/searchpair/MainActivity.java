package com.example.searchpair;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    int[] array = new int[16];
    Map<Integer, ImageView> map1 = new HashMap<>();

    private ImageView image1, image2, image3, image4;
    Animation animation1 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        generateRandom();
        print();
        imagesOnClickListener();


    }
    //инициализация
    private void init(){
        //в начале присвоить айдишники
        animation1 = AnimationUtils.loadAnimation(this, R.anim.anim_one);
        image1 = findViewById(R.id.imageView1);
        image2 = findViewById(R.id.imageView2);
        image3 = findViewById(R.id.imageView3);
        image4 = findViewById(R.id.imageView4);
        map1.put(1, image1);
        map1.put(2, image2);
        map1.put(3, image3);
        map1.put(4, image4);


    }
    //рандом
    private void generateRandom(){
        for (int i = 0; i < array.length; i++){
            array[i] = 1 + (int)(Math.random()*16);
        }
    }
    //принт логов
    private void print(){
        for (int i = 0; i < array.length; i++){
            Log.d("Log", "" +array[i]);
        }
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
                    map1.get(pair.getKey()).setImageResource(R.drawable.ic_launcher_foreground);
                    map1.get(pair.getKey()).startAnimation(animation1);
                    animation1.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            map1.get(pair.getKey()).setImageResource(R.drawable.ic_launcher_background);
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
    private void animationListener(){
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                image1.setImageResource(R.drawable.ic_launcher_background);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


}