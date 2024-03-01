
package com.qhrtech.emr.restapi.services.impl;

import com.qhrtech.emr.accuro.api.medications.AlternativeHealthProductManager;
import com.qhrtech.emr.accuro.api.medications.FormulationManager;
import com.qhrtech.emr.accuro.api.medications.GenericDrugManager;
import com.qhrtech.emr.accuro.api.medications.ManufacturedDrugManager;
import com.qhrtech.emr.accuro.api.medications.NaturalHealthProductManager;
import com.qhrtech.emr.accuro.api.medications.fdb.FdbCcddMappingManager;
import com.qhrtech.emr.accuro.api.prescription.IngredientManager;
import com.qhrtech.emr.accuro.api.security.UserSecurity;
import com.qhrtech.emr.accuro.model.exceptions.ProtossException;
import com.qhrtech.emr.accuro.model.medications.AlternativeHealthProduct;
import com.qhrtech.emr.accuro.model.medications.Formulation;
import com.qhrtech.emr.accuro.model.medications.GenericDrug;
import com.qhrtech.emr.accuro.model.medications.ManufacturedDrug;
import com.qhrtech.emr.accuro.model.medications.MedicationType;
import com.qhrtech.emr.accuro.model.medications.NaturalHealthProduct;
import com.qhrtech.emr.accuro.model.medications.fdb.ExternalVocabularyType;
import com.qhrtech.emr.accuro.model.medications.fdb.FdbCcddMapping;
import com.qhrtech.emr.accuro.model.medications.fdb.FdbVocabularyType;
import com.qhrtech.emr.accuro.model.prescription.Ingredient;
import com.qhrtech.emr.accuro.model.prescription.PrescriptionMedication;
import com.qhrtech.emr.accuro.permissions.AccessLevel;
import com.qhrtech.emr.accuro.permissions.AccessType;
import com.qhrtech.emr.accuro.permissions.roles.RoleSection;
import com.qhrtech.emr.accuro.permissions.roles.RoleSectionAccess;
import com.qhrtech.emr.restapi.models.dto.medications.AlternativeHealthProductDto;
import com.qhrtech.emr.restapi.models.dto.medications.FormulationDto;
import com.qhrtech.emr.restapi.models.dto.medications.GenericDrugDto;
import com.qhrtech.emr.restapi.models.dto.medications.ManufacturedDrugDto;
import com.qhrtech.emr.restapi.models.dto.medications.NaturalHealthProductDto;
import com.qhrtech.emr.restapi.models.dto.medications.compounds.CustomCompoundDto;
import com.qhrtech.emr.restapi.models.dto.medications.compounds.IngredientDto;
import com.qhrtech.emr.restapi.models.dto.medications.fdb.FdbCcddMappingDto;
import com.qhrtech.emr.restapi.models.dto.prescriptions.PrescriptionDetailsDto;
import com.qhrtech.emr.restapi.security.exceptions.PrescriptionConversionException;
import com.qhrtech.emr.restapi.services.AccuroApiService;
import com.qhrtech.emr.restapi.services.PrescriptionDetailsService;
import com.qhrtech.emr.restapi.services.SecurityContextService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implement of {@link PrescriptionDetailsService} to set up the prescription details according to
 * {@link PrescriptionMedication}.
 */
@Service
public class DefaultPrescriptionDetailsService implements PrescriptionDetailsService {

  @Autowired
  private SecurityContextService securityContextService;
  @Autowired
  private Mapper mapper;
  @Autowired
  private AccuroApiService api;

  @Override
  public PrescriptionDetailsDto getPrescriptionDetails(PrescriptionMedication p,
      boolean includeCcddMapping)
      throws ProtossException {

    PrescriptionDetailsDto prescriptionDetails = new PrescriptionDetailsDto();
    int rxId = p.getPrescriptionId();
    String din = p.getDin();
    int medicationId = p.getMedicationId();
    MedicationType medicationType = p.getDinSystem();

    if (medicationType == null) {
      throw new PrescriptionConversionException(
          "Cannot convert prescription with no medication type.");
    }

    switch (medicationType) {
      case DIN:
        populateManufacturedDrugDetails(prescriptionDetails, din);
        break;
      case GD:
        populateGenericDrugDetails(prescriptionDetails, medicationId);
        break;
      case NPN:
        populateNaturalHealthProductDetails(prescriptionDetails, din);
        break;
      case AHP:
        populateAlternativeHealthProductDetails(prescriptionDetails, medicationId);
        break;
      case GF:
        populateFormulationDetails(prescriptionDetails, din, includeCcddMapping);
        break;
      case COMPOUND:
        populateCompoundDetails(prescriptionDetails, p.getCompoundName(), p.getCompoundDetails(),
            rxId);
        break;
      default:
        throw new PrescriptionConversionException("No valid medication type: " + medicationType);
    }
    return prescriptionDetails;
  }

  /**
   * Creates new UserSecurity object with roles and permission required for medical summary
   * endpoint.
   */
  @Override
  public UserSecurity getPermissionsForMedicalSummary() {

    UserSecurity userSecurity = new UserSecurity.SecurityBuilder()
        .withRole(RoleSection.EMR_MEDS, RoleSectionAccess.READ_ONLY)
        .withProviderPermission(AccessType.EMR, AccessLevel.ReadOnly).build();

    return userSecurity;
  }

  private void populateManufacturedDrugDetails(PrescriptionDetailsDto rx, String din)
      throws ProtossException {
    ManufacturedDrugManager manufacturedDrugManager = getImpl(ManufacturedDrugManager.class);
    ManufacturedDrug manufacturedDrug = manufacturedDrugManager.getManufacturedDrug(din);
    if (manufacturedDrug != null) {
      ManufacturedDrugDto manufacturedDrugDto = mapDto(manufacturedDrug, ManufacturedDrugDto.class);
      rx.setManufacturedDrug(manufacturedDrugDto);
    }
  }

  private void populateGenericDrugDetails(PrescriptionDetailsDto rx, int medicationId)
      throws ProtossException {
    GenericDrugManager genericDrugManager = getImpl(GenericDrugManager.class);
    GenericDrug genericDrug = genericDrugManager.getGenericDrug(medicationId);
    if (genericDrug != null) {
      GenericDrugDto genericDrugDto = mapDto(genericDrug, GenericDrugDto.class);
      rx.setGenericDrug(genericDrugDto);
    }
  }

  private void populateNaturalHealthProductDetails(PrescriptionDetailsDto rx, String din)
      throws ProtossException {
    NaturalHealthProductManager nhpManager = getImpl(NaturalHealthProductManager.class);
    NaturalHealthProduct nhp = nhpManager.getNaturalHealthProduct(din);
    if (nhp != null) {
      NaturalHealthProductDto naturalHealthProductDto = mapDto(nhp, NaturalHealthProductDto.class);
      rx.setNaturalHealthProduct(naturalHealthProductDto);
    }
  }

  private void populateAlternativeHealthProductDetails(PrescriptionDetailsDto rx, int medicationId)
      throws ProtossException {
    AlternativeHealthProductManager ahpManager = getImpl(AlternativeHealthProductManager.class);
    AlternativeHealthProduct ahp = ahpManager.getAlternativeHealthProduct(medicationId);
    if (ahp != null) {
      AlternativeHealthProductDto ahpDto = mapDto(ahp, AlternativeHealthProductDto.class);
      rx.setAlternativeHealthProduct(ahpDto);
    }
  }

  private void populateFormulationDetails(PrescriptionDetailsDto rx, String din,
      boolean includeCcddMapping)
      throws ProtossException {

    // formulation manager only accepts the din as an integer as opposed to a string
    int formulationId;
    try {
      formulationId = Integer.parseInt(din);
    } catch (NumberFormatException e) {
      throw new PrescriptionConversionException("Formulation id (DIN) must be number: " + din, e);
    }

    FormulationManager formulationManager = getImpl(FormulationManager.class);
    Formulation formulation = formulationManager.getFormulation(formulationId);
    if (formulation != null) {
      FormulationDto formulationDto = mapDto(formulation, FormulationDto.class);

      if (includeCcddMapping) {
        FdbCcddMappingManager fdbCcddMappingManager = getImpl(FdbCcddMappingManager.class);
        List<FdbCcddMapping> fdbCcddMappings = new ArrayList<>(fdbCcddMappingManager
            .getFdbCcddMappings(FdbVocabularyType.GenericCodeNumber,
                din,
                ExternalVocabularyType.NonProprietaryTherapeuticProduct));
        fdbCcddMappings.sort(Comparator.comparing(FdbCcddMapping::getFdbVocabId));
        formulationDto
            .setFdbCcddMappings(mapDto(fdbCcddMappings, FdbCcddMappingDto.class, ArrayList::new));
      }

      rx.setFormulation(formulationDto);
    }
  }

  private void populateCompoundDetails(PrescriptionDetailsDto rx, String compoundName,
      String compoundDetails, int rxId) throws ProtossException {
    CustomCompoundDto compoundDto = new CustomCompoundDto();
    compoundDto.setName(compoundName);
    compoundDto.setDescription(compoundDetails);

    // set ingredients on a custom compound
    IngredientManager ingredientManager = getImpl(IngredientManager.class);
    Set<Ingredient> rxIngredients = ingredientManager.get(rxId);

    // rx stores ingredients in two categories, included and excluded
    Map<Boolean, Set<Ingredient>> ingredients = rxIngredients.stream()
        .collect(Collectors.groupingBy(Ingredient::isDrugIncluded, Collectors.toSet()));

    Set<IngredientDto> included =
        mapDto(ingredients.getOrDefault(true, new HashSet<>()), IngredientDto.class, HashSet::new);
    Set<IngredientDto> excluded =
        mapDto(ingredients.getOrDefault(false, new HashSet<>()), IngredientDto.class, HashSet::new);

    compoundDto.setIncludedIngredients(included);
    compoundDto.setExcludedIngredients(excluded);

    rx.setCustomCompound(compoundDto);
  }

  private <T> T getImpl(Class<T> interfaceClass) {
    String tenantId = securityContextService.getSecurityContext().getTenantId();
    return api.getImpl(interfaceClass, tenantId);
  }

  protected <S, D, C extends Collection<D>> C mapDto(
      Collection<S> source,
      Class<D> destinationType,
      Supplier<C> collectionFactory) {
    return source
        .stream()
        .map(s -> mapDto(s, destinationType))
        .collect(Collectors.toCollection(collectionFactory));
  }

  private <S, D> D mapDto(S s, Class<D> clazz) {
    return mapper.map(s, clazz);
  }

}
