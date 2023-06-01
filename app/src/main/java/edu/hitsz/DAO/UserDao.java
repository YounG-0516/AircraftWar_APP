package edu.hitsz.DAO;

import java.io.IOException;
import java.util.List;

public interface UserDao {


     //将新的成绩加入榜单，并对榜单进行由高到低排序
     void addData(User user) throws IOException, ClassNotFoundException;

     //删除数据
     void deleteData(int num);

     //将数据存回文件
     void storage(List<User> userList) throws IOException;

     //获取所有数据
     List<User> getAllData() throws IOException;

}
