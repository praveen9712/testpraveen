/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */

package com.qhrtech.emr.restapi.startup;

import com.qhrtech.emr.restapi.scheduled.RegistrySyncTask;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author kevin.kendall
 */
public class StartupContextListener implements ServletContextListener {

  @Autowired
  private RegistrySyncTask registrySyncTask;

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    WebApplicationContext context = WebApplicationContextUtils
        .getRequiredWebApplicationContext(servletContextEvent.getServletContext());
    context.getAutowireCapableBeanFactory().autowireBean(this);

    registrySyncTask.refreshFromRegistry();
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    registrySyncTask.stop();
  }

}
