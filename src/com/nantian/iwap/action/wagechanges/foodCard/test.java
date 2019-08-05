package com.nantian.iwap.action.wagechanges.foodCard;

import java.util.*;

public class test {
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<String, String>();
        Map<String, String> map1 = new HashMap<String, String>();
        map.put("1", "4");
        map.put("dede", "4");
        map.put("5", "5");
        map.put("3", "3");
        map.put("4", "5");
        map.put("2", "5");
        map.put("6", "5");
        Set<String> strings = map.keySet();
        int a = strings.size();
        String[] y = new String[]{"6", "3", "5", "2", "4"};
        int j = y.length;
        //删除索引集合
        List<String> delIndex = new ArrayList<>();
        while (a > 0) {
            //遍历key数组
            for (String string : strings) {
                //例如key是1，就会查一遍过滤数组y
                for (int i = y.length - 1; i >= 0; i--) {
                    String key = y[i];
                    if (string.equals(key)) {
                        break;
                    }
                    if (i == 0) {
                        delIndex.add(string);
                        break;
                    }
                }
            }

            a--;

        }
        for (String index : delIndex) {
            //删除
            map.remove(index);

        }
        System.out.println(map.size());

    }


}
