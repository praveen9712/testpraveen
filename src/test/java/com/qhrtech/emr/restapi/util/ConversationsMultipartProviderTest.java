
package com.qhrtech.emr.restapi.util;


import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.MockitoAnnotations.openMocks;
import com.qhrtech.emr.restapi.scheduled.RegistrySyncTask;
import java.sql.SQLException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

public class ConversationsMultipartProviderTest {


  @Mock
  private MessageContext messageContext;

  @Mock
  private UriInfo uriInfo;

  @InjectMocks
  @Spy
  private ConversationsMultipartProvider multipartProvider;


  @Before
  public void setUp() throws SQLException {

    openMocks(this);
  }

  @Test
  public void testForConversationResource() throws Exception {
    String path = "v1/provider-portal/conversations/1342/messages/1324/attachments/8/contents";
    doReturn(uriInfo).when(messageContext).getUriInfo();
    doReturn(path).when(uriInfo).getPath();
    Class<?> type = Attachment.class;
    MediaType mt = new MediaType("multipart", "form-data");
    boolean readable = multipartProvider.isReadable(type, null, null, mt);
    Assert.assertTrue(readable);

  }

  @Test
  public void testForNonConversationResource() throws Exception {
    String path = "v1/provider-portal/appointments/1342";
    doReturn(uriInfo).when(messageContext).getUriInfo();
    doReturn(path).when(uriInfo).getPath();
    Class<?> type = Attachment.class;
    MediaType mt = new MediaType("multipart", "form-data");
    boolean readable = multipartProvider.isReadable(type, null, null, mt);
    Assert.assertFalse(readable);

  }

  @Test
  public void testInit() throws Exception {
    multipartProvider.init();

  }

}
