package com.example.gonulluproje;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class EkleActivity extends AppCompatActivity {
    TextView konumkaydet;
    Button buton;
    FirebaseDatabase db;
    FirebaseUser firebaseUser ;
    DatabaseReference ref;
    ImageView resim, yuklendi;
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri galeriUri;
    Uri downloadUri;
    FusedLocationProviderClient fusedLocationProviderClient;
    ProgressBar  progressBar ;
    LocationManager mLocationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekle);
        tanimla();
        btnEkle();
        konumBul();

        Foto gelenUrun = (Foto) getIntent().getSerializableExtra("fotoNesnesi");
        if (gelenUrun != null) {
            Picasso.get().load(gelenUrun.getResim()).into(resim);
        }

        resim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimSec();
            }
        });
    }

    public void tanimla(){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        progressBar = findViewById(R.id.progress);
        buton = findViewById(R.id.btnkaydet);
        db = FirebaseDatabase.getInstance();
        ref = db.getReference("fotolar");
        resim = findViewById(R.id.resim);
        konumkaydet=findViewById(R.id.konum_kaydet);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("images");
        yuklendi = findViewById(R.id.check1);
    }

    private void resimSec() {
        CropImage.activity()
                .start(EkleActivity.this);
    }

    public void btnEkle() {
        buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimYukle(galeriUri, 1);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            galeriUri = result.getUri();
            resim.setImageURI(galeriUri);
        }
    }

    public void resimYukle(Uri uri, final int resimNo) {
        final StorageReference yukleRef = storageReference.child("resim" + UUID.randomUUID() + ".jpg");
        progressBar.setVisibility(View.VISIBLE);
        yukleRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(EkleActivity.this, "Resim yüklendi", Toast.LENGTH_SHORT).show();
                        yukleRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                System.out.println("Download uri: " + uri.toString());
                                if (resimNo == 1) {
                                    downloadUri = uri;
                                    yuklendi.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);

                                }
                                if (yuklendi.getVisibility() == View.VISIBLE) {

                                    final Foto yeniFoto = new Foto(downloadUri.toString(),konumkaydet.getText().toString(),firebaseUser.getUid(),null);

                                    String yenikey = ref.push().getKey();
                                    yeniFoto.setKey(yenikey);

                                    ref.child(yenikey).setValue(yeniFoto)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(EkleActivity.this, "Resim Eklendi", Toast.LENGTH_SHORT).show();
                                                    finish();

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(EkleActivity.this, "Resim Eklemede hata oluştu", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            });
                                    progressBar.setVisibility(View.GONE);

                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(EkleActivity.this, "Resim yüklemede hata oluştu" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void konumBul(){
        ///konum bul/////////////////////////////
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(EkleActivity.this
                , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //permission granted
            getLocation();
        } else {
            //when permission denied
            ActivityCompat.requestPermissions(EkleActivity.this
                    , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        //konumBul bitiş/////////////////////////////
    }

    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission")
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        return bestLocation;
    }
    private void getLocation()
    {
        try {
            //initilaize geocoder
            Geocoder geocoder = new Geocoder(EkleActivity.this,
                    Locale.getDefault());
            //initlaize addresss list
            List<Address> addresses = geocoder.getFromLocation(
                    getLastKnownLocation().getLatitude(), getLastKnownLocation().getLongitude(), 1
            );
            //set latitude on textView
            konumkaydet.setText(addresses.get(0).getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
