
package com.qhrtech.emr.restapi.services;

import com.qhrtech.emr.accdocs.api.docs.AccDocManager;
import com.qhrtech.emr.accdocs.api.docs.DefaultAccDocManager;
import com.qhrtech.emr.accdocs.api.docs.DefaultFolderManager;
import com.qhrtech.emr.accdocs.api.docs.FolderManager;
import com.qhrtech.emr.accuro.api.allergy.AllergyCommentManager;
import com.qhrtech.emr.accuro.api.allergy.AllergyReactionManager;
import com.qhrtech.emr.accuro.api.allergy.DefaultAllergyCommentManager;
import com.qhrtech.emr.accuro.api.allergy.DefaultAllergyReactionManager;
import com.qhrtech.emr.accuro.api.allergy.DefaultNoKnownAllergyManager;
import com.qhrtech.emr.accuro.api.allergy.DefaultPatientAllergyManager;
import com.qhrtech.emr.accuro.api.allergy.NoKnownAllergyManager;
import com.qhrtech.emr.accuro.api.allergy.PatientAllergyManager;
import com.qhrtech.emr.accuro.api.attachments.DefaultDiagnosisLinkManager;
import com.qhrtech.emr.accuro.api.attachments.DiagnosisLinkManager;
import com.qhrtech.emr.accuro.api.billing.BillProcedureManager;
import com.qhrtech.emr.accuro.api.billing.DefaultBillProcedureManager;
import com.qhrtech.emr.accuro.api.codes.CodeSubSystemManager;
import com.qhrtech.emr.accuro.api.codes.CodeSystemManager;
import com.qhrtech.emr.accuro.api.codes.DefaultCodeSubSystemManager;
import com.qhrtech.emr.accuro.api.codes.DefaultCodeSystemManager;
import com.qhrtech.emr.accuro.api.customfield.CustomFieldManager;
import com.qhrtech.emr.accuro.api.customfield.DefaultCustomFieldManager;
import com.qhrtech.emr.accuro.api.customfield.DefaultPatientCustomFieldManager;
import com.qhrtech.emr.accuro.api.customfield.PatientCustomFieldManager;
import com.qhrtech.emr.accuro.api.demographics.DefaultGenderManager;
import com.qhrtech.emr.accuro.api.demographics.DefaultInsurerManager;
import com.qhrtech.emr.accuro.api.demographics.DefaultLocationManager;
import com.qhrtech.emr.accuro.api.demographics.DefaultPersonTitleManager;
import com.qhrtech.emr.accuro.api.demographics.GenderManager;
import com.qhrtech.emr.accuro.api.demographics.InsurerManager;
import com.qhrtech.emr.accuro.api.demographics.LocationManager;
import com.qhrtech.emr.accuro.api.demographics.PersonTitleManager;
import com.qhrtech.emr.accuro.api.docs.DefaultDocsAccblobManager;
import com.qhrtech.emr.accuro.api.docs.DefaultDocsDataManager;
import com.qhrtech.emr.accuro.api.docs.DefaultDocumentManager;
import com.qhrtech.emr.accuro.api.docs.DefaultFolderTypeManager;
import com.qhrtech.emr.accuro.api.docs.DefaultSpadeDocumentManager;
import com.qhrtech.emr.accuro.api.docs.DefaultSpadeFolderManager;
import com.qhrtech.emr.accuro.api.docs.DocsAccblobManager;
import com.qhrtech.emr.accuro.api.docs.DocsDataManager;
import com.qhrtech.emr.accuro.api.docs.DocumentManager;
import com.qhrtech.emr.accuro.api.docs.FolderTypeManager;
import com.qhrtech.emr.accuro.api.docs.SpadeDocumentManager;
import com.qhrtech.emr.accuro.api.docs.SpadeFolderManager;
import com.qhrtech.emr.accuro.api.eprescribe.DefaultEprescribeCancelRequestManager;
import com.qhrtech.emr.accuro.api.eprescribe.DefaultEprescribeCancelResponseManager;
import com.qhrtech.emr.accuro.api.eprescribe.DefaultEprescribeJobHistoryManager;
import com.qhrtech.emr.accuro.api.eprescribe.DefaultEprescribeJobManager;
import com.qhrtech.emr.accuro.api.eprescribe.DefaultEprescribeJobPrescriptionMedicationManager;
import com.qhrtech.emr.accuro.api.eprescribe.DefaultEprescribeJobTaskManager;
import com.qhrtech.emr.accuro.api.eprescribe.DefaultEprescribeJobTypeManager;
import com.qhrtech.emr.accuro.api.eprescribe.DefaultEprescribeOrderStatusManager;
import com.qhrtech.emr.accuro.api.eprescribe.DefaultExternalPatientManager;
import com.qhrtech.emr.accuro.api.eprescribe.EprescribeCancelRequestManager;
import com.qhrtech.emr.accuro.api.eprescribe.EprescribeCancelResponseManager;
import com.qhrtech.emr.accuro.api.eprescribe.EprescribeJobHistoryManager;
import com.qhrtech.emr.accuro.api.eprescribe.EprescribeJobManager;
import com.qhrtech.emr.accuro.api.eprescribe.EprescribeJobPrescriptionMedicationManager;
import com.qhrtech.emr.accuro.api.eprescribe.EprescribeJobTaskManager;
import com.qhrtech.emr.accuro.api.eprescribe.EprescribeJobTypeManager;
import com.qhrtech.emr.accuro.api.eprescribe.EprescribeOrderStatusManager;
import com.qhrtech.emr.accuro.api.eprescribe.ExternalPatientManager;
import com.qhrtech.emr.accuro.api.identity.DefaultExternalUserIdentityManager;
import com.qhrtech.emr.accuro.api.identity.ExternalUserIdentityManager;
import com.qhrtech.emr.accuro.api.immunization.DefaultImmunizationScheduleManager;
import com.qhrtech.emr.accuro.api.immunization.DefaultPatientImmunizationManager;
import com.qhrtech.emr.accuro.api.immunization.DefaultScheduleVaccineManager;
import com.qhrtech.emr.accuro.api.immunization.ImmunizationScheduleManager;
import com.qhrtech.emr.accuro.api.immunization.PatientImmunizationManager;
import com.qhrtech.emr.accuro.api.immunization.ScheduleVaccineManager;
import com.qhrtech.emr.accuro.api.labs.DefaultLabLinkGroupManager;
import com.qhrtech.emr.accuro.api.labs.DefaultLabManager;
import com.qhrtech.emr.accuro.api.labs.LabLinkGroupManager;
import com.qhrtech.emr.accuro.api.labs.LabManager;
import com.qhrtech.emr.accuro.api.letters.DefaultGeneratedLetterManager;
import com.qhrtech.emr.accuro.api.letters.DefaultLetterManager;
import com.qhrtech.emr.accuro.api.letters.GeneratedLetterManager;
import com.qhrtech.emr.accuro.api.letters.LetterManager;
import com.qhrtech.emr.accuro.api.locks.DefaultProtectionLockManager;
import com.qhrtech.emr.accuro.api.locks.DefaultScheduleLockManager;
import com.qhrtech.emr.accuro.api.locks.ProtectionLockManager;
import com.qhrtech.emr.accuro.api.locks.ScheduleLockManager;
import com.qhrtech.emr.accuro.api.logging.DefaultLogManager;
import com.qhrtech.emr.accuro.api.logging.LogManager;
import com.qhrtech.emr.accuro.api.masking.DefaultMaskingManager;
import com.qhrtech.emr.accuro.api.masking.MaskingManager;
import com.qhrtech.emr.accuro.api.medeo.DefaultMedeoOrganizationManager;
import com.qhrtech.emr.accuro.api.medeo.DefaultMedeoPatientManager;
import com.qhrtech.emr.accuro.api.medeo.DefaultMedeoProviderLinkManager;
import com.qhrtech.emr.accuro.api.medeo.MedeoOrganizationManager;
import com.qhrtech.emr.accuro.api.medeo.MedeoPatientManager;
import com.qhrtech.emr.accuro.api.medeo.MedeoProviderLinkManager;
import com.qhrtech.emr.accuro.api.medicalhistory.ContactManager;
import com.qhrtech.emr.accuro.api.medicalhistory.DefaultContactManager;
import com.qhrtech.emr.accuro.api.medicalhistory.DefaultDiagnosisStatusManager;
import com.qhrtech.emr.accuro.api.medicalhistory.DefaultExternalContactIdentifierManager;
import com.qhrtech.emr.accuro.api.medicalhistory.DefaultHistoryRegularItemManager;
import com.qhrtech.emr.accuro.api.medicalhistory.DefaultHistoryTypeManager;
import com.qhrtech.emr.accuro.api.medicalhistory.DefaultPatientDiagnosisHistoryManager;
import com.qhrtech.emr.accuro.api.medicalhistory.DefaultPatientDiagnosisManager;
import com.qhrtech.emr.accuro.api.medicalhistory.DefaultPatientHistoryRegularHistoryManager;
import com.qhrtech.emr.accuro.api.medicalhistory.DefaultPatientHistoryRegularManager;
import com.qhrtech.emr.accuro.api.medicalhistory.DefaultPatientHistoryTextHistoryManager;
import com.qhrtech.emr.accuro.api.medicalhistory.DefaultPatientHistoryTextManager;
import com.qhrtech.emr.accuro.api.medicalhistory.DefaultPatientHistoryTrackingManager;
import com.qhrtech.emr.accuro.api.medicalhistory.DefaultPatientHistoryUrlHistoryManager;
import com.qhrtech.emr.accuro.api.medicalhistory.DefaultPatientHistoryUrlManager;
import com.qhrtech.emr.accuro.api.medicalhistory.DefaultSelectionListManager;
import com.qhrtech.emr.accuro.api.medicalhistory.DefaultVaccineManager;
import com.qhrtech.emr.accuro.api.medicalhistory.DiagnosisStatusManager;
import com.qhrtech.emr.accuro.api.medicalhistory.ExternalContactIdentifierManager;
import com.qhrtech.emr.accuro.api.medicalhistory.HistoryRegularItemManager;
import com.qhrtech.emr.accuro.api.medicalhistory.HistoryTypeManager;
import com.qhrtech.emr.accuro.api.medicalhistory.PatientDiagnosisHistoryManager;
import com.qhrtech.emr.accuro.api.medicalhistory.PatientDiagnosisManager;
import com.qhrtech.emr.accuro.api.medicalhistory.PatientHistoryRegularHistoryManager;
import com.qhrtech.emr.accuro.api.medicalhistory.PatientHistoryRegularManager;
import com.qhrtech.emr.accuro.api.medicalhistory.PatientHistoryTextHistoryManager;
import com.qhrtech.emr.accuro.api.medicalhistory.PatientHistoryTextManager;
import com.qhrtech.emr.accuro.api.medicalhistory.PatientHistoryTrackingManager;
import com.qhrtech.emr.accuro.api.medicalhistory.PatientHistoryUrlHistoryManager;
import com.qhrtech.emr.accuro.api.medicalhistory.PatientHistoryUrlManager;
import com.qhrtech.emr.accuro.api.medicalhistory.SelectionListManager;
import com.qhrtech.emr.accuro.api.medicalhistory.VaccineManager;
import com.qhrtech.emr.accuro.api.medications.AlternativeHealthProductManager;
import com.qhrtech.emr.accuro.api.medications.DefaultAlternativeHealthProductManager;
import com.qhrtech.emr.accuro.api.medications.DefaultFormulationManager;
import com.qhrtech.emr.accuro.api.medications.DefaultGenericDrugManager;
import com.qhrtech.emr.accuro.api.medications.DefaultLimitedUseCodeManager;
import com.qhrtech.emr.accuro.api.medications.DefaultManufacturedDrugManager;
import com.qhrtech.emr.accuro.api.medications.DefaultMedicationLogEntryManager;
import com.qhrtech.emr.accuro.api.medications.DefaultNaturalHealthProductManager;
import com.qhrtech.emr.accuro.api.medications.FormulationManager;
import com.qhrtech.emr.accuro.api.medications.GenericDrugManager;
import com.qhrtech.emr.accuro.api.medications.LimitedUseCodeManager;
import com.qhrtech.emr.accuro.api.medications.ManufacturedDrugManager;
import com.qhrtech.emr.accuro.api.medications.MedicationLogEntryManager;
import com.qhrtech.emr.accuro.api.medications.NaturalHealthProductManager;
import com.qhrtech.emr.accuro.api.medications.fdb.DefaultFdbCcddMappingManager;
import com.qhrtech.emr.accuro.api.medications.fdb.FdbCcddMappingManager;
import com.qhrtech.emr.accuro.api.messaging.eprescription.DefaultDispenseNotificationManager;
import com.qhrtech.emr.accuro.api.messaging.eprescription.DefaultEprescribeOutcomeCodeManager;
import com.qhrtech.emr.accuro.api.messaging.eprescription.DefaultEprescribeOutcomeManager;
import com.qhrtech.emr.accuro.api.messaging.eprescription.DispenseNotificationManager;
import com.qhrtech.emr.accuro.api.messaging.eprescription.EprescribeOutcomeCodeManager;
import com.qhrtech.emr.accuro.api.messaging.eprescription.EprescribeOutcomeManager;
import com.qhrtech.emr.accuro.api.office.DefaultOfficeManager;
import com.qhrtech.emr.accuro.api.office.OfficeManager;
import com.qhrtech.emr.accuro.api.patient.DefaultPatientChartLockManager;
import com.qhrtech.emr.accuro.api.patient.DefaultPatientFlagManager;
import com.qhrtech.emr.accuro.api.patient.DefaultPatientManager;
import com.qhrtech.emr.accuro.api.patient.DefaultPatientProfilePictureManager;
import com.qhrtech.emr.accuro.api.patient.DefaultPatientStatusManager;
import com.qhrtech.emr.accuro.api.patient.PatientChartLockManager;
import com.qhrtech.emr.accuro.api.patient.PatientFlagManager;
import com.qhrtech.emr.accuro.api.patient.PatientManager;
import com.qhrtech.emr.accuro.api.patient.PatientProfilePictureManager;
import com.qhrtech.emr.accuro.api.patient.PatientStatusManager;
import com.qhrtech.emr.accuro.api.person.DefaultRelationshipStatusManager;
import com.qhrtech.emr.accuro.api.person.RelationshipStatusManager;
import com.qhrtech.emr.accuro.api.physician.DefaultMasterNumberManager;
import com.qhrtech.emr.accuro.api.physician.MasterNumberManager;
import com.qhrtech.emr.accuro.api.preferences.AccuroPreferenceManager;
import com.qhrtech.emr.accuro.api.preferences.DefaultAccuroPreferenceManager;
import com.qhrtech.emr.accuro.api.prescribeit.DefaultRenewalRequestGroupManager;
import com.qhrtech.emr.accuro.api.prescribeit.DefaultRenewalRequestManager;
import com.qhrtech.emr.accuro.api.prescribeit.DefaultRenewalRequestResponseManager;
import com.qhrtech.emr.accuro.api.prescribeit.RenewalRequestGroupManager;
import com.qhrtech.emr.accuro.api.prescribeit.RenewalRequestManager;
import com.qhrtech.emr.accuro.api.prescribeit.RenewalRequestResponseManager;
import com.qhrtech.emr.accuro.api.prescription.AnnotationManager;
import com.qhrtech.emr.accuro.api.prescription.DefaultAnnotationManager;
import com.qhrtech.emr.accuro.api.prescription.DefaultDosageManager;
import com.qhrtech.emr.accuro.api.prescription.DefaultIngredientManager;
import com.qhrtech.emr.accuro.api.prescription.DefaultInteractionManagementDetailsManager;
import com.qhrtech.emr.accuro.api.prescription.DefaultInteractionManager;
import com.qhrtech.emr.accuro.api.prescription.DefaultPrescriptionIndicationManager;
import com.qhrtech.emr.accuro.api.prescription.DefaultPrescriptionMacroManager;
import com.qhrtech.emr.accuro.api.prescription.DefaultPrescriptionMedicationManager;
import com.qhrtech.emr.accuro.api.prescription.DefaultStatusHistoryManager;
import com.qhrtech.emr.accuro.api.prescription.DefaultWellnetPrescriptionLinkManager;
import com.qhrtech.emr.accuro.api.prescription.DosageManager;
import com.qhrtech.emr.accuro.api.prescription.IngredientManager;
import com.qhrtech.emr.accuro.api.prescription.InteractionManagementDetailsManager;
import com.qhrtech.emr.accuro.api.prescription.InteractionManager;
import com.qhrtech.emr.accuro.api.prescription.PrescriptionIndicationManager;
import com.qhrtech.emr.accuro.api.prescription.PrescriptionMacroManager;
import com.qhrtech.emr.accuro.api.prescription.PrescriptionMedicationManager;
import com.qhrtech.emr.accuro.api.prescription.StatusHistoryManager;
import com.qhrtech.emr.accuro.api.prescription.WellnetPrescriptionLinkManager;
import com.qhrtech.emr.accuro.api.provider.DefaultPhysicianLabIdsManager;
import com.qhrtech.emr.accuro.api.provider.DefaultProviderManager;
import com.qhrtech.emr.accuro.api.provider.DefaultProviderScheduleCalendarManager;
import com.qhrtech.emr.accuro.api.provider.PhysicianLabIdsManager;
import com.qhrtech.emr.accuro.api.provider.ProviderManager;
import com.qhrtech.emr.accuro.api.provider.ProviderScheduleCalenderManager;
import com.qhrtech.emr.accuro.api.referral.DefaultReferralOrderManager;
import com.qhrtech.emr.accuro.api.referral.ReferralOrderManager;
import com.qhrtech.emr.accuro.api.registry.DefaultRegistryEntryManager;
import com.qhrtech.emr.accuro.api.registry.RegistryEntryManager;
import com.qhrtech.emr.accuro.api.repliform.DefaultPatientRepliformReportableManager;
import com.qhrtech.emr.accuro.api.repliform.PatientRepliformReportableManager;
import com.qhrtech.emr.accuro.api.scheduling.AppointmentHistoryManager;
import com.qhrtech.emr.accuro.api.scheduling.AppointmentManager;
import com.qhrtech.emr.accuro.api.scheduling.AppointmentReminderManager;
import com.qhrtech.emr.accuro.api.scheduling.AvailabilityManager;
import com.qhrtech.emr.accuro.api.scheduling.DefaultAppointmentHistoryManager;
import com.qhrtech.emr.accuro.api.scheduling.DefaultAppointmentManager;
import com.qhrtech.emr.accuro.api.scheduling.DefaultAppointmentReminderManager;
import com.qhrtech.emr.accuro.api.scheduling.DefaultAvailabilityManager;
import com.qhrtech.emr.accuro.api.scheduling.DefaultPriorityManager;
import com.qhrtech.emr.accuro.api.scheduling.DefaultScheduleSettingsManager;
import com.qhrtech.emr.accuro.api.scheduling.DefaultSiteManager;
import com.qhrtech.emr.accuro.api.scheduling.DefaultStatusManager;
import com.qhrtech.emr.accuro.api.scheduling.DefaultSuggestionManager;
import com.qhrtech.emr.accuro.api.scheduling.PriorityManager;
import com.qhrtech.emr.accuro.api.scheduling.ScheduleSettingsManager;
import com.qhrtech.emr.accuro.api.scheduling.SiteManager;
import com.qhrtech.emr.accuro.api.scheduling.StatusManager;
import com.qhrtech.emr.accuro.api.scheduling.SuggestionManager;
import com.qhrtech.emr.accuro.api.scheduling.rooms.DefaultScheduleRoomManager;
import com.qhrtech.emr.accuro.api.scheduling.rooms.DefaultWaitRoomEntryManager;
import com.qhrtech.emr.accuro.api.scheduling.rooms.ScheduleRoomManager;
import com.qhrtech.emr.accuro.api.scheduling.rooms.WaitRoomEntryManager;
import com.qhrtech.emr.accuro.api.security.AccuroApiContextManager;
import com.qhrtech.emr.accuro.api.security.AccuroUserManager;
import com.qhrtech.emr.accuro.api.security.AuthorizedClientManager;
import com.qhrtech.emr.accuro.api.security.DefaultAccuroApiContextManager;
import com.qhrtech.emr.accuro.api.security.DefaultAccuroUserManager;
import com.qhrtech.emr.accuro.api.security.DefaultAuthorizedClientManager;
import com.qhrtech.emr.accuro.api.security.DefaultModuleManager;
import com.qhrtech.emr.accuro.api.security.DefaultProviderPermissionManager;
import com.qhrtech.emr.accuro.api.security.DefaultUserAuthenticationManager;
import com.qhrtech.emr.accuro.api.security.DefaultUserInfoManager;
import com.qhrtech.emr.accuro.api.security.DefaultUserPermissionManager;
import com.qhrtech.emr.accuro.api.security.DefaultUserPreferencesManager;
import com.qhrtech.emr.accuro.api.security.ModuleManager;
import com.qhrtech.emr.accuro.api.security.ProviderPermissionManager;
import com.qhrtech.emr.accuro.api.security.UserAuthenticationManager;
import com.qhrtech.emr.accuro.api.security.UserInfoManager;
import com.qhrtech.emr.accuro.api.security.UserPermissionManager;
import com.qhrtech.emr.accuro.api.security.UserPreferencesManager;
import com.qhrtech.emr.accuro.api.security.roles.DefaultRoleManager;
import com.qhrtech.emr.accuro.api.security.roles.RoleManager;
import com.qhrtech.emr.accuro.api.synapse.ConversationContactManager;
import com.qhrtech.emr.accuro.api.synapse.ConversationExternalPatientManager;
import com.qhrtech.emr.accuro.api.synapse.ConversationManager;
import com.qhrtech.emr.accuro.api.synapse.ConversationMessageAttachmentContentManager;
import com.qhrtech.emr.accuro.api.synapse.ConversationMessageAttachmentManager;
import com.qhrtech.emr.accuro.api.synapse.ConversationMessageManager;
import com.qhrtech.emr.accuro.api.synapse.ConversationMessageStatusManager;
import com.qhrtech.emr.accuro.api.synapse.ConversationPatientManager;
import com.qhrtech.emr.accuro.api.synapse.ConversationUnmatchedPatientManager;
import com.qhrtech.emr.accuro.api.synapse.DefaultConversationContactManager;
import com.qhrtech.emr.accuro.api.synapse.DefaultConversationExternalPatientManager;
import com.qhrtech.emr.accuro.api.synapse.DefaultConversationManager;
import com.qhrtech.emr.accuro.api.synapse.DefaultConversationMessageAttachmentContentManager;
import com.qhrtech.emr.accuro.api.synapse.DefaultConversationMessageAttachmentManager;
import com.qhrtech.emr.accuro.api.synapse.DefaultConversationMessageManager;
import com.qhrtech.emr.accuro.api.synapse.DefaultConversationMessageStatusManager;
import com.qhrtech.emr.accuro.api.synapse.DefaultConversationPatientManager;
import com.qhrtech.emr.accuro.api.synapse.DefaultConversationUnmatchedPatientManager;
import com.qhrtech.emr.accuro.api.sysinfo.DefaultSystemInformationManager;
import com.qhrtech.emr.accuro.api.sysinfo.SystemInformationManager;
import com.qhrtech.emr.accuro.api.tasks.DefaultTaskManager;
import com.qhrtech.emr.accuro.api.tasks.DefaultTaskReasonManager;
import com.qhrtech.emr.accuro.api.tasks.TaskManager;
import com.qhrtech.emr.accuro.api.tasks.TaskReasonManager;
import com.qhrtech.emr.accuro.api.waitlist.ConsultPriorityManager;
import com.qhrtech.emr.accuro.api.waitlist.ConsultStatusManager;
import com.qhrtech.emr.accuro.api.waitlist.DefaultConsultPriorityManager;
import com.qhrtech.emr.accuro.api.waitlist.DefaultConsultStatusManager;
import com.qhrtech.emr.accuro.api.waitlist.DefaultWaitlistProviderManager;
import com.qhrtech.emr.accuro.api.waitlist.DefaultWaitlistRequestManager;
import com.qhrtech.emr.accuro.api.waitlist.WaitlistProviderManager;
import com.qhrtech.emr.accuro.api.waitlist.WaitlistRequestManager;
import com.qhrtech.emr.accuro.db.DatabaseType;
import com.qhrtech.emr.restapi.services.listeners.ListenerType;
import com.qhrtech.emr.restapi.util.ManagerWrapper;
import java.util.HashMap;
import java.util.Map;

/**
 * Maintains a immutable mapping of Protoss manager interfaces to their preferred implementation.
 * The Map is stored in a uniform way so that they can be created interchangeably regardless of
 * their constructor.
 */

public class ManagerMapping {

  private static final Map<Class<?>, ManagerConstructor<?>> constructorMap;

  static {
    constructorMap = new HashMap<>();

    registerClass(AppointmentManager.class,
        ManagerWrapper
            .withPermissionsAndListener(DefaultAppointmentManager::new, ListenerType.APPOINTMENT));
    registerClass(AvailabilityManager.class,
        ManagerWrapper.withPermissions(DefaultAvailabilityManager::new));
    registerClass(InsurerManager.class,
        ManagerWrapper.withDatasource(DefaultInsurerManager::new));
    registerClass(LabManager.class, ManagerWrapper.withPermissions(DefaultLabManager::new));
    registerClass(GenderManager.class,
        ManagerWrapper.withDatasource(DefaultGenderManager::new));
    registerClass(LetterManager.class,
        ManagerWrapper.withPermissions(DefaultLetterManager::new));
    registerClass(LocationManager.class,
        ManagerWrapper.withDatasource(DefaultLocationManager::new));
    registerClass(LogManager.class, ManagerWrapper.withDatasource(DefaultLogManager::new));
    registerClass(OfficeManager.class,
        ManagerWrapper.withDatasource(DefaultOfficeManager::new));
    registerClass(PatientManager.class,
        ManagerWrapper.withPermissions(DefaultPatientManager::new));
    registerClass(PatientCustomFieldManager.class,
        ManagerWrapper.withDatasource(DefaultPatientCustomFieldManager::new));
    registerClass(PriorityManager.class,
        ManagerWrapper.withDatasource(DefaultPriorityManager::new));
    registerClass(ProtectionLockManager.class,
        ManagerWrapper.withPermissions(DefaultProtectionLockManager::new));
    registerClass(ProviderManager.class,
        ManagerWrapper.withPermissions(DefaultProviderManager::new));
    registerClass(RegistryEntryManager.class,
        ManagerWrapper.withDatasource(DefaultRegistryEntryManager::new));
    registerClass(ScheduleLockManager.class,
        ManagerWrapper.withPermissions(DefaultScheduleLockManager::new));
    registerClass(ScheduleSettingsManager.class,
        ManagerWrapper.withPermissions(DefaultScheduleSettingsManager::new));
    registerClass(SiteManager.class, ManagerWrapper.withPermissions(DefaultSiteManager::new));
    registerClass(StatusManager.class,
        ManagerWrapper.withPermissions(DefaultStatusManager::new));
    registerClass(SuggestionManager.class,
        ManagerWrapper.withPermissions(DefaultSuggestionManager::new));
    registerClass(MedeoPatientManager.class,
        ManagerWrapper.withDatasource(DefaultMedeoPatientManager::new));
    registerClass(AppointmentHistoryManager.class,
        ManagerWrapper.withPermissions(DefaultAppointmentHistoryManager::new));
    registerClass(DocumentManager.class,
        ManagerWrapper.withPermissions(DefaultDocumentManager::new));
    registerClass(DocsAccblobManager.class,
        ManagerWrapper.withPermissions(DefaultDocsAccblobManager::new));
    registerClass(BillProcedureManager.class,
        ManagerWrapper.withPermissions(DefaultBillProcedureManager::new));
    registerClass(AccDocManager.class,
        ManagerWrapper.withDatasource(DefaultAccDocManager::new, DatabaseType.Documents));
    registerClass(UserAuthenticationManager.class,
        ManagerWrapper.withDatasource(DefaultUserAuthenticationManager::new));
    registerClass(AccuroPreferenceManager.class,
        ManagerWrapper.withDatasource(DefaultAccuroPreferenceManager::new));
    registerClass(AppointmentReminderManager.class,
        ManagerWrapper.withPermissions(DefaultAppointmentReminderManager::new));
    registerClass(PatientFlagManager.class,
        ManagerWrapper.withDatasource(DefaultPatientFlagManager::new));
    registerClass(LimitedUseCodeManager.class,
        ManagerWrapper.withDatasource(DefaultLimitedUseCodeManager::new));
    registerClass(SystemInformationManager.class,
        ManagerWrapper.withDatasource(DefaultSystemInformationManager::new));
    registerClass(PatientImmunizationManager.class,
        ManagerWrapper.withDatasource(DefaultPatientImmunizationManager::new));
    registerClass(DiagnosisStatusManager.class,
        ManagerWrapper.withDatasource(DefaultDiagnosisStatusManager::new));
    registerClass(HistoryTypeManager.class,
        ManagerWrapper.withDatasource(DefaultHistoryTypeManager::new));
    registerClass(ImmunizationScheduleManager.class,
        ManagerWrapper.withDatasource(DefaultImmunizationScheduleManager::new));
    registerClass(VaccineManager.class,
        ManagerWrapper.withDatasource(DefaultVaccineManager::new));
    registerClass(HistoryRegularItemManager.class,
        ManagerWrapper.withDatasource(DefaultHistoryRegularItemManager::new));
    registerClass(PatientAllergyManager.class,
        ManagerWrapper.withDatasource(DefaultPatientAllergyManager::new));
    registerClass(PatientDiagnosisManager.class,
        ManagerWrapper.withDatasource(DefaultPatientDiagnosisManager::new));
    registerClass(ManufacturedDrugManager.class,
        ManagerWrapper.withDatasource(DefaultManufacturedDrugManager::new));
    registerClass(GenericDrugManager.class,
        ManagerWrapper.withDatasource(DefaultGenericDrugManager::new));
    registerClass(AllergyCommentManager.class,
        ManagerWrapper.withDatasource(DefaultAllergyCommentManager::new));
    registerClass(NaturalHealthProductManager.class,
        ManagerWrapper.withDatasource(DefaultNaturalHealthProductManager::new));
    registerClass(AlternativeHealthProductManager.class, ManagerWrapper.withDatasource(
        DefaultAlternativeHealthProductManager::new));
    registerClass(FormulationManager.class,
        ManagerWrapper.withDatasource(DefaultFormulationManager::new));
    registerClass(IngredientManager.class,
        ManagerWrapper.withPermissions(DefaultIngredientManager::new));
    registerClass(AllergyReactionManager.class,
        ManagerWrapper.withDatasource(DefaultAllergyReactionManager::new));
    registerClass(NoKnownAllergyManager.class,
        ManagerWrapper.withDatasource(DefaultNoKnownAllergyManager::new));
    registerClass(PrescriptionMedicationManager.class,
        ManagerWrapper.withPermissions(DefaultPrescriptionMedicationManager::new));
    registerClass(ScheduleVaccineManager.class,
        ManagerWrapper.withDatasource(DefaultScheduleVaccineManager::new));
    registerClass(DosageManager.class,
        ManagerWrapper.withPermissions(DefaultDosageManager::new));
    registerClass(PrescriptionIndicationManager.class,
        ManagerWrapper.withPermissions(DefaultPrescriptionIndicationManager::new));
    registerClass(AnnotationManager.class,
        ManagerWrapper.withPermissions(DefaultAnnotationManager::new));
    registerClass(StatusHistoryManager.class,
        ManagerWrapper.withPermissions(DefaultStatusHistoryManager::new));
    registerClass(InteractionManager.class,
        ManagerWrapper.withPermissions(DefaultInteractionManager::new));
    registerClass(InteractionManagementDetailsManager.class, ManagerWrapper.withPermissions(
        DefaultInteractionManagementDetailsManager::new));
    registerClass(WellnetPrescriptionLinkManager.class,
        ManagerWrapper.withPermissions(DefaultWellnetPrescriptionLinkManager::new));
    registerClass(PatientHistoryRegularManager.class,
        ManagerWrapper.withDatasource(DefaultPatientHistoryRegularManager::new));
    registerClass(PatientHistoryUrlManager.class,
        ManagerWrapper.withDatasource(DefaultPatientHistoryUrlManager::new));
    registerClass(PatientHistoryTextManager.class,
        ManagerWrapper.withDatasource(DefaultPatientHistoryTextManager::new));
    registerClass(PatientHistoryTrackingManager.class,
        ManagerWrapper.withDatasource(DefaultPatientHistoryTrackingManager::new));
    registerClass(PatientHistoryRegularHistoryManager.class, ManagerWrapper.withDatasource(
        DefaultPatientHistoryRegularHistoryManager::new));
    registerClass(PatientHistoryUrlHistoryManager.class, ManagerWrapper.withDatasource(
        DefaultPatientHistoryUrlHistoryManager::new));
    registerClass(PatientHistoryTextHistoryManager.class, ManagerWrapper.withDatasource(
        DefaultPatientHistoryTextHistoryManager::new));
    registerClass(FolderTypeManager.class,
        ManagerWrapper.withDatasource(DefaultFolderTypeManager::new));
    registerClass(PatientDiagnosisHistoryManager.class,
        ManagerWrapper.withDatasource(DefaultPatientDiagnosisHistoryManager::new));
    registerClass(FolderManager.class,
        ManagerWrapper.withDatasource(DefaultFolderManager::new, DatabaseType.Documents));
    registerClass(ContactManager.class,
        ManagerWrapper.withDatasource(DefaultContactManager::new));
    registerClass(GeneratedLetterManager.class,
        ManagerWrapper.withPermissions(DefaultGeneratedLetterManager::new));
    registerClass(MedicationLogEntryManager.class,
        ManagerWrapper.withPermissions(DefaultMedicationLogEntryManager::new));
    registerClass(AccuroApiContextManager.class,
        ManagerWrapper.withDatasource(DefaultAccuroApiContextManager::new));
    registerClass(SpadeFolderManager.class,
        ManagerWrapper.withDualDb(DefaultSpadeFolderManager::new));
    registerClass(SpadeDocumentManager.class,
        ManagerWrapper.withDualDb(DefaultSpadeDocumentManager::new));
    registerClass(DocsDataManager.class,
        ManagerWrapper.withDualDb(DefaultDocsDataManager::new));
    registerClass(SelectionListManager.class,
        ManagerWrapper.withDatasource(DefaultSelectionListManager::new));
    registerClass(ReferralOrderManager.class,
        ManagerWrapper.withPermissions(DefaultReferralOrderManager::new));
    registerClass(UserInfoManager.class,
        ManagerWrapper.withDatasource(DefaultUserInfoManager::new));
    registerClass(AccuroUserManager.class,
        ManagerWrapper.withDatasource(DefaultAccuroUserManager::new));
    registerClass(RoleManager.class, ManagerWrapper.withDatasource(DefaultRoleManager::new));
    registerClass(WaitRoomEntryManager.class,
        ManagerWrapper.withDatasource(DefaultWaitRoomEntryManager::new));
    registerClass(DiagnosisLinkManager.class,
        ManagerWrapper.withDatasource(DefaultDiagnosisLinkManager::new));
    registerClass(WaitlistRequestManager.class,
        ManagerWrapper.withPermissions(DefaultWaitlistRequestManager::new));
    registerClass(ConsultPriorityManager.class,
        ManagerWrapper.withDatasource(DefaultConsultPriorityManager::new));
    registerClass(WaitlistProviderManager.class,
        ManagerWrapper.withDatasource(DefaultWaitlistProviderManager::new));
    registerClass(ConsultStatusManager.class,
        ManagerWrapper.withDatasource(DefaultConsultStatusManager::new));
    registerClass(ScheduleRoomManager.class,
        ManagerWrapper.withDatasource(DefaultScheduleRoomManager::new));
    registerClass(PrescriptionMacroManager.class,
        ManagerWrapper.withPermissions(DefaultPrescriptionMacroManager::new));
    registerClass(ProviderScheduleCalenderManager.class, ManagerWrapper.withPermissions(
        DefaultProviderScheduleCalendarManager::new));
    registerClass(LabLinkGroupManager.class,
        ManagerWrapper.withDatasource(DefaultLabLinkGroupManager::new));
    registerClass(PatientProfilePictureManager.class,
        ManagerWrapper.withDatasource(DefaultPatientProfilePictureManager::new));
    registerClass(CustomFieldManager.class,
        ManagerWrapper.withDatasource(DefaultCustomFieldManager::new));
    registerClass(ExternalUserIdentityManager.class,
        ManagerWrapper.withDatasource(DefaultExternalUserIdentityManager::new));
    registerClass(TaskManager.class, ManagerWrapper.withDatasource(DefaultTaskManager::new));
    registerClass(TaskReasonManager.class,
        ManagerWrapper.withDatasource(DefaultTaskReasonManager::new));
    registerClass(PatientStatusManager.class,
        ManagerWrapper.withDatasource(DefaultPatientStatusManager::new));
    registerClass(CodeSystemManager.class,
        ManagerWrapper.withDatasource(DefaultCodeSystemManager::new));
    registerClass(CodeSubSystemManager.class,
        ManagerWrapper.withDatasource(DefaultCodeSubSystemManager::new));
    registerClass(RelationshipStatusManager.class,
        ManagerWrapper.withPermissions(DefaultRelationshipStatusManager::new));
    registerClass(PersonTitleManager.class,
        ManagerWrapper.withDatasource(DefaultPersonTitleManager::new));
    registerClass(MasterNumberManager.class,
        ManagerWrapper.withDatasource(DefaultMasterNumberManager::new));
    registerClass(MaskingManager.class,
        ManagerWrapper.withPermissions(DefaultMaskingManager::new));
    registerClass(ConversationManager.class,
        ManagerWrapper.withDatasource(DefaultConversationManager::new));
    registerClass(ConversationMessageManager.class,
        ManagerWrapper.withDatasource(DefaultConversationMessageManager::new));
    registerClass(ConversationMessageStatusManager.class,
        ManagerWrapper.withDatasource(DefaultConversationMessageStatusManager::new));
    registerClass(FdbCcddMappingManager.class,
        ManagerWrapper.withDatasource(DefaultFdbCcddMappingManager::new));
    registerClass(RenewalRequestManager.class,
        ManagerWrapper.withPermissions(DefaultRenewalRequestManager::new));
    registerClass(RenewalRequestResponseManager.class,
        ManagerWrapper.withPermissions(DefaultRenewalRequestResponseManager::new));
    registerClass(ConversationUnmatchedPatientManager.class, ManagerWrapper.withDatasource(
        DefaultConversationUnmatchedPatientManager::new));
    registerClass(ConversationContactManager.class,
        ManagerWrapper.withPermissions(DefaultConversationContactManager::new));
    registerClass(ExternalContactIdentifierManager.class, ManagerWrapper.withDatasource(
        DefaultExternalContactIdentifierManager::new));
    registerClass(RenewalRequestGroupManager.class, ManagerWrapper.withPermissions(
        DefaultRenewalRequestGroupManager::new));
    registerClass(EprescribeJobManager.class, ManagerWrapper.withDatasource(
        DefaultEprescribeJobManager::new));
    registerClass(EprescribeCancelRequestManager.class, ManagerWrapper.withPermissions(
        DefaultEprescribeCancelRequestManager::new));
    registerClass(EprescribeCancelResponseManager.class, ManagerWrapper.withPermissions(
        DefaultEprescribeCancelResponseManager::new));
    registerClass(EprescribeJobTaskManager.class, ManagerWrapper.withDatasource(
        DefaultEprescribeJobTaskManager::new));
    registerClass(EprescribeJobHistoryManager.class, ManagerWrapper.withDatasource(
        DefaultEprescribeJobHistoryManager::new));
    registerClass(EprescribeJobPrescriptionMedicationManager.class, ManagerWrapper.withDatasource(
        DefaultEprescribeJobPrescriptionMedicationManager::new));
    registerClass(EprescribeJobTypeManager.class, ManagerWrapper.withDatasource(
        DefaultEprescribeJobTypeManager::new));
    registerClass(EprescribeOutcomeManager.class, ManagerWrapper.withPermissions(
        DefaultEprescribeOutcomeManager::new));
    registerClass(ConversationMessageAttachmentManager.class, ManagerWrapper.withDatasource(
        DefaultConversationMessageAttachmentManager::new));
    registerClass(ConversationMessageAttachmentContentManager.class, ManagerWrapper.withDatasource(
        DefaultConversationMessageAttachmentContentManager::new));
    registerClass(DispenseNotificationManager.class, ManagerWrapper.withPermissions(
        DefaultDispenseNotificationManager::new));
    registerClass(EprescribeOutcomeCodeManager.class, ManagerWrapper.withDatasource(
        DefaultEprescribeOutcomeCodeManager::new));
    registerClass(ConversationPatientManager.class, ManagerWrapper.withDatasource(
        DefaultConversationPatientManager::new));
    registerClass(EprescribeOrderStatusManager.class, ManagerWrapper.withPermissions(
        DefaultEprescribeOrderStatusManager::new));
    registerClass(PatientChartLockManager.class, ManagerWrapper.withPermissions(
        DefaultPatientChartLockManager::new));
    registerClass(ModuleManager.class, ManagerWrapper.withDatasource(
        DefaultModuleManager::new));
    registerClass(AuthorizedClientManager.class, ManagerWrapper.withPermissions(
        DefaultAuthorizedClientManager::new));
    registerClass(UserPermissionManager.class, ManagerWrapper.withDatasource(
        DefaultUserPermissionManager::new));
    registerClass(UserPreferencesManager.class, ManagerWrapper.withPermissions(
        DefaultUserPreferencesManager::new));
    registerClass(ConversationExternalPatientManager.class, ManagerWrapper.withPermissions(
        DefaultConversationExternalPatientManager::new));
    registerClass(ExternalPatientManager.class, ManagerWrapper.withPermissions(
        DefaultExternalPatientManager::new));
    registerClass(ProviderPermissionManager.class, ManagerWrapper.withPermissions(
        DefaultProviderPermissionManager::new));
    registerClass(PatientRepliformReportableManager.class, ManagerWrapper.withDatasource(
        DefaultPatientRepliformReportableManager::new));
    registerClass(MedeoProviderLinkManager.class, ManagerWrapper.withDatasource(
        DefaultMedeoProviderLinkManager::new));
    registerClass(MedeoOrganizationManager.class,
        ManagerWrapper.withDatasource(DefaultMedeoOrganizationManager::new));
    registerClass(PhysicianLabIdsManager.class,
        ManagerWrapper.withPermissions(DefaultPhysicianLabIdsManager::new));
  }


  private ManagerMapping() {
  }

  private static <T> void registerClass(Class<T> clazz,
      ManagerConstructor<? extends T> constructor) {
    constructorMap.put(clazz, constructor);
  }

  public static <T, U extends T> ManagerConstructor<U> getConstructor(Class<T> clazz) {
    return (ManagerConstructor<U>) constructorMap.get(clazz);
  }

}
