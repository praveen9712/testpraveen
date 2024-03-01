
package com.qhrtech.emr.restapi.services.listeners;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.AuthorizationContext;
import com.qhrtech.emr.accuro.api.demographics.InsurerManager;
import com.qhrtech.emr.accuro.api.patient.PatientManager;
import com.qhrtech.emr.accuro.api.scheduling.AppointmentHistoryManager;
import com.qhrtech.emr.accuro.api.scheduling.rooms.WaitRoomEntryManager;
import com.qhrtech.emr.accuro.api.security.ModuleManager;
import com.qhrtech.emr.accuro.model.exceptions.dataaccess.DatabaseInteractionException;
import com.qhrtech.emr.accuro.model.patient.Patient;
import com.qhrtech.emr.accuro.model.scheduling.Appointment;
import com.qhrtech.emr.accuro.model.scheduling.AppointmentHistory;
import com.qhrtech.emr.accuro.model.scheduling.rooms.WaitRoomEntry;
import com.qhrtech.emr.accuro.model.security.modules.Module;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import javax.sql.DataSource;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class WaitRoomListenerTest {

  private final PodamFactory podamFactory;
  private final AuditLogUser user;
  private final AuthorizationContext authorizationContext;
  private final AppointmentHistoryManager appointmentHistoryManager;
  private final PatientManager patientManager;
  private final WaitRoomEntryManager waitRoomEntryManager;
  private final InsurerManager insurerManager;
  private final ModuleManager moduleManager;
  private final WaitRoomListener waitRoomListener;

  public WaitRoomListenerTest() {
    user = mock(AuditLogUser.class);
    authorizationContext = mock(AuthorizationContext.class);
    appointmentHistoryManager = mock(AppointmentHistoryManager.class);
    patientManager = mock(PatientManager.class);
    waitRoomEntryManager = mock(WaitRoomEntryManager.class);
    insurerManager = mock(InsurerManager.class);
    moduleManager = mock(ModuleManager.class);
    DataSource ds = mock(DataSource.class);
    waitRoomListener = spy(new WaitRoomListener(ds));

    this.podamFactory = new PodamFactoryImpl();
  }

  @Before
  public void setup() {
    when(waitRoomListener.getWaitRoomEntryManager()).thenReturn(waitRoomEntryManager);
    when(waitRoomListener.getAppointmentHistoryManager(authorizationContext, user))
        .thenReturn(appointmentHistoryManager);
    when(waitRoomListener.getPatientManager(authorizationContext, user)).thenReturn(patientManager);
    when(waitRoomListener.getInsurerManager()).thenReturn(insurerManager);
    when(waitRoomListener.getModuleManager()).thenReturn(moduleManager);
  }

  @Test
  public void testCreated() throws Exception {

    Appointment appointment = getFixture(Appointment.class);
    appointment.setDate(new LocalDate());
    int appointmentId = appointment.getAppointmentId();
    List<AppointmentHistory> appointmentHistory =
        getFixtures(AppointmentHistory.class, ArrayList::new, 5);
    Patient patient = getFixture(Patient.class);
    WaitRoomEntry waitRoomEntry = generateWaitRoomEntry(appointment, patient.getInsurerId());
    Module trafficManager = Module.TrafficManager;

    when(appointmentHistoryManager.getHistoryForAppointment(appointmentId))
        .thenReturn(appointmentHistory);
    when(patientManager.getPatientById(appointment.getPatientId())).thenReturn(patient);
    when(waitRoomEntryManager.create(waitRoomEntry, user)).thenReturn(waitRoomEntry.getId());
    when(moduleManager.getActiveModules()).thenReturn(Collections.singleton(trafficManager));

    waitRoomListener.created(appointment, authorizationContext, user);
    verify(appointmentHistoryManager, never()).getHistoryForAppointment(appointmentId);
    verify(patientManager).getPatientById(appointment.getPatientId());
    verify(waitRoomEntryManager).create(waitRoomEntry, user);
    verify(moduleManager).getActiveModules();
  }

  @Test
  public void testCreateWithDefaultInsurerId() throws Exception {
    Appointment appointment = getFixture(Appointment.class);
    appointment.setDate(new LocalDate());
    int appointmentId = appointment.getAppointmentId();
    List<AppointmentHistory> appointmentHistory =
        getFixtures(AppointmentHistory.class, ArrayList::new, 5);
    Patient patient = getFixture(Patient.class);
    patient.setInsurerId(null);
    Integer insurerId = TestUtilities.nextId();
    WaitRoomEntry waitRoomEntry = generateWaitRoomEntry(appointment, insurerId);
    Module trafficManager = Module.TrafficManager;

    when(appointmentHistoryManager.getHistoryForAppointment(appointmentId))
        .thenReturn(appointmentHistory);
    when(patientManager.getPatientById(appointment.getPatientId())).thenReturn(patient);
    when(waitRoomEntryManager.create(waitRoomEntry, user)).thenReturn(waitRoomEntry.getId());
    when(moduleManager.getActiveModules()).thenReturn(Collections.singleton(trafficManager));
    when(insurerManager.getDefaultInsurer()).thenReturn(insurerId);

    waitRoomListener.created(appointment, authorizationContext, user);
    verify(appointmentHistoryManager, never()).getHistoryForAppointment(appointmentId);
    verify(patientManager).getPatientById(appointment.getPatientId());
    verify(waitRoomEntryManager).create(waitRoomEntry, user);
    verify(moduleManager).getActiveModules();
    verify(insurerManager).getDefaultInsurer();
  }

  @Test
  public void testCreateWhenInsurerBlManagerOccursSqlException() throws Exception {
    Appointment appointment = getFixture(Appointment.class);
    appointment.setDate(new LocalDate());
    int appointmentId = appointment.getAppointmentId();
    List<AppointmentHistory> appointmentHistory =
        getFixtures(AppointmentHistory.class, ArrayList::new, 5);
    Patient patient = getFixture(Patient.class);
    patient.setInsurerId(null);
    Integer insurerId = TestUtilities.nextId();
    WaitRoomEntry waitRoomEntry = generateWaitRoomEntry(appointment, insurerId);
    Module trafficManager = Module.TrafficManager;

    when(appointmentHistoryManager.getHistoryForAppointment(appointmentId))
        .thenReturn(appointmentHistory);
    when(patientManager.getPatientById(appointment.getPatientId())).thenReturn(patient);
    when(waitRoomEntryManager.create(waitRoomEntry, user)).thenReturn(waitRoomEntry.getId());
    when(moduleManager.getActiveModules()).thenReturn(Collections.singleton(trafficManager));
    when(insurerManager.getDefaultInsurer()).thenThrow(new DatabaseInteractionException(""));

    waitRoomListener.created(appointment, authorizationContext, user);
    verify(appointmentHistoryManager, never()).getHistoryForAppointment(appointmentId);
    verify(patientManager).getPatientById(appointment.getPatientId());
    verify(waitRoomEntryManager, never()).create(waitRoomEntry, user);
    verify(moduleManager).getActiveModules();
    verify(insurerManager).getDefaultInsurer();
  }

  @Test
  public void testCreatedTrafficManagerModuleOff() throws Exception {
    when(moduleManager.getActiveModules()).thenReturn(Collections.emptySet());

    waitRoomListener.created(null, authorizationContext, user);

    verify(moduleManager).getActiveModules();
  }

  @Test
  public void testUpdated() throws Exception {
    Appointment appointment = getFixture(Appointment.class);
    appointment.setDate(new LocalDate());
    appointment.getAppointmentDetails().setArrived(true);
    int appointmentId = appointment.getAppointmentId();
    List<AppointmentHistory> appointmentHistory =
        getFixtures(AppointmentHistory.class, ArrayList::new, 5);
    appointmentHistory.get(1).setPatientId(appointment.getPatientId());
    Patient patient = getFixture(Patient.class);
    WaitRoomEntry waitRoomEntry = generateWaitRoomEntry(appointment, patient.getInsurerId());
    Module trafficManager = Module.TrafficManager;

    when(appointmentHistoryManager.getHistoryForAppointment(appointmentId))
        .thenReturn(appointmentHistory);
    when(patientManager.getPatientById(appointment.getPatientId())).thenReturn(patient);
    when(waitRoomEntryManager.create(waitRoomEntry, user)).thenReturn(waitRoomEntry.getId());
    when(moduleManager.getActiveModules()).thenReturn(Collections.singleton(trafficManager));

    waitRoomListener.updated(appointment, authorizationContext, user);
    verify(appointmentHistoryManager).getHistoryForAppointment(appointmentId);
    verify(patientManager).getPatientById(appointment.getPatientId());
    verify(waitRoomEntryManager).create(waitRoomEntry, user);
    verify(moduleManager).getActiveModules();
  }


  @Test
  public void testUpdatedWhenPatientIsNotArrive() throws Exception {
    Appointment appointment = getFixture(Appointment.class);
    appointment.setDate(new LocalDate());
    appointment.getAppointmentDetails().setArrived(false);
    int appointmentId = appointment.getAppointmentId();
    List<AppointmentHistory> appointmentHistory =
        getFixtures(AppointmentHistory.class, ArrayList::new, 5);
    appointmentHistory.get(1).setPatientId(appointment.getPatientId());
    Patient patient = getFixture(Patient.class);
    WaitRoomEntry waitRoomEntry = generateWaitRoomEntry(appointment, patient.getInsurerId());
    List<WaitRoomEntry> waitRoomEntries = getFixtures(WaitRoomEntry.class, ArrayList::new, 10);
    Module trafficManager = Module.TrafficManager;

    when(appointmentHistoryManager.getHistoryForAppointment(appointmentId))
        .thenReturn(appointmentHistory);
    when(patientManager.getPatientById(appointment.getPatientId())).thenReturn(patient);
    when(waitRoomEntryManager.create(waitRoomEntry, user)).thenReturn(waitRoomEntry.getId());
    when(moduleManager.getActiveModules()).thenReturn(Collections.singleton(trafficManager));
    when(waitRoomEntryManager.getForAppointment(appointmentId)).thenReturn(waitRoomEntries);

    waitRoomListener.updated(appointment, authorizationContext, user);
    verify(appointmentHistoryManager, never()).getHistoryForAppointment(appointmentId);
    verify(patientManager, never()).getPatientById(appointment.getPatientId());
    verify(waitRoomEntryManager, never()).create(waitRoomEntry, user);
    verify(moduleManager).getActiveModules();
    for (WaitRoomEntry entry : waitRoomEntries) {
      verify(waitRoomEntryManager).delete(entry.getId(), false, user);
    }
  }

  @Test
  public void testUpdatedWithChangedPatient() throws Exception {
    Appointment appointment = getFixture(Appointment.class);
    appointment.setDate(new LocalDate());
    int appointmentId = appointment.getAppointmentId();
    List<AppointmentHistory> appointmentHistory =
        getFixtures(AppointmentHistory.class, ArrayList::new, 5);
    Patient patient = getFixture(Patient.class);
    WaitRoomEntry waitRoomEntry = generateWaitRoomEntry(appointment, patient.getInsurerId());
    List<WaitRoomEntry> waitRoomEntries = getFixtures(WaitRoomEntry.class, ArrayList::new, 10);
    Module trafficManager = Module.TrafficManager;

    when(appointmentHistoryManager.getHistoryForAppointment(appointmentId))
        .thenReturn(appointmentHistory);
    when(patientManager.getPatientById(appointment.getPatientId())).thenReturn(patient);
    when(waitRoomEntryManager.create(waitRoomEntry, user)).thenReturn(waitRoomEntry.getId());
    when(moduleManager.getActiveModules()).thenReturn(Collections.singleton(trafficManager));
    when(waitRoomEntryManager.getForAppointment(appointmentId)).thenReturn(waitRoomEntries);

    waitRoomListener.updated(appointment, authorizationContext, user);
    verify(appointmentHistoryManager).getHistoryForAppointment(appointmentId);
    verify(patientManager).getPatientById(appointment.getPatientId());
    verify(waitRoomEntryManager).create(waitRoomEntry, user);
    verify(moduleManager).getActiveModules();
    for (WaitRoomEntry entry : waitRoomEntries) {
      verify(waitRoomEntryManager).delete(entry.getId(), false, user);
    }
  }

  @Test
  public void testUpdatedWithDuplicateWaitRoomEntry() throws Exception {
    Appointment appointment = getFixture(Appointment.class);
    appointment.setDate(new LocalDate());
    appointment.getAppointmentDetails().setArrived(true);
    int appointmentId = appointment.getAppointmentId();
    List<AppointmentHistory> appointmentHistory =
        getFixtures(AppointmentHistory.class, ArrayList::new, 5);
    appointmentHistory.get(1).setPatientId(appointment.getPatientId());
    Patient patient = getFixture(Patient.class);
    WaitRoomEntry waitRoomEntry = generateWaitRoomEntry(appointment, patient.getInsurerId());
    Module trafficManager = Module.TrafficManager;

    when(appointmentHistoryManager.getHistoryForAppointment(appointmentId))
        .thenReturn(appointmentHistory);
    when(patientManager.getPatientById(appointment.getPatientId())).thenReturn(patient);
    when(moduleManager.getActiveModules()).thenReturn(Collections.singleton(trafficManager));
    when(waitRoomEntryManager.getForAppointment(waitRoomEntry.getAppointmentId()))
        .thenReturn(Collections.singletonList(waitRoomEntry));

    waitRoomListener.updated(appointment, authorizationContext, user);
    verify(appointmentHistoryManager).getHistoryForAppointment(appointmentId);
    verify(patientManager).getPatientById(appointment.getPatientId());
    verify(waitRoomEntryManager).getForAppointment(waitRoomEntry.getAppointmentId());
    verifyNoMoreInteractions(waitRoomEntryManager);
    verify(moduleManager).getActiveModules();
  }

  @Test
  public void testUpdatedTrafficManagerModuleOff() throws Exception {
    when(moduleManager.getActiveModules()).thenReturn(Collections.emptySet());

    waitRoomListener.updated(null, authorizationContext, user);

    verify(moduleManager).getActiveModules();
    verifyZeroInteractions(waitRoomEntryManager);
    verifyZeroInteractions(appointmentHistoryManager);
  }

  @Test
  public void testCancelled() throws Exception {
    Appointment appointment = new Appointment();
    appointment.setAppointmentId(TestUtilities.nextId());
    List<WaitRoomEntry> waitRoomEntries = getFixtures(WaitRoomEntry.class, ArrayList::new, 10);
    Module trafficManager = Module.TrafficManager;

    when(moduleManager.getActiveModules()).thenReturn(Collections.singleton(trafficManager));
    when(waitRoomEntryManager.getForAppointment(appointment.getAppointmentId()))
        .thenReturn(waitRoomEntries);

    waitRoomListener.cancelled(appointment, authorizationContext, user);
    verify(appointmentHistoryManager, never())
        .getHistoryForAppointment(appointment.getAppointmentId());
    verify(moduleManager).getActiveModules();
    for (WaitRoomEntry entry : waitRoomEntries) {
      verify(waitRoomEntryManager).delete(entry.getId(), false, user);
    }
  }

  @Test
  public void testCancelledDataAccessException() throws Exception {
    Appointment appointment = new Appointment();
    appointment.setAppointmentId(TestUtilities.nextId());
    Module trafficManager = Module.TrafficManager;

    when(moduleManager.getActiveModules()).thenReturn(Collections.singleton(trafficManager));
    when(waitRoomEntryManager.getForAppointment(appointment.getAppointmentId()))
        .thenThrow(new DatabaseInteractionException(""));

    waitRoomListener.cancelled(appointment, authorizationContext, user);
    verify(moduleManager).getActiveModules();
    verify(waitRoomEntryManager).getForAppointment(appointment.getAppointmentId());
  }

  @Test
  public void testCancelledTrafficManagerModuleOff() throws Exception {
    when(moduleManager.getActiveModules()).thenReturn(Collections.emptySet());

    waitRoomListener.cancelled(null, authorizationContext, user);

    verify(moduleManager).getActiveModules();
    verifyZeroInteractions(waitRoomEntryManager);
  }

  @Test
  public void testDeleted() throws Exception {
    Appointment appointment = new Appointment();
    appointment.setAppointmentId(TestUtilities.nextId());
    List<WaitRoomEntry> waitRoomEntries = getFixtures(WaitRoomEntry.class, ArrayList::new, 10);
    Module trafficManager = Module.TrafficManager;

    when(moduleManager.getActiveModules()).thenReturn(Collections.singleton(trafficManager));
    when(waitRoomEntryManager.getForAppointment(appointment.getAppointmentId()))
        .thenReturn(waitRoomEntries);

    waitRoomListener.deleted(appointment, authorizationContext, user);
    verify(appointmentHistoryManager, never())
        .getHistoryForAppointment(appointment.getAppointmentId());
    verify(moduleManager).getActiveModules();
    for (WaitRoomEntry entry : waitRoomEntries) {
      verify(waitRoomEntryManager).delete(entry.getId(), false, user);
    }
  }

  @Test
  public void testDeletedDataAccessException() throws Exception {
    Appointment appointment = new Appointment();
    appointment.setAppointmentId(TestUtilities.nextId());
    Module trafficManager = Module.TrafficManager;

    when(moduleManager.getActiveModules()).thenReturn(Collections.singleton(trafficManager));
    when(waitRoomEntryManager.getForAppointment(appointment.getAppointmentId()))
        .thenThrow(new DatabaseInteractionException(""));

    waitRoomListener.deleted(appointment, authorizationContext, user);
    verify(moduleManager).getActiveModules();
    verify(waitRoomEntryManager).getForAppointment(appointment.getAppointmentId());
  }

  @Test
  public void testDeletedTrafficManagerModuleOff() throws Exception {
    when(moduleManager.getActiveModules()).thenReturn(Collections.emptySet());

    waitRoomListener.deleted(null, authorizationContext, user);

    verify(moduleManager).getActiveModules();
    verifyZeroInteractions(waitRoomEntryManager);
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

  protected <C> C getFixture(Class<C> clazz, Type... args) {
    return podamFactory.manufacturePojo(clazz, args);
  }

  protected <C extends Collection<D>, D> C getFixtures(Class<D> clazz,
      Supplier<C> collectionFactory, int count, Type... args) {
    C c = collectionFactory.get();
    IntStream.range(0, count).forEach(i -> c.add(getFixture(clazz, args)));
    return c;
  }
}

