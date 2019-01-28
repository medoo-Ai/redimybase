package com.redimybase.manager.security.service.impl;

import com.redimybase.manager.security.entity.dto.UserAddressListDTO;

import java.util.*;

/**
 * Created by Vim 2019/1/13 18:13
 *
 * @author Vim
 */
public class UserAddressSort {

    /**
     * 字母大小写标识 capital:大写
     */
    private static final String LETTER_FLAG_CAPITAL = "capital";

    public static void main(String[] args) {
        UserAddressSort sort = new UserAddressSort();
        List<UserAddressListDTO> nameList = new ArrayList<>();
        UserAddressListDTO user1 = new UserAddressListDTO();
        user1.setUserName("杜甫");
        UserAddressListDTO user2 = new UserAddressListDTO();
        user2.setUserName("李元芳");
        UserAddressListDTO user3 = new UserAddressListDTO();
        user3.setUserName("王宝强");
        UserAddressListDTO user4 = new UserAddressListDTO();
        user4.setUserName("irany");

        nameList.add(user1);
        nameList.add(user2);
        nameList.add(user3);
        nameList.add(user4);

        Map<String, List<UserAddressListDTO>> sortMap = sort.sort(nameList);

        for (Map.Entry<String, List<UserAddressListDTO>> entry : sortMap.entrySet()) {
            System.out.println(String.format("[%s] %s", entry.getKey(), entry.getValue().get(0).getUserName()));
        }
    }

    /**
     * 排序的方法
     *
     * @param list 需要排序的List集合
     */
    public Map<String, List<UserAddressListDTO>> sort(List<UserAddressListDTO> list) {
        Map<String, List<UserAddressListDTO>> map = new HashMap<>();
        List<UserAddressListDTO> arraylist = new ArrayList<>();
        String[] alphatableb =
                {
                        "A", "B", "C", "D", "E", "F", "G", "H", "I",
                        "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"
                };
        for (String a : alphatableb) {
            for (UserAddressListDTO userEntity : list) {
                //为了排序都返回大写字母
                if (a.equals(string2alphaFirst(userEntity.getUserName(), LETTER_FLAG_CAPITAL))) {
                    arraylist.add(userEntity);
                }
            }
            if (arraylist.size() != 0) {
                map.put(a, arraylist);
            }
            arraylist = new ArrayList<>();
        }
        return map;
    }

    /**
     * 字母Z使用了两个标签，这里有２７个值
     * i, u, v都不做声母, 跟随前面的字母
     */
    private char[] chartable =
            {
                    '啊', '芭', '擦', '搭', '蛾', '发', '噶', '哈', '哈',
                    '击', '喀', '垃', '妈', '拿', '哦', '啪', '期', '然',
                    '撒', '塌', '塌', '塌', '挖', '昔', '压', '匝', '座'
            };

    /**
     * 大写字母匹配数组
     */
    private char[] alphatableb =
            {
                    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
                    'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '#'
            };

    /**
     * 小写字母匹配数组
     */
    private char[] alphatables =
            {
                    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
                    'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '#'
            };

    /**
     * c胡世华
     */
    private int[] table = new int[27];

    {
        for (int i = 0; i < 27; ++i) {
            table[i] = gbValue(chartable[i]);
        }
    }

    /**
     * 主函数,输入字符,得到他的声母,英文字母返回对应的大小写字母,英文字母返回对应的大小写字母
     *
     * @param ch   字符
     * @param type 大小写类型标识
     */
    private char char2alpha(char ch, String type) {
        if (ch >= 'a' && ch <= 'z')
        //为了按字母排序先返回大写字母
        {
            return (char) (ch - 'a' + 'A');
        }

        if (ch >= 'A' && ch <= 'Z') {
            return ch;
        }
        int gb = gbValue(ch);
        if (gb < table[0]) {
            return '0';
        }

        int i;
        for (i = 0; i < 26; ++i) {
            if (match(i, gb)) {
                break;
            }
        }

        if (i >= 26) {
            return '#';
        } else {
            if (LETTER_FLAG_CAPITAL.equals(type)) {
                //大写
                return alphatableb[i];
            } else {//小写
                return alphatables[i];
            }
        }
    }

    /**
     * 根据一个包含汉字的字符串返回一个汉字拼音首字母的字符串
     *
     * @param sourcestr 目标字符串
     * @param type      大小写类型
     * @return
     */
    public String String2Alpha(String sourcestr, String type) {
        String result = "";
        int strLength = sourcestr.length();
        int i;
        try {
            for (i = 0; i < strLength; i++) {
                result += char2alpha(sourcestr.charAt(i), type);
            }
        } catch (Exception e) {
            result = "";
        }
        return result;
    }

    /**
     * 根据一个包含汉字的字符串返回第一个汉字拼音首字母的字符串
     *
     * @param sourceStr 目标字符串
     * @param type      大小写类型
     */
    private String string2alphaFirst(String sourceStr, String type) {
        String result = "";
        try {
            result += char2alpha(sourceStr.charAt(0), type);
        } catch (Exception e) {
            result = "";
        }
        return result;
    }

    private boolean match(int i, int gb) {
        if (gb < table[i]) {
            return false;
        }
        int j = i + 1;

        //字母Z使用了两个标签
        while (j < 26 && (table[j] == table[i])) {
            ++j;
        }
        if (j == 26) {
            return gb <= table[j];
        } else {
            return gb < table[j];
        }
    }

    /**
     * 取出汉字的编码
     */
    private int gbValue(char ch) {
        String str = "";
        str += ch;
        try {
            byte[] bytes = str.getBytes("GBK");
            if (bytes.length < 2) {
                return 0;
            }
            return (bytes[0] << 8 & 0xff00) + (bytes[1] &
                    0xff);
        } catch (Exception e) {
            return 0;
        }
    }
}