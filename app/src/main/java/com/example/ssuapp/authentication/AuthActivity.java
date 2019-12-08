package com.example.ssuapp.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ssuapp.R;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Button firebaseauthbtn = (Button)findViewById(R.id.firebaseui);
        firebaseauthbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.firebaseui:
                Intent i = new Intent(this, FirebaseUIActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }
}
