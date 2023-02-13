package com.xiao;

import com.spring.XiaoxiaoApplicationContext;
import com.xiao.service.UserService;

public class Test1 {

    public static void main(String[] args) {
        //扫描  -创建Bean
        XiaoxiaoApplicationContext context=new XiaoxiaoApplicationContext(Appconfig.class);

        UserService userService= (UserService) context.getBean("userService");
        UserService userService1= (UserService) context.getBean("userService");

        UserService userService2= (UserService) context.getBean("userService");
        System.out.println(userService);
        System.out.println(userService1);
        System.out.println(userService2);

        userService.show();

    }
}
