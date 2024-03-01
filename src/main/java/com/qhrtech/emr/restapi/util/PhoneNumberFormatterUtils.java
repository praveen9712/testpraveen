
package com.qhrtech.emr.restapi.util;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonemetadata.NumberFormat;
import com.google.i18n.phonenumbers.Phonenumber;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.validators.RegexValidator;
import java.util.Collections;
import java.util.regex.Matcher;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response.Status;

public final class PhoneNumberFormatterUtils {

  public static final String PATTERN = "(\\d{3})(\\d{3})(\\d+)";
  public static final String FORMAT = "($1) $2-$3";

  /**
   * Private Constructor.
   *
   * @throws AssertionError if this constructor is called
   */
  private PhoneNumberFormatterUtils() {
    throw new AssertionError("Cannot create instance of this final class");
  }


  /**
   * Helper method to convert phone number to required format as Accuro for e.g. (123) 456-7890
   *
   * @param phoneNumber the phone number
   * @return formatted phone number
   */
  public static String formatPhoneNumber(String phoneNumber) {
    try {
      PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
      NumberFormat numberFormat = new NumberFormat();
      numberFormat.setPattern(PATTERN);
      numberFormat.setFormat(FORMAT);
      Phonenumber.PhoneNumber convertedPhoneNumber = phoneNumberUtil.parse(phoneNumber, "CA");
      return phoneNumberUtil.formatByPattern(convertedPhoneNumber, PhoneNumberFormat.NATIONAL,
          Collections.singletonList(numberFormat));
    } catch (NumberParseException e) {
      throw new BadRequestException("Invalid Phone number", e);
    }
  }

  /**
   * Helper method to validate phone number is in required format. For e.g the valid phone number
   * with 10 digits 123 456-7890, (123) 456-7890, 1234567890
   *
   * @param number the phone number
   */
  public static void validatePhoneNumber(String number) {
    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(RegexValidator.PHONE_PATTERN);
    Matcher matcher = pattern.matcher(number);
    if (!matcher.matches()) {
      throw Error.webApplicationException(Status.BAD_REQUEST,
          "Invalid phone number format: " + number
              + ". Accepted formats: 1234567890 or (123) 456-7890");
    }
  }

}
