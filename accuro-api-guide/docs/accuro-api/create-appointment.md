---
sidebar_position: 5
---
# Creating an Appointment

After determining the `available slots for new appointments` , client applications have most of the information they need to create an appointment.

The information needed to create an appointment is:

```js
{
  "date" : "2019-01-31",
  "startTime" : 1300,
  "endTime" : 1315,
  "subColumn" : 1,
  "patientId" : 2,
  "providerId" : 3,
  "resourceId" : 4,
  "officeId" : 5,
  "maskId" : 6,
  "appointmentDetails" : {
    "billingProviderId" : 3,
    "referringProviderId" : null, // Optional
    "notes" : "Patient Would like to discuss flu vaccines",
    "alerts" : "Patient is always late, booked an extra 10 minutes",
    "reasonId" : 7,
    "typeId" : 8,
    "priorityId" : 1,
    "arrived" : false,
    "confirmed" : false,
    "serviceLocation" : "O"
  }
}
```
```text title="Collection: Schedule Endpoints"
POST /v1/provider-portal/scheduler/appointments
```

Below is how the above information should be populated:
-  `date`, `startTime`, `endTime` The specifics of a date and time for an appointment is determined by which available slot is chosen during the online booking process.
- `patientId` To get a valid `patientId`
- `providerId`, `billingProviderId`, `resourceId`, `subColumn`, `typeId`, `reasonId`

### From Applied Suggestion

Each available slot for a new appointment has an underlying _applied suggestion_ which has data fields that **must** be transferred into the new _appointment_.

These fields are:

- **providerId**, **billingProviderId** The provider associated with the _applied suggestion_ and available slot
- **resourceId** May exist for some Accuro instances and clinics. Corresponds to a resource like equipment
- **subColumn** The specific column the _applied suggestion_ is in

**Example Applied Suggestion**

```js
{
  "name" : "Checkup",
  "providerId" : 3,
  "resourceId" : 4,
  "subColumn" : 0,
  "date" : "2019-01-31",
  "startTime" : 800,
  "endTime" : 1700,
  "grouped" : true,
  "allowMedeoEBookingRequests" : true,
  "allowProviderCreation" : true
}
```

### From Suggestion

To populate the `typeId` and `reasonId` fields when creating an appointment, the actual _suggestion_ resource should be retrieved by retrieving the full list of _suggestions_ and extracting the `typeId` and `reasonId` field from the resource that matches the `name` field on the _applied suggestion_.

**Example Suggestion**

```js
{
  "name" : "Checkup",
  "color" : {...},
  "abbreviation" : "CH",
  "reasonId" : 7,
  "typeId" : 8,
  "allowProviderCreation" : true,
  "allowMedeoEBookingRequests" : true,
  "barDisplay" : true,
  "active" : true
}
```

- `officeId`: Accuro is capable of containing multiple _offices_. However, since a schedule (e.g. _applied suggestions_) is associated with a provider and not an office in any way, you **must** provide a way for Accuro users to select which office to associate appointments to. Applications are able to retrieve the list of _offices_ and should enable users to select the appropriate office.

```text title="Collection: Office Endpoints"
GET /v1/offices
```
- `maskId`: This field should be omitted and ignored.
- `priorityId`: Applications are able to retrieve a list of the priorities that exist in Accuro.

```text title="Collection: Schedule Endpoints- Public"
GET /v1/scheduler/priorities
```
- `serviceLocation`: The `serviceLocation` should default to the capital letter 'O' which corresponds to 'The Provider's Office'. Applications may enable users to configure another location to use.

**Example of service locations**

![Service Locations](/img/service-location-accuro.png)


### User defined fields

Applications can provide text values for the `notes` and `alerts` fields either with user input or configured values. The `alerts` field value will be shown in a prompt when the appointment is opened within Accuro.
