package com.example.houst.health;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.houst.health.Model.User;
import com.example.houst.health.Common.Common;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.security.Permission;

import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {
    private Button btnSignIn,btnRegister;
    RelativeLayout rootLayout;
    FirebaseDatabase db ;
    DatabaseReference users;
    FirebaseAuth auth;
    private  final static int PERMESSION=1000;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/Arkhip_font.ttf")
                .setFontAttrId(R.attr.fontPath).build());
        setContentView(R.layout.activity_main);

        auth=FirebaseAuth.getInstance();
        db =FirebaseDatabase.getInstance();
        users=db.getReference(Common.users);
        btnRegister=(Button)findViewById(R.id.btnRegister);
        btnSignIn=(Button)findViewById(R.id.btnSignIn);
        rootLayout=findViewById(R.id.rootLayout);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(MainActivity.this,CustomerLoginActivity.class);
                startActivity(intent);
                finish();
                return;*/


                showRegisterDialog();

            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialog();
            }
        });
    }
    private void showRegisterDialog() {
        final AlertDialog.Builder dialog  = new AlertDialog.Builder(this);
        dialog.setTitle("REGISTER");
        dialog.setMessage("Please use email to register");

        LayoutInflater inflater =LayoutInflater.from(this);
        View register_layout=inflater.inflate(R.layout.layout_register,null);

        final MaterialEditText edtEmail =register_layout.findViewById(R.id.edtEmail);
        final MaterialEditText edtPassword =register_layout.findViewById(R.id.edtPassword);
        final MaterialEditText edtName =register_layout.findViewById(R.id.edtName);
        final MaterialEditText edtPhone =register_layout.findViewById(R.id.edtPhone);
        dialog.setView(register_layout);

        dialog.setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(TextUtils.isEmpty(edtEmail.getText().toString())){
                    Snackbar.make(rootLayout,"Plase enter email adress",Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }
                if(TextUtils.isEmpty(edtPhone.getText().toString())){
                    Snackbar.make(rootLayout,"Plase enter phone number",Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }
                if(TextUtils.isEmpty(edtPassword.getText().toString())){
                    Snackbar.make(rootLayout,"Plase enter password",Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }
                if(edtPassword.getText().toString().length()<6){
                    Snackbar.make(rootLayout,"Password too short ",Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                //register new user
                auth.createUserWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                //save user to db
                                User user   =new User();
                                user.setEmail(edtEmail.getText().toString());
                                user.setName(edtName.getText().toString());
                                user.setPhone(edtPhone.getText().toString());
                                user.setPassword(edtPassword.getText().toString());
                                //use email to key
                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Snackbar.make(rootLayout,"Register success fully !!!",Snackbar.LENGTH_SHORT)
                                                        .show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Snackbar.make(rootLayout,"Failed"+e.getMessage(),Snackbar.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make( rootLayout,"Failed"+e.getMessage(),Snackbar.LENGTH_SHORT)
                                .show();
                    }
                });
            }
        });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();


    }
    private void showLoginDialog() {

        final AlertDialog.Builder dialog  = new AlertDialog.Builder(this);
        dialog.setTitle("SIGN IN");
        dialog.setMessage("Please use email to sign in ");
        LayoutInflater inflater =LayoutInflater.from(this);
        View login_layout=inflater.inflate(R.layout.layout_login,null);
        final MaterialEditText edtEmail =login_layout.findViewById(R.id.edtEmail);
        final MaterialEditText edtPassword =login_layout.findViewById(R.id.edtPassword);

        dialog.setView(login_layout);
        dialog.setPositiveButton("SIGN IN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                btnSignIn.setEnabled(true);




                if(TextUtils.isEmpty(edtEmail.getText().toString())){
                    Snackbar.make(rootLayout,"Plase enter email adress",Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                if(TextUtils.isEmpty(edtPassword.getText().toString())){
                    Snackbar.make(rootLayout,"Plase enter password",Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }
                if(edtPassword.getText().toString().length()<6){
                    Snackbar.make(rootLayout,"Password too short ",Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }
                final AlertDialog waitingDialog =new SpotsDialog(MainActivity.this);
                waitingDialog.show();
                //login
                auth.signInWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                waitingDialog.dismiss();
                                Intent ii =new Intent(MainActivity.this,Home.class);
                                ii.putExtra("name",edtEmail.getText().toString());
                                startActivity(ii);
                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        waitingDialog.dismiss();
                        Snackbar.make(rootLayout,"Failed"+e.getMessage(),Snackbar.LENGTH_SHORT).show();
                        btnRegister.setEnabled(true);
                    }
                });

            }
        });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();


    }
}
