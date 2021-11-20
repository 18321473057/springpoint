package org.line.com.javabase;

/**
 * @Author: yangcs
 * @Date: 2021/11/10 8:38
 * @Description:
 */
public class gotoTest {

    //java 没有goto跳转  但是有这个
    public static void main(String[] args) {
        System.out.println("开始");
        whileLabel:
        //跳出后并不能循环执行
        for (int i = 0; i < 4; i++) {
            System.out.println(i);
            if (i == 3) {
                break whileLabel;
            }
        }
    }


}
