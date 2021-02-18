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
//        Collections.shuffle(arrayResourcesImages);
        Collections.shuffle(arrayCards);

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

        addToArrayCards();
//        addResourcesImagesToArray();
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
        arrayCards.add(new Card(R.drawable.image1, "1"));
        arrayCards.add(new Card(R.drawable.image2, "2"));
        arrayCards.add(new Card(R.drawable.image3, "3"));
        arrayCards.add(new Card(R.drawable.image4, "4"));
        arrayCards.add(new Card(R.drawable.image5, "5"));
        arrayCards.add(new Card(R.drawable.image6, "6"));
        arrayCards.add(new Card(R.drawable.image7, "7"));
        arrayCards.add(new Card(R.drawable.image8, "8"));
        arrayCards.add(new Card(R.drawable.image1, "1"));
        arrayCards.add(new Card(R.drawable.image2, "2"));
        arrayCards.add(new Card(R.drawable.image3, "3"));
        arrayCards.add(new Card(R.drawable.image4, "4"));
        arrayCards.add(new Card(R.drawable.image5, "5"));
        arrayCards.add(new Card(R.drawable.image6, "6"));
        arrayCards.add(new Card(R.drawable.image7, "7"));
        arrayCards.add(new Card(R.drawable.image8, "8"));
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
                img.setImageResource(arrayCards.get(0).getImageResource());
                //поработать над этим (вынести в отдельный метод)
                imageViewFirstCard.setTag(arrayCards.get(0).getTagImage());
                break;
            case "2":
                img.setImageResource(arrayCards.get(1).getImageResource());
                break;
            case "3":
                img.setImageResource(arrayCards.get(2).getImageResource());
                break;
            case "4":
                img.setImageResource(arrayCards.get(3).getImageResource());
                break;
            case "5":
                img.setImageResource(arrayCards.get(4).getImageResource());
                break;
            case "6":
                img.setImageResource(arrayCards.get(5).getImageResource());
                break;
            case "7":
                img.setImageResource(arrayCards.get(6).getImageResource());
                break;
            case "8":
                img.setImageResource(arrayCards.get(7).getImageResource());
                break;
            case "9":
                img.setImageResource(arrayCards.get(8).getImageResource());
                break;
            case "10":
                img.setImageResource(arrayCards.get(9).getImageResource());
                break;
            case "11":
                img.setImageResource(arrayCards.get(10).getImageResource());
                break;
            case "12":
                img.setImageResource(arrayCards.get(11).getImageResource());
                break;
            case "13":
                img.setImageResource(arrayCards.get(12).getImageResource());
                break;
            case "14":
                img.setImageResource(arrayCards.get(13).getImageResource());
                break;
            case "15":
                img.setImageResource(arrayCards.get(14).getImageResource());
                break;
            case "16":
                img.setImageResource(arrayCards.get(15).getImageResource());
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

                        if(counterOpenedImages == 1){
                            imageViewFirstCard.setTag(img.getTag());
                        }
                        if(counterOpenedImages == 2){
                            imageViewTwoCard.setTag(img.getTag());
                        }
                    }else {
                        //закрыть все карты
                        closeAllImages();
                        counterOpenedImages = 0;
                        imageViewFirstCard.setTag("0");
                        imageViewTwoCard.setTag("0");
                    }
                    System.out.println("clicked images:  " + img.getDrawable());
                    System.out.println("first card:  " + imageViewFirstCard.getTag());
                    System.out.println("two card:  " + imageViewTwoCard.getTag());

                }
            });
        }
    }

    //сравнить открытые картинки
    private void checkCards(){
        if (imageViewFirstCard.getTag().equals(imageViewTwoCard.getTag())) {
            System.out.println("Cards equals");
        }
    }

    //закрыть все карты
    private void closeAllImages(){
        for (ImageView img : arrayImageViewsButtons){
            img.setImageResource(R.drawable.imageshirt);
        }

    }

}









