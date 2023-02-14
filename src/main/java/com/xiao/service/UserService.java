package com.xiao.service;

import com.spring.Autowired;
import com.spring.Component;
import com.spring.InitializingBean;
import com.spring.Scope;

@Component
@Scope()
public class UserService  implements InitializingBean {

    @Autowired
    private ShoesService shoesService;

    public void show(){
        //productService.getProduct();
        System.out.println("test singletons ");

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.shoesService.setColor("red");
        System.out.println("inital afterPropertiesSet");
    }

    public ShoesService getShoesService() {
        return shoesService;
    }

    public void setShoesService(ShoesService shoesService) {
        this.shoesService = shoesService;
    }
}
