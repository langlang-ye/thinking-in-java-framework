package com.langlang.service;

import com.langlang.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

    @Autowired
    UserDao userDao;

    public void tt() {
        System.out.println("HelloService.tt start " );
        System.out.println("不输出11");
        System.out.println("不输出222");

        userDao.dao();
        System.out.println("不输出");

        System.out.println("HelloService.tt end " );
    }

}
