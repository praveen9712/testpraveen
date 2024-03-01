
package com.qhrtech.emr.restapi.services.listeners;

import com.qhrtech.emr.accuro.api.AuthorizationContext;
import com.qhrtech.emr.accuro.api.demographics.DefaultInsurerManager;
import com.qhrtech.emr.accuro.api.demographics.InsurerManager;
import com.qhrtech.emr.accuro.api.patient.DefaultPatientManager;
import com.qhrtech.emr.accuro.api.patient.PatientManager;
import com.qhrtech.emr.accuro.api.scheduling.AppointmentHistoryManager;
import com.qhrtech.emr.accuro.api.scheduling.AppointmentListener;
import com.qhrtech.emr.accuro.api.scheduling.DefaultAppointmentHistoryManager;
import com.qhrtech.emr.accuro.api.scheduling.rooms.DefaultWaitRoomEntryManager;
import com.qhrtech.emr.accuro.api.scheduling.rooms.WaitRoomEntryManager;
import com.qhrtech.emr.accuro.api.security.DefaultModuleManager;
import com.qhrtech.emr.accuro.api.security.ModuleManager;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.patient.Patient;
import com.qhrtech.emr.accuro.model.scheduling.Appointment;
import com.qhrtech.emr.accuro.model.scheduling.AppointmentHistory;
import com.qhrtech.emr.accuro.model.scheduling.rooms.WaitRoomEntry;
import com.qhrtech.emr.accuro.model.security.modules.Module;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.slf4j.LoggerFactory;

public class WaitRoomListener implements AppointmentListener {

  private final DataSource ds;

  public WaitRoomListener(DataSource ds) {
    this.ds = ds;
  }

  @Override
  public void created(Appointment appointment, AuthorizationContext authContext,
      AuditLogUser user) {
    try {
      if (hasTrafficManagerModuleOn() && appointment != null) {
        int days = Days.daysBetween(appointment.getDate(), new LocalDate()).getDays();
        if (days == 0 && appointment.getAppointmentDetails() != null
            && appointment.getPatientId() != null) {
          if (appointment.getAppointmentDetails().isArrived()) {
            Integer insurerId = getInsurerId(appointment, authContext, user);
            WaitRoomEntryManager waitRoomEntryManager = getWaitRoomEntryManager();
            WaitRoomEntry waitRoomEntry = generateWaitRoomEntry(appointment, insurerId);
            waitRoomEntryManager.create(waitRoomEntry, user);
          }
        }
      }
    } catch (ProtossException e) {
      LoggerFactory.getLogger(getClass()).error("Failed to update wait room entry.", e);
    }
  }

  @Override
  public void updated(Appointment appointment, AuthorizationContext authContext,
      AuditLogUser user) {
    try {
      if (hasTrafficManagerModuleOn() && appointment != null) {
        int days = Days.daysBetween(appointment.getDate(), new LocalDate()).getDays();
        if (days == 0 && appointment.getAppointmentDetails() != null
            && appointment.getPatientId() != null) {
          if (appointment.getAppointmentDetails().isArrived()) {
            AppointmentHistoryManager appointmentHistoryManager =
                getAppointmentHistoryManager(authContext, user);
            List<AppointmentHistory> histories =
                appointmentHistoryManager.getHistoryForAppointment(appointment.getAppointmentId());
            Integer previousPatientId = null;
            if (!histories.isEmpty()) {
              // retrieve the patient id from the second latest appointment of the appointment
              // history to compare the previous and current patient id
              previousPatientId = histories.get(1).getPatientId();
            }

            // delete waitroom entry if an appointment(created with arrived for a patient) updates
            // with a different patient.
            if (!appointment.getPatientId().equals(previousPatientId)) {
              deleteWaitRoomEntriesByAppointmentId(appointment.getAppointmentId(), user);
            }

            Integer insurerId = getInsurerId(appointment, authContext, user);
            WaitRoomEntry waitRoomEntry = generateWaitRoomEntry(appointment, insurerId);

            boolean createCheck = true;

            // make sure if the wait room entry already exists for the same patient
            WaitRoomEntryManager waitRoomEntryManager = getWaitRoomEntryManager();
            List<WaitRoomEntry> existedEntries =
                waitRoomEntryManager.getForAppointment(waitRoomEntry.getAppointmentId());
            for (WaitRoomEntry existedEntry : existedEntries) {
              if (existedEntry.getPatientId() == waitRoomEntry.getPatientId()) {
                createCheck = false;
                break;
              }
            }
            if (createCheck) {
              waitRoomEntryManager.create(waitRoomEntry, user);
            }
          } else {
            deleteWaitRoomEntriesByAppointmentId(appointment.getAppointmentId(), user);
          }
        }
      }
    } catch (ProtossException e) {
      LoggerFactory.getLogger(getClass()).error("Failed to update wait room entry.", e);
    }
  }

  @Override
  public void cancelled(Appointment appointment, AuthorizationContext authContext,
      AuditLogUser user) {
    try {
      if (hasTrafficManagerModuleOn()) {
        deleteWaitRoomEntriesByAppointmentId(appointment.getAppointmentId(), user);
      }
    } catch (ProtossException e) {
      LoggerFactory.getLogger(getClass()).error("Failed to update wait room entry.", e);
    }
  }

  @Override
  public void deleted(Appointment appointment, AuthorizationContext authContext,
      AuditLogUser user) {
    try {
      if (hasTrafficManagerModuleOn()) {
        deleteWaitRoomEntriesByAppointmentId(appointment.getAppointmentId(), user);
      }
    } catch (ProtossException e) {
      LoggerFactory.getLogger(getClass()).error("Failed to update wait room entry.", e);
    }
  }

  private void deleteWaitRoomEntriesByAppointmentId(int appointmentId, AuditLogUser user)
      throws ProtossException {
    WaitRoomEntryManager waitRoomEntryManager = getWaitRoomEntryManager();
    List<WaitRoomEntry> waitRoomEntries =
        waitRoomEntryManager.getForAppointment(appointmentId);
    for (WaitRoomEntry waitRoomEntry : waitRoomEntries) {
      waitRoomEntryManager.delete(waitRoomEntry.getId(), false, user);
    }
  }

  private WaitRoomEntry generateWaitRoomEntry(Appointment appointment, Integer insurerId) {
    WaitRoomEntry waitRoomEntry = new WaitRoomEntry();
    waitRoomEntry.setPatientId(appointment.getPatientId());
    waitRoomEntry.setInsurerId(insurerId);
    waitRoomEntry.setPhysicianId(appointment.getProviderId());
    waitRoomEntry.setAppointmentId(appointment.getAppointmentId());
    waitRoomEntry.setOfficeId(appointment.getOfficeId());
    waitRoomEntry.setRoomId(-1);
    return waitRoomEntry;
  }

  private boolean hasTrafficManagerModuleOn() throws ProtossException {
    Set<Module> modules = getModuleManager().getActiveModules();
    return modules.contains(Module.TrafficManager);
  }

  private Integer getInsurerId(Appointment appointment, AuthorizationContext authContext,
      AuditLogUser user) throws ProtossException {
    Integer insurerId = null;
    if (appointment.getPatientId() != null) {
      PatientManager patientManager = getPatientManager(authContext, user);
      Patient patient = patientManager.getPatientById(appointment.getPatientId());
      if (patient != null) {
        insurerId = patient.getInsurerId();
      }
    }
    if (insurerId == null) {
      InsurerManager insurerManager = getInsurerManager();
      insurerId = insurerManager.getDefaultInsurer();
    }
    return insurerId;
  }

  /**
   * Helper methods for instantiating managers.
   */

  WaitRoomEntryManager getWaitRoomEntryManager() {
    return new DefaultWaitRoomEntryManager(ds);
  }

  AppointmentHistoryManager getAppointmentHistoryManager(AuthorizationContext authContext,
      AuditLogUser user) {
    return new DefaultAppointmentHistoryManager(ds, authContext, user);
  }

  PatientManager getPatientManager(AuthorizationContext authContext, AuditLogUser user) {
    return new DefaultPatientManager(ds, authContext, user);
  }

  ModuleManager getModuleManager() {
    return new DefaultModuleManager(ds);
  }

  InsurerManager getInsurerManager() {
    return new DefaultInsurerManager(ds);
  }
}
