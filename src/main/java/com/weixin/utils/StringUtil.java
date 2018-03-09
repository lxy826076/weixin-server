package com.weixin.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * 字符串辅助类
 * 
 * @author Munan </br>2011-11-15 下午03:40:08
 * @version V1.0
 */
public class StringUtil {
    //private static final Logger log = Logger.getLogger(StringUtil.class);
    
    public static final String BLANK_STR = "";

    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0 || "".equals(str.trim()));
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 屏蔽客户敏感信息-用户名
     * @param userName
     * @return
     */
    public static String hiddenUserName(String userName){
    	if(userName == null || userName.isEmpty()){
    		return userName;
    	}
    	userName = "*" + userName.substring(1);
    	return userName;
    }
    
    /**
     * 屏蔽客户敏感信息-身份证
     * @param idCard
     * @return
     */
    public static String hiddenIDCard(String idCard){
    	if(idCard == null || idCard.isEmpty()){
    		return idCard;
    	}
    	if(idCard.length() > 6){
    		idCard = idCard.substring(0, idCard.length()-6) + "******";
    	}
    	return idCard;
    }
    
    /**
     * 屏蔽客户敏感信息-手机号
     * @param phoneNum
     * @return
     */
    public static String hiddenPhoneNum(String phoneNum){
    	if(phoneNum == null || phoneNum.isEmpty()){
    		return phoneNum;
    	}
    	if(phoneNum.length() > 4){
    		phoneNum = phoneNum.substring(0, phoneNum.length()-4) + "****";
    	}
    	return phoneNum;
    }
    
    /**
     * 截取一段字符的长度(汉、日、韩文字符长度为2),不区分中英文,如果数字不正好，则少取一个字符位
     * 
     * @param str
     *            原始字符串
     * @param specialCharsLength
     *            截取长度(汉、日、韩文字符长度为2)
     * @return
     */
    public static String trim(String str, int specialCharsLength) {
        if (str == null || "".equals(str) || specialCharsLength < 1) {
            return "";
        }
        char[] chars = str.toCharArray();
        int charsLength = getCharsLength(chars, specialCharsLength);
        return new String(chars, 0, charsLength);
    }
    /**
	 * 功能说明:去掉字符串2端空格或空白。如果参数字符串为null，那么返回结果为空白字符串，即"";
	 * @param s 需要过滤的字符串
	 * @return
	 * 创建日期: 2011-04-28
	 * 修改人：
	 * 修改日期:
	 * 修改内容:
	 *
	 */
	public static String trim(String s){
		return trim(s,false);
	}
	/**
	 * TODO 转义没有实现，下版实现。
	 * 功能说明:去掉字符串2端空格或空白。如果参数字符串为null，那么返回结果为空白字符串，即"";
	 * @param s 需要过滤的字符串
	 * @param isTransferred 是否对html特殊字符转义 
	 * 修改人：
	 * 修改日期:
	 * 修改内容:
	 *
	 */
	public static String trim(String s,boolean isTransferred){
		if(isTransferred){
			return s == null ? "" : s.trim();
		}else{
			return s == null ? "" : s.trim();
		}
	}
    /**
     * 获取一段字符的长度，输入长度中汉、日、韩文字符长度为2，输出长度中所有字符均长度为1
     * 
     * @param chars
     *            一段字符
     * @param specialCharsLength
     *            输入长度，汉、日、韩文字符长度为2
     * @return 输出长度，所有字符均长度为1
     */
    private static int getCharsLength(char[] chars, int specialCharsLength) {
        int count = 0;
        int normalCharsLength = 0;
        for (int i = 0; i < chars.length; i++) {
            int specialCharLength = getSpecialCharLength(chars[i]);
            if (count <= specialCharsLength - specialCharLength) {
                count += specialCharLength;
                normalCharsLength++;
            } else {
                break;
            }
        }
        return normalCharsLength;
    }

    /**
     * 获取字符长度：汉、日、韩文字符长度为2，ASCII码等字符长度为1
     * 
     * @param c
     *            字符
     * @return 字符长度
     */
    private static int getSpecialCharLength(char c) {
        if (isLetter(c)) {
            return 1;
        } else {
            return 2;
        }
    }

    /**
     * 判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符）
     * 
     * @param char c, 需要判断的字符
     * @return boolean, 返回true,Ascill字符
     */
    private static boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0 ? true : false;
    }

    /**
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1
     * 
     * @param s
     *            需要得到长度的字符串
     * @return i得到的字符串长度
     */
    public static int length(String s) {
        if (s == null)
            return 0;
        char[] c = s.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            len++;
            if (!isLetter(c[i])) {
                len++;
            }
        }
        return len;
    }

    /**
     * 截取一段字符的长度,不区分中英文,如果数字不正好，则少取一个字符位
     * 
     * 
     * @param origin
     *            原始字符串
     * @param len
     *            截取长度(一个汉字长度按2算的)
     * @param c
     *            后缀
     * @return 返回的字符串
     */
    public static String substring(String origin, int len, String c) {
        if (origin == null || origin.equals("") || len < 1)
            return "";
        byte[] strByte = new byte[len];
        if (len > length(origin)) {
            return origin + c;
        }
        try {
            System.arraycopy(origin.getBytes("GBK"), 0, strByte, 0, len);
            int count = 0;
            for (int i = 0; i < len; i++) {
                int value = (int) strByte[i];
                if (value < 0) {
                    count++;
                }
            }
            if (count % 2 != 0) {
                len = (len == 1) ? ++len : --len;
            }
            return new String(strByte, 0, len, "GBK") + c;
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 
     * 将字符串转换成list
     * 
     * @param str
     * @return List
     */
    @SuppressWarnings("unchecked")
	public static List toList(String str) {
        List list = new ArrayList();
        if (!Util.isNull(str)) {
            String specValues[] = str.split(",");
            list = Arrays.asList(specValues);
        }
        return list;
    }

    public static String replaceEx(String str, String subStr, String reStr) {
        if (str == null) {
            return null;
        }
        if ((subStr == null)
            || (subStr.equals(""))
            || (subStr.length() > str.length())
            || (reStr == null)) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        int lastIndex = 0;
        while (true) {
            int index = str.indexOf(subStr, lastIndex);
            if (index < 0) {
                break;
            }
            sb.append(str.substring(lastIndex, index));
            sb.append(reStr);

            lastIndex = index + subStr.length();
        }
        sb.append(str.substring(lastIndex));
        return sb.toString();
    }

    public static String javaEncode(String txt) {
        if ((txt == null) || (txt.length() == 0)) {
            return txt;
        }
        txt = replaceEx(txt, "\\", "\\\\");
        txt = replaceEx(txt, "\r\n", "\n");
        txt = replaceEx(txt, "\r", "\\r");
        txt = replaceEx(txt, "\t", "\\t");
        txt = replaceEx(txt, "\n", "\\n");
        txt = replaceEx(txt, "\"", "\\\"");
        txt = replaceEx(txt, "'", "\\'");
        return txt;
    }
    public static String leftPad(String srcString, char c, int length)
    {
      if (srcString == null) {
        srcString = "";
      }
      int tLen = srcString.length();
  
      if (tLen >= length)
        return srcString;
      int iMax = length - tLen;
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < iMax; i++) {
        sb.append(c);
      }
      sb.append(srcString);
      return sb.toString();
    }
    /**
	 * 获得当前访问URL
	 * 
	 * @param request
	 * @return
	 */
	public static String getCurrentURL(HttpServletRequest request) {
		String paras = request.getQueryString();

		if (paras == null) {
			return (request.getRequestURI());
		} else {
			return (request.getRequestURI() + "?" + paras);
		}
	}
	
	/**
	 * 过滤器过滤url特殊字符
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static boolean checkStringValue(String str,String[] list){
		boolean flag = true;
		if(str!=null&&!"".equals(str)){
			if(str.indexOf("%")<0){
				str = URLEncoder.encode(str);
			}
			for(int i=0;i<list.length;i++){
				if(str.indexOf(list[i])>-1){
					flag = false;
					break;
				}
			}
		}
		return flag;
	}
	/**
	 * 过滤器过滤参数特殊字符
	 * 
	 * @param request
	 * @return
	 */
	public static boolean checkTextStringValue(String str,String[] list){
		boolean flag = true;
		if(str!=null&&!"".equals(str)){
			for(int i=0;i<list.length;i++){
				if(str.indexOf(list[i])>-1){
					flag = false;
					break;
				}
			}
		}
		return flag;
	}
}
