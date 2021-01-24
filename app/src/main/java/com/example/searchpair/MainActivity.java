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
    private void fillInTheList(ArrayList<Integer> arr) {
        //заполнить лист значениями
        int index = 0;
        for (int i = 0; i < 12; i++) {
            index++;
            arr.add(index);
        }
        //перемешать
        Collections.shuffle(arrayValues);
    }

    private void fillInTheImageList() {
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
        for (final ImageView img : arrayImages) {
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //проверить какая кнопка по Таг нажата была
                    //запуск метода открыть ячейку
                    switch (arrayImages.indexOf(img)) {
                        case 0:
                            //если индекс перемешанного массива равен 1, то красим кнопку в 5ку
                            if (arrayValues.get(0) == 1)
                                img.setImageResource(R.drawable.token5);
                            if (arrayValues.get(0) == 2)
                                img.setImageResource(R.drawable.token10);
                            if (arrayValues.get(0) == 3)
                                img.setImageResource(R.drawable.token25);
                            if (arrayValues.get(0) == 4)
                                img.setImageResource(R.drawable.token50);
                            if (arrayValues.get(0) == 5)
                                img.setImageResource(R.drawable.token100);
                            if (arrayValues.get(0) == 6)
                                img.setImageResource(R.drawable.token250);
                            if (arrayValues.get(0) == 7)
                                img.setImageResource(R.drawable.token5);
                            if (arrayValues.get(0) == 8)
                                img.setImageResource(R.drawable.token10);
                            if (arrayValues.get(0) == 9)
                                img.setImageResource(R.drawable.token25);
                            if (arrayValues.get(0) == 10)
                                img.setImageResource(R.drawable.token50);
                            if (arrayValues.get(0) == 11)
                                img.setImageResource(R.drawable.token100);
                            if (arrayValues.get(0) == 12)
                                img.setImageResource(R.drawable.token250);
                            break;
                        case 1:
                            switch (arrayValues.get(1)){
                                case 1:
                                    img.setImageResource(R.drawable.token5);
                                    break;
                                case 2:
                                    img.setImageResource(R.drawable.token10);
                                    break;
                                case 3:
                                    img.setImageResource(R.drawable.token25);
                                    break;
                                case 4:
                                    img.setImageResource(R.drawable.token50);
                                    break;
                                case 5:
                                    img.setImageResource(R.drawable.token100);
                                    break;
                                case 6:
                                    img.setImageResource(R.drawable.token250);
                                    break;
                                case 7:
                                    img.setImageResource(R.drawable.token5);
                                    break;
                                case 8:
                                    img.setImageResource(R.drawable.token10);
                                    break;
                                case 9:
                                    img.setImageResource(R.drawable.token25);
                                    break;
                                case 10:
                                    img.setImageResource(R.drawable.token50);
                                    break;
                                case 11:
                                    img.setImageResource(R.drawable.token100);
                                    break;
                                case 12:
                                    img.setImageResource(R.drawable.token250);
                                    break;
                            }
                            break;

                        case 2:
                            switch (arrayValues.get(2)) {
                                case 1:
                                    img.setImageResource(R.drawable.token5);
                                    break;
                                case 2:
                                    img.setImageResource(R.drawable.token10);
                                    break;
                                case 3:
                                    img.setImageResource(R.drawable.token25);
                                    break;
                                case 4:
                                    img.setImageResource(R.drawable.token50);
                                    break;
                                case 5:
                                    img.setImageResource(R.drawable.token100);
                                    break;
                                case 6:
                                    img.setImageResource(R.drawable.token250);
                                    break;
                                case 7:
                                    img.setImageResource(R.drawable.token5);
                                    break;
                                case 8:
                                    img.setImageResource(R.drawable.token10);
                                    break;
                                case 9:
                                    img.setImageResource(R.drawable.token25);
                                    break;
                                case 10:
                                    img.setImageResource(R.drawable.token50);
                                    break;
                                case 11:
                                    img.setImageResource(R.drawable.token100);
                                    break;
                                case 12:
                                    img.setImageResource(R.drawable.token250);
                                    break;
                            }
                            break;
                        case 3:
                            switch (arrayValues.get(3)) {
                                case 1:
                                    img.setImageResource(R.drawable.token5);
                                    break;
                                case 2:
                                    img.setImageResource(R.drawable.token10);
                                    break;
                                case 3:
                                    img.setImageResource(R.drawable.token25);
                                    break;
                                case 4:
                                    img.setImageResource(R.drawable.token50);
                                    break;
                                case 5:
                                    img.setImageResource(R.drawable.token100);
                                    break;
                                case 6:
                                    img.setImageResource(R.drawable.token250);
                                    break;
                                case 7:
                                    img.setImageResource(R.drawable.token5);
                                    break;
                                case 8:
                                    img.setImageResource(R.drawable.token10);
                                    break;
                                case 9:
                                    img.setImageResource(R.drawable.token25);
                                    break;
                                case 10:
                                    img.setImageResource(R.drawable.token50);
                                    break;
                                case 11:
                                    img.setImageResource(R.drawable.token100);
                                    break;
                                case 12:
                                    img.setImageResource(R.drawable.token250);
                                    break;
                            }
                            break;

                        case 4:
                            switch (arrayValues.get(4)) {
                                case 1:
                                    img.setImageResource(R.drawable.token5);
                                    break;
                                case 2:
                                    img.setImageResource(R.drawable.token10);
                                    break;
                                case 3:
                                    img.setImageResource(R.drawable.token25);
                                    break;
                                case 4:
                                    img.setImageResource(R.drawable.token50);
                                    break;
                                case 5:
                                    img.setImageResource(R.drawable.token100);
                                    break;
                                case 6:
                                    img.setImageResource(R.drawable.token250);
                                    break;
                                case 7:
                                    img.setImageResource(R.drawable.token5);
                                    break;
                                case 8:
                                    img.setImageResource(R.drawable.token10);
                                    break;
                                case 9:
                                    img.setImageResource(R.drawable.token25);
                                    break;
                                case 10:
                                    img.setImageResource(R.drawable.token50);
                                    break;
                                case 11:
                                    img.setImageResource(R.drawable.token100);
                                    break;
                                case 12:
                                    img.setImageResource(R.drawable.token250);
                                    break;
                            }
                            break;
                        case 5:
                            switch (arrayValues.get(5)) {
                                case 1:
                                    img.setImageResource(R.drawable.token5);
                                    break;
                                case 2:
                                    img.setImageResource(R.drawable.token10);
                                    break;
                                case 3:
                                    img.setImageResource(R.drawable.token25);
                                    break;
                                case 4:
                                    img.setImageResource(R.drawable.token50);
                                    break;
                                case 5:
                                    img.setImageResource(R.drawable.token100);
                                    break;
                                case 6:
                                    img.setImageResource(R.drawable.token250);
                                    break;
                                case 7:
                                    img.setImageResource(R.drawable.token5);
                                    break;
                                case 8:
                                    img.setImageResource(R.drawable.token10);
                                    break;
                                case 9:
                                    img.setImageResource(R.drawable.token25);
                                    break;
                                case 10:
                                    img.setImageResource(R.drawable.token50);
                                    break;
                                case 11:
                                    img.setImageResource(R.drawable.token100);
                                    break;
                                case 12:
                                    img.setImageResource(R.drawable.token250);
                                    break;
                            }
                            break;
                        case 6:
                            switch (arrayValues.get(6)) {
                                case 1:
                                    img.setImageResource(R.drawable.token5);
                                    break;
                                case 2:
                                    img.setImageResource(R.drawable.token10);
                                    break;
                                case 3:
                                    img.setImageResource(R.drawable.token25);
                                    break;
                                case 4:
                                    img.setImageResource(R.drawable.token50);
                                    break;
                                case 5:
                                    img.setImageResource(R.drawable.token100);
                                    break;
                                case 6:
                                    img.setImageResource(R.drawable.token250);
                                    break;
                                case 7:
                                    img.setImageResource(R.drawable.token5);
                                    break;
                                case 8:
                                    img.setImageResource(R.drawable.token10);
                                    break;
                                case 9:
                                    img.setImageResource(R.drawable.token25);
                                    break;
                                case 10:
                                    img.setImageResource(R.drawable.token50);
                                    break;
                                case 11:
                                    img.setImageResource(R.drawable.token100);
                                    break;
                                case 12:
                                    img.setImageResource(R.drawable.token250);
                                    break;
                            }
                            break;
                        case 7:
                            switch (arrayValues.get(7)) {
                                case 1:
                                    img.setImageResource(R.drawable.token5);
                                    break;
                                case 2:
                                    img.setImageResource(R.drawable.token10);
                                    break;
                                case 3:
                                    img.setImageResource(R.drawable.token25);
                                    break;
                                case 4:
                                    img.setImageResource(R.drawable.token50);
                                    break;
                                case 5:
                                    img.setImageResource(R.drawable.token100);
                                    break;
                                case 6:
                                    img.setImageResource(R.drawable.token250);
                                    break;
                                case 7:
                                    img.setImageResource(R.drawable.token5);
                                    break;
                                case 8:
                                    img.setImageResource(R.drawable.token10);
                                    break;
                                case 9:
                                    img.setImageResource(R.drawable.token25);
                                    break;
                                case 10:
                                    img.setImageResource(R.drawable.token50);
                                    break;
                                case 11:
                                    img.setImageResource(R.drawable.token100);
                                    break;
                                case 12:
                                    img.setImageResource(R.drawable.token250);
                                    break;
                            }
                            break;
                        case 8:
                            switch (arrayValues.get(8)) {
                                case 1:
                                    img.setImageResource(R.drawable.token5);
                                    break;
                                case 2:
                                    img.setImageResource(R.drawable.token10);
                                    break;
                                case 3:
                                    img.setImageResource(R.drawable.token25);
                                    break;
                                case 4:
                                    img.setImageResource(R.drawable.token50);
                                    break;
                                case 5:
                                    img.setImageResource(R.drawable.token100);
                                    break;
                                case 6:
                                    img.setImageResource(R.drawable.token250);
                                    break;
                                case 7:
                                    img.setImageResource(R.drawable.token5);
                                    break;
                                case 8:
                                    img.setImageResource(R.drawable.token10);
                                    break;
                                case 9:
                                    img.setImageResource(R.drawable.token25);
                                    break;
                                case 10:
                                    img.setImageResource(R.drawable.token50);
                                    break;
                                case 11:
                                    img.setImageResource(R.drawable.token100);
                                    break;
                                case 12:
                                    img.setImageResource(R.drawable.token250);
                                    break;
                            }
                            break;
                        case 9:
                            switch (arrayValues.get(9)) {
                                case 1:
                                    img.setImageResource(R.drawable.token5);
                                    break;
                                case 2:
                                    img.setImageResource(R.drawable.token10);
                                    break;
                                case 3:
                                    img.setImageResource(R.drawable.token25);
                                    break;
                                case 4:
                                    img.setImageResource(R.drawable.token50);
                                    break;
                                case 5:
                                    img.setImageResource(R.drawable.token100);
                                    break;
                                case 6:
                                    img.setImageResource(R.drawable.token250);
                                    break;
                                case 7:
                                    img.setImageResource(R.drawable.token5);
                                    break;
                                case 8:
                                    img.setImageResource(R.drawable.token10);
                                    break;
                                case 9:
                                    img.setImageResource(R.drawable.token25);
                                    break;
                                case 10:
                                    img.setImageResource(R.drawable.token50);
                                    break;
                                case 11:
                                    img.setImageResource(R.drawable.token100);
                                    break;
                                case 12:
                                    img.setImageResource(R.drawable.token250);
                                    break;
                            }
                            break;
                        case 10:
                            switch (arrayValues.get(10)) {
                                case 1:
                                    img.setImageResource(R.drawable.token5);
                                    break;
                                case 2:
                                    img.setImageResource(R.drawable.token10);
                                    break;
                                case 3:
                                    img.setImageResource(R.drawable.token25);
                                    break;
                                case 4:
                                    img.setImageResource(R.drawable.token50);
                                    break;
                                case 5:
                                    img.setImageResource(R.drawable.token100);
                                    break;
                                case 6:
                                    img.setImageResource(R.drawable.token250);
                                    break;
                                case 7:
                                    img.setImageResource(R.drawable.token5);
                                    break;
                                case 8:
                                    img.setImageResource(R.drawable.token10);
                                    break;
                                case 9:
                                    img.setImageResource(R.drawable.token25);
                                    break;
                                case 10:
                                    img.setImageResource(R.drawable.token50);
                                    break;
                                case 11:
                                    img.setImageResource(R.drawable.token100);
                                    break;
                                case 12:
                                    img.setImageResource(R.drawable.token250);
                                    break;
                            }
                            break;
                        case 11:
                            switch (arrayValues.get(11)) {
                                case 1:
                                    img.setImageResource(R.drawable.token5);
                                    break;
                                case 2:
                                    img.setImageResource(R.drawable.token10);
                                    break;
                                case 3:
                                    img.setImageResource(R.drawable.token25);
                                    break;
                                case 4:
                                    img.setImageResource(R.drawable.token50);
                                    break;
                                case 5:
                                    img.setImageResource(R.drawable.token100);
                                    break;
                                case 6:
                                    img.setImageResource(R.drawable.token250);
                                    break;
                                case 7:
                                    img.setImageResource(R.drawable.token5);
                                    break;
                                case 8:
                                    img.setImageResource(R.drawable.token10);
                                    break;
                                case 9:
                                    img.setImageResource(R.drawable.token25);
                                    break;
                                case 10:
                                    img.setImageResource(R.drawable.token50);
                                    break;
                                case 11:
                                    img.setImageResource(R.drawable.token100);
                                    break;
                                case 12:
                                    img.setImageResource(R.drawable.token250);
                                    break;
                            }
                            break;
                    }

                }
            });
        }
    }
}









