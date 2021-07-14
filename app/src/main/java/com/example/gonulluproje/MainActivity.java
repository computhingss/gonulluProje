package com.example.gonulluproje;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText txtmail,txtsifre;
    Button btngiris,btnKaydol;
    FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tanimla();
        giris();
        mAuth = FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        if (mUser!=null){
            Intent i = new Intent(MainActivity.this, anasayfaActivity.class);
            startActivity(i);
        }
    }

    public void tanimla(){
        txtmail =findViewById(R.id.txtEmail);
        txtsifre =findViewById(R.id.txtSifre);
        btngiris =findViewById(R.id.btngiris);
        btnKaydol =findViewById(R.id.btnkaydol);
    }

    public void giris(){
        btngiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=txtmail.getText().toString();
                String pwd=txtsifre.getText().toString();
                if (email.isEmpty()){
                    txtmail.setError("email boş geçilemez");
                    txtmail.requestFocus();

                }

                else if (pwd.isEmpty()){
                    txtsifre.setError("şifre boş geçilemez");
                    txtsifre.requestFocus();
                }


                else if (pwd.isEmpty() && email.isEmpty()){
                    Toast.makeText(MainActivity.this, "Bilgiler boş bırakılamaz", Toast.LENGTH_SHORT).show();

                }

                else if (!(pwd.isEmpty() && email.isEmpty())){
                    // String email = txtmail.getText().toString();
                    String password = txtsifre.getText().toString();
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Toast.makeText(MainActivity.this, "Giriş başarılı.", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(MainActivity.this, anasayfaActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, "Hatalı giriş.\n\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else {
                    Toast.makeText(MainActivity.this, "Bir hata oluştu", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnKaydol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=txtmail.getText().toString();
                String pwd=txtsifre.getText().toString();
                if (email.isEmpty()){
                    txtmail.setError("email boş geçilemez");
                    txtmail.requestFocus();

                }

                else if (pwd.isEmpty()){
                    txtsifre.setError("şifre boş geçilemez");
                    txtsifre.requestFocus();
                }


                else if (pwd.isEmpty() && email.isEmpty()){
                    Toast.makeText(MainActivity.this, "Bilgiler boş bırakılamaz", Toast.LENGTH_SHORT).show();

                }

                else if (!(pwd.isEmpty() && email.isEmpty())){
                    String password = txtsifre.getText().toString();
                    Toast.makeText(MainActivity.this, ""+email, Toast.LENGTH_SHORT).show();
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Toast.makeText(MainActivity.this, "Kullanıcı kaydedildi.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, "Kayıt işlemi hatalı.\n\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else {
                    Toast.makeText(MainActivity.this, "Bir hata oluştu", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}