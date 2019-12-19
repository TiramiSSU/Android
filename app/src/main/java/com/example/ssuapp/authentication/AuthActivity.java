package com.example.ssuapp.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.example.ssuapp.ProjectListActivity;
import com.example.ssuapp.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        SignInButton firebaseuiauthbtn = findViewById(R.id.firebaseuiauthbtn);        //"인증" 버튼
        firebaseuiauthbtn.setOnClickListener(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {  //인증이 되어 있는 상태
            firebaseuiauthbtn.setEnabled(false);
        } else            //인증이 되어 있지 않은 상태
        {
            firebaseuiauthbtn.setEnabled(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                Intent i = new Intent(this, SignedInActivity.class);
                i.putExtras(data);
                startActivity(i);
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.firebaseuiauthbtn)
            signin();
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
     *
     * @return 테마 정보
     */
    private int getSelectedTheme() {
        return AuthUI.getDefaultTheme();    //기본테마
    }

    private int getSelectedLogo() {
        return R.drawable.icon_dog;
    }

    /**
     * FirebaseUI를 통해 제공 받을 인증 서비스 설정
     *
     * @return 인증 서비스
     */
    private List<AuthUI.IdpConfig> getSelectedProviders() {
        List<AuthUI.IdpConfig> selectedProviders = new ArrayList<>();
        CheckBox googlechk = (CheckBox) findViewById(R.id.google_provider);

        if (googlechk.isChecked()) {
            selectedProviders.add(new AuthUI.IdpConfig.GoogleBuilder().build());
        }

        return selectedProviders;
    }
}