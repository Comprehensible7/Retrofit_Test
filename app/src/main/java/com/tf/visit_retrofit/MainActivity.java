package com.tf.visit_retrofit;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dao.VisitRetrofitDao;
import myutil.MyUtil;
import vo.VisitVo;

public class MainActivity extends AppCompatActivity {

    //상수선언
    public static final int REQUEST_CODE_VISIT_DETAIL = 1;
    public static final int REQUEST_CODE_VISIT_INPUT  = 2;
    public static final int REQUEST_CODE_VISIT_UPDATE = 3;

    RecyclerView rv_visit_list;

    VisitRetrofitDao dao;

    List<VisitVo> visit_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //리사이클러뷰 참조값
        rv_visit_list = findViewById(R.id.rv_visit_list);
        //배치모형 설정
        rv_visit_list.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        //Decoration설정
        rv_visit_list.addItemDecoration(new VisitViewHolderDecoration());


        //Network상에서 데이터 처리객체
        dao = VisitRetrofitDao.getInstance();

        //방명록 읽어오기
        read_visit_list();

    }

    //방명록 읽어오기
    public void read_visit_list(){

        //retrofit api 이용
        dao.selectList(new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(msg.what==1){
                    visit_list = dao.getList();

                    //배치 어뎁터 설정
                    rv_visit_list.setAdapter(new VisitAdapter());
                }
            }
        });

    }

    //방명록 쓰기 클릭시
    public void onClick(View view){

        Intent intent = new Intent(this,VisitInsertActivity.class);
        startActivityForResult(intent,REQUEST_CODE_VISIT_INPUT);

    }

    //방명록 갱신하기
    public void onClickRefresh(View view){

        //방명록 읽어오기
        read_visit_list();
    }

//---리사이클러뷰 배치어뎁터 ---------------------------------
    class VisitAdapter extends RecyclerView.Adapter<VisitAdapter.VisitViewHolder> {

        @NonNull
        @Override
        public VisitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            //ViewHolder에 넣어줄 뷰생성(inflate)
            View view = getLayoutInflater().inflate(R.layout.visit,parent,false);

            return new VisitViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull VisitViewHolder holder, int position) {
            //View Holder내의 ItemView내용 채우기
            VisitVo vo = visit_list.get(position);

            String content = vo.getContent().replaceAll("<br>","\n");
            holder.tv_content.setText(content);

            String str_name_date = String.format("%s(%s)",
                                             vo.getName(),
                                             vo.getRegdate().substring(0,10));
            holder.tv_name.setText(str_name_date);

            holder.select_vo = vo;
        }

        @Override
        public int getItemCount() {
            //ViewHolder(ItemView)갯수 지정
            return visit_list.size();
        }

        //ItemView
        class VisitViewHolder extends RecyclerView.ViewHolder{

            TextView tv_content;
            TextView tv_name;

            VisitVo select_vo;

            public VisitViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_content  = itemView.findViewById(R.id.tv_content);
                tv_name     = itemView.findViewById(R.id.tv_name);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*Toast.makeText(MainActivity.this,
                                select_vo.getName() + ": ViewHolder Click",
                                Toast.LENGTH_SHORT).show();*/
                        //상세보기 화면 띄우기
                        Intent intent = new Intent(MainActivity.this,VisitDetailActivity.class);
                        //intent통해서 전달되는 객체는 반드시 직렬화가능한 객체이어야 한다.
                        intent.putExtra("vo",select_vo);
                        startActivityForResult(intent,REQUEST_CODE_VISIT_DETAIL);

                    }
                });
            }

        }
    }//end VisitAdapter

    //Activity결과 수신용 메소드 재정의
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_CODE_VISIT_DETAIL){
            if(resultCode==RESULT_OK){

                String job_state = data.getStringExtra("job_state");
                VisitVo vo = (VisitVo)data.getSerializableExtra("vo");
                if(job_state.equals("delete")){
                    //삭제작업
                    dao.delete(vo.getIdx(),new Handler(){
                        @Override
                        public void handleMessage(@NonNull Message msg) {
                            super.handleMessage(msg);
                            if(msg.what==1){
                                //목록가져오기
                                read_visit_list();
                                MyUtil.showMessageDialog(MainActivity.this,"","삭제성공");
                            }else{
                                MyUtil.showMessageDialog(MainActivity.this,"","삭제실패");
                            }
                        }
                    });


                }else if(job_state.equals("update")){
                    //수정작업
                    Intent intent = new Intent(MainActivity.this,VisitUpdateActivity.class);
                    //수정대상
                    intent.putExtra("vo",vo);

                    //호출->결과수신
                    startActivityForResult(intent,REQUEST_CODE_VISIT_UPDATE);
                }
            }
        }
        //입력(VisitInsertActivity)로 부터 결과가 수신되면
        else if(requestCode==REQUEST_CODE_VISIT_INPUT) {
            if (resultCode == RESULT_OK) {
                //직렬화를 통해서 전달된 객체
                VisitVo vo = (VisitVo) data.getSerializableExtra("vo");

                //vo내용을 insert 시킨다

                dao.insert(vo,new Handler(){
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        if(msg.what==1){
                            //목록가져오기
                            read_visit_list();
                            MyUtil.showMessageDialog(MainActivity.this,"","등록성공");
                        }else{
                            MyUtil.showMessageDialog(MainActivity.this,"","등록실패");
                        }
                    }
                });



            }
        }//end: REQUEST_CODE_VISIT_INPUT
        //수정(VisitUpdateActivity)로 부터 결과가 수신되면
        else if(requestCode==REQUEST_CODE_VISIT_UPDATE) {
            if (resultCode == RESULT_OK) {
                VisitVo vo = (VisitVo) data.getSerializableExtra("vo");

                //수정
                dao.update(vo,new Handler(){
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        if(msg.what==1){
                            //목록가져오기
                            read_visit_list();
                            MyUtil.showMessageDialog(MainActivity.this,"","수정성공");
                        }else{
                            MyUtil.showMessageDialog(MainActivity.this,"","수정실패");
                        }
                    }
                });

            }
        }
    }

    //ItemDecoration-------------
    class VisitViewHolderDecoration extends  RecyclerView.ItemDecoration{

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int index = parent.getChildAdapterPosition(view)+1;
            if(index%3==0)
                outRect.set(20,20,20,80);
            else
                outRect.set(20,20,20,20);

            view.setBackgroundColor(0xffece9ff);
            ViewCompat.setElevation(view,20.0f);
        }
    }
//End VisitViewHolderDecoration

}
