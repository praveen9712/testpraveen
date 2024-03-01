---
sidebar_position: 4
---

# Combined Appointment

For all appointments, Accuro API is able to provide appointment + history, patient, provider, and office information in a single endpoint: the `/v2/materials/appointments` endpoint.

```js
GET {Accuro API base url}/rest/v2/materials/appointments?uuid={Accuro uuid}&startDate={start}&endDate={end}&pageSize=50&startingId=0
```

Other filter query parameters include:

- **providerId** Retrieve appointments for a specific provider
- **officeId** Retrieve appointments for a specific office. Multiple offices can be provided e.g. officeId=1234&officeId=4321
- **includeNullOffice** Retrieves appointments not associated with any office. This can occur if importing appointments from another source

## Paginated results

Results returned by the API are paginated with a default page size of 25 and a max page size of 50. You can set the page size by passing the desired size in `pageSize` query parameter. Each response will contain:

```js
{
  ...,
  count: {the number of items included in the response. Usually is the same as the pageSize},
  lastId: {the next value for the startingId query parameter},
  total: {the total remaining records, including the items in the current page}
}
```

### Determining the last page of results

There are two recommended approaches for determining when all results have been received and you have the last page of data.

1. When `total` is less than or equal to `pageSize` you have received all the pages and data.
2. When you request the next page and `total`, `count`, `lastId` is `0`.

### Example of multiple results

Assuming a `pageSize` of 50 and a total of 120 appointments.

**Request 0**

To request the first page of appointment data, set `?startingId=0`.

```js
GET {Accuro API base url}/rest/v2/materials/appointments?uuid={Accuro uuid}&startDate={start}&endDate={end}&pageSize=50&startingId=0
```

```js
{
  ...,
  count: 50,
  lastId: 123456789,
  total: 120
}
```
**Request 1**

```text
GET {Accuro API base url}/rest/v2/materials/appointments?uuid={Accuro uuid}&startDate={start}&endDate={end}&pageSize=50&startingId=123456789
```

```js
{
  ...,
  count: 50,
  lastId: 234567891,
  total: 70
}
```

**Request 2**

```text
GET {Accuro API base url}/rest/v2/materials/appointments?uuid={Accuro uuid}&startDate={start}&endDate={end}&pageSize=50&startingId=234567891
```

```js
{
  ...,
  count: 20,
  lastId: 345678912,
  total: 20
}
```

Since the total (the amount of remaining items) is `20` and `pageSize` is `50` then all items and pages have been retrieved. The `lastId` returned here can be used in another request at a later time to retrieve any new or updated appointments.

**Request 3 - No more results**

```text
GET {Accuro API base url}/rest/v2/materials/appointments?uuid={Accuro uuid}&startDate={start}&endDate={end}&pageSize=50&startingId=345678912
```

```js
{
  ...,
  count: 0,
  lastId: 0,
  total: 0
}
```

**Request 4 - After some appointments get created/updated**

Updated and new appointments will be added to the end of the list. They can be retrieved by requesting the next page(s). Assuming 10 appointments were created or updated after last request.

```text
GET {Accuro API base url}/rest/v2/materials/appointments?uuid={Accuro uuid}&startDate={start}&endDate={end}&pageSize=50&startingId=345678912
```

```js
{
  ...,
  count: 10,
  lastId: 456789123,
  total: 10
}
```
