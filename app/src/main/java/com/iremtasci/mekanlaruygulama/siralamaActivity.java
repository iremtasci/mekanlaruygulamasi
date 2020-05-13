package com.iremtasci.mekanlaruygulama;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;


public class siralamaActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    Button button;
    private Toolbar toolbar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<String> userEmailFromFB;
    ArrayList<String> userCommentFromFB;
    ArrayList<String> userImageFromFB;
    ArrayList<String> userKonumFromFB;
    SiralamaRecyclerAdapter siralamaRecyclerAdapter;
    private SiralamaRecyclerAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siralama);

        //toolbar = findViewById(R.id.toolbar);

        userCommentFromFB = new ArrayList<>();
        userEmailFromFB = new ArrayList<>();
        userImageFromFB = new ArrayList<>();
        userKonumFromFB = new ArrayList<>();

        button = findViewById(R.id.button3);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(siralamaActivity.this,resimActivity.class);
                startActivity(intent);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        getDataFromFirestore();


        //RecyclerView

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        siralamaRecyclerAdapter = new SiralamaRecyclerAdapter(userEmailFromFB,userCommentFromFB,userImageFromFB,userKonumFromFB);
        recyclerView.setAdapter(siralamaRecyclerAdapter);

    }
    public void getDataFromFirestore() {
        System.out.println("bos");
        //firebasedeki referansı bulmak için oluşturulan sınıf

        CollectionReference collectionReference = firebaseFirestore.collection("Posts");

        collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>(){//tarihe göre sıralama
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Toast.makeText(siralamaActivity.this,e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }

                if (queryDocumentSnapshots != null) {

                    userCommentFromFB.clear();
                    userEmailFromFB.clear();
                    userImageFromFB.clear();
                    userKonumFromFB.clear();

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {

                        Map<String,Object> data = snapshot.getData();

                        //Casting yani string olarak ata



                        String konum = (String) data.get("konum");
                        String userEmail = (String) data.get("useremail");
                        String downloadUrl = (String) data.get("downloadurl");
                        String comment = (String) data.get("comment");


                        userCommentFromFB.add(comment);
                        userEmailFromFB.add(userEmail);
                        userImageFromFB.add(downloadUrl);
                        userKonumFromFB.add(konum);
                        //dataları okuyabiliyoruz.

                        siralamaRecyclerAdapter.notifyDataSetChanged();

                    }


                }

            }
        });


    }

    public void getDataFromFirestoreFilter(String filtre) {
        System.out.println("filter");
        //firebasedeki referansı bulmak için oluşturulan sınıf

        System.out.println(filtre);
        CollectionReference collectionReference = firebaseFirestore.collection("Posts");


        collectionReference.orderBy("comment").startAt(filtre).endAt(filtre+"\uf8ff").addSnapshotListener(new EventListener<QuerySnapshot>(){//tarihe göre sıralama
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    Toast.makeText(siralamaActivity.this,e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }

                if (queryDocumentSnapshots != null) {

                    userCommentFromFB.clear();
                    userEmailFromFB.clear();
                    userImageFromFB.clear();
                    userKonumFromFB.clear();

                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {

                        Map<String,Object> data = snapshot.getData();

                        //Casting yani string olarak ata
                        String konum = (String) data.get("konum");
                        String userEmail = (String) data.get("useremail");
                        String downloadUrl = (String) data.get("downloadurl");
                        String comment = (String) data.get("comment");

                        userCommentFromFB.add(comment);
                        userEmailFromFB.add(userEmail);
                        userImageFromFB.add(downloadUrl);
                        userKonumFromFB.add(konum);
                        //dataları okuyabiliyoruz.

                        siralamaRecyclerAdapter.notifyDataSetChanged();
                        System.out.println();
                    }


                }

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {

   if(newText.isEmpty())
   {
       getDataFromFirestore();
   }
   else
   {
       getDataFromFirestoreFilter(newText.toString().trim());
   }

        return true;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_resim) {
            Intent intent = new Intent(getApplicationContext(), resimActivity.class);
            intent.putExtra("info", "new");
            startActivity(intent);

        } else if (item.getItemId()==R.id.cikis_yap){//çıkış işlemleri

            firebaseAuth.signOut();

            Intent intent = new Intent(siralamaActivity.this,SignUpActivity.class);
            startActivity(intent);//giris sayfasına atar


        }

        return super.onOptionsItemSelected(item);
    }*/



}


