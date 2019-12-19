package com.example.ssuapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;

public class ProjectListActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance(); //파이어베이스 db 접근

    //유저 아이디 부분
    TextView userName;
    TextView userEmail;
    TextView userId;

    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        userId = findViewById(R.id.userId);
        String name = "baseUser";
        if (curUser != null) {
            for (UserInfo profile : curUser.getProviderData()) {
                String providerID = profile.getProviderId();
                userid = profile.getUid();

                name = profile.getDisplayName();
                String email = profile.getEmail();
                Uri photoUri = profile.getPhotoUrl();

                if (name != "")
                    userName.setText(name);
                if (email != "")
                    userEmail.setText(email);
                if (userid != "")
                    userId.setText(userid);
            }
        }
    }

    @Override
    protected void onResume() {
//프로젝트 리스트 출력부분
        //각 기기의 고유값으로 userID생성후 접근 but 지금은 정적으로 접근
        final ViewGroup.LayoutParams btnParams = new ViewGroup.LayoutParams(400, ViewGroup.LayoutParams.MATCH_PARENT);

        DocumentReference docRef = db.collection("UserID").document(userid);
        docRef.addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                LinearLayout projectListLayout = findViewById(R.id.ProjectListLinearLayout);
                projectListLayout.removeAllViews();
                //비어있을때는 새로 생성
                if (documentSnapshot.getLong("projectcnt") == null) {
                    ProjectList userProjectList = new ProjectList();
                    userProjectList.setProjectcnt(0);
                    userProjectList.setProjectlist(null);
                    db.collection("UserID").document(userid).set(userProjectList);
                }
                //참여중프로젝트가 0개일때
                else if (documentSnapshot.getLong("projectcnt") == 0) {
                } else {
                    String projectlist = documentSnapshot.getString("projectlist");
                    int slashcnt = documentSnapshot.getLong("projectcnt").intValue();

                    //참여중인 프로젝트가 1개일때
                    if (slashcnt == 1) {
                        final Button joinProjectBtn = new Button(getBaseContext());
                        joinProjectBtn.setId(View.generateViewId());
                        joinProjectBtn.setText(documentSnapshot.getString("projectlist"));
                        joinProjectBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getApplicationContext(), ProjectManagementActivity.class);
                                intent.putExtra("ProjectName", joinProjectBtn.getText().toString());
                                startActivity(intent);
                            }
                        });
                        projectListLayout.addView(joinProjectBtn, btnParams);
                    }

                    //참여중인 프로젝트가 2개 이상일때
                    else {
                        String[] arr = documentSnapshot.getString("projectlist").split("@");
                        Button[] joinedProjectBtn = new Button[arr.length];
                        for (int i = 0; i < arr.length; i++) {
                            joinedProjectBtn[i] = new Button(getBaseContext());
                            joinedProjectBtn[i].setId(View.generateViewId());
                            joinedProjectBtn[i].setText(arr[i]);
                            final String tempStr = arr[i];
                            joinedProjectBtn[i].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getApplicationContext(), ProjectManagementActivity.class);
                                    intent.putExtra("ProjectName", tempStr);
                                    startActivity(intent);
                                }
                            });
                            projectListLayout.addView(joinedProjectBtn[i], btnParams);
                        }
                    }
                }
                //추가하는 버튼은 항상 추가
                Button addProjectBtn = new Button(getBaseContext());
                addProjectBtn.setId(View.generateViewId());
                addProjectBtn.setText("프로젝트를 추가해주세요");
                addProjectBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), ProjectListAddActivity.class);
                        startActivity(intent);
                    }
                });
                projectListLayout.addView(addProjectBtn, btnParams);
            }
        });
        //각 팀에 속해있는지 판단후 팀이 없다면 추가를 있다면 리스트도 포함하여 출력

        super.onResume();
    }
}
