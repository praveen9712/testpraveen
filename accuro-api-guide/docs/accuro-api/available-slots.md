---
sidebar_position: 3
---
# Available Slots for New Appointments

The scheduling functionality in the Accuro API exposes booked appointments and can be used to determine at what times the provider is available for new appointments. Other resources include appointment types and reasons, suggestion types, appointment statuses and billing details.

This guide will introduce the concepts that are required to enable online booking functionality.

## The Suggestion Concept

A core concept to Accuro's schedule is the **Suggestion** resource. Accuro users create _suggestions_ that are associated with an appointment type and reason. They can then apply the suggestion which adds the suggestion to a specific provider, date, time. Key fields in the _suggestion_ resource are:

- **name** a unique text identifier for the _suggestion_. The `name` field is used to link a _suggestion_ and an _applied suggestion_ (an instance of a suggestion that exists on a provider's schedule and has a start and end time)
- **appointment type** ID indicating the type of appointment allowed to be booked
- **appointment reason** ID indication the reason for the appointment being booked
- **allowed to book** boolean controlling if any appointment can be booked

Suggestions are configured within Accuro and can be added or 'applied' to a providers schedules at specific times.


```js title="Example Suggestion"
{
  "name" : "General Checkups",
  "color" : {
    "blue" : 12345,
    "green" : 12345,
    "transparency" : 12345,
    "RGB" : 12345,
    "red" : 12345,
    "colorSpace" : {
      "CS_sRGB" : true,
      "numComponents" : 12345,
      "type" : 12345
    },
    "alpha" : 12345
  },
  "abbreviation" : "CH",
  "typeId" : 1,
  "reasonId" : 2,
  "allowProviderCreation" : true,
  "allowMedeoEBookingRequests" : true,
  "barDisplay" : true,
  "active" : true
}
```
```text title="Collection: Schedule Endpoints"
GET /v1/provider-portal/scheduler/suggestions
```

## Applied Suggestions

When _suggestions_ are associated with a specific provider, date, and time an _applied suggestion_ instance is created. _Applied suggestions_ indicate when the provider can see patients and for which appointment type.

To retrieve more details (appointment type and reason) about an _applied suggestion_ you must use the `name` field to find the associated _suggestion_. The `name` field links a _suggestion_ with an _applied suggestion_.


```js title="Example Applied Suggestion"
{
  "name" : "Checkup",
  "providerId" : 1,
  "resourceId" : 1,
  "subColumn" : 0,
  "date" : "2018-08-07T00:00:00.000-0700",
  "startTime" : 800,
  "endTime" : 815,
  "grouped" : true,
  "allowMedeoEBookingRequests" : true,
  "allowProviderCreation" : true
}
```
```text title="Collection: Schedule Endpoints"
GET /v1/provider-portal/scheduler/applied-suggestions
```

## Example of Providers Schedule

A typical schedule for a provider looks like the following diagram. All times they are available for an appointment is marked with a _suggestion_ (called an _applied suggestion_) which indicates the appointment type and reason of the appointment the provider is available for.

![Example Schedule](/img/example-schedule.png)

## Calculating Available Slots for Appointments

A providers schedule can be thought of as a series of layers. The first layer is simply time, then applied suggestions, then appointments.

To determine the available slots for new appointments, applications need to retrieve the applied suggestions to discover the possible times a provider is available for a specific appointment type. Out of those possible times, if there is an appointment that has been booked that time should be removed as an available slot for a new appointment.

![Appointment Slots](/img/appointment-slots-set.png)

### Gotcha: Appointment Duration

In the above example schedule, appointments actually have a duration of 30 minutes even though the _applied suggestions_ span longer than 30 minutes. This means that multiple appointments can be booked on top of an _applied suggestion_ if the _applied suggestion_ is longer than 30 minutes (specifically a multiple of 30 minutes).

However, _Appointment types_, _Suggestions_, and _Applied Suggestions_ do not have any preset duration and this data will not come back in any response objects. Applications using the Accuro API will need to provide a way for users to map a _Suggestion_ to a specific duration.

Once the duration of appointments is known for a specific _suggestion_, applications are able to calculate the available slots. For example, if an _applied suggestion_ occurs on a providers schedule from 10:00 to 11:30, and the _suggestion_ is mapped to a 30 minute appointment duration, then there will be 3 avaibable slots for new appointments at 10:00, 10:30 and 11:00.

![Example Slots](/img/example-available-slots.png)

# Next Step

Once available slots for new appointments are known, these can be shown to users and selected by them to create a new appointment. 
