package com.example.ssuapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static java.lang.Math.toIntExact;

public class ProjectManagementActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance(); //파이어베이스 db 접근
    Button[] totalBtnAry;   //전체목표 버튼
    int[] totalBtnId;       //전체목표 버튼ID 저장
    int total0btn;          //전체목표0 버튼ID저장

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("jinwoo/", "프로젝트 진행도 관리 액티비티 실행");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_management);

//전체 진행도 관리
        DocumentReference docRef = db.
                collection("PatraSSU").
                document("TeamJw").
                collection("ProjectManagement").
                document("TotalAim");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("jinwoo/project_management", "프로젝트 진행도 db에서 받아오기");
                //값 설정
                int totalAimcnt = toIntExact(documentSnapshot.getLong("index"));
                String index, totalAim;

                //세팅할 동적 버튼 설정
                totalBtnAry = new Button[totalAimcnt];

                //전체목표 버튼을 생성할 스크롤뷰 안의 레이아웃
                LinearLayout totalAimLayout = (LinearLayout) findViewById(R.id.totalAimLinearLayout);
                //전체 목표가 없을 시
                if (totalAimcnt == 0) {
                    Log.d("jinwoo/project_management", "전체 목표 개수: 0");
                    Button total0Btn = new Button(getBaseContext());
                    total0Btn.setId(View.generateViewId());
                    total0Btn.setHeight(400);
                    total0Btn.setWidth(400);
                    total0Btn.setText("전체 목표 추가");
                    totalAimLayout.addView(total0Btn);
                }
                //전체 목표가 있을시 버튼 추가
                else
                    for (int i = 0; i < totalAimcnt; i++) {
                        Log.d("jinwoo/project_management", "전체 목표 개수: " + totalAimcnt);
                        totalBtnAry[i] = new Button(getBaseContext());
                        index = Integer.toString(i);
                        totalAim = documentSnapshot.getString(index);

                        totalBtnAry[i].setId(View.generateViewId());
                        totalBtnAry[i].setHeight(400);
                        totalBtnAry[i].setWidth(400);
                        totalBtnAry[i].setText(totalAim);
                        totalAimLayout.addView(totalBtnAry[i]);
                    }
            }
        });
//전체 진행도 관리

//개인 진행도 관리
//개인 진행도 관리

//참고자료 관리
//참고자료 관리
    }
}
