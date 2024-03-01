
package com.qhrtech.emr.restapi.endpoints.utilities;

import com.qhrtech.emr.accuro.db.util.PreparedStatementBuffer;
import com.qhrtech.emr.accuro.model.prescription.PrescriptionMedication;
import com.qhrtech.emr.restapi.endpoints.AbstractEndpoint;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CleanPrescriptionRecords extends AbstractEndpointIntegrationTest {

  public CleanPrescriptionRecords(AbstractEndpoint endpoint,
      Class endpointType) throws IOException {
    super(endpoint, endpointType);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    return new HashMap<>();
  }


  public void cleanMedicationRecord(PrescriptionMedication medication)
      throws SQLException {

    deletePrescription(medication.getPrescriptionId());
    deletePatientOfficeProvider(medication.getPatientId());
    deletePatient(medication.getPatientId());
    deleteUser(medication.getUserId());

  }

  private void deletePrescription(int prescriptionId) throws SQLException {
    PreparedStatementBuffer query = new PreparedStatementBuffer();
    query.append("DELETE FROM prescription_medication WHERE prescription_id = ? ",
        prescriptionId);
    executeLocalQuery(query);
  }

  private void deletePatient(int patientId) throws SQLException {
    PreparedStatementBuffer query = new PreparedStatementBuffer();
    query.append("DELETE FROM patient WHERE patient_id = ? ", patientId);
    executeLocalQuery(query);
  }

  private void deletePatientOfficeProvider(int patientId) throws SQLException {
    PreparedStatementBuffer query = new PreparedStatementBuffer();
    query.append("DELETE FROM patient_office_provider_history WHERE patient_id = ? ", patientId);
    executeLocalQuery(query);
  }

  private void deleteUser(int userId) throws SQLException {
    PreparedStatementBuffer query = new PreparedStatementBuffer();
    query.append("DELETE FROM security WHERE user_id = ? ", userId);
    executeLocalQuery(query);
  }
}
