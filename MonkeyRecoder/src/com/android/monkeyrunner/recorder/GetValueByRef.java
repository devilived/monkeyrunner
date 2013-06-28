package com.android.monkeyrunner.recorder;


import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.Method;

 



 

public class GetValueByRef {

     

    /**

     * 用反射获取 字段的值
10
     * @param srcObj 作用对象
11
     * @param fieldName 字段名称
12
     * @return
13
     */

     

    public static Object getValueByRef(Object srcObj, String fieldName){

        Object value = null;

        Class objClass = srcObj.getClass();

        fieldName =fieldName.replaceFirst(fieldName.substring(0, 1), fieldName.substring(0, 1).toUpperCase());

        String getMethodName = "get"+fieldName;

        try {

            Method method = objClass.getMethod(getMethodName);//第一个参数为调用的方法名。第二个为方法的返回值:类型  

            value = method.invoke(srcObj);///第一个参数表示要调用的对象，后者为传给这个方法的参数  

        }  catch (IllegalAccessException e) {

            e.printStackTrace();

        } catch (IllegalArgumentException e) {

            e.printStackTrace();

        } catch (InvocationTargetException e) {

            e.printStackTrace();

        }catch (NoSuchMethodException e) {

            e.printStackTrace();

        } catch (SecurityException e) {

            e.printStackTrace();

        }

        return value;

    }

}

