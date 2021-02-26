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

    ArrayList<ImageView> arrayImageViewsButtons = new ArrayList<>(); //лист с кнопками
    ArrayList<String> arrayTags = new ArrayList<>(); //лист с тагами (за конкретным тагом закреплена конкретная картинка)

    private ImageView imageView1, imageView2, imageView3, imageView4,
            imageView5, imageView6, imageView7, imageView8,
            imageView9, imageView10, imageView11, imageView12,
            imageView13, imageView14, imageView15, imageView16,
            imageViewFirstCard, imageViewTwoCard;

    Animation animation1 = null;
    Animation animation2 = null;
    private int counterOpenedImages = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        for (String str : arrayTags) {
            System.out.println(str);
        }


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
        imageViewFirstCard = findViewById(R.id.idImageFirstCard);
        imageViewTwoCard = findViewById(R.id.idImageTwoCard);

        addTagsToList();
        Collections.shuffle(arrayTags);
        addToArrayImageViews();
        onClickImageViews();
        createTagsForImageViews();
    }
    //заполнить лист тагов
    private void addTagsToList(){
        for (int i = 1; i < 9; i++){
            arrayTags.add(String.valueOf(i));
        }
        for (int i = 1; i < 9; i++){
            arrayTags.add(String.valueOf(i));
        }
    }
    //Заполнение массива с кнопками (для присвоения слушателей нажатий через цикл for-each)
    private void addToArrayImageViews(){
        arrayImageViewsButtons.add(imageView1);
        arrayImageViewsButtons.add(imageView2);
        arrayImageViewsButtons.add(imageView3);
        arrayImageViewsButtons.add(imageView4);
        arrayImageViewsButtons.add(imageView5);
        arrayImageViewsButtons.add(imageView6);
        arrayImageViewsButtons.add(imageView7);
        arrayImageViewsButtons.add(imageView8);
        arrayImageViewsButtons.add(imageView9);
        arrayImageViewsButtons.add(imageView10);
        arrayImageViewsButtons.add(imageView11);
        arrayImageViewsButtons.add(imageView12);
        arrayImageViewsButtons.add(imageView13);
        arrayImageViewsButtons.add(imageView14);
        arrayImageViewsButtons.add(imageView15);
        arrayImageViewsButtons.add(imageView16);
    }

    //присвоить таги для ImageViews
    private void createTagsForImageViews(){
        arrayImageViewsButtons.get(0).setTag(arrayTags.get(0));
        arrayImageViewsButtons.get(1).setTag(arrayTags.get(1));
        arrayImageViewsButtons.get(2).setTag(arrayTags.get(2));
        arrayImageViewsButtons.get(3).setTag(arrayTags.get(3));
        arrayImageViewsButtons.get(4).setTag(arrayTags.get(4));
        arrayImageViewsButtons.get(5).setTag(arrayTags.get(5));
        arrayImageViewsButtons.get(6).setTag(arrayTags.get(6));
        arrayImageViewsButtons.get(7).setTag(arrayTags.get(7));
        arrayImageViewsButtons.get(8).setTag(arrayTags.get(8));
        arrayImageViewsButtons.get(9).setTag(arrayTags.get(9));
        arrayImageViewsButtons.get(10).setTag(arrayTags.get(10));
        arrayImageViewsButtons.get(11).setTag(arrayTags.get(11));
        arrayImageViewsButtons.get(12).setTag(arrayTags.get(12));
        arrayImageViewsButtons.get(13).setTag(arrayTags.get(13));
        arrayImageViewsButtons.get(14).setTag(arrayTags.get(14));
        arrayImageViewsButtons.get(15).setTag(arrayTags.get(15));
    }
    //открыть карту
    private void openCard(ImageView img) {
        //установить ресурс для этой вью из списка в зависимости от индекса
        switch (img.getTag().toString()) {
            case "1":
                img.setImageResource(R.drawable.image1);
                break;
            case "2":
                img.setImageResource(R.drawable.image2);
                break;
            case "3":
                img.setImageResource(R.drawable.image3);
                break;
            case "4":
                img.setImageResource(R.drawable.image4);
                break;
            case "5":
                img.setImageResource(R.drawable.image5);
                break;
            case "6":
                img.setImageResource(R.drawable.image6);
                break;
            case "7":
                img.setImageResource(R.drawable.image7);
                break;
            case "8":
                img.setImageResource(R.drawable.image8);
                break;
        }
    }
    //слушатель нажатий для картинок
    private void onClickImageViews(){
        for(final ImageView img : arrayImageViewsButtons){
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (counterOpenedImages != 2) {
                        //запуск первой половины анимации
                        img.startAnimation(animation1);
                        animation1.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                //присваивание
                                if(counterOpenedImages == 0){
                                    imageViewFirstCard = img;
                                    imageViewFirstCard.setClickable(false);
                                }
                                if(counterOpenedImages == 1){
                                    imageViewTwoCard = img;
                                    imageViewTwoCard.setClickable(false);
                                }
                                counterOpenedImages++;
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                //запуск второй половины анимации
                                img.startAnimation(animation2);
                                animation2.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {
                                        openCard(img);
                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        checkCards();
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });



                    }
                    System.out.println("Clicked ImageView Tag:  " + img.getTag());
                    System.out.println("first card:  " + imageViewFirstCard.getTag());
                    System.out.println("two card:  " + imageViewTwoCard.getTag());

                }
            });
        }
    }

    //сравнить открытые картинки (идея поделить таг на 2 и сравнить, но для начала привести его к инту)
    private void checkCards(){
        if (imageViewFirstCard.getTag().equals(imageViewTwoCard.getTag())) {
            System.out.println("Cards equals");
            imageViewFirstCard.setVisibility(View.INVISIBLE);
            imageViewTwoCard.setVisibility(View.INVISIBLE);
            counterOpenedImages = 0;
        }else {
            //закрыть все карты
            if(counterOpenedImages == 2) {
                closeAllImages();
                counterOpenedImages = 0;

            }
        }
    }

    //закрыть все карты
    private void closeAllImages(){
        for (ImageView img : arrayImageViewsButtons){
            img.setImageResource(R.drawable.imageshirt);
        }

        imageViewFirstCard.setClickable(true);
        imageViewTwoCard.setClickable(true);
        imageViewFirstCard = findViewById(R.id.idImageFirstCard);
        imageViewTwoCard = findViewById(R.id.idImageTwoCard);

    }

}









