package com.spring;


import com.xiao.service.BeanNameAwre;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XiaoxiaoApplicationContext {
    private Class configclass;

    private Map<String,BeanDefinition> beanDefinitionMap=new HashMap<>();
    private Map<String,Object> singletonObjects=new HashMap<>();
    //针对所有bean
    private List<BeanPostProcessor> beanPostProcessorList=new ArrayList<>();

    public XiaoxiaoApplicationContext(Class configclass){
        this.configclass=configclass;
        //扫描
        scan(configclass);
        for(Map.Entry<String,BeanDefinition> entry:beanDefinitionMap.entrySet()){
            String beanName=entry.getKey();
            BeanDefinition beanDefinition=entry.getValue();
            if(beanDefinition.getScope().equals("singleton")){
                Object bean=createBean(beanName,beanDefinition);
                singletonObjects.put(beanName,bean);
            }
        }

    }
    //创建Bean
    public Object createBean(String beanName,BeanDefinition beanDefinition){
        Object bean=null;
        Class classType=beanDefinition.getClassType();
        try {
            bean=classType.getConstructor().newInstance();
            for(Field field :classType.getDeclaredFields()){
                //属性对象需要依赖注入
                if(field.isAnnotationPresent(Autowired.class)){
                    //反射
                    field.setAccessible(true);
                    //根据属性名字找对象，赋值给当前类的对象属性
                    Object object=getBean(field.getName());
                    field.set(bean,object);
                }
            }

            //回调Aware
            if (bean instanceof BeanNameAwre) {
                ((BeanNameAwre) bean).setBeanName(beanName);
            }

            //初始化
            if(bean instanceof InitializingBean){
                ((InitializingBean) bean).afterPropertiesSet();
            }

            //初始化后
            for(BeanPostProcessor postProcessor:beanPostProcessorList){
                bean=postProcessor.postProcessAfterInitialization(bean,beanName);
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }

    public void scan(Class configclass){
        if(configclass.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScan = (ComponentScan) configclass.getAnnotation(ComponentScan.class);
            String path = componentScan.value();
            path = path.replace(".", "/");

            ClassLoader classLoader = XiaoxiaoApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(path);
            File file = new File(resource.getFile());
            List<String> absPatharr = new ArrayList<String>();
            getClassPath(file, classLoader, absPatharr);
            for (String absPath : absPatharr) {
                try {
                    Class<?> clazz = classLoader.loadClass(absPath);
                    if (clazz.isAnnotationPresent(Component.class)) {
                        //当前类实现了BeanPostProcessor接口
                        if(BeanPostProcessor.class.isAssignableFrom(clazz)){
                            BeanPostProcessor beanPostProcessor=(BeanPostProcessor)clazz.getConstructor().newInstance();
                            beanPostProcessorList.add(beanPostProcessor);
                        }

                        Component component = clazz.getAnnotation(Component.class);
                        String benaName = component.value();
                        if ("".equals(benaName)) {
                            benaName=Introspector.decapitalize(clazz.getSimpleName());
                        }
                        BeanDefinition beanDefinition = new BeanDefinition();

                        String scopeValue="";
                        if (clazz.isAnnotationPresent(Scope.class)) {
                            Scope scope = clazz.getAnnotation(Scope.class);
                            scopeValue=scope.value();
                        }
                        beanDefinition.setScope(scopeValue);
                        if(!"prototype".equals(scopeValue)){
                            beanDefinition.setScope("singleton");
                        }
                        beanDefinition.setClassType(clazz);
                        beanDefinitionMap.put(benaName, beanDefinition);

                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void getClassPath(File file, ClassLoader classLoader,List<String> absPatharr ) {

        if (file.isDirectory()) {
            for (File file1 : file.listFiles()) {
                String absPath = file1.getAbsolutePath();
                if (absPath.endsWith(".class")) {
                    absPath = absPath.substring(absPath.indexOf("com"), absPath.indexOf(".class"));
                    absPath = absPath.replace("\\", ".");
                    absPatharr.add(absPath);
                } else {
                    //循环递归
                    getClassPath(new File(absPath), classLoader,absPatharr);
                }
            }
        }
    }

    public Object getBean(String beanName){
        if(!beanDefinitionMap.containsKey(beanName)){
            throw new NullPointerException();
        }
        BeanDefinition beanDefinition=beanDefinitionMap.get(beanName);
        if(beanDefinition.getScope().equals("singleton")){
            //依赖注入对象时，被注入的对象可能还没有创建bean
            if(singletonObjects.containsKey(beanName)){
                return singletonObjects.get(beanName);
            }else{
                Object bean=createBean(beanName,beanDefinition);
                singletonObjects.put(beanName,bean);
                return bean;
            }
        }else{
            return createBean(beanName,beanDefinition);
        }
    }
}
