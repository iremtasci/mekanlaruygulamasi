package com.iremtasci.mekanlaruygulama;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class resimActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    TextView konumText;
    Bitmap chosenImage;
    EditText commentText;
    ImageView imageView;
    EditText locationText;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    static ArrayList<String> konumlar = new ArrayList<String>();
    Uri imageData;

    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_place, menu);
        ArrayList<String> konum = new ArrayList<String>(konumlar);
        //konumText = findViewById(R.id.konumlarS);
        System.out.println(konum);
        commentText.setText(konum.get(konum.size()-1));





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
    protected void onCreate(Bundle savedInstanceState) {


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

                konumlar.add(nameFromDatabase);

                Double l1 = Double.parseDouble(latitudeFromDatabase);
                Double l2 = Double.parseDouble(longitudeFromDatabase);

                LatLng locationFromDatabase = new LatLng(l1,l2);

                //locations.add(locationFromDatabase);

            }

            cursor.close();

        } catch (Exception e) {

        }


        //textView2 = findViewById(R.id.textView2);
        //String Tempholder = getIntent().getStringExtra(""+ textView);
       // textView2.setText(MainActivity.names.get(1));

        firebaseAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resim);

        commentText = findViewById(R.id.resim_activity_yorum);
        locationText = findViewById(R.id.user_location);
        imageView = findViewById(R.id.resim_activity_resim);


        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();//obje elde ediyorum
    }

    public void upload (View view){

        if (imageData != null) {


            //universal unique id yani uydurma id
            UUID uuid = UUID.randomUUID();
            final String imageName = "images/" + uuid + ".jpg";//klasöre koyuyor

            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //Download URL'yi almak için

                    StorageReference newReference = FirebaseStorage.getInstance().getReference(imageName);//bul
                    newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String downloadUrl = uri.toString();

                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            String userEmail = firebaseUser.getEmail();//user emailini almak için

                            String comment = commentText.getText().toString();//comment almak
                            String location = locationText.getText().toString();

                            HashMap<String, Object> postData = new HashMap<>();

                            postData.put("useremail",userEmail);
                            postData.put("downloadurl",downloadUrl);
                            postData.put("comment",comment);
                            postData.put("konum",location);
                            postData.put("date", FieldValue.serverTimestamp());//güncel tarihi almak için ve en son girilen en üstte olsun diye

                            //database'e kaydetme
                            firebaseFirestore.collection("Posts").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {

                                    Intent intent = new Intent(resimActivity.this,siralamaActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);

                                }
                            }).addOnFailureListener(new OnFailureListener(){
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(resimActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                                }
                            });




                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(resimActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
            });

        }



    }
    public void chooseImage(View view){
        //kullanıcının galerisine gidicez burada


            //izinler için
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},2);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//izin varsa gidip seçme
            startActivityForResult(intent,1);
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==2){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//izin varsa gidip seçme
                startActivityForResult(intent,1);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            //kayıtlı yolu alıyoruz.
             imageData = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageData);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        super.onActivityResult(requestCode, resultCode, data);
    }



    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.add_picture) {
            Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
            intent.putExtra("info","new");
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }*/


}
