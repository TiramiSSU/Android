package com.example.ssuapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectManagementActivity extends AppCompatActivity {

    final String userid = "iw2swiambfs";
    FirebaseFirestore db = FirebaseFirestore.getInstance(); //파이어베이스 db 접근
    AlertDialog myProjectDeleteDialog;  //프로젝트 삭제 다이얼로그
    String projectName;


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_project_management);

        //Intent 로 프로젝트 이름 받아오기
        Intent intent = getIntent();
        projectName = intent.getExtras().getString("ProjectName");
        Log.d("jinwoo/", "받은 이름: " + projectName);

        db.collection(projectName).document("Progress");

    }

    //프로젝트 삭제 및 참여자 버튼은 툴바에 바꿀것
    public void onClickProjectDelete(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.icon_warning);
        builder.setTitle("경고");
        builder.setMessage("정말 삭제 하시겠습니까?");
        //확인버튼 취소버튼 아이콘 확인해보기
        builder.setPositiveButton("삭제", dialogListener);
        builder.setNegativeButton("취소", null);
        myProjectDeleteDialog = builder.create();
        myProjectDeleteDialog.show();
    }

    public void onClickJoinedPeople(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("참여자");
        myProjectDeleteDialog = builder.create();
        myProjectDeleteDialog.show();
    }

    //프로젝트 삭제에서 삭제 했을때 부를 리스너
    //여기서 DB값도 같이 삭제함
    DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (dialog == myProjectDeleteDialog) {
                Log.d("jinwoo/", projectName + "프로젝트 삭제버튼 클릭");
                DocumentReference docRef = db.collection("UserID").document(userid);
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        int cnt;
                        String projectlist;
                        String[] eachProject;
                        DBUserInformation userInfo = documentSnapshot.toObject(DBUserInformation.class);
                        cnt = userInfo.getProjectcnt();
                        //삭제할 프로젝트가 없을때 예외처리
                        if (cnt == 0) {
                            Log.d("jinwoo/", "프로젝트 0개일때 삭제");
                        }
                        //삭제할 프로젝트가 1개일때
                        else if (cnt == 1) {
                            //projectcnt 감소
                            cnt--;
                            userInfo.setProjectcnt(cnt);
                            projectlist = "";
                            userInfo.setProjectlist(projectlist);
                            db.collection("UserID").document(userid).set(userInfo);
                            Log.d("jinwoo/", "프로젝트 1개일때 삭제");
                        }
                        //그외의 경우
                        else {
                            //구분자를 활용하여 '/'  String 을 사용 관리하여 가지고있는 프로젝트 리스트 관리
                            cnt--;
                            userInfo.setProjectcnt(cnt);
                            eachProject = documentSnapshot.getString("projectlist").split("/");
                            List<String> list = new ArrayList<>(Arrays.asList(eachProject));
                            list.remove(projectName);
                            projectlist = list.stream().collect(Collectors.joining("/"));
                            userInfo.setProjectlist(projectlist);
                            db.collection("UserID").document(userid).set(userInfo);
                            Log.d("jinwoo/", "프로젝트 2개이상일때 삭제");
                        }
                    }
                });
                finish();   //삭제후 종료
            }
        }
    };

    //유동적으로 동적 뷰 관리
    @Override
    public void onResume(){
        LinearLayout totalAimLayout = findViewById(R.id.totalAimLinearLayout);
        totalAimLayout.removeAllViews();

        //커스텀뷰 관리
        TodoListView todoListView = new TodoListView(getApplicationContext());
        todoListView.setTotalTodoText("이것이 들어간다면 성공입니다");
        totalAimLayout.addView(todoListView);
        TodoListView todoListView_2 = new TodoListView(getApplicationContext());
        todoListView_2.setTotalTodoText("이것도 넣어야징~");
        todoListView_2.setPadding(20,0,0,0);
        totalAimLayout.addView(todoListView_2);

        super.onResume();
    }
//
//    FirebaseFirestore db = FirebaseFirestore.getInstance(); //파이어베이스 db 접근
//    Button[] totalBtnAry;   //전체목표 버튼
//    int[] totalBtnId;       //전체목표 버튼ID 저장
//    int total0btnId;          //전체목표0 버튼ID저장
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_project_management);
//        final String TAG_JW = getString(R.string.TAG_JW);
////        //전체 진행도 관리
////        Log.d(TAG_JW, "프로젝트 진행도 관리 액티비티 실행");
////        DocumentReference docRef = db.
////                collection("PatraSSU").
////                document("TeamJw").
////                collection("ProjectManagement").
////                document("TotalAim");
////        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
////            @Override
////            public void onSuccess(DocumentSnapshot documentSnapshot) {
////                Log.d(TAG_JW+"project_management", "프로젝트 진행도 db에서 받아오기");
////                //값 설정
////                int totalAimcnt = toIntExact(documentSnapshot.getLong("Aimcnt"));
////                String index, totalAim;
////
////                //세팅할 동적 버튼 설정
////                totalBtnAry = new Button[totalAimcnt];
////
////                //전체목표 버튼을 생성할 스크롤뷰 안의 레이아웃
////                LinearLayout totalAimLayout = (LinearLayout) findViewById(R.id.totalAimLinearLayout);
////                //전체 목표가 없을 시
////                if (totalAimcnt == 0) {
////                    Log.d(TAG_JW+"project_management", "전체 목표 개수: 0");
////                    Button total0Btn = new Button(getBaseContext());
////                    total0Btn.setId(View.generateViewId());
////                    total0Btn.setHeight(400);
////                    total0Btn.setWidth(400);
////                    total0Btn.setText("전체 목표 추가");
////                    //추가버튼 클릭시 추가하는 액티비티로 전환
////                    total0Btn.setOnClickListener(new View.OnClickListener() {
////                        @Override
////                        public void onClick(View view) {
////                            Intent intent = new Intent(getApplicationContext(), ProjectAimAddActivity.class);
////                            startActivity(intent);
////                        }
////                    });
////                    totalAimLayout.addView(total0Btn);
////                }
////                //전체 목표가 있을시 버튼 추가
////                else {
////                    Log.d(TAG_JW+"project_management", "전체 목표 개수: " + totalAimcnt);
////                    for (int i = 0; i < totalAimcnt; i++) {
////                        totalBtnAry[i] = new Button(getBaseContext());
////                        index = Integer.toString(i);
////                        totalAim = documentSnapshot.getString(index);
////
////                        totalBtnAry[i].setId(View.generateViewId());
////                        totalBtnAry[i].setHeight(400);
////                        totalBtnAry[i].setWidth(400);
////                        totalBtnAry[i].setText(totalAim);
////                        totalAimLayout.addView(totalBtnAry[i]);
////                    }
////                }
////            }
////        });
//////전체 진행도 관리
////개인 진행도 관리
////개인 진행도 관리
//
////참고자료 관리
////참고자료 관리
//    }
//
//    @Override
//    public void onResume() {
//        final String TAG_JW = getString(R.string.TAG_JW);
//        //데이터 변경되었을때
//        final DocumentReference docRef = db.
//                collection("PatraSSU").
//                document("TeamJw").
//                collection("ProjectManagement").
//                document("TotalAim");
//
//        docRef.addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                Log.d("jinwoo/", "데이터가 변경되었음");
//                Log.d("jinwoo/", documentSnapshot.getId() + "=>" + documentSnapshot.getData());
//
//                LinearLayout linearLayout = findViewById(R.id.totalAimLinearLayout);
//                linearLayout.removeAllViews();
////전체 진행도 관리
//                Log.d(TAG_JW, "프로젝트 진행도 관리 액티비티 실행");
//
//
//                Log.d(TAG_JW + "project_management", "프로젝트 진행도 db에서 받아오기");
//                //값 설정
//                int totalAimcnt = toIntExact(documentSnapshot.getLong("Aimcnt"));
//                String index, totalAim;
//
//                //세팅할 동적 버튼 설정
//                totalBtnAry = new Button[totalAimcnt];
//
//                //전체목표 버튼을 생성할 스크롤뷰 안의 레이아웃
//                LinearLayout totalAimLayout = findViewById(R.id.totalAimLinearLayout);
//                //전체 목표가 없을 시
//                if (totalAimcnt == 0) {
//                    Log.d(TAG_JW + "project_management", "전체 목표 개수: 0");
//                    Button total0Btn = new Button(getBaseContext());
//                    total0Btn.setId(View.generateViewId());
//                    total0Btn.setHeight(400);
//                    total0Btn.setWidth(400);
//                    total0Btn.setText("전체 목표 추가");
//                    //추가버튼 클릭시 추가하는 액티비티로 전환
//                    total0Btn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Intent intent = new Intent(getApplicationContext(), ProjectAimAddActivity.class);
//                            startActivity(intent);
//                        }
//                    });
//                    totalAimLayout.addView(total0Btn);
//                }
//                //전체 목표가 있을시 버튼 추가
//                else {
//                    Log.d(TAG_JW + "project_management", "전체 목표 개수: " + totalAimcnt);
//                    for (int i = 0; i < totalAimcnt; i++) {
//                        totalBtnAry[i] = new Button(getBaseContext());
//                        index = Integer.toString(i);
//                        totalAim = documentSnapshot.getString(index);
//
//                        totalBtnAry[i].setId(View.generateViewId());
//                        totalBtnAry[i].setHeight(400);
//                        totalBtnAry[i].setWidth(400);
//                        totalBtnAry[i].setText(totalAim);
//                        totalAimLayout.addView(totalBtnAry[i]);
//                    }
//                }
////전체 진행도 관리
//            }
//        });
//        super.onResume();
//    }
}
