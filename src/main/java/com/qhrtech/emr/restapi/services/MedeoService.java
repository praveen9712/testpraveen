/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.services;

import java.util.Calendar;
import org.springframework.stereotype.Service;

/**
 *
 * @author kevin.kendall
 */
@Service
public interface MedeoService {

  public boolean signUp(
      String firstName,
      String lastName,
      String phoneNumber,
      String email,
      String password,
      Calendar birthdate,
      String phn);

}
