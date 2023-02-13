package com.spring;


public class BeanDefinition {
    private  Class classType;
    private String scope;
    private boolean lazy;
    public void setClassType(Class classType){
        this.classType=classType;
    }
    public void setScope(String scope){
        this.scope=scope;
    }
    public void setLazy(boolean lazy){
        this.lazy=lazy;
    }
    public Class getClassType(){
        return classType;
    }
    public String getScope(){
        return scope;
    }
    public boolean getLazy(){
        return lazy;
    }

}
