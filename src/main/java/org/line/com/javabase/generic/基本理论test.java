package org.line.com.javabase.generic;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yangcs
 * @Date: 2021/11/10 9:04
 * @Description:
 */
public class 基本理论test {

    public static void main(String[] args) {
        /**
         *  List<Object>  List<String> 都是原生态类型List 的子类型
         *
         * */
        List<Object> ol = new ArrayList<Object>();
        if (ol instanceof List) {
            System.out.println("是");
        }
        List<String> sl = new ArrayList<String>();
        if (sl instanceof List) {
            System.out.println("是");
        }

        /**
         *  List<String> 并不是 List<Object> 的子类;
         *  List<String>类型的对象 并不能添加到  ArrayList<List<Object>> 集合中
         * */
        List<List<Object>> oll = new ArrayList<List<Object>>();
//        oll.add(sl);


        /**
         * 数组是协变的, String[] Object[] 是存在父子关系; List<Object[]> 都可以加入
         * 但是 List<Object> 和 List<String> 是不同的二级子类型
         * */

        String[] sa = new String[2];
        Object[] oa = new Object[2];
        List<Object[]> ool = new ArrayList<Object[]>();
        ool.add(oa);
        ool.add(sa);
        //
        Object[] oaa = new Long[2];
        oaa[1] = "long 怎么能装入String 呢?????????????";





        /**
         *   List<Object>  List<String> 都能添加到 原生态类型List中
         * */
        List list = new ArrayList<>();
        list.add(ol);
        list.add(sl);
        List<Object> olist = new ArrayList<Object>();
        olist.add(ol);
        olist.add(sl);


        /**
         *  List<Object>与原生态类型List 的区别;
         *  List<Object>告诉编译器 可以传递任何数据;
         *  原生态类型List 逃避了类型检查;
         *   List<?> 包含未知对象类型
         * */

    }
}
