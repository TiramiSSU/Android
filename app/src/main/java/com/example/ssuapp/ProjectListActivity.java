package com.example.ssuapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ssuapp.authentication.AuthActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GithubAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
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
    TextView userNameTextView;

    DBUserInformation userInformation;
    String userid;
    String useremail;
    String username;

    Button invitedProject;
    AlertDialog invitedDialog;
    String invitedProjectName;
    String changeStr;
    int cnt;

    Button signOutBtn;
    Button timetableBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        signOutBtn = findViewById(R.id.signOut);
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance()
                        .signOut(ProjectListActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(ProjectListActivity.this, AuthActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                } else {
                                }
                            }
                        });
            }
        });

        timetableBtn = findViewById(R.id.view_timetable_btn);
        timetableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectListActivity.this, TimetableActivity.class);
                startActivity(intent);
            }
        });

        FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
        userNameTextView = findViewById(R.id.userName);
        if (curUser != null) {
            for (UserInfo profile : curUser.getProviderData()) {
                if (!profile.getUid().equals(""))
                    userid = profile.getUid();
                if (!profile.getDisplayName().equals(""))
                    username = profile.getDisplayName();
                if (!profile.getEmail().equals(""))
                    useremail = profile.getEmail();

                if (!username.equals(""))
                    userNameTextView.setText(username);
            }
        }

        DocumentReference documentReference = db.collection("UserID").document(userid);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
                    userInformation = documentSnapshot.toObject(DBUserInformation.class);
                    invitedProjectName = userInformation.getInvitedproject();
                    changeStr = userInformation.getProjectlist();
                    cnt = userInformation.getProjectcnt();
                } catch (NullPointerException e) {
                    DBUserInformation newUser = new DBUserInformation();
                    newUser.setId(userid);
                    newUser.setEmail(useremail);
                    newUser.setName(username);
                    newUser.setProjectcnt(0);
                    newUser.setProjectlist("");
                    newUser.setInvitedproject("");
                    db.collection("UserID").document(userid).set(newUser);
                }
            }
        });


        invitedProject = findViewById(R.id.invited_project_btn);
        invitedProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProjectListActivity.this);
                builder.setIcon(R.drawable.icon_invite);
                builder.setTitle("초대");
                try {
                    if (invitedProjectName.equals("")) {
                        builder.setMessage("초대받은 프로젝트가 없습니다.");
                        builder.setNegativeButton("확인", null);
                    } else {
                        builder.setMessage(invitedProjectName + "\r프로젝트에 참가하시겠습니까?");
                        builder.setPositiveButton("수락", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DocumentReference docRef = db.collection("UserID").document(userid);
                                docRef.update("invitedproject", "", "projectlist", changeStr + "@" + invitedProjectName, "projectcnt", ++cnt)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                                invitedProjectName = "";
                            }
                        });
                        builder.setNegativeButton("거절", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                invitedProjectName = "";
                                DocumentReference docRef = db.collection("UserID").document(userid);
                                docRef.update("invitedproject", "")
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                            }
                        });
                    }
                } catch (NullPointerException e) {
                    Log.d("jinwoo/", "초대 없다고뜸!");
                    builder.setMessage("초대받은 프로젝트가 없습니다.");
                    builder.setNegativeButton("확인", null);
                }

                invitedDialog = builder.create();
                invitedDialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        //프로젝트 리스트 출력부분
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

        DocumentReference documentReference = db.collection("UserID").document(userid);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                invitedProjectName = documentSnapshot.getString("invitedproject");
            }
        });
        //각 팀에 속해있는지 판단후 팀이 없다면 추가를 있다면 리스트도 포함하여 출력

        super.onResume();
    }
}
