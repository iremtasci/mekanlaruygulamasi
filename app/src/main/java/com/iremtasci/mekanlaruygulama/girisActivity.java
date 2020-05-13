package com.iremtasci.mekanlaruygulama;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class girisActivity extends AppCompatActivity {

    ImageView konumlarim;
    ImageView anasayfa;
    ImageView fotografekle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);
        konumlarim = findViewById(R.id.konumlarim);

        konumlarim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(girisActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        anasayfa = findViewById(R.id.anasayfa);

        anasayfa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(girisActivity.this,siralamaActivity.class);
                startActivity(intent);
            }
        });
        fotografekle = findViewById(R.id.fotografekle);

        fotografekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(girisActivity.this,resimActivity.class);
                startActivity(intent);
            }
        });


    }
}
