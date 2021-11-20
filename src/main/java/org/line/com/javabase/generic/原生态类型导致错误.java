package org.line.com.javabase.generic;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yangcs
 * @Date: 2021/11/10 9:54
 * @Description:
 */
public class 原生态类型导致错误 {


    public static void main(String[] args) {

        List<String> Strings = new ArrayList();
        addOb(Strings, new Integer(1));
        //运行异常
//        String objStr = Strings.get(0);
        //但是可以使用object 接收; 这运行时实际上绕过了类型检测, 也体现了java的泛型是伪泛型
        Object obj = Strings.get(0);
        System.out.println(obj);
    }

    private static void addOb(List strings, Integer integer) {
        strings.add(integer);
    }
}
