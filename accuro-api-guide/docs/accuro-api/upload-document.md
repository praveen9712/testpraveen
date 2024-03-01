---
sidebar_position: 13
---
# Upload a Document to Patient Chart

Accuro API provides a way to upload PDF documents to a patient's chart. Documents on a patient's chart are placed into folders and sub-folders. These folders and sub-folders can be retrieved by applications and their ID values used to place the document in the correct location (folder and sub-folder) on a patient's chart.

## Retieving folders

To retrieve folders, applications can **GET /v1/provider-portal/folders** which returns a list of folders and any sub-folders contained within each folder.

**Collection: Folder Endpoints**
<details>
  <summary><code>GET</code> <code><b>/v1/provider-portal/folders</b></code></summary>

##### Headers

> | name                        |  type     | data type                                    | description                      |
> |-----------------------------|-----------|----------------------------------------------|----------------------------------|
> | Content-Type                |  required | application/x-www-form-urlencoded            | none.                            |
> | Authorization               |  required | bearer eyJraWQiOiJyd...                      | none.                            |
> | X-QHR-Subscription-Key      |  required | 23456abcdfg456789abcds                       | none.                            |

##### Parameters

> | name              |  type     | data type      | description                         |
> |-------------------|-----------|----------------|-------------------------------------|
> | `uuid`            |  required | string         | none                                |

##### Responses

> | http code     | content-type                      | response                                                            |
> |---------------|-----------------------------------|---------------------------------------------------------------------|
> | `200`         | `application/json`                | `[ { "id": 2, "name": "Document", "subFolders": [ { "id": 1, "name": "Document" }, { "id": 2, "name": "Family History" } ] } ]`                               |
> | `401`         | `application/json`                | `{"code":"401","message":"Unauthorized"}`                           |

##### Example cURL

> ```javascript
> curl --location --globoff '{{host}}{{port}}{{servlet}}/rest/v1/provider-portal/folders/?uuid={{uuid}}' \ --header 'Authorization: bearer {{auth_provider}}' \ --header 'Accept: application/json' \ --header 'Content-Type: application/json' \ --header 'X-QHR-Subscription-Key: {{Subscription-Key}}'
> ```
</details>

```js title="Response"
{
  "id": 2,
  "name": "Labs",
  "subFolders": [{
    "id": 2,
    "name" : "Reports"
  }, {
    "id": 3,
    "name": "Full Results"
  }]
}
```

## Creating folders

Applications may want to create folders that are specific to the application. This may help users identify documents that have been uploaded by your application. Accuro users always have the option to assign the document to another folder.

To create a folder **POST /v1/provider-portal/folders** with:

**Collection: Folder Endpoints**
<details>
  <summary><code>POST</code> <code><b>/v1/provider-portal/folders</b></code></summary>

##### Headers

> | name                        |  type     | data type                                    | description                      |
> |-----------------------------|-----------|----------------------------------------------|----------------------------------|
> | Content-Type                |  required | application/x-www-form-urlencoded            | none.                            |
> | Authorization               |  required | bearer eyJraWQiOiJyd...                      | none.                            |
> | X-QHR-Subscription-Key      |  required | 23456abcdfg456789abcds                       | none.                            |

##### Parameters

> | name              |  type     | data type      | description                         |
> |-------------------|-----------|----------------|-------------------------------------|
> | `uuid`            |  required | string         | none                                |

##### Responses

> | http code     | content-type                      | response                                                            |
> |---------------|-----------------------------------|---------------------------------------------------------------------|
> | `200`         | `application/json`                | `1`                                                                 |
> | `401`         | `application/json`                | `{"code":"401","message":"Unauthorized"}`                           |
> | `404`         | `application/json`                | `{"code":"404","message":"Resource not found"}`                           |

##### Example cURL

> ```javascript
> ccurl --location --globoff '{{host}}{{port}}{{servlet}}/rest/v1/provider-portal/folders?uuid={{uuid}}' --header 'Authorization: bearer {{auth_provider}}' --header 'Accept: application/json' --header 'Content-Type: application/json' --header 'X-QHR-Subscription-Key: {{Subscription-Key}}' --data '{ "name": "Pediatricstest", "subFolders": [ { "id": 2611, "name": "hemogram test" } ] }'
> ```
</details>

```js title="Request"
{
  "name": "new folder",
  "subFolders": [
      {
        "name": "new subfolder1"
      },
      {
        "name": "new subfolder2"
      }
  ]
}
```
:::tip
The folder can be created with below mentioned scenarios:
- To create the folder without any sub-folders to link with: only folder name field is needed and rest of the fields must be null.
- To create the folder with linked sub-folders: along with folder name, desired sub-folder must be provided to the request.
- To link the new sub-folder, provide only sub-folder name.This will create the sub-folder and link it to the folder created.
- To link the existing sub-folder, provide the sub-folder with both id and name. This will link to the created folder.
:::

## Uploading the document

Now that folders and sub-folders can be retrieved, the upload document request can be built.

The upload document request accepts `multipart/form-data` as the content of the request. It's recommended that applications use a library to construct the form data. The form data contains 2 fields: `details` and the `document`.

### `details`

The `details` field is populated with a stringified JSON object containing metadata about the document like filename, patient, provider etc.

```js title="Request"
{
  "file_name": "test.pdf",
  "folder_id": 3,
  "sub_folder_id": 3, // Optional and Accuro may not have sub-folders enabled
  "patient_id": 3,
  "description": "A helpful description",
  "document_date": 2019-01-31, // Date document is stamped with - Optional defaults to current time
  "received_date": 2019-02-31, // Date document was received by Accuro - Optional defaults to current time
  "date_created": 2019-03-31, // Date document was created - Optional defaults to current time
  "reviews": [
    {
      "physician_id": 3, // A valid provider ID. A list of providers can be retrieved via the /v1/providers endpoint
      "reviewDate": 2019-01-31 // When this exists, this marks the document as 'reviewed' and will not appear in the providers inbox
    }
  ] // Optional - if omitted document will appear as unassigned
}
```

#### **Example: Setting the document as 'Unassigned'**

The following `details` object is an example of how to make the document get marked as 'Unassigned' in Accuro's Home > Document section.

```js title="Request"
{
  "file_name": "test.pdf",
  "folder_id": 3,
  "sub_folder_id": 3,
  "patient_id": 3,
  "description": "A helpful description",
  "documentDate": 2019-01-31,
  "receivedDate": 2019-02-31,
  "dateCreated": 2019-03-31,
  "reviews": null
  ]
}
```

#### **Example: Sending the document to a provider to review**

The following `details` object is an example of how to send the document for a provider to review.

```js title="Request"
{
  "file_name": "test.pdf",
  "folder_id": 3,
  "sub_folder_id": 3,
  "patient_id": 3,
  "description": "A helpful description",
  "documentDate": 2019-01-31,
  "receivedDate": 2019-02-31,
  "dateCreated": 2019-03-31,
  "reviews": [
    {
      "physician_id": 3
    },
    {
      "physician_id": 4
    }
  ]
}
```

If multiple review items are included then the document will be sent to each provider specified in the request.

#### **Example: Marking the document as 'reviewed'**

The following `details` object is an example of how to mark the document as 'reviewed'. The document will not appear in the provider's inbox.

```js title="Request"
{
  "file_name": "test.pdf",
  "folder_id": 3,
  "sub_folder_id": 3,
  "patient_id": 3,
  "description": "A helpful description",
  "documentDate": 2019-01-31,
  "receivedDate": 2019-02-31,
  "dateCreated": 2019-03-31,
  "reviews": [
    {
      "physician_id": 3,
      "reviewDate": 2019-01-31
    }
  ]
}
```

### `document`

This field in the form data should contain the raw bytes of the document file that the application is uploading to the patient's chart.


**Collection: Document Endpoints**
<details>
 <summary><code>POST</code> <code><b>/v1/provider-portal/documents</b></code></summary>

##### Headers

> | name                        |  type     | data type                                    | description                      |
> |-----------------------------|-----------|----------------------------------------------|----------------------------------|
> | Content-Type                |  required | multipart/form-data                          | none.                            |
> | Authorization               |  required | bearer eyJraWQiOiJyd...                      | none.                            |
> | X-QHR-Subscription-Key      |  required | 23456abcdfg456789abcds                       | none.                            |

##### Parameters

> | name              |  type     | data type      | description                         |
> |-------------------|-----------|----------------|-------------------------------------|
> | `uuid`            |  required | string         | none                                |

##### Responses

> | http code     | content-type                      | response                                                            |
> |---------------|-----------------------------------|---------------------------------------------------------------------|
> | `200`         | `application/json`                | `[0101]`                                                            |
> | `401`         | `application/json`                | `{"code":"401","message":"Unauthorized"}`                           |
> | `403`         | `application/json`                | `{"code":"401","message":"document uploaded is more than 20MB"}`    |

##### Example cURL

> ```javascript
> curl --location --globoff '{{host}}{{port}}{{servlet}}/rest/v1/provider-portal/documents?uuid={{uuid}}' --header 'Authorization: bearer {{auth_provider}}' --header 'Accept: application/json' --header 'Content-Type: multipart/form-data' --header 'X-QHR-Subscription-Key: {{Subscription-Key}}' --form 'details="{ \"file_name\" : \"Test.PDF\", \"folder_id\" : 1, \"priority\" : \"3\", \"patient_id\" : 123, \"description\" : \"This is description again feb 19-3\", \"received_date\" : \"2019-01-4\", \"date_created\" : \"2023-08-08\", \"from_id\" : -1, \"sub_folder_id\" : 1, \"from_name\" : \"Dr David\", \"from_type\" : \"OneTimeRecipient\", \"reviews\" : [ { \"physician_id\" : 12345, \"reviewDate\" : \"2023-2-15\" } ] }"' \ --form 'document=@"/C:/yourpath/Desktop/TEST.pdf"'
> ```
</details>

```js title="Request"
{
  "file_name" : "Test.PDF",
  "folder_id" : 1,
  "priority" : "3",
  "patient_id" : 123,
  "description" : "This is description again feb 19-3",
  "received_date" : "2019-01-4",
  "date_created" : "2023-08-08",
  "from_id" : -1,
  "sub_folder_id" : 1,
  "from_name" : "Dr David",
  "from_type" : "OneTimeRecipient",
  "reviews" : [
			{
  	       "physician_id" :  1234,
               "reviewDate" :  "2023-2-15"
			}
	]
 
  }
```

