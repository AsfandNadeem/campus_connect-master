package com.example.asfand.androidproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText username,password;
    ProgressDialog progress;
    String department="department";
    SharedPreferences list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        username=(EditText)findViewById(R.id.editText);
        password=(EditText)findViewById(R.id.editText2);

        list = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void loginClick(View view)
    {
        if(username.getText().toString()==""||password.getText().toString()=="")
        {
            Toast.makeText(getApplicationContext(),"please fill the fields",Toast.LENGTH_SHORT).show();
        }
        else
        {
            mAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                //Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                String id = user.getUid();
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("users");
                                //DatabaseReference u=myRef.child(id);
                                myRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        SharedPreferences.Editor editor = list.edit();
                                        editor.putString(department, dataSnapshot.child("department").getValue(String.class).toString());
                                        editor.commit();
                                        Log.d("depart", dataSnapshot.child("department").getValue(String.class).toString());

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {}

                                });
                                Intent i = new Intent(getApplicationContext(), HomePage.class);
                                startActivity(i);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("abcdefgh", "signInWithEmail:failure", task.getException());
                                /*Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();*/
                                Toast.makeText(getApplicationContext(),task.getException().toString(),Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        }

    }

    public void SignUpClick(View view)
    {



        Intent i = new Intent(this, Login.class);
        startActivity(i);

    }
}
