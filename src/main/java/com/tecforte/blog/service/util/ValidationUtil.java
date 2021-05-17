package com.tecforte.blog.service.util;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.tecforte.blog.domain.enumeration.Emoji;

public final class ValidationUtil {
    
    private final static List<String> positiveWords = Arrays.asList("LOVE", "HAPPY", "TRUST");
    
    private final static List<String> negativeWords = Arrays.asList("SAD", "FEAR", "LONELY");
    
    private ValidationUtil() {
    	
    }
    
    /**
     * 
     * @param isPositive isPositive of the blog
     * @param emoji the emoji of the entry
     * @return is the emoji follow the rule
     */
    public static boolean validateEmoji(boolean isPositive, Emoji emoji) {
    	switch(emoji) {
    		case WOW: return true;
    		case LIKE: 
    		case HAHA: return (isPositive == true);
    		case SAD:
    		case ANGRY: return (isPositive == false);
    		default: return false;
    	}
    }
    
    /**
     * 
     * @param isPositive isPositive of the blog
     * @param contentOrTitle the contentOrTitle, it may be content of entry or title of blog
     * @return is the contentOrTitle follow the rule
     */
    public static boolean validateContentOrTitle(boolean isPositive, String contentOrTitle) {
    	if(isPositive) {
        	return StringMatchingUtil.containKeyword(negativeWords, contentOrTitle) == false;
    	}
    	else {
        	return StringMatchingUtil.containKeyword(positiveWords, contentOrTitle) == false;
    	}
    }
}
