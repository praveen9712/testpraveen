/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.util;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

/**
 *
 * @author kevin.kendall
 */
public class PatchInInterceptor extends AbstractPhaseInterceptor<Message> {

  public PatchInInterceptor() {
    super(Phase.PRE_LOGICAL);
  }

  @Override
  public void handleMessage(Message message) throws Fault {
    if (message.get(Message.HTTP_REQUEST_METHOD).equals("PATCH")) {
      message.put("use.async.http.conduit", true);
    }
  }

}
