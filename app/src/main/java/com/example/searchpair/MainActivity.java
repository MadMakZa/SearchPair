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
        Collections.shuffle(arrayResourcesImages);

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

        addResourcesImagesToArray();
        addToArrayImageViews();
        onClickImageViews();
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
    //Создание экземпляров карточек
    private void addToArrayCards(){
        for (int counter = 1; counter < 16; counter++) {
            arrayCards.add(new Card(R.drawable.imageshirt));
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

    //открыть карту
    private void openCard(ImageView img) {
        //установить ресурс для этой вью из списка в зависимости от индекса
        switch (img.getTag().toString()) {
            case "1":
                img.setImageResource(arrayResourcesImages.get(0));
                break;
            case "2":
                img.setImageResource(arrayResourcesImages.get(1));
                break;
            case "3":
                img.setImageResource(arrayResourcesImages.get(2));
                break;
            case "4":
                img.setImageResource(arrayResourcesImages.get(3));
                break;
            case "5":
                img.setImageResource(arrayResourcesImages.get(4));
                break;
            case "6":
                img.setImageResource(arrayResourcesImages.get(5));
                break;
            case "7":
                img.setImageResource(arrayResourcesImages.get(6));
                break;
            case "8":
                img.setImageResource(arrayResourcesImages.get(7));
                break;
            case "9":
                img.setImageResource(arrayResourcesImages.get(8));
                break;
            case "10":
                img.setImageResource(arrayResourcesImages.get(9));
                break;
            case "11":
                img.setImageResource(arrayResourcesImages.get(10));
                break;
            case "12":
                img.setImageResource(arrayResourcesImages.get(11));
                break;
            case "13":
                img.setImageResource(arrayResourcesImages.get(12));
                break;
            case "14":
                img.setImageResource(arrayResourcesImages.get(13));
                break;
            case "15":
                img.setImageResource(arrayResourcesImages.get(14));
                break;
            case "16":
                img.setImageResource(arrayResourcesImages.get(15));
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
                        openCard(img);
                        counterOpenedImages++;
                    }else {
                        //закрыть все карты
                        closeAllImages();
                        counterOpenedImages = 0;
                    }

                }
            });
        }
    }

    //сравнить открытые картинки
    private void checkCards(){
        if (imageViewFirstCard.getTag().equals(imageViewTwoCard.getTag())) {
            imageViewFirstCard.setVisibility(View.INVISIBLE);
            imageViewTwoCard.setVisibility(View.INVISIBLE);
            imageViewFirstCard.setImageResource(0);
            imageViewTwoCard.setImageResource(0);
        }else{
            imageViewFirstCard = null;
            imageViewTwoCard = null;
        }
    }

    //закрыть все карты
    private void closeAllImages(){
        for (ImageView img : arrayImageViewsButtons){
            img.setImageResource(R.drawable.imageshirt);
        }

    }

}









