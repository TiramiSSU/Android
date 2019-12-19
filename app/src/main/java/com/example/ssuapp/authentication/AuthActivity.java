package com.example.ssuapp.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.ssuapp.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int RC_SIGN_IN=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Button firebaseuiauthbtn = (Button)findViewById(R.id.firebaseuiauthbtn);        //"인증" 버튼
        firebaseuiauthbtn.setOnClickListener(this);

        Button firebasesignout = (Button) findViewById(R.id.firebasesignout);
        firebasesignout.setOnClickListener(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {  //인증이 되어 있는 상태
            firebaseuiauthbtn.setEnabled(false);
            firebasesignout.setEnabled(true);
        } else            //인증이 되어 있지 않은 상태
        {
            firebaseuiauthbtn.setEnabled(true);
            firebasesignout.setEnabled(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN)
        {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK)
            {
                // Successfully signed in
                Intent i = new Intent(this,SignedInActivity.class);
                i.putExtras(data);
                startActivity(i);
            }
            else
            {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.firebaseuiauthbtn:
                signin();
                break;
            case R.id.firebasesignout:
                signOut();
                break;
            default:
                break;
        }
    }

    /**
     * 인증 요청
     */

    //AuthUI를 통해 인증을 요청하는 메소드
    private void signin() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setTheme(getSelectedTheme())                 // Theme 설정
                        .setLogo(getSelectedLogo())                  // 로고 설정
                        .setAvailableProviders(getSelectedProviders())// Providers 설정
                        .setTosAndPrivacyPolicyUrls("https://naver.com",
                                "https://google.com")
                        .setIsSmartLockEnabled(true)              //SmartLock 설정
                        .build(),
                RC_SIGN_IN);
    }

    /**
     * FirebaseUI에 표시할 테마 정보
     * @return 테마 정보
     */
    private int getSelectedTheme()
    {
        return AuthUI.getDefaultTheme();    //기본테마
    }

    private int getSelectedLogo()
    {
        return R.drawable.icon_dog;
    }

    /**
     * FirebaseUI를 통해 제공 받을 인증 서비스 설정
     * @return 인증 서비스
     */
    private List<AuthUI.IdpConfig> getSelectedProviders()
    {
        List<AuthUI.IdpConfig> selectedProviders = new ArrayList<>();
        CheckBox googlechk = (CheckBox)findViewById(R.id.google_provider);

        if(googlechk.isChecked())
        {
            selectedProviders.add(new AuthUI.IdpConfig.GoogleBuilder().build());
        }

        return selectedProviders;
    }

    private void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            finish();
                        }
                        else
                        {
                        }
                    }
                });
    }
}