
package com.qhrtech.emr.restapi.util;

import java.util.HashMap;
import java.util.TreeMap;

public class EmrUnitsMapping {

  private HashMap<String, String> allergySeveritiesMap;
  private HashMap<String, String> allergyStatusesMap;
  private HashMap<String, String> allImmRoutesMap;

  private TreeMap<String, String> rxRoutesMap;
  private TreeMap<String, String> rxFreqsMap;
  private TreeMap<String, String> rxDurUnitsMap;
  private TreeMap<String, String> rxDosageUnitsMap;

  public HashMap<String, String> getAllergySeveritiesMap() {
    allergySeveritiesMap = new HashMap<String, String>();
    allergySeveritiesMap.put("4", "SV"); // High -> Severe -> SV
    allergySeveritiesMap.put("5", "MO"); // Medium -> Moderate -> MO
    allergySeveritiesMap.put("6", "MI"); // Low -> Mild -> MI
    allergySeveritiesMap.put("7", "UN"); // Unknown -> Unknown -> UN

    return allergySeveritiesMap;
  }

  public HashMap<String, String> getAllAlergyStatuses() {
    allergyStatusesMap = new HashMap<String, String>();
    allergyStatusesMap.put("1", "C"); // Active -> Confirmed or verified -> C
    allergyStatusesMap.put("2", "I"); // Inactive -> Confirmed but inactive -> I
    allergyStatusesMap.put("3", "I"); // Remission -> Confirmed but inactive -> I
    allergyStatusesMap.put("20", "C"); // Confirmed -> Confirmed or verified -> C
    allergyStatusesMap.put("21", "S"); // Suspected -> Suspect -> S
    allergyStatusesMap.put("22", "R"); // Refuted -> Refuted -> R

    return allergyStatusesMap;
  }

  public HashMap<String, String> getAllImmRoutesMap() {
    allImmRoutesMap = new HashMap<String, String>();
    allImmRoutesMap.put("92", "0-UNASSIGNED");
    allImmRoutesMap.put("77", "ARTHOGRAPHY");
    allImmRoutesMap.put("1", "BLOCK/INFILTRATION");
    allImmRoutesMap.put("3", "BUCCAL");
    allImmRoutesMap.put("4", "CAUDAL BLOCK");
    allImmRoutesMap.put("76", "CYSTOGRAPHY");
    allImmRoutesMap.put("5", "DENTAL");
    allImmRoutesMap.put("6", "DIALYSIS");
    allImmRoutesMap.put("2", "DISINFECTANT (BARN)");
    allImmRoutesMap.put("87", "DISINFECTANT (CONTACT LENS)");
    allImmRoutesMap.put("93", "DISINFECTANT (Domestic)");
    allImmRoutesMap.put("8", "DISINFECTANT (FOOD PREMISES)");
    allImmRoutesMap.put("9", "DISINFECTANT (HOSPITAL AREA)");
    allImmRoutesMap.put("94", "DISINFECTANT (INSTITUTIONAL)");
    allImmRoutesMap.put("30", "DISINFECTANT (MEDICAL INSTRUMENTS)");
    allImmRoutesMap.put("7", "EPIDURAL");
    allImmRoutesMap.put("90", "EXTRACORPOREAL");
    allImmRoutesMap.put("83", "IMPLANT");
    allImmRoutesMap.put("29", "INHALATION");
    allImmRoutesMap.put("39", "INSTILLATION");
    allImmRoutesMap.put("11", "INTRA-AMNIOTIC");
    allImmRoutesMap.put("12", "INTRA-ARTERIAL");
    allImmRoutesMap.put("10", "INTRA-ARTICULAR");
    allImmRoutesMap.put("25", "INTRABRACHIAL");
    allImmRoutesMap.put("13", "INTRABURSAL");
    allImmRoutesMap.put("14", "INTRACARDIAC");
    allImmRoutesMap.put("84", "INTRACAVERNOSAL");
    allImmRoutesMap.put("17", "INTRACAVITY");
    allImmRoutesMap.put("16", "INTRACRANIAL");
    allImmRoutesMap.put("18", "INTRACUTANEOUS");
    allImmRoutesMap.put("19", "INTRADERMAL");
    allImmRoutesMap.put("82", "INTRAGANGLIONAL");
    allImmRoutesMap.put("100", "INTRAINTESTINAL (UPPER)");
    allImmRoutesMap.put("21", "INTRALESIONAL");
    allImmRoutesMap.put("23", "INTRAMAMMARY");
    allImmRoutesMap.put("22", "INTRAMUSCULAR");
    allImmRoutesMap.put("32", "INTRAOCULAR");
    allImmRoutesMap.put("33", "INTRAPERITONEAL");
    allImmRoutesMap.put("34", "INTRAPLEURAL");
    allImmRoutesMap.put("35", "INTRAPULMONARY");
    allImmRoutesMap.put("37", "INTRARUMINAL");
    allImmRoutesMap.put("40", "INTRASPINAL");
    allImmRoutesMap.put("42", "INTRASYNOVIAL");
    allImmRoutesMap.put("88", "INTRATENDINOUS");
    allImmRoutesMap.put("31", "INTRATHECAL");
    allImmRoutesMap.put("45", "INTRATRACHEAL");
    allImmRoutesMap.put("48", "INTRAUTERINE");
    allImmRoutesMap.put("97", "INTRAVASCULAR");
    allImmRoutesMap.put("49", "INTRAVENOUS");
    allImmRoutesMap.put("96", "INTRAVESICAL");
    allImmRoutesMap.put("47", "INTRAVESICULAR");
    allImmRoutesMap.put("80", "INTRAVITREAL");
    allImmRoutesMap.put("36", "IRRIGATION");
    allImmRoutesMap.put("51", "LABORATORY TEST");
    allImmRoutesMap.put("52", "MISCELLANEOUS");
    allImmRoutesMap.put("53", "NASAL");
    allImmRoutesMap.put("54", "NIL");
    allImmRoutesMap.put("55", "OPHTHALMIC");
    allImmRoutesMap.put("56", "ORAL");
    allImmRoutesMap.put("57", "OTIC");
    allImmRoutesMap.put("59", "PARENTERAL (UNSPECIFIED)");
    allImmRoutesMap.put("85", "PERCUTANEOUS");
    allImmRoutesMap.put("62", "RECTAL");
    allImmRoutesMap.put("61", "REFER (SEE DOSAGE FORM)");
    allImmRoutesMap.put("60", "RETROBULBAR");
    allImmRoutesMap.put("63", "SOFT TISSUE INJECTION");
    allImmRoutesMap.put("64", "SUBARACHNOIDAL");
    allImmRoutesMap.put("65", "SUBCUTANEOUS");
    allImmRoutesMap.put("89", "SUBGINGIVAL");
    allImmRoutesMap.put("66", "SUBLINGUAL");
    allImmRoutesMap.put("109", "SUBMUCOSAL");
    allImmRoutesMap.put("67", "SURGICAL");
    allImmRoutesMap.put("69", "TEAT-DIP");
    allImmRoutesMap.put("70", "TOPICAL");
    allImmRoutesMap.put("105", "TOPICAL (COMMERCIAL)");
    allImmRoutesMap.put("107", "TOPICAL (HOSPITAL/HC FACILITIES)");
    allImmRoutesMap.put("104", "TOPICAL (HOUSEHOLD)");
    allImmRoutesMap.put("71", "TRANSDERMAL");
    allImmRoutesMap.put("73", "UDDER WASH");
    allImmRoutesMap.put("72", "URETHRAL");
    allImmRoutesMap.put("74", "VAGINAL");

    return allImmRoutesMap;
  }

  public TreeMap<String, String> getRxRoutesMap() {
    rxRoutesMap = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
    rxRoutesMap.put("INH", "Inhalation");
    rxRoutesMap.put("IM", "Intramuscular");
    rxRoutesMap.put("IV", "Intravenous");
    rxRoutesMap.put("NS", "Nasal");
    rxRoutesMap.put("NG", "Nasogastric");
    rxRoutesMap.put("PO", "Oral");
    rxRoutesMap.put("PR", "Rectal");
    rxRoutesMap.put("SQ", "Subcutaneous");
    rxRoutesMap.put("SL", "Sublingual");
    rxRoutesMap.put("TOP", "Topical");
    rxRoutesMap.put("VAG", "Vaginal");

    return rxRoutesMap;
  }

  public TreeMap<String, String> getAllRxFreqs() {
    rxFreqsMap = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
    rxFreqsMap.put("STAT", "stat");
    rxFreqsMap.put("ONCE", "Once");
    rxFreqsMap.put("QD", "Once daily");
    rxFreqsMap.put("BID", "Two times daily");
    rxFreqsMap.put("TID", "Three times daily");
    rxFreqsMap.put("QID", "Four times daily");
    rxFreqsMap.put("5x/day", "5 x per day");
    rxFreqsMap.put("Q2D", "Every other day");
    rxFreqsMap.put("Q3D", "Every three days");
    rxFreqsMap.put("QHS", "Every hour");
    rxFreqsMap.put("Q1H", "Every hour");
    rxFreqsMap.put("Q2H", "Every 2 hour");
    rxFreqsMap.put("Q3H", "Every 4 hour");
    rxFreqsMap.put("Q4H", "Every 4 hour");
    rxFreqsMap.put("Q4-6H", "Every 4 to 6 hour");
    rxFreqsMap.put("Q6H", "Every 6 hour");
    rxFreqsMap.put("Q8H", "Every 8 hour");
    rxFreqsMap.put("Q12H", "Every 12 hour");
    rxFreqsMap.put("2x/week", "2 times/week");
    rxFreqsMap.put("3x/week", "3 times/week");
    rxFreqsMap.put("4x/week", "4 times/week");
    rxFreqsMap.put("5x/week", "5 times/week");
    rxFreqsMap.put("6x/week", "6 times/week");
    rxFreqsMap.put("1x/week", "Qnce a week");
    rxFreqsMap.put("Q2W", "Once every two weeks");
    rxFreqsMap.put("Q6W", "Once every 6 weeks");
    rxFreqsMap.put("Q8W", "Once every 8 weeks");
    rxFreqsMap.put("Q12W", "Once every 12 weeks");
    rxFreqsMap.put("1x/month", "Once a month");
    rxFreqsMap.put("1x/6month", "Once every 6 months");

    return rxFreqsMap;
  }

  public TreeMap<String, String> getAllDurUnits() {
    rxDurUnitsMap = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
    rxDurUnitsMap.put("Day", "Day(s)");
    rxDurUnitsMap.put("Year", "Year(s)");
    rxDurUnitsMap.put("Week", "Week(s)");
    rxDurUnitsMap.put("MO28", "Mth28");
    rxDurUnitsMap.put("MO30", "Mth30");

    return rxDurUnitsMap;
  }

  public TreeMap<String, String> getAllRxDosageUnits() {
    rxDosageUnitsMap = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
    rxDosageUnitsMap.put("AMP", "Amp(s)");
    rxDosageUnitsMap.put("APPLN", "application(s)");
    rxDosageUnitsMap.put("CAP", "Capsule(s)");
    rxDosageUnitsMap.put("DROPS", "Drop(s)");
    rxDosageUnitsMap.put("DOSE", "Dose(s)");
    rxDosageUnitsMap.put("g", "gram(s)");
    rxDosageUnitsMap.put("INHLN", "Inhalation(s)");
    rxDosageUnitsMap.put("mcg", "mcg(s)");
    rxDosageUnitsMap.put("mg", "mg(s)");
    rxDosageUnitsMap.put("mL", "mL(s)");
    rxDosageUnitsMap.put("NEB", "Neb(s)");
    rxDosageUnitsMap.put("oz", "oz");
    rxDosageUnitsMap.put("PATCH", "patch");
    rxDosageUnitsMap.put("PUFF", "puff(s)");
    rxDosageUnitsMap.put("SPRAY", "spray(s)");
    rxDosageUnitsMap.put("SUP", "Suppository");
    rxDosageUnitsMap.put("TAB", "Tab(s)");
    rxDosageUnitsMap.put("TBSP", "tbs(s)");
    rxDosageUnitsMap.put("tsp", "tsp(s)");
    rxDosageUnitsMap.put("TUB", "tube(s)");
    rxDosageUnitsMap.put("U", "unit(s)");
    rxDosageUnitsMap.put("VIAL", "vial(s)");

    return rxDosageUnitsMap;
  }
}
