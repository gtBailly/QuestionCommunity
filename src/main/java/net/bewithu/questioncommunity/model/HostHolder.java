package net.bewithu.questioncommunity.model;

import org.springframework.stereotype.Component;

// 声明为单例bean，容器内全局唯一，因为ThreadLocal会使每个请求线程都有自己独有的user对象
@Component
public class HostHolder {
    private static  ThreadLocal<User> users= new ThreadLocal<User>();

    public  User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }
    public  void clear(){
       users.remove();
    }
}
