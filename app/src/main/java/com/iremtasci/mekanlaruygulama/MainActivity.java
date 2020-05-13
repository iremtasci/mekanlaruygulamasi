package com.iremtasci.mekanlaruygulama;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    TextView textView;
    Button button;
    static ArrayList<String> names = new ArrayList<String>();
    static ArrayList<LatLng> locations = new ArrayList<LatLng>();
    static ArrayAdapter arrayAdapter;
    //static ArrayList<String> deneme = new ArrayList<String>(names);





    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_place, menu);
        ArrayList<String> deneme = new ArrayList<String>(names);
        //textView = findViewById(R.id.textView);
        System.out.println(deneme);
        //textView.setText(deneme.get(deneme.size()-1));

        button = findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayAdapter.clear();



            }
        });




        /*DocumentReference docRef = db.collection("posts").document("konum");


        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        System.out.println(document.getData());
                    } else {

                    }
                } else {
                    System.out.println(task.getException());
                }
            }
        });*/


        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.add_place) {
            Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
            intent.putExtra("info","new");
            startActivity(intent);
        } else if (item.getItemId()==R.id.cikis_yap){//çıkış işlemleri

         //   firebaseAuth.signOut();

            Intent intentToSignUp = new Intent(MainActivity.this,SignUpActivity.class);
            startActivity(intentToSignUp);



        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);







        firebaseAuth = FirebaseAuth.getInstance();//çıkış işlemleri için

        ListView listView = (ListView) findViewById(R.id.listView);


        try {

            MapsActivity.database = this.openOrCreateDatabase("Places",MODE_PRIVATE,null);
            Cursor cursor = MapsActivity.database.rawQuery("SELECT * FROM places",null);

            int nameIx = cursor.getColumnIndex("name");
            int latitudeIx = cursor.getColumnIndex("latitude");
            int longitudeIx = cursor.getColumnIndex("longitude");


            while (cursor.moveToNext()) {

                String nameFromDatabase = cursor.getString(nameIx);
                String latitudeFromDatabase = cursor.getString(latitudeIx);
                String longitudeFromDatabase = cursor.getString(longitudeIx);

                names.add(nameFromDatabase);

                Double l1 = Double.parseDouble(latitudeFromDatabase);
                Double l2 = Double.parseDouble(longitudeFromDatabase);

                LatLng locationFromDatabase = new LatLng(l1,l2);

                locations.add(locationFromDatabase);

            }

            cursor.close();

        } catch (Exception e) {

        }

        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,names);

        listView.setAdapter(arrayAdapter);






        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),siralamaActivity.class);
                intent.putExtra("info","old");
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });







    }





}
