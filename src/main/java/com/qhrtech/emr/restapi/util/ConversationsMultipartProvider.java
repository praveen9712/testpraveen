
package com.qhrtech.emr.restapi.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.annotation.PostConstruct;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.jaxrs.provider.MultipartProvider;
import org.springframework.stereotype.Component;

@Component
public class ConversationsMultipartProvider extends MultipartProvider {

  private static final String CONVERSATIONS_URL = "conversations";
  private static final String CONVERSATION_ATTACHMENTS_MAX_SIZE = "52428800"; // 50mb

  @Context
  private MessageContext mc;

  @PostConstruct
  public void init() {
    super.setAttachmentMaxSize(CONVERSATION_ATTACHMENTS_MAX_SIZE);
  }

  @Override
  public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations,
      MediaType mt) {
    return mc.getUriInfo().getPath().contains(CONVERSATIONS_URL)
        && super.isReadable(type, genericType, annotations, mt);
  }

}
