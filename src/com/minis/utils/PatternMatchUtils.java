package com.minis.utils;

public class PatternMatchUtils {
    /**
     * 用给定的模式匹配字符串。
     * 模式格式: "xxx*", "*xxx", "*xxx*" 以及 "xxx*yyy"，*代表若干个字符。
     */
    public static boolean simpleMatch(String pattern, String str) {
        //先判断串或者模式是否为空
        if (pattern == null || str == null) {
            return false;
        }
        //再判断模式中是否包含*
        int firstIndex = pattern.indexOf('*');
        if (firstIndex == -1) {
            return pattern.equals(str);
        }
        //是否首字符就是*,意味着这个是*XXX格式
        if (firstIndex == 0) {
            if (pattern.length() == 1) {  //模式就是*,通配全部串
                return true;
            }
            //尝试查找下一个*
            int nextIndex = pattern.indexOf('*', 1);
            if (nextIndex == -1) { //没有下一个*，说明后续不需要再模式匹配了，直接endsWith判断
                return str.endsWith(pattern.substring(1));
            }
            //截取两个*之间的部分
            String part = pattern.substring(1, nextIndex);
            if (part.isEmpty()) { //这部分为空，形如**，则移到后面的模式进行匹配
                return simpleMatch(pattern.substring(nextIndex), str);
            }
            //两个*之间的部分不为空，则在串中查找这部分子串
            int partIndex = str.indexOf(part);
            while (partIndex != -1) {
                //模式串移位到第二个*之后，目标字符串移位到字串之后，递归再进行匹配
                if (simpleMatch(pattern.substring(nextIndex), str.substring(partIndex + part.length()))) {
                    return true;
                }
                partIndex = str.indexOf(part, partIndex + 1);
            }
            return false;
        }

        //对不是*开头的模式，前面部分要精确匹配，然后后面的子串重新递归匹配
        return (str.length() >= firstIndex &&
                pattern.substring(0, firstIndex).equals(str.substring(0, firstIndex)) &&
                simpleMatch(pattern.substring(firstIndex), str.substring(firstIndex)));
    }
}
