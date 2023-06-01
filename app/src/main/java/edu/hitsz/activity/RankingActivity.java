package edu.hitsz.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.hitsz.DAO.User;
import edu.hitsz.DAO.UserDao;
import edu.hitsz.DAO.UserDaoImpl;
import edu.hitsz.R;

public class RankingActivity extends AppCompatActivity {

    UserDao userDao;
    User user;
    List<User> playerList = null;
    String name;
    int score;
    String time;

    Button returnButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankinglist);
        returnButton=findViewById(R.id.return_btn);

        switch(OffLineActivity.gameType){
            case 1:
                userDao = new UserDaoImpl(this,"simpledata.dat");
                break;
            case 2:
                userDao = new UserDaoImpl(this,"mediumdata.dat");
                break;
            case 3:
                userDao = new UserDaoImpl(this,"harddata.dat");
                break;
            default:
                System.out.println("ERROR");
        }

        user = new User();
        score = getIntent().getIntExtra("user_score",0);
        time = getIntent().getStringExtra("user_time");
        inputName();

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RankingActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }

    public void showList(){

        String[] simpleCursor = new String[] {"name", "score", "time"};

        try {
            playerList = userDao.getAllData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ListView listView = findViewById(R.id.list);
        List<Map<String, String>> maps = new LinkedList<>();
        for (User curPlayer : playerList) {
            Map<String, String> curMap = new HashMap<>();
            curMap.put("name", curPlayer.getName());
            curMap.put("score", String.valueOf(curPlayer.getScore()));
            curMap.put("time", curPlayer.getTime());
            maps.add(curMap);
        }

        SimpleAdapter adapter = new SimpleAdapter(
                this, maps, R.layout.ranking_item,
                simpleCursor, new int[] {R.id.name, R.id.score, R.id.time}
        );

        /**
         // 设置分割线
         listView.setDivider(new ColorDrawable(Color.WHITE));
         // 设置分割线的宽度
         listView.setDividerHeight(3);
         */

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {

                AlertDialog alertDialog = new AlertDialog.Builder(RankingActivity.this)
                        .setTitle("提示")
                        .setMessage("确认删除该条记录吗")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                maps.remove(position);

                                //更新排行榜文件
                                try {
                                    playerList.remove(position);
                                    userDao.storage(playerList);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                adapter.notifyDataSetChanged();
                            }
                        })

                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .create();
                alertDialog.show();
            }
        });
    }

    private void inputName() {
        EditText input = new EditText(this);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("请输入名字以记录得分")
                .setView(input);
        //.setNegativeButton("取消", null)
        builder.setPositiveButton("确定", (dialogInterface, i) -> {
            name = input.getText().toString();
            if (name.isEmpty()){
                Toast.makeText(this, "输入为空", Toast.LENGTH_SHORT).show();
            }else{
                user.setName(name);
                user.setScore(score);
                user.setOverTime(time);
                try {
                    userDao.addData(user);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                showList();
            }
        });

        builder.setNegativeButton("取消",(dialogInterface, i) -> {
            Toast.makeText(this, "您还未输入姓名", Toast.LENGTH_SHORT).show();
            showList();
        });

        builder.show();
    }
}
