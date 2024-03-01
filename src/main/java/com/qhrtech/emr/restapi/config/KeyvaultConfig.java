
package com.qhrtech.emr.restapi.config;

import com.azure.core.credential.TokenCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyvaultConfig {

  @Value("${keyvault.tenant-id}")
  private String tenantId;

  @Value("${keyvault.client-id:}")
  private String clientId;

  @Value("${keyvault.client-secret:}")
  private String clientSecret;

  @Bean
  public Supplier<SecretClientBuilder> clientBuilderFactory() {
    TokenCredential credential;
    if (clientId.isEmpty() || clientSecret.isEmpty()) {
      credential = new DefaultAzureCredentialBuilder()
          .tenantId(tenantId)
          .build();
    } else {
      credential = new ClientSecretCredentialBuilder()
          .tenantId(tenantId)
          .clientId(clientId)
          .clientSecret(clientSecret)
          .build();
    }

    return () -> new SecretClientBuilder().credential(credential);
  }
}
