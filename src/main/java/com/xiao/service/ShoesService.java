package com.xiao.service;

import com.spring.Component;

@Component
public class ShoesService implements  BeanNameAwre{

    private String color;

    private String beanName;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName=beanName;
    }

    public String getBeanName() {
        return beanName;
    }
}
