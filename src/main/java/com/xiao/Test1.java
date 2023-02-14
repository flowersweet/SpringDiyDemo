package com.xiao;

import com.spring.XiaoxiaoApplicationContext;
import com.xiao.service.ProductInterface;
import com.xiao.service.ShoesService;
import com.xiao.service.UserService;

public class Test1 {

    public static void main(String[] args) {
        //扫描  -创建Bean
        XiaoxiaoApplicationContext context=new XiaoxiaoApplicationContext(Appconfig.class);

        UserService userService= (UserService) context.getBean("userService");

        //System.out.println(userService);
        ProductInterface productInterface= (ProductInterface) context.getBean("productService");

        System.out.println(productInterface);
        System.out.println(userService.getShoesService().getColor());

        ShoesService shoesService= (ShoesService) context.getBean("shoesService");
        System.out.println(shoesService.getBeanName());
        // userService.show();

    }
}
