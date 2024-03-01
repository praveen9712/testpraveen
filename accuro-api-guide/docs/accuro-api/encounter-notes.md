---
sidebar_position: 6
---
# Encounter Notes

Notes (letters) contain information, such as progress notes or summaries, related to an interaction with a patient (which may be an appointment, phone call, an interaction with a clinical tool etc). Accuro API provides the ability to create notes on a patient's chart and format this content using the Rich Text Format.

## Letter/Note Types

Accuro users can create different types of letters/notes. You should retrieve the list of available types configured in Accuro and present these to the user to select the appropriate type.

```js title="Collection: Letter Endpoints"
GET /v1/provider-portal/letters/types?uuid={Accuro uuid}
```

```js title="Response"
[
  {
    typeId: 1,
    typeName: "Clinical Note",
    abbreviation: "CN"
  }
];
```

## Creating a letter/note

After selecting the specific type for the letter, you can now create the letter/note.

```js title="Collection: Letter Endpoints"
PUT {Accuro API base url}/rest/v1/provider-portal/letters?uuid={Accuro uuid}
```
```js title="Request"
{
  typeId: 1, // Required
  patientId: 1, // Required
  providerId: 1, // Optional
  appointmentId: 1, // Optional - Used to associate this letter/note with an appointment
  title: "Summary from your Application", // Required
  content: "{\\rtf1\\ansi\\n{\\fonttbl\\f0\\fnil\\Monospaced;}\\n{\\colortbl\\red0\\green0\\blue0; \\red166\\green169\\blue0;\n\\red0\\green0\\blue170;}\\n\\b Your content here --\\~ \\n}\\n" // Required
}
```

### Plain Text

For simple notes that don't require any formatting, you are able to just provide a string to the `content` field like: `content: "The patient has 100% recovered!"`.

### Rich Text Format

If your application requires notes to be formatted and styled, you are able to provide a Rich Text Format file to the `content` field.

The [RTF Pocket Guide](https://www.oreilly.com/library/view/rtf-pocket-guide/9781449302047/ch01.html) is a good resource for understanding the basics of RTF syntax. Accuro and Accuro API do not guarantee that all syntax is supported. You should test to ensure that the RTF features you are using are supported. If things are not correctly formatted, it's likely that the particular keyword that is not rendering correctly is not supported.

Accuro utilizes [RTFEditorKit](https://docs.oracle.com/javase/8/docs/api/index.html?javax/swing/text/rtf/RTFEditorKit.html) to display letters/notes which does not support all RTF features like images and tables.

### Character Encoding

You should only send ASCII characters to ensure no characters become mangled.
