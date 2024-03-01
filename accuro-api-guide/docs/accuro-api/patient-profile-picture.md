---
sidebar_position: 10
---
# Managing Patient Profile Pictures

You are able to add and retrieve patient profile pictures via Accuro API. Your application will need the **patientId** prior to uploading patient profile pictures.

Any uploads to the patient profile picture via the API will overwrite any existing profile picture.

## Uploading a Picture

To add a profile picture to a patient you can use:

```text
POST {Accuro API base url}/rest/v1/provider-portal/patients/{patientId}/profile-picture?uuid={Accuro uuid}
```

The profile picture endpoint accepts `multipart/form-data` as the content of the request. It's recommended that applications use a library to construct the form data. The form data contains a single field named `image`. The value for the `image` field of the `multipart/form-data` must be the raw bytes of the image file.



**Note:** Accuro API converts all images to `PNG` images.

### Supported File Types

The supported file types are `JPEG` and `PNG`.

### Supported File Size

Only images that are less than **10 MB** will be accepted.

API requests with an image with a size greater than **10 MB** will return an error with a status code of `413 Request Entity Too Large`.

## Retrieving a Profile Picture

To retrieve a profile picture for a patient you can use:

```text
GET {Accuro API base url}/rest/v1/provider-portal/patients/{patientId}/profile-picture?uuid={Accuro uuid}
```

Accuro API returns the raw bytes of the image file with a content type of `application/octet-stream`.

As mentioned above, Accuro and the API converts all image files to `PNG`.

<a href="/guides/accuro-api/reference#/Patient_Endpoints/getProfilePicture">API Reference</a>

