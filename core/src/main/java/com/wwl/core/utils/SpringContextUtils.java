package com.wwl.core.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author wwl
 * @date 2022/7/26 13:39
 * @desc 以静态变量保存Spring ApplicationContext, 可在任何代码任何地方任何时候中取出ApplicaitonContext.
 */
@Order(Integer.MIN_VALUE)
@Component
public class SpringContextUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    /**
     * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量.
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextUtils.applicationContext = applicationContext; // NOSONAR
    }

    /**
     * 取得存储在静态变量中的ApplicationContext.
     */
    public static ApplicationContext getApplicationContext() {
        checkApplicationContext();
        return applicationContext;
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        checkApplicationContext();
        return (T) applicationContext.getBean(name);
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        return  applicationContext.getBean(clazz);
    }

    public static boolean hasBean(Class tClass){
        checkApplicationContext();
        String[] names = applicationContext.getBeanNamesForType(tClass);
        for (String name : names) {
            if(applicationContext.containsBean(name)){
                return true;
            }
        }
        return false;
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    public static <T> List<T> getAllBean(Class<T> clazz) {
        checkApplicationContext();
        Map<String, T> beanMaps = applicationContext.getBeansOfType(clazz);;
        List<T> allBeans = new ArrayList<>(beanMaps.values());
        allBeans.sort(Comparator.comparingInt(SpringContextUtils::getOrderValue));
        return allBeans;
    }

    private static int getOrderValue(Object value){
        if(value==null)
        {
            return 0;
        }
        Order order =  value.getClass().getAnnotation(Order.class);
        if(order!=null){
            return order.value();
        }
        return 0;
    }

    /**
     * 清除applicationContext静态变量.
     */
    public static void cleanApplicationContext() {
        applicationContext = null;
    }

    private static void checkApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("applicaitonContext未注入,请在applicationContext.xml中定义SpringContextHolder");
        }
    }

    public static void injectService(Object instance){
        if (instance==null) {
            return;
        }

        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            Object fieldBean = null;
            // with bean-id, bean could be found by both @Resource and @Autowired, or bean could only be found by @Autowired
            if (AnnotationUtils.getAnnotation(field, Resource.class) != null) {
                try {
                    Resource resource = AnnotationUtils.getAnnotation(field, Resource.class);
                    if (resource.name()!=null && resource.name().length()>0){
                        fieldBean = applicationContext.getBean(resource.name());
                    } else {
                        fieldBean = applicationContext.getBean(field.getName());
                    }
                } catch (Exception e) {
                }
                if (fieldBean==null ) {
                    fieldBean = applicationContext.getBean(field.getType());
                }
            } else if (AnnotationUtils.getAnnotation(field, Autowired.class) != null) {
                Qualifier qualifier = AnnotationUtils.getAnnotation(field, Qualifier.class);
                if (qualifier!=null && qualifier.value()!=null && qualifier.value().length()>0) {
                    fieldBean =applicationContext.getBean(qualifier.value());
                } else {
                    fieldBean = applicationContext.getBean(field.getType());
                }
            }
            if (fieldBean!=null) {
                field.setAccessible(true);
                try {
                    field.set(instance, fieldBean);
                } catch (Exception e) {
                }
            }
        }
    }
}
