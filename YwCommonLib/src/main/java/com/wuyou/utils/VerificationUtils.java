/*
 * Copyright (c) 2020.
 * Created by QiuQiu on 2020/01/09.
 * All Rights Reserved.
 */

package com.wuyou.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

/**
 * <pre>
 *     author: YanWen
 *     time  : 2020/01/09
 *     desc  : 验证工具类
 *      验证市面上常用的字符串，比如手机号，邮箱等
 * </pre>
 */
public final class VerificationUtils {

    private VerificationUtils() {
        throw new UnsupportedOperationException("U can't instantiate me...");
    }

    /**
     * 判断姓名格式
     *  1.如果是英文名,可以允许英文名字中出现空格
     *  2.英文名的空格可以是多个,但是不能连续出现多个
     *  3.汉字不能出现空格
     */
    public static boolean matchRealName(String value) {
        String regex = "^([\\u4e00-\\u9fa5]+|([a-zA-Z]+\\s?)+)$";
        return matchRegex(regex, value);
    }

    /**
     * 手机号
     * 判断手机号格式  (匹配11数字,并且13-19开头)
     */
    public static boolean matchPhoneNum(String value) {
        String regex = "^(\\+?\\d{2}-?)?(1[0-9])\\d{9}$";
        return matchRegex(regex, value);
    }

    /**
     * 账户
     * 判断账号格式 (4-20位字符)
     */
    public static boolean matchAccount(String value) {
        String regex = "[\\u4e00-\\u9fa5a-zA-Z0-9\\-]{4,20}";
        return matchRegex(regex, value);
    }

    /**
     * 密码1
     * 判断密码格式 (6-12位字母或数字)
     */
    public static boolean matchPassword(String value) {
        String regex = "^[a-zA-Z0-9]{6,12}$";
        return matchRegex(regex, value);
    }

    /**
     * 密码2
     * 判断密码格式 (6-12位字母或数字,必须同时包含字母和数字)
     */
    public static boolean matchPassword2(String value) {
        String regex = "(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,}";
        return matchRegex(regex, value);
    }

    /**
     * 邮箱
     * 判断邮箱格式
     */
    public static boolean matchEmail(String value) {
//      String regex = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)" +
//                "+[a-zA-Z]{2,}$";
        String regex = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+" +
                "(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+" +
                "[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";
        return matchRegex(regex, value);
    }

    /**
     * IP地址
     * 判断IP地址
     */
    public static boolean matchIP(String value) {
        String regex = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\" +
                "d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\" +
                "d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b";
        return matchRegex(regex, value.toLowerCase());
    }

    /**
     * Url
     * 判断URL (http,https,ftp)
     */
    public static boolean matchUrl(String value) {
        //String regex = "^(([hH][tT]{2}[pP][sS]?)|([fF][tT][pP]))\\:\\/\\/[wW]{3}\\.[\\w-]+\\.\\w{2,4}(\\/.*)?$";
        String regex = "^(([hH][tT]{2}[pP][sS]?)|([fF][tT][pP]))\\:\\/\\/[\\w-]+\\.\\w{2,4}(\\/.*)?$";
        return matchRegex(regex, value.toLowerCase());
    }

    /**
     * 中国车牌号
     * 判断中国民用车辆号牌
     */
    public static boolean matchVehicleNumber(String value) {
        String regex = "^[京津晋冀蒙辽吉黑沪苏浙皖闽赣鲁豫鄂湘粤桂琼川贵云藏陕甘青宁新渝]?[A-Z][A-HJ-NP-Z0-9学挂港澳练]{5}$";
        return matchRegex(regex, value.toLowerCase());
    }

    /**
     * 邮政编码
     * 匹配中国邮政编码
     */
    public static boolean matchPostcode(String postcode) {
        String regex = "[1-9]\\d{5}";
        return Pattern.matches(regex, postcode);
    }

    /**
     * 身份证号
     * 判断身份证号码格式
     */
    public static boolean matchIdentityCard(String value) {
//        String regex = "^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|" +
//                "(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|" +
//                "\\d{3}[Xx])$)$";
//        return testRegex(regex, value);
        IDCardTester idCardTester = new IDCardTester();

        return idCardTester.test(value);
    }

    private static class IDCardTester {
        public boolean test(String content) {
            if (TextUtils.isEmpty(content)) {
                return false;
            }
            final int length = content.length();
            if (15 == length) {
                try {
                    return isOldCNIDCard(content);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return false;
                }
            } else if (18 == length) {
                return isNewCNIDCard(content);
            } else {
                return false;
            }
        }

        final int[] WEIGHT = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

        final char[] VALID = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

        public boolean isNewCNIDCard(String numbers) {
            numbers = numbers.toUpperCase();
            int sum = 0;
            for (int i = 0; i < WEIGHT.length; i++) {
                final int cell = Character.getNumericValue(numbers.charAt(i));
                sum += WEIGHT[i] * cell;
            }
            int index = sum % 11;
            return VALID[index] == numbers.charAt(17);
        }

        public boolean isOldCNIDCard(String numbers) {
            String yymmdd = numbers.substring(6, 11);
            boolean aPass = numbers.equals(String.valueOf(Long.parseLong(numbers)));
            boolean yPass = true;
            try {
                new SimpleDateFormat("yyMMdd").parse(yymmdd);
            } catch (Exception e) {
                e.printStackTrace();
                yPass = false;
            }
            return aPass && yPass;
        }
    }

    /**
     * 数字
     * 是否数值型
     */
    public static boolean matchNumeric(String input) {
        if (TextUtils.isEmpty(input)) {
            return false;
        }
        char[] chars = input.toCharArray();
        int sz = chars.length;
        boolean hasExp = false;
        boolean hasDecPoint = false;
        boolean allowSigns = false;
        boolean foundDigit = false;
        int start = (chars[0] == '-' || chars[0] == '+') ? 1 : 0;
        if (sz > start + 1) {
            if (chars[start] == '0' && chars[start + 1] == 'x') {
                int i = start + 2;
                if (i == sz) {
                    return false;
                }
                for (; i < chars.length; i++) {
                    if ((chars[i] < '0' || chars[i] > '9')
                            && (chars[i] < 'a' || chars[i] > 'f')
                            && (chars[i] < 'A' || chars[i] > 'F')) {
                        return false;
                    }
                }
                return true;
            }
        }
        sz--;
        int i = start;
        while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                foundDigit = true;
                allowSigns = false;

            } else if (chars[i] == '.') {
                if (hasDecPoint || hasExp) {
                    return false;
                }
                hasDecPoint = true;
            } else if (chars[i] == 'e' || chars[i] == 'E') {
                if (hasExp) {
                    return false;
                }
                if (!foundDigit) {
                    return false;
                }
                hasExp = true;
                allowSigns = true;
            } else if (chars[i] == '+' || chars[i] == '-') {
                if (!allowSigns) {
                    return false;
                }
                allowSigns = false;
                foundDigit = false;
            } else {
                return false;
            }
            i++;
        }
        if (i < chars.length) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                return true;
            }
            if (chars[i] == 'e' || chars[i] == 'E') {
                return false;
            }
            if (!allowSigns && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) {
                return foundDigit;
            }
            if (chars[i] == 'l' || chars[i] == 'L') {
                return foundDigit && !hasExp;
            }
            return false;
        }
        return !allowSigns && foundDigit;
    }

    /**
     * 是否匹配正则
     * @param regex
     * @param inputValue
     * @return
     */
    public static boolean matchRegex(String regex, String inputValue) {
        return Pattern.compile(regex).matcher(inputValue).matches();
    }
}
