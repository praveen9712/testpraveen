/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.config;

import java.io.IOException;
import java.io.StringReader;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author kevin.kendall
 */
@Configuration
public class IdpJwtConfiguration {

  @Value("${medeo.jwt_key}")
  private String rsaPublicKey;

  @Bean
  public JwtConsumer idpJwtConsumer() throws IOException, NoSuchAlgorithmException {
    Security.addProvider(new BouncyCastleProvider());
    PEMReader reader = new PEMReader(new StringReader(rsaPublicKey));
    Object object = reader.readObject();
    JwtConsumer jwtConsumer = new JwtConsumerBuilder()
        .setVerificationKey((Key) object)
        .setAllowedClockSkewInSeconds(60 * 60 * 24)
        .build();
    return jwtConsumer;
  }

}
