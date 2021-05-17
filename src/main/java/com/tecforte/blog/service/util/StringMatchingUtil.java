package com.tecforte.blog.service.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public final class StringMatchingUtil {
	private StringMatchingUtil() {
		
	}
	
	/**
     * 
     * @param keywords the keywords to match
     * @param stringToCheck the string be checked whether it contains any keyword whole word and case insensitive
     * @return does the string matches any keyword whole word and case insensitive
     */
    public static boolean containKeyword(List<String> keywords, String stringToCheck) {
        String patternString = "(?i)\\b(" + StringUtils.join(keywords, "|") + ")\\b";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(stringToCheck);
        return matcher.find();
    }
}
