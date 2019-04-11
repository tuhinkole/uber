package testing.uber.tuhinkole.uber;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import dmax.dialog.SpotsDialog;
import testing.uber.tuhinkole.uber.Model.User;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn,btnRegister;
    RelativeLayout rootLayout;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Before SetContentView
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                                          .setDefaultFontPath("fonts/Sofia-Regular.otf")
                                          .setFontAttrId(R.attr.fontPath)
                                            .build());

        setContentView(R.layout.activity_main);


        //Init Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");


        //Init View
        btnSignIn =(Button)findViewById(R.id.btnSignIn);
        btnRegister =(Button)findViewById(R.id.btnRegister);
        rootLayout = (RelativeLayout)findViewById(R.id.rootLayout);
        //Event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegisterDialog();
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoginDialog();
            }
        });
    }

    private void showLoginDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("SIGN IN");
        dialog.setMessage("Please Use Email To Sign In");

        LayoutInflater inflater = LayoutInflater.from(this);
        View login_layout = inflater.inflate(R.layout.layout_login,null);

        final MaterialEditText edtEmail = login_layout.findViewById(R.id.edtEmail);
        final MaterialEditText edtPassword = login_layout.findViewById(R.id.edtPassword);


        dialog.setView(login_layout);

        //Set Button
        dialog.setPositiveButton("SIGN IN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();

                        //Set disable button Sign In is processing

                        btnSignIn.setEnabled(false);



                        //Check Validation
                        if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                            Snackbar.make(rootLayout, "Please enter email address", Snackbar.LENGTH_SHORT)
                                    .show();
                            return;
                        }

                        if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                            Snackbar.make(rootLayout, "Please enter password", Snackbar.LENGTH_SHORT)
                                    .show();
                            return;
                        }
                        if (edtPassword.getText().toString().length() < 6) {
                            Snackbar.make(rootLayout, "password too short !!!", Snackbar.LENGTH_SHORT)
                                    .show();
                            return;
                        }

                        final SpotsDialog waitingDialog = new SpotsDialog(MainActivity.this);
                        waitingDialog.show();


                        //Login
                        auth.signInWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {

                                        waitingDialog.dismiss();
                                        startActivity(new Intent(MainActivity.this,Welcome.class));
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        waitingDialog.dismiss();

                                        Snackbar.make(rootLayout, "pFailed"+e.getMessage(), Snackbar.LENGTH_SHORT)
                                                .show();
                                        //Active button
                                        btnSignIn.setEnabled(true);
                                    }
                                });


                    }
                });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });



        dialog.show();
    }

    private void showRegisterDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("REGISTER");
        dialog.setMessage("Please Use Email To Register");

        LayoutInflater inflater = LayoutInflater.from(this);
        View register_layout = inflater.inflate(R.layout.activity_register,null);

        final MaterialEditText edtEmail = register_layout.findViewById(R.id.edtEmail);
        final MaterialEditText edtPassword = register_layout.findViewById(R.id.edtPassword);
        final MaterialEditText edtName= register_layout.findViewById(R.id.edtName);
        final MaterialEditText edtPhone = register_layout.findViewById(R.id.edtPhone);

        dialog.setView(register_layout);

        //Set Button
        dialog.setPositiveButton("REGISTER", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();


                //Check Validation
                if(TextUtils.isEmpty(edtEmail.getText().toString())){
                    Snackbar.make(rootLayout,"Please enter email address",Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }
                if(TextUtils.isEmpty(edtPhone.getText().toString())){
                    Snackbar.make(rootLayout,"Please enter phone number",Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }
                if(TextUtils.isEmpty(edtPassword.getText().toString())){
                    Snackbar.make(rootLayout,"Please enter password",Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }
                if(edtPassword.getText().toString().length()<6){
                    Snackbar.make(rootLayout,"password too short !!!",Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }
                final SpotsDialog waitingDialog = new SpotsDialog(MainActivity.this);
                waitingDialog.show();

                //Register New User
                auth.createUserWithEmailAndPassword(edtEmail.getText().toString(),edtPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {

                                // Save User to DB
                                User user = new User();
                                user.setEmail(edtEmail.getText().toString());
                                user.setName(edtName.getText().toString());
                                user.setPhone(edtPhone.getText().toString());
                                user.setPassword(edtPassword.getText().toString());

                                // Use Email to Key

                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                waitingDialog.dismiss();
                                                Snackbar.make(rootLayout,"Register Success fully",Snackbar.LENGTH_SHORT)
                                                        .show();

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                waitingDialog.dismiss();
                                                Snackbar.make(rootLayout,"Failed"+e.getMessage(),Snackbar.LENGTH_SHORT)
                                                        .show();

                                            }
                                        });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Snackbar.make(rootLayout,"Failed"+e.getMessage(),Snackbar.LENGTH_SHORT)
                                        .show();
                            }
                        });

            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();
    }
}
