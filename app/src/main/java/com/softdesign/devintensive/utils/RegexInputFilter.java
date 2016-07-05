package com.softdesign.devintensive.utils;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ant on 04.07.16.
 */
public class RegexInputFilter implements InputFilter {
    private Pattern mPattern;

    public RegexInputFilter(String pattern){
        mPattern = Pattern.compile(pattern);
    }

    @Override
    public CharSequence filter(CharSequence source,
                               int sourceStart, int sourceEnd,
                               Spanned destination, int destinationStart,
                               int destinationEnd)
    {
        String textToCheck = destination.subSequence(0, destinationStart).
                toString() + source.subSequence(sourceStart, sourceEnd) +
                destination.subSequence(
                        destinationEnd, destination.length()).toString();

        Matcher matcher = mPattern.matcher(textToCheck);

        // Entered text does not match the pattern
        if(!matcher.matches()){

            // It does not match partially too
            if(!matcher.hitEnd()){
                return "";
            }

        }

        return null;
    }

}
