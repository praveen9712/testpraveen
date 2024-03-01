---
sidebar_position: 14
---
# Access to the Accuro Waitlist

The Accuro Waitlist allows clinics to track patients who require an appointment currently not available in the Accuro scheduler. When an appointment availability becomes available, clinics are able to find which patient is next in priority to be booked for an appointment at that time.

![Accuro Waitlist](/img/accuro-waitlist-overview.png)

Accuro API enables client applications to get, create, and update consult waitlist booking requests.

## Searching Waitlist Requests

You are able to search and retrieve waitlist requests through the following endpoint and the results can be optionally filtered by a specific waitlist provider, patient or waitlist consult status.

**Collection: Waitlist Endpoints**
<details>
  <summary><code>GET</code> <code><b>/rest/v1/provider-portal/waitlists</b></code></summary>

##### Headers

> | name                        |  type     | data type                                    | description                      |
> |-----------------------------|-----------|----------------------------------------------|----------------------------------|
> | Content-Type                |  required | application/x-www-form-urlencoded            | none                             |
> | Authorization               |  required | bearer eyJraWQiOiJyd...                      | none                             |
> | X-QHR-Subscription-Key      |  required | 23456abcdfg456789abcds                       | none                             |

##### Parameters

> | name                |  type     | data type      | description                         |
> |---------------------|-----------|----------------|-------------------------------------|
> | `uuid`              |  required | string         | none                                |
> | `waitlistProviderId`|  optional | string         | none                                |
> | `patientId`         |  optional | string         | none                                |
> | `consultStatus`     |  optional | string         | none                                |
> | `pageSize`          |  optional | string         | The size of the pages with default value 25 and maximum value 50. If the page size is not provided or less than 1, the page size will be set to default value 25. If page size provided is more than maximum value, the page size will be set to the default maximum value 50.                                |
> | `startingId`        |  optional | string         | This id is EnvelopeDto.lastId of the previous page(request). It is the same as the id (waitlist request id) of the last record of the previous results.                                |

##### Responses

> | http code     | content-type                      | response                                                            |
> |---------------|-----------------------------------|---------------------------------------------------------------------|
> | `200`         | `application/json`                | `[ { "id": 0, "waitlistProviderId": 2, "patientId": 12, . . . }, { "id": 0, "waitlistProviderId": 2, "patientId": 12, . . . } ]`                                                                 |
> | `401`         | `application/json`                | `{"code":"401","message":"Unauthorized"}`                           |

##### Example cURL

> ```javascript
> curl --location --globoff '{{host}}{{port}}{{servlet}}/rest/v1/provider-portal/waitlists?pageSize={{pageSize}}&waitlistProviderId={{waitlistProviderId}}&patientId={{patientId}}&consultStatus={{consultStatus}}&startingId={{startingId}}&tenant={{tenant}}&uuid={{uuid}}' --header 'Authorization: bearer {{auth_provider}}' --header 'Accept: application/json' --header 'Content-Type: application/json' --header 'X-QHR-Subscription-Key: {{Subscription-Key}}'
> ```
</details>



```js title="Response"
{
    "contents": [
        {
            "id": 1000,
            "waitlistProviderId": 2,
            "patientId": 12,
            "consultPriority": "Emergency",
            "consultStatus": "Booked",
            "consultType": "EMG Consult",
            "expedited": false,
            "providerTypeId": 12,
            "referralTriaged": true,
            "notes": "string",
            "complaint": "string",
            "referringProviderId": 0,
            "specificProviderRequested": true,
            "firstAvailable": false,
            "referredOut": true,
            "consultRefusedReason": "string",
            "firstConsultDate": "2020-03-31T18:37:45.291Z",
            "firstContactDate": "2020-03-31T18:37:45.291Z",
            "patientAvailableForConsult": "2020-03-31T18:37:45.291Z",
            "tentativeDate": "2020-03-31T18:37:45.291Z",
            "urgentDate": "2020-03-31T18:37:45.291Z",
            "referralDate": "2020-03-31T18:37:45.291Z",
            "targetDate": "2020-03-31T18:37:45.291Z",
            "requestDate": "2020-03-31T18:37:45.291Z",
            "altWaitlistProviderId": 1,
            "caseState": "Active",
            "caseNumber": 1,
            "caseCloseDate": "2020-03-31T18:37:45.291Z",
            "followUp": false,
            "bookedDate": "2020-03-31T18:37:45.291Z",
            "decisionDate": "2020-03-31T18:37:45.291Z",
            "patientAvailableForSurgery": "2020-03-31T18:37:45.291Z"
        }
    ],
    "count": 1,
    "total": 1,
    "lastId": 1000
}
```

### Pagination

Results returned are paginated. The maximum `pageSize` is **50** with a default value of **25** if no `pageSize` parameter is provided. To request the next page, you need to set the `startingId` parameter to the value of `lastId` received in the previous request.

The `total` field contained in the response is the total number of results and remains constant as you requests additional pages.

When you receive a response like:

```js title="Object"
{
    "contents": [],
    "count": 0,
    "total": 15,
    "lastId": 0
}
```

You have reached the end of the results.

## Creating/Updating a Waitlist Request

Your application is able to create waitlist requests by making the following request:

**Collection: Waitlist Endpoints**
<details>
  <summary><code>POST</code> <code><b>/rest/v1/provider-portal/waitlists</b></code></summary>

##### Headers

> | name                        |  type     | data type                                    | description                      |
> |-----------------------------|-----------|----------------------------------------------|----------------------------------|
> | Content-Type                |  required | application/x-www-form-urlencoded            | none                             |
> | Authorization               |  required | bearer eyJraWQiOiJyd...                      | none                             |
> | X-QHR-Subscription-Key      |  required | 23456abcdfg456789abcds                       | none                             |

##### Parameters

> | name                |  type     | data type      | description                         |
> |---------------------|-----------|----------------|-------------------------------------|
> | `uuid`              |  required | string         | none                                |

##### Responses

> | http code     | content-type                      | response                                                            |
> |---------------|-----------------------------------|---------------------------------------------------------------------|
> | `200`         | `application/json`                | `0`                                                                 |
> | `401`         | `application/json`                | `{"code":"401","message":"Unauthorized"}`                           |

##### Example cURL

> ```javascript
>curl --location --globoff '{{host}}{{port}}{{servlet}}/rest/v1/provider-portal/waitlists?uuid={{uuid}}'  --header 'Authorization: bearer {{auth_provider}}'  --header 'Accept: application/json'  --header 'Content-Type: application/json'  --header 'X-QHR-Subscription-Key: {{Subscription-Key}}'  --data '{ "decisionDate": "2020-05-06T00:00:00.000", "patientAvailableForSurgery": "2020-03-06T00:00:00.000", "waitlistProviderId": 5, "patientId": 20719, "consultPriority": "1 month", "consultStatus": "Booked", "consultType": "Daycare", "expedited": true, "providerTypeId": 12, "referralTriaged": true, "notes": "test note?", "complaint": "", "referringProviderId": 12, "specificProviderRequested": false, "firstAvailable": true, "referredOut": true, "consultRefusedReason": "refuse", "firstConsultDate": "2020-03-06T00:00:00.000", "firstContactDate": "2020-03-07T00:00:00.000", "patientAvailableForConsult": "2020-03-05T00:00:00.000", "tentativeDate": "2020-03-05T00:00:00.000", "urgentDate": "2020-03-05T00:00:00.000", "referralDate": "2020-03-04T00:00:00.000", "targetDate": "2020-03-04T00:00:00.000", "requestDate": "2020-02-26T00:00:00.000", "altWaitlistProviderId": 5, "caseState": "Active", "caseNumber": 13, "caseCloseDate": "2020-03-05T00:00:00.000", "followUp": false, "bookedDate": "2020-04-23T00:00:00.000" }'
> ```
</details>



```js title="Request"
{
    "decisionDate": "2020-05-06T00:00:00.000",
    "patientAvailableForSurgery": "2020-03-06T00:00:00.000",
    "waitlistProviderId": 5,
    "patientId": 20719,
    "consultPriority": "1 month",
    "consultStatus": "Booked",
    "consultType": "Daycare",
    "expedited": true,
    "providerTypeId": 12,
    "referralTriaged": true,
    "notes": "test note?",
    "complaint": "",
    "referringProviderId": 12,
    "specificProviderRequested": false,
    "firstAvailable": true,
    "referredOut": true,
    "consultRefusedReason": "refuse",
    "firstConsultDate": "2020-03-06T00:00:00.000",
    "firstContactDate": "2020-03-07T00:00:00.000",
    "patientAvailableForConsult": "2020-03-05T00:00:00.000",
    "tentativeDate": "2020-03-05T00:00:00.000",
    "urgentDate": "2020-03-05T00:00:00.000",
    "referralDate": "2020-03-04T00:00:00.000",
    "targetDate": "2020-03-04T00:00:00.000",
    "requestDate": "2020-02-26T00:00:00.000",
    "altWaitlistProviderId": 5,
    "caseState": "Active",
    "caseNumber": 13,
    "caseCloseDate": "2020-03-05T00:00:00.000",
    "followUp": false,
    "bookedDate": "2020-04-23T00:00:00.000"
}
```

Above is an example containing only the required fields needed to create a waitlist booking request. Both the `referralDate` (date referral was sent) and the `requestDate` (date referral was received) are required and, in most cases, will be the same date.

The equivalent action in Accuro is shown below with the relevant fields highlighted. Note that currenly only access to **Consult** Waitlist Requests is available.


### Adding Attachments

Your application is able to attach either a document or letter (a.k.a encounter note) to an existing waitlist request. In order to successfully add an attachment, you will need:

- An existing Waitlist Request and it's ID
- An existing document or letter that was previously created and it's ID

Below is an example request:



**Collection: WaitlistAttachment Endpoints**
<details>
  <summary><code>POST</code> <code><b>/rest/v1/provider-portal/waitlists/(waitlistRequestId)/attachments</b></code></summary>

##### Headers

> | name                        |  type     | data type                                    | description                      |
> |-----------------------------|-----------|----------------------------------------------|----------------------------------|
> | Content-Type                |  required | application/x-www-form-urlencoded            | none                             |
> | Authorization               |  required | bearer eyJraWQiOiJyd...                      | none                             |
> | X-QHR-Subscription-Key      |  required | 23456abcdfg456789abcds                       | none                             |

##### Parameters

> | name                |  type     | data type      | description                         |
> |---------------------|-----------|----------------|-------------------------------------|
> | `uuid`              |  required | string         | none                                |
> | `waitlistRequestId` |  required | string         | url path value                      |

##### Responses

> | http code     | content-type                      | response                                                            |
> |---------------|-----------------------------------|---------------------------------------------------------------------|
> | `204`         | `application/json`                | `no content`                                                                 |
> | `401`         | `application/json`                | `{"code":"401","message":"Unauthorized"}`                           |

##### Example cURL

> ```javascript
>curl --location --globoff '{{host}}{{port}}{{servlet}}/rest/v1/provider-portal/waitlists/{{listId}}/attachments?uuid={{uuid}}' \ --header 'Authorization: bearer {{auth_provider}}' \ --header 'Accept: application/json' \ --header 'Content-Type: application/json' \ --header 'X-QHR-Subscription-Key: {{Subscription-Key}}' \ --data '[ { "itemId": 1012, "itemCategory": "Letter" }, { "itemId": 165, "itemCategory": "Document" } ]'
> ```
</details>


```js title="Request"
[
    {
        
        "itemId": 1012,
        "itemCategory": "Letter"
    },
    {
     
        "itemId": 165,
        "itemCategory": "Document"
    }
]
```

## Waitlist Dates and Additional Information

If you recall **Searching Waitlist Requests**, it's possible that more fields exist for a particular waitlist request than just the required fields and dates, e.g. `firstConsultDate`, `patientAvailableForConsult` etc. These dates are optional when creating a waitlist request due to the fact that waitlist dates are highly configurable.

Clinics are able to configure which dates are shown for the **Consult** tab when creating or viewing the details of a waitlist request. Your application should consult with the clinic on which dates exist on the **Consult** tab and what types of dates are expected to populate each field.

The other additional fields, like `expedited`, `firstAvailable` and others, can be populated when creating or updating a waitlist request. These are optional as clinics may like to populate these fields themselves or not use these at all. Clinics should be consulted for how these fields should be populated.

## Waitlist Providers

To create a waitlist request associated with a provider, that provider **needs** to be configured as a waitlist provider. You are able to retrieve the list of providers currently configured for the Accuro Waitlist via the `GET /v1/provider-portal/waitlist-providers` which returns a result like:

```js
[
  {
    id: 20003,
    practitionerNumber: "1234",
    payeeNumber: "",
    firstName: "David",
    lastName: "Doctor",
    phoneNumber: "(___) ___-____",
    phoneExt: "",
    providerId: 12345,
    active: true
  }
];
```

The ID value `20003`, is the **Waitlist Provider ID** and must be used as the `waitlistProviderId` field.

The `providerId` field can be used to cross-reference this list with the `/v1/providers` endpoint.

## Supporting Information

You are able to retrieve the list of current consult priorities, statuses, types and complaints through the following endpoints:

**Waitlist Endpoints**
- `GET /v1/provider-portal/waitlist-consult-priorities`
- `GET /v1/provider-portal/waitlist-consult-statuses`
- `GET /v1/provider-portal/waitlist-consult-types`
- `GET /v1/provider-portal/waitlist-consult-complaints`

The create/update waitlist request endpoint expects the string value of the priority, status, type and complaint.
