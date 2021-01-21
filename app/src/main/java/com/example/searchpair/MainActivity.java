package com.example.searchpair;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    int[] array = new int[16];

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //в начале присвоить айдишник
        image = findViewById(R.id.imageView1);

        generateRandom();
        print();
        clickImage();


    }
    //инициализация
    private void init(){

    }

    private void generateRandom(){
        for (int i = 0; i < array.length; i++){
            array[i] = 1 + (int)(Math.random()*16);
        }
    }

    private void print(){
        for (int i = 0; i < array.length; i++){
            Log.d("Log", "" +array[i]);
        }
    }
    public void clickImage(){
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.setImageResource(R.drawable.ic_launcher_background);
            }
        });
    }
}