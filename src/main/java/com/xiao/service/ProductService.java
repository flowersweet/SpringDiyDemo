package com.xiao.service;

import com.spring.Component;

@Component
public class ProductService  implements ProductInterface{
    private String proNo;
    public void getProduct(){
        System.out.println("chanping 11");
    }

    public String getProNo() {
        return proNo;
    }

    public void setProNo(String proNo) {
        this.proNo = proNo;
    }
}
