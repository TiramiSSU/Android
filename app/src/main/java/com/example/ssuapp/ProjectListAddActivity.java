package com.example.ssuapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class ProjectListAddActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance(); //파이어베이스 db 접근
    String userid;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list_add);

        FirebaseUser curUser = FirebaseAuth.getInstance().getCurrentUser();
        if (curUser != null) {
            for (UserInfo profile : curUser.getProviderData()) {
                userid = profile.getUid();
                if (profile.getDisplayName() != "")
                    username = profile.getDisplayName();
            }
        }
    }

    //저장버튼 클릭
    public void onClickSave(View view) {
        //저장버튼 클릭시 edittext의 값을 데이터베이스에 저장해야함
        //데이터베이스 저장할때 UserId 컬렉션에 projectlist String 추가해주고 루트 컬렉션을 추가함 && 루트컬렉션 만들면 기본 문서 초기화하고 만든사람 참여자로 넣가
        //기본 초기화 == 팀이름 최초컬렉션
        //팀이름 - progress - totalprogress
        //                  - eachprogress
        EditText editText = findViewById(R.id.editTextProjectName);
        final String projectName = editText.getText().toString().trim();
        //프로젝트 이름 입력칸이 비어있을때
        if (projectName.length() == 0) {
            Toast myToast = Toast.makeText(this.getApplicationContext(), "프로젝트 이름을 입력해주세요", Toast.LENGTH_SHORT);
            myToast.show();
        } else if (projectName.contains("@")) {
            Toast myToast = Toast.makeText(this.getApplicationContext(), "@  은 사용 불가능한 문자입니다.", Toast.LENGTH_SHORT);
            myToast.show();
        }
        //프로젝트 이름 입력이 되어있을때
        //데이터 베이스에 추가해줌
        else {
            //DB에 projectName을 가진 새 컬렉션 만들기
            Map<String, String> myUId = new HashMap<>();
            myUId.put(userid, username);
            db.collection(projectName).document("Progress")
                    .set(myUId)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("jinwoo/", "DB에 추가 완료");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("jinwoo/", "DB에 추가 실패");
                        }
                    });

            DBTodoList initTodoList = new DBTodoList();
            db.collection(projectName).document("Progress")
                    .collection("TotalProgress").document("TotalList").set(initTodoList);

            //DB UserID에 새 프로젝트 추가해주기
            DocumentReference docRef = db.collection("UserID").document(userid);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    int cnt;
                    String projectlist;
                    DBUserInformation userInfo = documentSnapshot.toObject(DBUserInformation.class);
                    //기존 프로젝트가 0개일때
                    if (userInfo.getProjectcnt() == 0) {
                        cnt = userInfo.getProjectcnt();
                        cnt++;
                        projectlist = projectName;
                        userInfo.setProjectcnt(cnt);
                        userInfo.setProjectlist(projectlist);
                        //이부분은 나중에 각자의 이름으로 바꿔야하나 지금은 개인으로 사용중
                        userInfo.setName(username);
                        db.collection("UserID").document(userid).set(userInfo);
                    }
                    //추가할게 1개이상일 때
                    else {
                        cnt = userInfo.getProjectcnt();
                        cnt++;
                        projectlist = userInfo.getProjectlist();
                        projectlist = projectlist.concat("@" + projectName);
                        userInfo.setProjectcnt(cnt);
                        userInfo.setProjectlist(projectlist);
                        userInfo.setName(username);
                        db.collection("UserID").document(userid).set(userInfo);
                    }
                }
            });
            finish();
        }
    }

    //취소버튼 클릭
    public void onClickCancle(View view) {
        finish();
    }
}
