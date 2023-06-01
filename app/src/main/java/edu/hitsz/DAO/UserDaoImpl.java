package edu.hitsz.DAO;

import android.content.Context;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UserDaoImpl implements UserDao{

    //模拟数据库数据
    public List<User> userList = new ArrayList<>();
    private Context context;
    private String filename;

    public UserDaoImpl(Context context,String filename){
        this.context=context;
        this.filename=filename;
        try(ObjectInputStream ois = new ObjectInputStream(context.openFileInput(this.filename))){
            userList = (List<User>)ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addData(User user) throws IOException, ClassNotFoundException {
        userList.add(user);
        storage(userList);
    }

    public void storage(List<User> userList) throws IOException {
        //根据分数排序
        Collections.sort(userList, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o2.getScore()-o1.getScore();
            }
        });

        //将榜单写回文件
        ObjectOutputStream oos = new ObjectOutputStream(context.openFileOutput(this.filename, Context.MODE_PRIVATE));
        oos.writeObject(userList);
        oos.close();
    }


    @Override
    public void deleteData(int num){
        userList.remove(userList.get(num));

        Collections.sort(userList, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o2.getScore()-o1.getScore();
            }
        });
    }

    @Override
    public List<User> getAllData() {
        return userList;
    }
}
