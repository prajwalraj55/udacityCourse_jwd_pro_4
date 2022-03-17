package com.example.demo;

import java.lang.reflect.Field;

public class UtilTesting {

    public static void injectObject(Object target,String fieldName,Object toInject){
        boolean wasPrivate = false;
        try{
            Field field = target.getClass().getDeclaredField(fieldName);
            if(!field.isAccessible()){
                field.setAccessible(true);
            }
            field.set(target,toInject);
            if(wasPrivate){
                field.setAccessible(false);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            //catch for getDeclaredField//catch for set methord
            e.printStackTrace();
        }

    }
}
