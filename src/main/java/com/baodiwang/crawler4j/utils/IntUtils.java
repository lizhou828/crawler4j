/*
 *Project: crawler4j
 *File: com.baodiwang.crawler4j.utils.IntUtils.java <2018年08月24日}>
 ****************************************************************
 * 版权所有@2015 国裕网络科技  保留所有权利.
 ***************************************************************/

package com.baodiwang.crawler4j.utils;

import java.util.Random;

/**
 * @author lizhou
 * @version 1.0
 * @Date 2018年08月24日 08时40分
 */
public class IntUtils {

    /**
     * 取某个范围的随机数
     * 原理:要得到的随机数的范围是[2,100]，假设返回的伪随机数的范围是[0,N)，也即[0,N-1];对得到的这个数模99，于是计算得到的数的范围是[0,98]；再把结果加2，范围就是[2,100]了。
     * @param min
     * @param max
     * @return   return value >= min  or  <= max
     */
    public static int getRandomInt(int min, int max){
        if(min > max){
            return 0;
        }else if (min == max){
            return min;
        }
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;

    }

    public static void main(String[] args) {
        System.out.println(getRandomInt(3,7));
    }
}
