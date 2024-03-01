
package com.qhrtech.emr.restapi.validators;

public class RegexValidator {

  /**
   * This pattern will accept all these date patterns: 2018-12-31T05:52:20.698 2018-12-31T05:52:20
   * 2018-12-31T05:52 2018-12-31T05 2018-12-31
   */

  public static final String DATETIME_PATTERN = "^$|(^\\d{4}-[01]\\d-[0-3]\\d(?:T[0-2]\\d(?::[0-5]"
      + "\\d(?::[0-5]\\d(?:.\\d{1,3})?)?)?)?)";
  public static final String DATETIME_MESSAGE = "Invalid date or datetime format."
      + " Please refer to the documentation for more details";
  public static final String POSTALZIP_PATTERN = "^[A-Za-z]\\d[A-Za-z][\\s]{0,1}\\d[A-Za-z]"
      + "\\d$|^$";

  public static final String HCN_PATTERN_9DIGITS = "^[0-9]{0,9}$|^$";
  public static final String HCN_MESSAGE_9DIGITS = "Health card number should have numbers "
      + "with maximum 9 digits.";

  public static final String HCN_PATTERN_10DIGITS = "^[0-9]{0,10}$|^$";
  public static final String HCN_MESSAGE_10DIGITS = "Health card number should have numbers "
      + "with maximum of 10 digits.";

  public static final String HCN_PATTERN_12DIGITS = "^[0-9]{0,12}$|^$";
  public static final String HCN_MESSAGE_12DIGITS = "Health card number should have numbers "
      + "with maximum of 12 digits.";

  public static final String HCN_PATTERN_MB = "^([A-Za-z]|[0-9]{1})[0-9]{0,8}$|^$";
  public static final String HCN_MESSAGE_MB = "Health card number can have one alphabet followed"
      + " by numbers or only numbers with maximum length of 9.";
  /**
   * The phone number with 10 digits For e.g 123 456-7890 (123) 456-7890 1234567890
   */
  public static final String PHONE_PATTERN = "(^[(]{0,1}[0-9]{3}[)]{0,1}[\\s]{0,1}[\\d]{3}"
      + "[-]{0,1}[\\d]{4})$|^$";
  public static final String PHONE_MESSAGE = "Valid phone number pattern "
      + "is (123) 456-7890 or 1234567890";


  public static final String PERSONTYPE_PATTERN = "(^$|PYST|RECP|RFRC)";
  public static final String PERSONTYPE_MESSAGE = "Valid person types are: PYST,RECP or RFRC";


  public static final String NEWBORNCODE_PATTERN = "(^$|ADOP|LVBR|MULT|STBN)";
  public static final String NEWBORNCODE_MESSAGE = "Valid new born codes are"
      + ": ADOP,LVBR,MULT or STBN";

  public static final String UUID_PATTERN =
      "^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$";

}
