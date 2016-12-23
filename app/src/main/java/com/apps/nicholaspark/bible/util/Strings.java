package com.apps.nicholaspark.bible.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

public final class Strings {

  private Strings() {
    throw new AssertionError("No instances");
  }

  public static boolean isBlank(final CharSequence cs) {
    if (cs == null) {
      return true;
    }
    int strLen = cs.length();
    if (strLen == 0) {
      return true;
    }
    for (int i = 0; i < strLen; i++) {
      if (!Character.isWhitespace(cs.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  public static String titleize(final String str) {
    if (isBlank(str)) {
      return str;
    }
    final char[] buffer = str.toCharArray();
    boolean capitalizeNext = true;
    for (int i = 0; i < buffer.length; i++) {
      final char ch = buffer[i];
      if (Character.isWhitespace(ch)) {
        capitalizeNext = true;
      } else if (capitalizeNext) {
        buffer[i] = Character.toTitleCase(ch);
        capitalizeNext = false;
      } else {
        buffer[i] = Character.toLowerCase(ch);
        capitalizeNext = false;
      }
    }
    return new String(buffer);
  }

  public static String truncateAt(String string, int length) {
    return string.length() > length ? string.substring(0, length) : string;
  }

  public static String stringToDoubleString(String amount) {
    Float myAmount = Float.parseFloat(amount);
    String totalAmount = String.format("%.02f", myAmount);
    return totalAmount;
  }

  public static String parseDate(String initialDate) {
    try {
      Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(initialDate);
      return new SimpleDateFormat("MMM dd, yyyy").format(date);
    } catch (ParseException e) {
      Timber.e("Exception " + e);
    }
    return null;
  }

  public static String getDayOfMonthSuffix(Date date) {
    int n = date.getDate();
    if (n >= 1 && n <= 31) {
      if (n >= 11 && n <= 13) {
        return n + "th";
      }
      switch (n % 10) {
        case 1:
          return n + "st";
        case 2:
          return n + "nd";
        case 3:
          return n + "rd";
        default:
          return n + "th";
      }
    }
    return "";
  }
}
