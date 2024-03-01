---
sidebar_position: 8
---

# Retrieve Patient Chart Items as PDF

As part of generating documentation to send to other providers (such as when referring a patient to a specialist), Accuro users are include items from a patient's chart. These can include:

- Medical history (diagnoses, medications etc.)
- Encounter notes
- Medical documents
- Lab results

These items are included in a PDF document which is called a _Generated Letter_.

At a high level, the workflow is:

1. Accuro user creates a _generated letter_ and attaches chart items
2. Accuro user launches CDS link to integrating application (that's your application!)
3. Using the Accuro context (current patient and user), retrieve the list of _generated letters_
4. User is shown list and selects the desired _generated letter_
5. Application retrieves _generated letter_ contents as a PDF

The steps to retrieve a _generated letter_ with the Accuro API is describe below.

## Listing available _Generated Letters_

Applications can retrieve the list of _generated letter_ metadata. To minimize the potential of applications retrieving the wrong _generated letter_ the list is filtered by two required filters: the patient and the user that created the letter.

The minimal request to retrieve the list of _generated letters_ is:

```js
GET /v1/provider-portal/generated-letters?patientId={id}&userId={id}
```
### Using a CDS Link to get `patientId` and `userId`

A CDS Link in Accuro allows an external web page to be loaded from Accuro and provides the option to include some Accuro context as part of the URL. When patient and user context is configured in a CDS link, applications will receive the context like so:

```js
URL Loaded: https://{url to web page or application}?userId={id}&patientId={id}
```
Applications can parse out the query parameters and use those values to construct the list _generated letters_ request.

### _Generated Letter_ Metadata

Fields that are helpful for users to identify the correct _generated letter_ are:

- `appointmentId` if the user knows what appointment the letter is attached to
- `title` which is a short description the user can enter into Accuro for reference later
- `status` _Generated letters_ with a `FILE_READY` status indicates that the PDF document is available and has not been processed before. Applications will be able to update this status to indicate the _generated letter_ has been retrieved and should not be shown.

**Example Generated Letter Metadata**

```js
{
  "id" : 11,
  "letterId" : 12,
  "letterVersion" : 2,
  "physicianId" : 11,
  "officeId" : 10,
  "userId" : 9,
  "targetId" : 11,
  "appointmentId" : 10,
  "patientId" : 11,
  "cc" : 0,
  "withCoverSheet" : true,
  "finalized" : true,
  "withAttachments" : true,
  "queue" : true,
  "status" : "FILE_READY",
  "targetType" : "physician",
  "title" : "A good title",
  "generateTo" : "Doctor David",
  "generatedTime" : "2024-06-27T00:00:00.000",
  "username" : "Doctor David",
  "extension" : "pdf"
}
```

## Get _Generated Letter_ PDF Content

Once a user has selected the intended _generated letter_ applications can call:

```js
GET /v1/provider-portal/generated-letters/{id}** with the ID of the chosen _generated letter_
```
**Example _Generated Letter_ Content**

```js
--uuid:2202b992-5d86-4162-acfc-52f1bd4c15bf
Content-Type: application/json
Content-Transfer-Encoding: binary
Content-ID: <0>

{"id":11,"letterId":12,"letterVersion":2,"physicianId":11,"officeId":10,"userId":9,"targetId":0,"appointmentId":10,"patientId":11,"cc":0,"withCoverSheet":false,"finalized":false,"withAttachments":false,"queue":true,"status":"FILE_READY","targetType":"OneTimeRecipient","title":"Clinical Note Title","generateTo":"Doctor David","generatedTime":"2024-06-27T00:00:00.000","username":"Doctor David","extension":"pdf"}
--uuid:2202b992-5d86-4162-acfc-52f1bd4c15bf
Content-Type: application/pdf
Content-Transfer-Encoding: binary
Content-ID: <1>

<PDF Binary Data>
```

The response is a `multipart` response and contains both the metadata and the PDF binary content of the _generated letter_.

Applications will likely be required to manually parse out the binary content of the PDf themselves.

### Tips for Parsing a Multipart Response

#### Extract boundary value

```js
The response will have _Content-Type: multipart/form-data; boundary={boundary value}_. Like the above example response, the boundary will be similar to: uuid:2202b992-5d86-4162-acfc-52f1bd4c15bf.
```
#### Extracting the binary content part

Using the boundary value contained in the Content-Type header, convert the response into an ASCII encoded string. Converting to ASCII allows to easily parse out the response and also determine the exact byte position that the binary content starts at.

Once the position of the binary content is known (and it's length):

- Get the raw bytes of the response
- Using the start position and length of the binary content, slice out the bytes of the PDF content from the response
- Use the binary content (e.g. convert to base64, pass to a renderer etc.)

**Javascript snippet that parses binary content and converts to base64**

```js
response.arrayBuffer().then(ab => {
  // Decode bytes to a single byte encoded format (ASCII)
  var text = new TextDecoder("ascii").decode(ab);

  // Split response into parts.
  // You should use the boundary value in the Content-Type header
  var parts = text.split(/--uuid:.*/gi);

  // ASSUMPTION for simplicity: binary content is at
  // position 2 of the parts array
  // Also have to parse out the metadata of the part
  var pdfString = parts[2].split("\r\n\r\n")[1].trim();

  // Find the start and end byte position of the pdf string
  var startByte = text.indexOf(pdfString);
  var endByte = startByte + pdfString.length;

  // Out of the original response bytes, slice only the binary PDF content
  var pdfArrayBuffer = ab.slice(startByte, endByte);

  // Convert bytes to base64 and print
  console.log(
    btoa(String.fromCharCode.apply(null, new Uint8Array(pdfArrayBuffer)))
  );
});
```

## Updating the _Generated Letter_ status

Applications can mark the _generated letter_ as `FILE_RETRIEVED`. This allows applications to ignore _generated letters_ they have already processed or have been processed by another application.

```js
POST /v1/provider-portal/generated-letters/{id}/status** with a body containing `FILE_RETRIEVED`.
```
