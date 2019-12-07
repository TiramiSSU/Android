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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class ProjectListAddActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance(); //파이어베이스 db 접근
    final String userid = "iw2swiambfs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list_add);
    }

    //저장버튼 클릭
    public void onClickSave(View view){
        //저장버튼 클릭시 edittext의 값을 데이터베이스에 저장해야함
        //데이터베이스 저장할때 UserId 컬렉션에 projectlist String 추가해주고 루트 컬렉션을 추가함 && 루트컬렉션 만들면 기본 문서 초기화하고 만든사람 참여자로 넣가
        //기본 초기화 == 팀이름 최초컬렉션
        //팀이름 - progress - totalprogress
        //                  - eachprogress
        EditText editText = findViewById(R.id.editTextProjectName);
        final String projectName = editText.getText().toString().trim();
        //프로젝트 이름 입력칸이 비어있을때
        if(projectName.length() == 0){
            Log.d("jinwoo/","프로젝트 이름칸이 비어있음");
            Toast myToast = Toast.makeText(this.getApplicationContext(),"프로젝트 이름을 입력해주세요",Toast.LENGTH_SHORT);
            myToast.show();
        }
        //프로젝트 이름 입력이 되어있을때
        //데이터 베이스에 추가해줌
        else{
            Log.d("jinwoo/","프로젝트 이름 ="+projectName);
            //DB에 새 컬렉션 만들기
            Map<String, String> myUId = new HashMap<>();
            myUId.put(userid,"박진우");
            db.collection(projectName).document("Progress")
                    .set(myUId)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("jinwoo/","DB에 추가 완료");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("jinwoo/","DB에 추가 실패");
                        }
                    });

            //DB UserID에 새 프로젝트 추가해주기
            DocumentReference docRef = db.collection("UserID").document(userid);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    int cnt;
                    String name, projectlist;
                    DBUserInformation userInfo = documentSnapshot.toObject(DBUserInformation.class);
                    //기존 프로젝트가 0개일때
                    if(userInfo.getProjectcnt() == 0){
                        cnt = userInfo.getProjectcnt();
                        cnt++;
                        projectlist = projectName;
                        userInfo.setProjectcnt(cnt);
                        userInfo.setProjectlist(projectlist);
                        Log.d("jinwoo/","DB에 추가할 내용 cnt"+userInfo.getProjectcnt()+ "리스트"+userInfo.getProjectlist());
                    }
                    //추가할게 1개이상일 때
                    else{
                        cnt = userInfo.getProjectcnt();
                        cnt++;
                        projectlist = userInfo.getProjectlist();
                        projectlist = projectlist.concat("/"+projectName);
                        userInfo.setProjectcnt(cnt);
                        userInfo.setProjectlist(projectlist);
                        Log.d("jinwoo/","DB에 추가할 내용 cnt"+userInfo.getProjectcnt()+ "리스트"+userInfo.getProjectlist());
                        db.collection("UserID").document(userid).set(userInfo);
                    }
                }
            });
        }
        finish();
    }
    //취소버튼 클릭
    public void onClickCancle(View view){
        finish();
    }
}
