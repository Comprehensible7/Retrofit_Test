package com.tf.visit_retrofit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import myutil.MyUtil;
import vo.VisitVo;

public class VisitDetailActivity extends AppCompatActivity {

    VisitVo vo;

    TextView tv_content_detail;
    TextView tv_name_detail;
    TextView tv_regdate_detail;
    EditText et_pwd_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("상세보기");
        setContentView(R.layout.activity_visit_detail);

        //참조변수 얻기
        tv_content_detail   = findViewById(R.id.tv_content_detail);
        tv_name_detail      = findViewById(R.id.tv_name_detail);
        tv_regdate_detail   = findViewById(R.id.tv_regdate_detail);
        et_pwd_detail       = findViewById(R.id.et_pwd_detail);

        //Scroll기능추가
        tv_content_detail.setMovementMethod(new ScrollingMovementMethod());

        //MainActivity에서
        vo = (VisitVo) getIntent().getSerializableExtra("vo");

        String content = vo.getContent().replaceAll("<br>","\n");
        tv_content_detail.setText(content);
        String name_ip = String.format("작성자: %s(%s)(%s)",vo.getName(),vo.getIp(),vo.getPwd());
        tv_name_detail.setText(name_ip);

        String str_regdate = String.format("작성일시: %s", vo.getRegdate().substring(0,16));
        tv_regdate_detail.setText(str_regdate);



    }

    public void onClickDel(View view){

        String pwd = et_pwd_detail.getText().toString().trim();

        //비밀번호 미입력시처리...
        if(pwd.isEmpty()){

            MyUtil.showMessageDialog(this,"","비밀번호를 입력하세요");
            et_pwd_detail.setText("");
            et_pwd_detail.requestFocus();
            return;
        }

        if(vo.getPwd().equals(pwd)==false){
            MyUtil.showMessageDialog(this,"","비밀번호가 틀립니다");
            et_pwd_detail.setText("");
            et_pwd_detail.requestFocus();
            return;
        }

        //삭제 유무 확인
        MyUtil.showConfirmDialog(this,
                                 "",
                                "정말삭제 하시겠습니까?",
                                 new Handler(){
                                     @Override
                                     public void handleMessage(@NonNull Message msg) {
                                         super.handleMessage(msg);
                                         //아니오 클릭시
                                         if(msg.what==0) return;

                                         //삭제의지
                                         //호출측에 응답할 데이터
                                         Intent data = new Intent();
                                         //삭제데이터 정보 전달
                                         data.putExtra("vo",vo);
                                         data.putExtra("job_state","delete");

                                         setResult(RESULT_OK,data);

                                         finish();

                                     }
                                 }
                );

    }

    public void onClickUpdate(View view){
        String pwd = et_pwd_detail.getText().toString().trim();

        //비밀번호 미입력시처리...
        if(pwd.isEmpty()){

            MyUtil.showMessageDialog(this,"","비밀번호를 입력하세요");
            et_pwd_detail.setText("");
            et_pwd_detail.requestFocus();
            return;
        }

        if(vo.getPwd().equals(pwd)==false){
            MyUtil.showMessageDialog(this,"","비밀번호가 틀립니다");
            et_pwd_detail.setText("");
            et_pwd_detail.requestFocus();
            return;
        }

        //호출측에 응답할 데이터
        Intent data = new Intent();
        //삭제데이터 정보 전달
        data.putExtra("vo",vo);
        data.putExtra("job_state","update");

        setResult(RESULT_OK,data);

        finish();

    }

    public void onClickCancel(View view){

        setResult(RESULT_CANCELED);
        finish();
    }
}
