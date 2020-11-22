package com.example.converseme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private Button updateAccount;
    private EditText userName,userStatus;
    private CircleImageView userImage;
    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();

        updateAccount = (Button) findViewById(R.id.updateButton);
        userName = (EditText) findViewById(R.id.userName);
        userStatus = (EditText) findViewById(R.id.status);
        userImage = (CircleImageView) findViewById(R.id.profile_image);
    }

    public void UpdateSettings(View view){
        String setUserName = userName.getText().toString();
        String setUserStatus = userStatus.getText().toString();

        if(TextUtils.isEmpty(setUserName)){
            Toast.makeText(this,"Please enter UserName",Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(setUserStatus)){
            Toast.makeText(this,"Please enter Bio",Toast.LENGTH_SHORT).show();
        }

        else{
            HashMap<String, String> profileMap = new HashMap<>();
            profileMap.put("uid",currentUserID);
            profileMap.put("name",setUserName);
            profileMap.put("status",setUserStatus);

            RootRef.child("Users").child(currentUserID).setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        SendUserToMainActivity();
                        Toast.makeText(SettingsActivity.this, "Profile Updated...", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        String msg = task.getException().toString();
                        Toast.makeText(SettingsActivity.this, "Error :" + msg, Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

    }

    private void SendUserToMainActivity(){
        Intent loginIntent = new Intent(SettingsActivity.this,MainActivity.class);
        //loginIntent.putExtra("cur",1);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

}