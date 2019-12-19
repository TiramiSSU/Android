package com.example.ssuapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ssuapp.authentication.AuthActivity;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button firebaseauthbtn = (Button)findViewById(R.id.firebaseauthbtn);
        firebaseauthbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.firebaseauthbtn:
                Intent i = new Intent(this, AuthActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }
}
