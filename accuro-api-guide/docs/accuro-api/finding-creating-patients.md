---
sidebar_position: 7
---

# Finding and Creating Patients

A typical workflow is for applications to search for a patient and if no patient exists, create that patient. Accuro API provides patient search and creation, enabling patient check-ins, referrals, updating existing patient information and other use cases.

## Searching a patient

You can search for a patient using various filters such as health card number, first name, last name, etc. using the search endpoint (see details).

**Collection: PatientV2 Endpoints**
<details>
    <summary><code>GET</code> <code><b>/v2/provider-portal/patients/search</b></code></summary>

##### Headers

> | name                        |  type     | data type                                    | description                      |
> |-----------------------------|-----------|----------------------------------------------|----------------------------------|
> | Content-Type                |  required | application/x-www-form-urlencoded            | none.                            |
> | Authorization               |  required | bearer eyJraWQiOiJyd...                      | none.                            |
> | X-QHR-Subscription-Key      |  required | 23456abcdfg456789abcds                       | none.                            |


##### Parameters

> | name         |  type      | data type      | description                                          |
> |--------------|------------|----------------|------------------------------------------------------|
> | `uuid`       |  required  | string         | none.                                                |
> | `phn `       |  optional  | string         | none.                                                |
> | `firstName`  |  optional  | string         | none.                                                |
> | `lastName`   |  optional  | string         | none.                                                |
> | `phone`      |  optional  | string         | none.                                                |
> | `fileNumber` |  optional  | string         | none.                                                |
> | `patientUuid`|  optional  | string         | none.                                                |
> | `pageSize`   |  optional  | string         | The size of the pages with default value 25 and maximum value 50. If the page size is not provided or less than 1, the page size will be set to default value 25. If page size provided is more than maximum value, the page size will be set to the default maximum value 50.                |
> | `startingId` |  optional  | string         | This id is EnvelopeDto.lastId of the previous page(request)It is same as the patientId of the last records of the previous results.                |

##### Responses

> | http code     | content-type                      | response                                                            |
> |---------------|-----------------------------------|---------------------------------------------------------------------|
> | `200`         | `application/json`                | `[{...patient...},{...patient2...}];`                               |
> | `400`         | `application/json`                | `{"code":"400","message":"Bad Request"}`                            |
> | `401`         | `application/json`                | `{"code":"401","message":"Unauthorized"}`                           |

##### Example cURL
> ```javascript
>  curl --location --globoff '{{baseurl}}/rest/v2/provider-portal/patients/search?uuid={{uuid}}&phn={{phn}}&firstName={{firstname}}&lastName={{lastName}}&phone={{phone}}&fileNumber={{fileNumber}}&patientUuid={{patientUuid}}&startingId={{startingId}}&pageSize={{pageSize}}&tenant={{tenant}}&patientId={{patientId}}' --header 'Authorization: bearer {{auth_provider}}' --header 'Accept: application/json' --header 'Content-Type: application/json' --header 'X-QHR-Subscription-Key: {{Subscription-Key}}'
> ```
</details>

```js title="Response"
[
  {
    "enrolledProvideTerminationReason": "PATIENT_DECEASED",
    "enrolledProviderTerminationReason": "PATIENT_DECEASED",
    "patientId": 123456,
    "demographics": {
      "firstName": "John",
      "lastName": "Doe",
      "middleName": "Smith",
      "title": "Mr.",
      "suffix": "Sr.",
      "birthday": "1759-02-16",
      "genderId": 1,
      "email": {
        "emailId": 1,
        "type": "Business",
        "address": "contact@doctor.com",
        "order": 1
      },
      "phones": [
        {
          "phoneId": 1,
          "number": "(123) 456-7890",
          "ext": "112",
          "equipType": "Cell",
          "usage": "string",
          "notes": "Only available between 8am and noon.",
          "order": 1,
          "contactType": "WorkPhone"
        }
      ],
      "addresses": [
        {
          "street": "102 Maple Street",
          "city": "Vancouver",
          "postalZip": "A12AA1",
          "locationId": 10,
          "note": "A quick note",
          "type": "Civil",
          "start": "2019-03-06",
          "end": "2020-03-31",
          "masks": [
            {
              "maskId": 1,
              "userId": 24,
              "masked": true,
              "maskedDate": "2024-02-14T18:00:18.490Z",
              "lastModified": "2024-02-14T18:00:18.490Z",
              "notes": "string",
              "patientId": 45,
              "fieldName": "STREET1",
              "maskAuthorizations": [
                {
                  "id": 32,
                  "maskId": 22,
                  "untilDate": "2024-02-14T18:00:18.490Z",
                  "Role id": 2,
                  "userId": 6
                }
              ]
            }
          ]
        }
      ],
      "healthCard": {
        "phn": "123133",
        "locationId": 1,
        "expiry": "2016-02-16"
      },
      "preferredContactType": "WorkPhone",
      "nextKinName": "Jane Doe",
      "nextKinPhone": {
        "phoneId": 1,
        "number": "(123) 456-7890",
        "ext": "112",
        "equipType": "Cell",
        "usage": "string",
        "notes": "Only available between 8am and noon.",
        "order": 1,
        "contactType": "WorkPhone"
      },
      "officialLanguageCode": "eng",
      "spokenLanguageCode": "bul",
      "relationshipStatusId": 2
    },
    "familyProviderId": 1,
    "enrolledProviderId": 1,
    "officeProviderId": 2,
    "referringProviderId": 1,
    "insurerId": 2,
    "fileNumber": "22-12345",
    "uuid": "abcd123-e456-789f-12a3-123456a789f",
    "registrationNumber": "123",
    "paperChartNote": " A note",
    "paperChart": false,
    "patientStatusId": 1,
    "gestationAge": "2017-11-08",
    "employerContactId": 1,
    "pharmacyContactId": 1,
    "referredDate": "2020-09-10",
    "onSocialAssistance": false,
    "hasArchivedRecords": false,
    "deceased": false,
    "deceasedDate": "2024-02-14T18:00:18.490Z",
    "occupation": "Lawyer",
    "alert": {
      "message": "A message",
      "lastUpdated": "2024-02-14T18:00:18.490Z",
      "flagUser": 12
    },
    "albertaDetails": {
      "type": "PYST",
      "newBornCode": "ADOP",
      "guardianUli": "234-123-564",
      "guardianRegistration": "123",
      "uli2": {
        "phn": "123133",
        "locationId": 1,
        "expiry": "2016-02-16"
      }
    },
    "ontarioDetails": {
      "validationStatus": true,
      "validationMessage": "valid",
      "admissionDate": "2000-05-31",
      "dischargeDate": "2000-05-31",
      "masterNumber": 123
    },
    "novaScotiaDetails": {
      "guardianHcn": "123-234-123",
      "secondaryHealthCard": {
        "phn": "123133",
        "locationId": 1,
        "expiry": "2016-02-16"
      },
      "lastUpdatedDatetime": "2024-02-14T18:00:18.490Z"
    },
    "manitobaDetails": {
      "healthRegistrationNumber": "123"
    }
  }
]
```
## Creating a patient

If your search for a patient and there are no results, you will be able to create the patient.

**Collection: PatientV2 Endpoints**

<details>
    <summary><code>POST</code> <code><b>/v2/provider-portal/patients</b></code></summary>

##### Headers

> | name                        |  type     | data type                                    | description                      |
> |-----------------------------|-----------|----------------------------------------------|----------------------------------|
> | Content-Type                |  required | application/x-www-form-urlencoded            | none.
> | Authorization               |  required | bearer eyJraWQiOiJyd...                      | none.
> | X-QHR-Subscription-Key      |  required | 23456abcdfg456789abcds                       | none.


##### Parameters

> | name         |  type      | data type      | description                                          |
> |--------------|------------|----------------|------------------------------------------------------|
> | `uuid`       |  required  | string         | none.                                                |

##### Responses

> | http code     | content-type                      | response                                                            |
> |---------------|-----------------------------------|---------------------------------------------------------------------|
> | `204`         | `application/json`                | `patientidvalue`                               |
> | `400`         | `application/json`                | `{"code":"400","message":"Bad Request"}`                            |
> | `401`         | `application/json`                | `{"code":"401","message":"Unauthorized"}`                           |

##### Example cURL
> ```javascript
> curl --location 'https://q1-accapi.accuroemr.com/rest/v2/provider-portal/patients/?uuid=e1a47a6d-f563-4f0f-a7f3-b6539ba21330' --header 'Authorization: bearer 4989a308-f870-4137-b83a-5b3259adf5ad' --header 'Accept: application/json' --header 'Content-Type: application/json' --data-raw '{ "demographics": { "firstName": "John", "lastName": "Doe", "middleName": "Smith", "title": "Mr.", "suffix": "Jr.", "birthday": null, "genderId": 1, "email": { "emailId": 1, "type": "1232", "address": "contact@doctor.com", "order": 1 }, "phones": [ { "phoneId": 1, "number": "(123) 456-7890", "ext": "112", "equipType": "Cell", "usage": "string", "notes": "Only available between 8am and noon.", "order": 1, "contactType": "HomePhone" } ], "addresses": [ { "street": "123 Some Street", "city": "Somewhere", "postalZip": "A1A 1A1", "locationId": 1, "note": "Address note...", "start": "2019-03-06", "end": "2020-03-31" } ], "healthCard": { "phn": "123466789123", "locationId": 1, "expiry": null }, "preferredContactType": null, "nextKinName": "", "nextKinPhone": { "phoneId": 1, "number": "(123) 456-7890", "ext": "112", "equipType": "Cell", "usage": "string", "notes": "Only available between 8am and noon.", "order": 1, "contactType": "WorkPhone" }, "officialLanguageCode": null, "spokenLanguageCode": null, "relationshipStatusId": 1 }, "familyProviderId": null, "enrolledProviderId": null, "officeProviderId": null, "referringProviderId": null, "insurerId": 23, "fileNumber": "11-12345", "registrationNumber": "", "paperChartNote": "", "paperChart": false, "patientStatusId": 1, "gestationAge": null, "employerContactId": null, "pharmacyContactId": null, "referredDate": null, "onSocialAssistance": false, "hasArchivedRecords": false, "deceased": false, "deceasedDate": null, "occupation": "", "alert": { "message": "A message", "lastUpdated": "2000-05-31", "flagUser": 12 }, "ontarioDetails": { "validationStatus": true, "validationMessage": "valid", "admissionDate": "2000-05-31", "dischargeDate": "2000-05-31" } }'
> ```
</details>

```js title="Request"
{
 "demographics": {
    "firstName": "John",
    "lastName": "Doe",
    "middleName": "Smith",
    "title": "Mr.",
    "suffix": "Jr.",
    "birthday": null,
    "genderId": 1,
    "email": {
      "emailId": 1,
      "type": "1232",
      "address": "contact@doctor.com",
      "order": 1
    },
    "phones": [
      {
        "phoneId": 1,
        "number": "(123) 456-7890",
        "ext": "112",
        "equipType": "Cell",
        "usage": "string",
        "notes": "Only available between 8am and noon.",
        "order": 1,
        "contactType": "HomePhone"
      }
    ],
    "addresses": [
      {
        "street": "123 Some Street",
        "city": "Somewhere",
        "postalZip": "A1A 1A1",
        "locationId": 1,
        "note": "Address note...",
        "start": "2019-03-06",
        "end": "2020-03-31"
      }
    ],
    "healthCard": {
      "phn": "123466789123",
      "locationId": 1,
      "expiry": null
      },
    "preferredContactType": null,
    "nextKinName": "",
    "nextKinPhone": {
      "phoneId": 1,
      "number": "(123) 456-7890",
      "ext": "112",
      "equipType": "Cell",
      "usage": "string",
      "notes": "Only available between 8am and noon.",
      "order": 1,
      "contactType": "WorkPhone"
    },
    "officialLanguageCode": null,
    "spokenLanguageCode": null,
    "relationshipStatusId": 1
  },
  "familyProviderId": null,
  "enrolledProviderId": null,
  "officeProviderId": null,
  "referringProviderId": null,
  "insurerId": 23,
  "fileNumber": "11-12345",
  "registrationNumber": "",
  "paperChartNote": "",
  "paperChart": false,
  "patientStatusId": 1,
  "gestationAge": null,
  "employerContactId": null,
  "pharmacyContactId": null,
  "referredDate": null,
  "onSocialAssistance": false,
  "hasArchivedRecords": false,
  "deceased": false,
  "deceasedDate": null,
  "occupation": "",
  "alert": {
    "message": "A message",
    "lastUpdated": "2000-05-31",
    "flagUser": 12
  },
  "ontarioDetails": {
    "validationStatus": true,
    "validationMessage": "valid",
    "admissionDate": "2000-05-31",
    "dischargeDate": "2000-05-31"

  }
}'
```
### Helpful hints for fields

- `demographics.genderId`: Possible values can be found here:
```js title="Collection: Gender Endpoints"
GET /v1/genders?{uuid}
```
```js title="Response"
[
  {
    "id": 11,
    "name": "Female",
    "builtIn": true,
    "shortName": "F",
    "simplifiesToMale": true
  }
]
```
- `demographics.healthCard.locationId`: The location ID indicates which jurisdiction (like a province or state) issued the heath card number. Accuro API provides all Canadian provinces and US states. For other jurisdictions, you are able to set the location to 'other'. Your application _must_ provide the location of the health number as Accuro uses this to validate the format and

```js title="Collection: Location Endpoints"
GET /v1/locations?{uuid}
```
```js title="Response"
[
  {
    "locationId": 2,
    "province": "BC",
    "country": "Canada",
    "description": "British Columbia"
  }
]
```
- `family|enrolled|office|refferingProviderId`: These fields require an ID for a provider that exists in the Accuro's address book. The reference for retrieving **all** providers is

```js title="Collection:Provider Endpoints"
GET /v1/providers?uuid={uuid}
```

```js title="Response"
[
  {
    "providerId": 1,
    "statusId": 1,
    "typeId": 1,
    "title": "Sir",
    "firstName": "Arthur",
    "middleName": "Conan",
    "lastName": "Doyle",
    "suffix": "Sr.",
    "address": "221B Baker St",
    "phones": [
      {
        "phoneId": 1,
        "number": "(123) 456-7890",
        "ext": "112",
        "equipType": "Cell",
        "usage": "string",
        "notes": "Only available between 8am and noon.",
        "order": 1,
        "contactType": "WorkPhone"
      }
    ],
    "email": {
      "emailId": 1,
      "type": "Business",
      "address": "contact@doctor.com",
      "order": 1
    },
    "specialty": "Family Practice",
    "defaultOffice": 17766,
    "cpsoNumber": "1442456",
    "practitionerNumber": "12345"
  }
]
```

- `insurerId`: You should populate this field with the default government insurer for the province.

| Province         | InsurerId |
| ---------------- | --------- |
| Alberta          | 24        |
| British Columbia | 1         |
| Saskatchewan     | 1         |
| Manitoba         | 1         |
| Ontario          | 1         |
| Nova Scotia      | 24        |

- `patientStatusId`: This indicates if a patient is active, inactive, or a custom status set by the clinic. Active is 1, Inactive is 0.

- `employerContactId`, `pharmacyContactId`: When creating a patient, you should NOT populate these fields (omit or set to `null`) since it's currently not possible to find these IDs. When updating the patient, you should keep the value that exists on the patient.

## Province-specific details

For each province of Accuro, there is some province specific data that needs to be included when creating a patient.

### British Columbia

No specific data is required for British Columbia.

### Alberta

**Example Alberta Details**

```js
{
  "type": "RECP", // PYST - Payee, RECP - Service Recipient, RFRC - Out of Province Referring Service Provider
  "newBornCode": "ADOP", // ADOP - Adoption, LVBR - Live Birth, MULT - Multiple Birth, STBN - Still born
  "guardianUli": "234123564", // Health card number
  "guardianRegistration": "123", // Registration number for private insurance
  "uli2": {
    "phn": "1254",
    "locationId": 1,
    "expiry": "2016-02-16T00:00:00.000-0800"
  }
}
```

**Minimal Albertal Details**

```js
{
  "uli2": {
  }
}
```

### Saskatchewan

No specific data is required for Saskatchewan.

### Manitoba

```js
{
  "healthRegistrationNumber": "123";
}
```

### Ontario

```js
{
  "validationStatus": false,
  "validationMessage": "Not validated",
  "admissionDate": "2019-05-31 00:00:00.000",
  "dischargeDate": "2019-05-31 00:00:00.000",
  "masterNumber": 123
}
```

### Nova Scotia

```js
{
  "guardianHcn": "123234123",
  "secondaryHealthCard": {
    "phn": "1254",
    "locationId": 1,
    "expiry": "2019-02-16T00:00:00.000-0800"
  },
  "lastUpdatedDatetime": "20019-05-31 00:00:00.000"
}
```
