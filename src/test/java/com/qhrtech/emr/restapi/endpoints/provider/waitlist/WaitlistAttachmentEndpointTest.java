
package com.qhrtech.emr.restapi.endpoints.provider.waitlist;

import static com.qhrtech.emr.accuro.model.attachments.DiagnosisLinkSourceType.SWL_REQUEST;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anySet;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import com.qhrtech.emr.accuro.api.attachments.DiagnosisLinkManager;
import com.qhrtech.emr.accuro.model.attachments.DiagnosisLink;
import com.qhrtech.emr.accuro.model.attachments.ItemCategory;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.AbstractEndpointTest;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.attachment.ItemCategoryDto;
import com.qhrtech.emr.restapi.models.dto.attachment.WaitlistAttachmentDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;

public class WaitlistAttachmentEndpointTest extends
    AbstractEndpointTest<WaitlistAttachmentEndpoint> {

  private final DiagnosisLinkManager diagnosisLinkManager;
  private final String commonUrl = "/v1/provider-portal/waitlists/{listId}/attachments";
  private AuditLogUser user;
  private ApiSecurityContext context;

  public WaitlistAttachmentEndpointTest() {
    super(new WaitlistAttachmentEndpoint(), WaitlistAttachmentEndpoint.class);
    diagnosisLinkManager = mock(DiagnosisLinkManager.class);
  }

  private static DiagnosisLink convertToDiagnosisLink(WaitlistAttachmentDto attachment,
      int listId) {
    DiagnosisLink link = new DiagnosisLink();
    link.setDiagnosisId(listId);
    link.setItemId(attachment.getItemId());
    if (attachment.getItemCategoryDto() != null) {
      link.setItemCategory(ItemCategory.lookup(attachment.getItemCategoryDto().getCategoryId()));
    }
    link.setSourceType(SWL_REQUEST);
    return link;
  }

  private static WaitlistAttachmentDto convertToAttachment(DiagnosisLink link) {
    if (link == null || SWL_REQUEST != link.getSourceType()) {
      return null;
    }
    WaitlistAttachmentDto attachment = new WaitlistAttachmentDto();
    attachment.setId(link.getId());
    attachment.setWaitlistId(link.getDiagnosisId());
    attachment.setItemId(link.getItemId());
    if (link.getItemCategory() != null) {
      attachment.setItemCategoryDto(ItemCategoryDto.lookup(link.getItemCategory().getCategoryId()));
    }
    return attachment;
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));

    user = new AuditLogUser(TestUtilities.nextId(),
        TestUtilities.nextId(),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10),
        TestUtilities.nextString(10));
    context.setUser(user);
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> services = new HashMap<>();
    services.put(DiagnosisLinkManager.class, diagnosisLinkManager);
    return services;
  }

  @Test
  public void testCreate() throws Exception {
    // random data
    int listId = TestUtilities.nextId();
    Set<WaitlistAttachmentDto> attachments =
        getFixtures(WaitlistAttachmentDto.class, HashSet::new, 5);
    attachments.forEach(l -> l.setWaitlistId(null));


    // test
    given()
        .when()
        .contentType(ContentType.JSON)
        .pathParam("listId", listId)
        .body(attachments)
        .post(getBaseUrl() + commonUrl)
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    // assertion
    Set<DiagnosisLink> links = new HashSet<>();
    for (WaitlistAttachmentDto attachment : attachments) {
      if (attachment != null) {
        links.add(convertToDiagnosisLink(attachment, listId));
      }
    }

    verify(diagnosisLinkManager).createLink(links, listId, user);
  }

  @Test
  public void testCreateWithWaitlistId() throws Exception {
    // random data
    int listId = TestUtilities.nextId();
    Set<WaitlistAttachmentDto> attachments =
        getFixtures(WaitlistAttachmentDto.class, HashSet::new, 5);

    // test
    given()
        .when()
        .contentType(ContentType.JSON)
        .pathParam("listId", listId)
        .body(attachments)
        .post(getBaseUrl() + commonUrl)
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(diagnosisLinkManager, never()).createLink(anySet(), anyInt(), any(AuditLogUser.class));
  }

  @Test
  public void testCreateWithInvalidItemCategory() throws Exception {
    // random data
    int listId = TestUtilities.nextId();
    Set<WaitlistAttachmentDto> attachments =
        getFixtures(WaitlistAttachmentDto.class, HashSet::new, 5);
    attachments.forEach(a -> {
      a.setItemCategoryDto(null);
      a.setWaitlistId(null);
    });

    // test
    given()
        .when()
        .contentType(ContentType.JSON)
        .pathParam("listId", listId)
        .body(attachments)
        .post(getBaseUrl() + commonUrl)
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(diagnosisLinkManager, never()).createLink(anySet(), anyInt(), any(AuditLogUser.class));
  }

  @Test
  public void testCreateWithEmptyAttachments() throws Exception {
    // random data
    int listId = TestUtilities.nextId();
    Set<WaitlistAttachmentDto> attachments = new HashSet<>();

    // test
    given()
        .when()
        .contentType(ContentType.JSON)
        .pathParam("listId", listId)
        .body(attachments)
        .post(getBaseUrl() + commonUrl)
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(diagnosisLinkManager, never()).createLink(anySet(), anyInt(), any(AuditLogUser.class));
  }

  @Test
  public void testGetById() throws Exception {
    DiagnosisLink diagnosisLink = getFixture(DiagnosisLink.class);
    WaitlistAttachmentDto expected = convertToAttachment(diagnosisLink);

    doReturn(diagnosisLink).when(diagnosisLinkManager).getLinkById(expected.getId());

    WaitlistAttachmentDto actual = given()
        .when()
        .pathParam("listId", expected.getWaitlistId())
        .pathParam("attachmentId", expected.getId())
        .get(getBaseUrl() + commonUrl + "/{attachmentId}")
        .then()
        .assertThat()
        .statusCode(Status.OK.getStatusCode())
        .extract()
        .as(WaitlistAttachmentDto.class);

    assertEquals(expected, actual);
    verify(diagnosisLinkManager).getLinkById(expected.getId());
  }

  @Test
  public void testGetByIdWithInvalidId() throws Exception {
    int attachmentId = TestUtilities.nextId();
    given()
        .when()
        .pathParam("listId", TestUtilities.nextId())
        .pathParam("attachmentId", attachmentId)
        .get(getBaseUrl() + commonUrl + "/{attachmentId}")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());

    verify(diagnosisLinkManager).getLinkById(attachmentId);
  }

  @Test

  public void testGetByIdWithInvalidWaitlistId() throws Exception {
    DiagnosisLink diagnosisLink = getFixture(DiagnosisLink.class);
    WaitlistAttachmentDto waitlistAttachmentDto =
        mapDto(diagnosisLink, WaitlistAttachmentDto.class);

    doReturn(diagnosisLink).when(diagnosisLinkManager).getLinkById(waitlistAttachmentDto.getId());

    given()
        .when()
        .pathParam("listId", TestUtilities.nextId())
        .pathParam("attachmentId", waitlistAttachmentDto.getId())
        .get(getBaseUrl() + commonUrl + "/{attachmentId}")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());

    verify(diagnosisLinkManager).getLinkById(waitlistAttachmentDto.getId());
  }

  @Test
  public void testGetByWaitlistId() throws Exception {
    int listId = TestUtilities.nextId();
    Set<DiagnosisLink> diagnosisLinks = getFixtures(DiagnosisLink.class, HashSet::new, 5);

    Set<WaitlistAttachmentDto> expected = new HashSet<>();
    for (DiagnosisLink link : diagnosisLinks) {
      WaitlistAttachmentDto attachment = convertToAttachment(link);
      if (attachment != null) {
        expected.add(attachment);
      }
    }

    doReturn(diagnosisLinks).when(diagnosisLinkManager)
        .getLinksByDiagnosisId(listId);

    Set<WaitlistAttachmentDto> actual = toCollection(
        given()
            .when()
            .pathParam("listId", listId)
            .get(getBaseUrl() + commonUrl)
            .then()
            .assertThat()
            .statusCode(Status.OK.getStatusCode())
            .extract()
            .as(WaitlistAttachmentDto[].class),
        HashSet::new);

    assertEquals(expected, actual);
    verify(diagnosisLinkManager).getLinksByDiagnosisId(listId);
  }

  @Test
  public void testDeleteById() throws Exception {
    DiagnosisLink diagnosisLink = getFixture(DiagnosisLink.class);
    int attachmentId = diagnosisLink.getId();

    doReturn(diagnosisLink).when(diagnosisLinkManager).getLinkById(diagnosisLink.getId());

    given()
        .when()
        .pathParam("listId", diagnosisLink.getDiagnosisId())
        .pathParam("attachmentId", attachmentId)
        .delete(getBaseUrl() + commonUrl + "/{attachmentId}")
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    verify(diagnosisLinkManager).getLinkById(attachmentId);
    verify(diagnosisLinkManager).deleteLinkById(attachmentId, user);
  }

  @Test
  public void testDeleteByInvalidId() throws Exception {
    DiagnosisLink diagnosisLink = getFixture(DiagnosisLink.class);
    int attachmentId = diagnosisLink.getId();

    given()
        .when()
        .pathParam("listId", diagnosisLink.getDiagnosisId())
        .pathParam("attachmentId", attachmentId)
        .delete(getBaseUrl() + commonUrl + "/{attachmentId}")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());

    verify(diagnosisLinkManager).getLinkById(diagnosisLink.getId());
    verify(diagnosisLinkManager, never()).deleteLinkById(attachmentId, user);
  }

  @Test
  public void testDeleteByIdWithInvalidWaitlistId() throws Exception {
    DiagnosisLink diagnosisLink = getFixture(DiagnosisLink.class);
    int attachmentId = diagnosisLink.getId();

    doReturn(diagnosisLink).when(diagnosisLinkManager).getLinkById(diagnosisLink.getId());

    given()
        .when()
        .pathParam("listId", TestUtilities.nextId())
        .pathParam("attachmentId", diagnosisLink.getId())
        .delete(getBaseUrl() + commonUrl + "/{attachmentId}")
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());

    verify(diagnosisLinkManager).getLinkById(diagnosisLink.getId());
    verify(diagnosisLinkManager, never()).deleteLinkById(attachmentId, user);
  }

  @Test
  public void testDeleteByWaitlistId() throws Exception {
    Set<DiagnosisLink> links = getFixtures(DiagnosisLink.class, HashSet::new, 5);
    int waitlistId = TestUtilities.nextId();

    doReturn(links).when(diagnosisLinkManager).getLinksByDiagnosisId(waitlistId);

    given()
        .when()
        .pathParam("listId", waitlistId)
        .delete(getBaseUrl() + commonUrl)
        .then()
        .assertThat()
        .statusCode(Status.NO_CONTENT.getStatusCode());

    verify(diagnosisLinkManager).getLinksByDiagnosisId(waitlistId);
    verify(diagnosisLinkManager).deleteLinksByDiagnosisId(waitlistId, user);
  }

  @Test
  public void testDeleteByInvalidWaitlistId() throws Exception {
    int waitlistId = TestUtilities.nextId();

    given()
        .when()
        .pathParam("listId", waitlistId)
        .delete(getBaseUrl() + commonUrl)
        .then()
        .assertThat()
        .statusCode(Status.NOT_FOUND.getStatusCode());

    verify(diagnosisLinkManager).getLinksByDiagnosisId(waitlistId);
    verify(diagnosisLinkManager, never()).deleteLinksByDiagnosisId(waitlistId, user);
  }

  @Test
  public void testDeleteByWaitlistIdWithInvalidSourceType()
      throws Exception {
    Set<DiagnosisLink> links = getFixtures(DiagnosisLink.class, HashSet::new, 5);
    links.forEach(l -> l.setSourceType(null));
    int waitlistId = TestUtilities.nextId();

    doReturn(links).when(diagnosisLinkManager).getLinksByDiagnosisId(waitlistId);

    given()
        .when()
        .pathParam("listId", waitlistId)
        .delete(getBaseUrl() + commonUrl)
        .then()
        .assertThat()
        .statusCode(Status.BAD_REQUEST.getStatusCode());

    verify(diagnosisLinkManager).getLinksByDiagnosisId(waitlistId);
    verify(diagnosisLinkManager, never()).deleteLinksByDiagnosisId(waitlistId, user);
  }
}
