
package com.qhrtech.emr.restapi.services;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.restapi.models.service.AcronConfiguration;
import com.qhrtech.emr.restapi.models.service.CloudStorageAccount;
import com.qhrtech.emr.restapi.models.service.KeyVaultDetails;
import com.qhrtech.emr.restapi.services.exceptions.RestServiceException;
import com.qhrtech.emr.restapi.services.exceptions.StorageServiceException;
import com.qhrtech.emr.restapi.services.impl.HostedDiscoveryDetailsService;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class HostedDiscoveryServiceApiTest {

  private HostedDiscoveryDetailsService discoveryDetailsService;

  private final RestTemplate restTemplate;
  String discoveryHost = "http://localhost/";

  public HostedDiscoveryServiceApiTest() {
    restTemplate = mock(RestTemplate.class);
    discoveryDetailsService = new HostedDiscoveryDetailsService(discoveryHost, restTemplate);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetDiscoveryDetailsWithInvalidAcron() throws Exception {
    discoveryDetailsService.getAcronymConfiguration("");
  }

  @Test(expected = IllegalStateException.class)
  public void testGetDiscoveryDetailsWithInvalidUrl() throws Exception {
    discoveryDetailsService = new HostedDiscoveryDetailsService("", restTemplate);
    String acron = RandomStringUtils.randomAlphanumeric(5);
    discoveryDetailsService.getAcronymConfiguration(acron);
  }


  @Test(expected = RestServiceException.class)
  public void testGetDiscoveryDetailsServerError() throws Exception {
    String acron = RandomStringUtils.randomAlphanumeric(5);
    java.net.URI uri = UriComponentsBuilder.fromHttpUrl(discoveryHost)
        .path("json/v1/acron/{acronym}.json").build(acron);

    when(restTemplate.getForObject(uri, AcronConfiguration.class)).thenThrow(
        new RestClientException(""));
    discoveryDetailsService.getAcronymConfiguration(acron);
  }

  @Test(expected = RestServiceException.class)
  public void testGetDiscoveryDetails404() throws Exception {
    String acron = RandomStringUtils.randomAlphanumeric(5);
    java.net.URI uri = UriComponentsBuilder.fromHttpUrl(discoveryHost)
        .path("json/v1/acron/{acronym}.json").build(acron);

    when(restTemplate.getForObject(uri, AcronConfiguration.class))
        .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

    AcronConfiguration returnedConfiguration =
        discoveryDetailsService.getAcronymConfiguration(acron);
    verify(restTemplate).getForObject(uri, AcronConfiguration.class);
  }

  @Test(expected = RestServiceException.class)
  public void testGetDiscoveryDetailsNullResponse() throws Exception {
    String acron = RandomStringUtils.randomAlphanumeric(5);
    java.net.URI uri = UriComponentsBuilder.fromHttpUrl(discoveryHost)
        .path("json/v1/acron/{acronym}.json").build(acron);

    when(restTemplate.getForObject(uri, AcronConfiguration.class)).thenReturn(null);
    discoveryDetailsService.getAcronymConfiguration(acron);
    verify(restTemplate).getForObject(uri, AcronConfiguration.class);
  }

  @Test
  public void testGetDiscoveryDetails() throws Exception {
    String acron = RandomStringUtils.randomAlphanumeric(5);
    AcronConfiguration acronConfiguration =
        new PodamFactoryImpl().manufacturePojo(AcronConfiguration.class);
    acronConfiguration.setName(acron);

    java.net.URI uri = UriComponentsBuilder.fromHttpUrl(discoveryHost)
        .path("json/v1/acron/{acronym}.json").build(acron);

    when(restTemplate.getForObject(uri, AcronConfiguration.class)).thenReturn(acronConfiguration);

    AcronConfiguration returnedConfiguration =
        discoveryDetailsService.getAcronymConfiguration(acron);
    assertNotNull(returnedConfiguration);
    verify(restTemplate).getForObject(uri, AcronConfiguration.class);
    assertEquals(acronConfiguration, returnedConfiguration);
  }

  @Test
  public void testGetDiscoveryDetailsNameNotMatch() throws Exception {
    String acron = RandomStringUtils.randomAlphanumeric(5);
    AcronConfiguration acronConfiguration =
        new PodamFactoryImpl().manufacturePojo(AcronConfiguration.class);
    acronConfiguration.setName(acron.substring(0, 4));

    java.net.URI uri = UriComponentsBuilder.fromHttpUrl(discoveryHost)
        .path("json/v1/acron/{acronym}.json").build(acron);

    when(restTemplate.getForObject(uri, AcronConfiguration.class)).thenReturn(acronConfiguration);

    AcronConfiguration returnedConfiguration =
        discoveryDetailsService.getAcronymConfiguration(acron);
    assertNotNull(returnedConfiguration);
    verify(restTemplate).getForObject(uri, AcronConfiguration.class);
    assertEquals(acronConfiguration, returnedConfiguration);
  }

  @Test
  public void testGetCloudStorageAccount() {
    String acron = RandomStringUtils.randomAlphanumeric(5);
    AcronConfiguration acronConfiguration =
        new PodamFactoryImpl().manufacturePojo(AcronConfiguration.class);
    acronConfiguration.setName(acron.substring(0, 4));

    java.net.URI uri = UriComponentsBuilder.fromHttpUrl(discoveryHost)
        .path("json/v1/acron/{acronym}.json").build(acron);

    when(restTemplate.getForObject(uri, AcronConfiguration.class)).thenReturn(acronConfiguration);

    CloudStorageAccount cloudStorageAccount =
        discoveryDetailsService.getCloudStorageAccount(acron);
    assertNotNull(cloudStorageAccount);
    verify(restTemplate).getForObject(uri, AcronConfiguration.class);
    assertEquals(acronConfiguration.getCloudStorageAccount(), cloudStorageAccount);
  }

  @Test
  public void testAcronKeyVault() {
    String acron = RandomStringUtils.randomAlphanumeric(5);
    AcronConfiguration acronConfiguration =
        new PodamFactoryImpl().manufacturePojo(AcronConfiguration.class);
    acronConfiguration.setName(acron.substring(0, 4));

    java.net.URI uri = UriComponentsBuilder.fromHttpUrl(discoveryHost)
        .path("json/v1/acron/{acronym}.json").build(acron);

    when(restTemplate.getForObject(uri, AcronConfiguration.class)).thenReturn(acronConfiguration);

    KeyVaultDetails keyVaultDetails =
        discoveryDetailsService.getAcronKeyVaultDetails(acron);
    assertNotNull(keyVaultDetails);
    verify(restTemplate).getForObject(uri, AcronConfiguration.class);
    assertEquals(acronConfiguration.getAcronKeyVault(), keyVaultDetails);
  }


  @Test
  public void testCommonKeyVault() {
    String acron = RandomStringUtils.randomAlphanumeric(5);
    AcronConfiguration acronConfiguration =
        new PodamFactoryImpl().manufacturePojo(AcronConfiguration.class);
    acronConfiguration.setName(acron.substring(0, 4));

    java.net.URI uri = UriComponentsBuilder.fromHttpUrl(discoveryHost)
        .path("json/v1/acron/{acronym}.json").build(acron);

    when(restTemplate.getForObject(uri, AcronConfiguration.class)).thenReturn(acronConfiguration);

    KeyVaultDetails commonKeyVaultDetails =
        discoveryDetailsService.getCommonKeyVaultDetails(acron);
    assertNotNull(commonKeyVaultDetails);
    verify(restTemplate).getForObject(uri, AcronConfiguration.class);
    assertEquals(acronConfiguration.getCommonKeyVault(), commonKeyVaultDetails);
  }

  @Test(expected = StorageServiceException.class)
  public void getAcronKeyVault() {
    String acron = RandomStringUtils.randomAlphanumeric(5);
    AcronConfiguration acronConfiguration =
        new PodamFactoryImpl().manufacturePojo(AcronConfiguration.class);
    acronConfiguration.setName(acron.substring(0, 4));

    acronConfiguration.setAcronKeyVault(null);

    java.net.URI uri = UriComponentsBuilder.fromHttpUrl(discoveryHost)
        .path("json/v1/acron/{acronym}.json").build(acron);

    when(restTemplate.getForObject(uri, AcronConfiguration.class)).thenReturn(acronConfiguration);
    discoveryDetailsService.getAcronKeyVaultDetails(acron);
  }

  @Test(expected = StorageServiceException.class)
  public void getCommonKeyVault() {
    String acron = RandomStringUtils.randomAlphanumeric(5);
    AcronConfiguration acronConfiguration =
        new PodamFactoryImpl().manufacturePojo(AcronConfiguration.class);
    acronConfiguration.setName(acron.substring(0, 4));
    acronConfiguration.setCommonKeyVault(null);
    java.net.URI uri = UriComponentsBuilder.fromHttpUrl(discoveryHost)
        .path("json/v1/acron/{acronym}.json").build(acron);

    when(restTemplate.getForObject(uri, AcronConfiguration.class)).thenReturn(acronConfiguration);
    discoveryDetailsService.getCommonKeyVaultDetails(acron);
  }

}

