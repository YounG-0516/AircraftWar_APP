package edu.hitsz.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import edu.hitsz.R;

public class OverActivity extends AppCompatActivity {

    TextView text_1,text_2,text_3;
    TextView winnerName;
    Button returnBtn;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over);
        text_1=findViewById(R.id.text1);
        text_2=findViewById(R.id.text2);
        text_3=findViewById(R.id.text3);
        winnerName=findViewById(R.id.winner_name);
        returnBtn=findViewById(R.id.return_button);


        int myScore = getIntent().getIntExtra("myScore",0);
        int otherScore = getIntent().getIntExtra("otherScore",0);
        String myName = getIntent().getStringExtra("myName");
        String otherName = getIntent().getStringExtra("opName");

        text_1.setText("你的分数是： "+myScore);
        text_2.setText("对手分数是： "+otherScore);

        if(myScore>otherScore){
            winnerName.setText(myName);
            text_3.setText("恭喜你，战胜了对手！");
        }else if((myScore<otherScore)){
            winnerName.setText(otherName);
            text_3.setText("很遗憾，你输了！");
        }else{
            winnerName.setText(otherName);
            text_3.setText("好巧呀！平局!");
        }


        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OverActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
