package com.example.ssuapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static java.lang.Math.toIntExact;

public class ProjectAimAddActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance(); //파이어베이스 db 접근

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_aim_add);
    }
    //저장버튼 클릭
    public void onClickSave(View view){
        DocumentReference docRef = db.
                collection("PatraSSU").
                document("TeamJw").
                collection("ProjectManagement").
                document("TotalAim");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //작성한 것을 db에 추가
                //db의 목표 개수 증가
                int aimcnt = toIntExact(documentSnapshot.getLong("Aimcnt"));
                aimcnt++;
                db.collection("PatraSSU").
                        document("TeamJw").
                        collection("ProjectManagement").
                        document("TotalAim").update("Aimcnt",aimcnt);
            }
        });
        finish();
    }
    //취소버튼 클릭
    public void onClickCancle(View view){
        finish();
    }
}
