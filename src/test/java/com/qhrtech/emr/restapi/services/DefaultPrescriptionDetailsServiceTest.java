
package com.qhrtech.emr.restapi.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.accuro.api.medications.AlternativeHealthProductManager;
import com.qhrtech.emr.accuro.api.medications.FormulationManager;
import com.qhrtech.emr.accuro.api.medications.GenericDrugManager;
import com.qhrtech.emr.accuro.api.medications.ManufacturedDrugManager;
import com.qhrtech.emr.accuro.api.medications.NaturalHealthProductManager;
import com.qhrtech.emr.accuro.api.medications.fdb.FdbCcddMappingManager;
import com.qhrtech.emr.accuro.api.prescription.IngredientManager;
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
import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.models.dto.medications.AlternativeHealthProductDto;
import com.qhrtech.emr.restapi.models.dto.medications.FormulationDto;
import com.qhrtech.emr.restapi.models.dto.medications.GenericDrugDto;
import com.qhrtech.emr.restapi.models.dto.medications.ManufacturedDrugDto;
import com.qhrtech.emr.restapi.models.dto.medications.NaturalHealthProductDto;
import com.qhrtech.emr.restapi.models.dto.medications.compounds.CustomCompoundDto;
import com.qhrtech.emr.restapi.models.dto.medications.compounds.IngredientDto;
import com.qhrtech.emr.restapi.models.dto.medications.fdb.FdbCcddMappingDto;
import com.qhrtech.emr.restapi.models.dto.prescriptions.PrescriptionDetailsDto;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import com.qhrtech.emr.restapi.security.exceptions.PrescriptionConversionException;
import com.qhrtech.emr.restapi.services.impl.DefaultPrescriptionDetailsService;
import com.qhrtech.emr.restapi.utilities.AbstractTest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;

public class DefaultPrescriptionDetailsServiceTest
    extends AbstractTest<DefaultPrescriptionDetailsService> {

  private final ManufacturedDrugManager manufacturedDrugManager;
  private final GenericDrugManager genericDrugManager;
  private final NaturalHealthProductManager naturalHealthProductManager;
  private final AlternativeHealthProductManager alternativeHealthProductManager;
  private final FormulationManager formulationManager;
  private final IngredientManager ingredientManager;
  private final FdbCcddMappingManager fdbCcddMappingManager;

  public DefaultPrescriptionDetailsServiceTest() {
    super(new DefaultPrescriptionDetailsService());
    manufacturedDrugManager = mock(ManufacturedDrugManager.class);
    genericDrugManager = mock(GenericDrugManager.class);
    naturalHealthProductManager = mock(NaturalHealthProductManager.class);
    alternativeHealthProductManager = mock(AlternativeHealthProductManager.class);
    formulationManager = mock(FormulationManager.class);
    ingredientManager = mock(IngredientManager.class);
    fdbCcddMappingManager = mock(FdbCcddMappingManager.class);
  }

  @Override
  protected ApiSecurityContext getSecurityContext() {
    ApiSecurityContext context = new ApiSecurityContext();
    context.setTenantId(TestUtilities.nextString(5));
    return context;
  }

  @Override
  protected Map<Class, Object> getContainerServices() {
    Map<Class, Object> servicesMap = new HashMap<>();
    servicesMap.put(ManufacturedDrugManager.class, manufacturedDrugManager);
    servicesMap.put(GenericDrugManager.class, genericDrugManager);
    servicesMap.put(NaturalHealthProductManager.class, naturalHealthProductManager);
    servicesMap.put(AlternativeHealthProductManager.class, alternativeHealthProductManager);
    servicesMap.put(FormulationManager.class, formulationManager);
    servicesMap.put(IngredientManager.class, ingredientManager);
    servicesMap.put(FdbCcddMappingManager.class, fdbCcddMappingManager);
    return servicesMap;
  }

  @Test
  public void getManufacturedDrug() throws ProtossException {
    // setup random data
    PrescriptionMedication prescription = getFixture(PrescriptionMedication.class);
    prescription.setDinSystem(MedicationType.DIN);

    ManufacturedDrug manufacturedDrug = getFixture(ManufacturedDrug.class);

    // mock dependencies
    when(manufacturedDrugManager.getManufacturedDrug(prescription.getDin()))
        .thenReturn(manufacturedDrug);

    ManufacturedDrugDto expected = mapDto(manufacturedDrug, ManufacturedDrugDto.class);

    // test
    PrescriptionDetailsDto details = testObject.getPrescriptionDetails(prescription, false);
    ManufacturedDrugDto actual = details.getManufacturedDrug();

    // assertions
    Assert.assertEquals(expected, actual);
    verify(manufacturedDrugManager).getManufacturedDrug(prescription.getDin());
  }

  @Test
  public void getGenericDrug() throws ProtossException {
    // setup random data
    PrescriptionMedication prescription = getFixture(PrescriptionMedication.class);
    prescription.setDinSystem(MedicationType.GD);

    GenericDrug genericDrug = getFixture(GenericDrug.class);

    // mock dependencies
    when(genericDrugManager.getGenericDrug(prescription.getMedicationId()))
        .thenReturn(genericDrug);

    GenericDrugDto expected = mapDto(genericDrug, GenericDrugDto.class);

    // test
    PrescriptionDetailsDto details = testObject.getPrescriptionDetails(prescription, false);
    GenericDrugDto actual = details.getGenericDrug();

    // assertions
    Assert.assertEquals(expected, actual);
    verify(genericDrugManager).getGenericDrug(prescription.getMedicationId());
  }

  @Test
  public void getNaturalHealthProduct() throws ProtossException {
    // setup random data
    PrescriptionMedication prescription = getFixture(PrescriptionMedication.class);
    prescription.setDinSystem(MedicationType.NPN);

    NaturalHealthProduct naturalHealthProduct = getFixture(NaturalHealthProduct.class);

    // mock dependencies
    when(naturalHealthProductManager.getNaturalHealthProduct(prescription.getDin()))
        .thenReturn(naturalHealthProduct);

    NaturalHealthProductDto expected = mapDto(naturalHealthProduct, NaturalHealthProductDto.class);

    // test
    PrescriptionDetailsDto details = testObject.getPrescriptionDetails(prescription, false);
    NaturalHealthProductDto actual = details.getNaturalHealthProduct();

    // assertions
    Assert.assertEquals(expected, actual);
    verify(naturalHealthProductManager).getNaturalHealthProduct(prescription.getDin());
  }

  @Test
  public void getAlternativeHealthProduct() throws ProtossException {
    // setup random data
    PrescriptionMedication prescription = getFixture(PrescriptionMedication.class);
    prescription.setDinSystem(MedicationType.AHP);

    AlternativeHealthProduct alternativeHealthProduct = getFixture(AlternativeHealthProduct.class);

    // mock dependencies
    when(
        alternativeHealthProductManager.getAlternativeHealthProduct(prescription.getMedicationId()))
            .thenReturn(alternativeHealthProduct);

    AlternativeHealthProductDto expected =
        mapDto(alternativeHealthProduct, AlternativeHealthProductDto.class);

    // test
    PrescriptionDetailsDto details = testObject.getPrescriptionDetails(prescription, false);
    AlternativeHealthProductDto actual = details.getAlternativeHealthProduct();

    // assertions
    Assert.assertEquals(expected, actual);
    verify(alternativeHealthProductManager)
        .getAlternativeHealthProduct(prescription.getMedicationId());
  }

  @Test
  public void getFormulation() throws ProtossException {
    // setup random data
    PrescriptionMedication prescription = getFixture(PrescriptionMedication.class);
    prescription.setDinSystem(MedicationType.GF);
    int formulationId = TestUtilities.nextId();
    String din = Integer.toString(formulationId);
    prescription.setDin(din);

    Formulation formulation = getFixture(Formulation.class);

    // mock dependencies
    when(formulationManager.getFormulation(formulationId))
        .thenReturn(formulation);

    Set<FdbCcddMapping> fdbCcddMappingSet = getFixtures(FdbCcddMapping.class, HashSet::new, 5);
    when(fdbCcddMappingManager.getFdbCcddMappings(FdbVocabularyType.GenericCodeNumber,
        din,
        ExternalVocabularyType.NonProprietaryTherapeuticProduct)).thenReturn(fdbCcddMappingSet);

    List<FdbCcddMapping> fdbCcddMappingList = new ArrayList<>(fdbCcddMappingSet);
    fdbCcddMappingList.sort(Comparator.comparing(FdbCcddMapping::getFdbVocabId));

    FormulationDto expected = mapDto(formulation, FormulationDto.class);
    expected
        .setFdbCcddMappings(mapDto(fdbCcddMappingList, FdbCcddMappingDto.class, ArrayList::new));

    // test
    PrescriptionDetailsDto details = testObject.getPrescriptionDetails(prescription, true);
    FormulationDto actual = details.getFormulation();

    // assertions
    Assert.assertEquals(expected, actual);
    verify(formulationManager).getFormulation(formulationId);
    verify(fdbCcddMappingManager).getFdbCcddMappings(FdbVocabularyType.GenericCodeNumber,
        din,
        ExternalVocabularyType.NonProprietaryTherapeuticProduct);
  }

  @Test
  public void getFormulationWithoutCcddMapping() throws ProtossException {
    // setup random data
    PrescriptionMedication prescription = getFixture(PrescriptionMedication.class);
    prescription.setDinSystem(MedicationType.GF);
    int formulationId = TestUtilities.nextId();
    String din = Integer.toString(formulationId);
    prescription.setDin(din);

    Formulation formulation = getFixture(Formulation.class);
    formulation.setFdbCcddMappings(null);
    // mock dependencies
    when(formulationManager.getFormulation(formulationId))
        .thenReturn(formulation);

    FormulationDto expected = mapDto(formulation, FormulationDto.class);

    // test
    PrescriptionDetailsDto details = testObject.getPrescriptionDetails(prescription, false);
    FormulationDto actual = details.getFormulation();

    // assertions
    Assert.assertEquals(expected, actual);
    verify(formulationManager).getFormulation(formulationId);
    verify(fdbCcddMappingManager, never()).getFdbCcddMappings(FdbVocabularyType.GenericCodeNumber,
        din,
        ExternalVocabularyType.NonProprietaryTherapeuticProduct);
  }

  @Test(expected = PrescriptionConversionException.class)
  public void getFormulationWithNonDigitDin() throws ProtossException {
    // setup random data
    PrescriptionMedication prescription = getFixture(PrescriptionMedication.class);
    prescription.setDinSystem(MedicationType.GF);

    // test
    testObject.getPrescriptionDetails(prescription, true);
  }

  @Test
  public void getCustomCompound() throws ProtossException {
    // setup random data
    PrescriptionMedication prescription = getFixture(PrescriptionMedication.class);
    prescription.setDinSystem(MedicationType.COMPOUND);

    CustomCompoundDto expected = new CustomCompoundDto();
    expected.setName(prescription.getCompoundName());
    expected.setDescription(prescription.getCompoundDetails());
    Set<Ingredient> ingredients = getFixtures(Ingredient.class, HashSet::new, 5);
    ingredients.forEach(i -> i.setDrugIncluded(TestUtilities.nextBoolean()));
    Set<IngredientDto> includedIngredients = ingredients
        .stream()
        .filter(Ingredient::isDrugIncluded)
        .map(i -> mapDto(i, IngredientDto.class))
        .collect(Collectors.toCollection(HashSet::new));
    Set<IngredientDto> excludedIngredients = ingredients
        .stream()
        .filter(i -> !i.isDrugIncluded())
        .map(i -> mapDto(i, IngredientDto.class))
        .collect(Collectors.toCollection(HashSet::new));
    expected.setIncludedIngredients(includedIngredients);
    expected.setExcludedIngredients(excludedIngredients);

    // mock dependencies
    when(ingredientManager.get(prescription.getPrescriptionId()))
        .thenReturn(ingredients);

    // test
    PrescriptionDetailsDto details = testObject.getPrescriptionDetails(prescription, false);
    CustomCompoundDto actual = details.getCustomCompound();

    // assertions
    Assert.assertEquals(expected, actual);
    verify(ingredientManager).get(prescription.getPrescriptionId());
  }

  @Test
  public void getCustomCompoundAllIncluded() throws Exception {
    // setup random data
    PrescriptionMedication prescription = getFixture(PrescriptionMedication.class);
    prescription.setDinSystem(MedicationType.COMPOUND);

    CustomCompoundDto expected = new CustomCompoundDto();
    expected.setName(prescription.getCompoundName());
    expected.setDescription(prescription.getCompoundDetails());
    Set<Ingredient> ingredients = getFixtures(Ingredient.class, HashSet::new, 5);
    ingredients.forEach(i -> i.setDrugIncluded(true));
    Set<IngredientDto> includedIngredients = ingredients
        .stream()
        .filter(Ingredient::isDrugIncluded)
        .map(i -> mapDto(i, IngredientDto.class))
        .collect(Collectors.toCollection(HashSet::new));
    Set<IngredientDto> excludedIngredients = ingredients
        .stream()
        .filter(i -> !i.isDrugIncluded())
        .map(i -> mapDto(i, IngredientDto.class))
        .collect(Collectors.toCollection(HashSet::new));
    expected.setIncludedIngredients(includedIngredients);
    expected.setExcludedIngredients(excludedIngredients);

    // mock dependencies
    when(ingredientManager.get(prescription.getPrescriptionId()))
        .thenReturn(ingredients);

    // test
    PrescriptionDetailsDto details = testObject.getPrescriptionDetails(prescription, false);
    CustomCompoundDto actual = details.getCustomCompound();

    // assertions
    Assert.assertEquals(expected, actual);
    verify(ingredientManager).get(prescription.getPrescriptionId());
  }

  @Test
  public void getCustomCompoundAllExcluded() throws Exception {
    // setup random data
    PrescriptionMedication prescription = getFixture(PrescriptionMedication.class);
    prescription.setDinSystem(MedicationType.COMPOUND);

    CustomCompoundDto expected = new CustomCompoundDto();
    expected.setName(prescription.getCompoundName());
    expected.setDescription(prescription.getCompoundDetails());
    Set<Ingredient> ingredients = getFixtures(Ingredient.class, HashSet::new, 5);
    ingredients.forEach(i -> i.setDrugIncluded(false));
    Set<IngredientDto> includedIngredients = ingredients
        .stream()
        .filter(Ingredient::isDrugIncluded)
        .map(i -> mapDto(i, IngredientDto.class))
        .collect(Collectors.toCollection(HashSet::new));
    Set<IngredientDto> excludedIngredients = ingredients
        .stream()
        .filter(i -> !i.isDrugIncluded())
        .map(i -> mapDto(i, IngredientDto.class))
        .collect(Collectors.toCollection(HashSet::new));
    expected.setIncludedIngredients(includedIngredients);
    expected.setExcludedIngredients(excludedIngredients);

    // mock dependencies
    when(ingredientManager.get(prescription.getPrescriptionId()))
        .thenReturn(ingredients);

    // test
    PrescriptionDetailsDto details = testObject.getPrescriptionDetails(prescription, false);
    CustomCompoundDto actual = details.getCustomCompound();

    // assertions
    Assert.assertEquals(expected, actual);
    verify(ingredientManager).get(prescription.getPrescriptionId());
  }

  @Test(expected = PrescriptionConversionException.class)
  public void getPrescriptionDetailsWithWrongMedicationType() throws ProtossException {
    // setup random data
    PrescriptionMedication prescription = getFixture(PrescriptionMedication.class);
    prescription.setDinSystem(null);

    // test
    testObject.getPrescriptionDetails(prescription, false);
  }
}
