package com.xiao.service;

import com.spring.BeanPostProcessor;
import com.spring.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 针对所有bean 都会调用postProcessAfterInitialization
 */
@Component
public class XiaoxiaoBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {

        if(bean instanceof ProductInterface){
            Object proxyInstance=Proxy.newProxyInstance(XiaoxiaoBeanPostProcessor.class.getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    //可执行切面逻辑
                    System.out.println("日志打印 切面逻辑");
                    //执行类对象的目标方法逻辑  bean 普通对象
                    return method.invoke(bean,args);
                }
            });
            System.out.println("beanName=="+beanName);
            //返回的是代理对象
            return proxyInstance;
        }
       return bean;
    }
}
