---
sidebar_position: 12
---
# Accuro Providers and Offices

## Offices

_Offices_ may represent:

- distinct real-world offices that contain distinct or shared providers
- various groups within a single real-world office
- distinct service types or functions the clinic provides like walk-ins, online-booking, or a family doctor clinic etc.
- custom groupings to address organizational needs

You should provide an ability for Accuro users to specify which office(s) your application should be interested in and interact with. For example, appointments need to be but schedule data is only associated with a provider. In order to know what office to populate when creating an appointment your application should provide a way for users to indicate which office to use.

### Retrieving the list of _offices_

To provide a list of offices to display to users so they can indicate the appropriate office to use in API interactions:

```text
GET {Accuro API base url}/rest/v1/offices?uuid={Accuro uuid}
```

```js
[
  {
    officeId: 12,
    name: "Test Office",
    address: {
      street: "102 Maple Street",
      city: "Vancouver",
      postalZip: "A1A2B2",
      locationId: 1,
      note: "A quick note.",
      type: "type",
      start: 2017,
      end: 2017
    },
    officePhone: {
      phoneId: 1,
      number: "(123) 456-7890",
      ext: "112",
      equipType: "Cell",
      usage: "Active",
      notes: "Only available between 8am and noon.",
      order: 2,
      contactType: "Business"
    },
    fax: {
      phoneId: 1,
      number: "(123) 456-7890",
      ext: "112",
      equipType: "Cell",
      usage: "Active",
      notes: "Only available between 8am and noon.",
      order: 2,
      contactType: "Business"
    },
    email: {
      emailId: 1,
      type: "Business",
      address: "contact@doctor.com",
      order: 1
    },
    website: "https://google.com/",
    facilityNumber: "132",
    abbreviation: "ABBRV",
    active: true
  }
];
```


## Providers

Providers can belong to multiple offices and are often required to be included when creating resources via the Accuro API (e.g. encounter letters/notes, labs, appointments etc.).

### Retrieve list of _providers_ in an _office_

To retrieve a list of providers for a specific office:

```text
GET {Accuro API base url}/rest/v1/providers/offices/{officeId}
```

```js
[
  {
    providerId: 1,
    statusId: 1,
    typeId: 1,
    title: "Sir",
    firstName: "Arthur",
    middleName: "Conan",
    lastName: "Doyle",
    suffix: "Sr.",
    address: {
      street: "102 Maple Street",
      city: "Vancouver",
      postalZip: "A1A2B2",
      locationId: 1,
      note: "A quick note.",
      type: "type",
      start: 2017,
      end: 2017
    },
    phones: [
      {
        phoneId: 1,
        number: "(123) 456-7890",
        ext: "112",
        equipType: "Cell",
        usage: "Active",
        notes: "Only available between 8am and noon.",
        order: 2,
        contactType: "Business"
      },
      {
        phoneId: 1,
        number: "(123) 456-7890",
        ext: "112",
        equipType: "Cell",
        usage: "Active",
        notes: "Only available between 8am and noon.",
        order: 2,
        contactType: "Business"
      }
    ],
    email: {
      emailId: 1,
      type: "Business",
      address: "contact@doctor.com",
      order: 1
    },
    specialty: "Family Practice",
    defaultOffice: null
  }
];
```
