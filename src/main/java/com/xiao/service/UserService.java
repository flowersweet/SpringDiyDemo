package com.xiao.service;

import com.spring.Component;
import com.spring.Scope;

@Component
@Scope()
public class UserService {

    public void show(){
        System.out.println("test singletons ");
    }
}
