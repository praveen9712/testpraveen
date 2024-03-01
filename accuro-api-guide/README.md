# Accuro API Documentation project

This project will manage the documentation made for Accuro-API which is exposed in the Developer Potal, as a guide for the different integrators.
This web site is built using [Docusaurus](https://docusaurus.io/), a modern static web site generator.

## Installation

### Scaffold project website
The easiest way to install Docusaurus is to use the command line tool that helps you scaffold a skeleton Docusaurus website. You can run this command anywhere in a new empty repository or within an existing repository, it will create a new directory containing the scaffolded files. The typescript version of this project is recommended for better compatibility with the Azure Developer Portal.

```
npx create-docusaurus@latest my-website classic --typescript
```

### Running the development server
To preview your changes as you edit the files, you can run a local development server that will serve your website and reflect the latest changes.

```
cd my-website
npm run start
```
and contents will be generated within the /build directory, which can be copied to any static file hosting service like GitHub pages, Vercel or Netlify. Check out the docs on deployment for more details.

#### Build

Docusaurus is a modern static website generator so we need to build the website into a directory of static contents and put it on a web server so that it can be viewed. To build the website:

```
cd my-website
npm run start
```


## Updating project version

package.json
```js
{
  "dependencies": {
    "@docusaurus/core": "3.1.1",
    "@docusaurus/preset-classic": "3.1.1",
    // ...
  }
}
```
Then, in the directory containing package.json, run your package manager's install command:
```
npm install
```

To check that the update occurred successfully, run:
```
npx docusaurus --version

```

## Search bar local

Offline / local search for Docusaurus **v2+** that works behind your firewall.

Feature Highlights:

- Supports multiple documentation versions
- Supports documentation written in languages other than English
- Highlights search results
- Customized parsers for docs, blogs, and general pages
- Lazy-loads the index

 
```
npm install @cmfcmf/docusaurus-search-local

```

## Usage

Add this plugin to the `plugins` array in `docusaurus.config.js`.

```js
module.exports = {
  // ...
  plugins: [require.resolve("@cmfcmf/docusaurus-search-local")],

  // or, if you want to specify options:

  // ...
  plugins: [
    [
      require.resolve("@cmfcmf/docusaurus-search-local"),
      {
        // Options here
      },
    ],
  ],
};
```

The following options are available (defaults are shown below):

```js
{
  // whether to index docs pages
  indexDocs: true,

  // Whether to also index the titles of the parent categories in the sidebar of a doc page.
  // 0 disables this feature.
  // 1 indexes the direct parent category in the sidebar of a doc page
  // 2 indexes up to two nested parent categories of a doc page
  // 3...
  //
  // Do _not_ use Infinity, the value must be a JSON-serializable integer.
  indexDocSidebarParentCategories: 0,

  // whether to index blog pages
  indexBlog: true,

  // whether to index static pages
  // /404.html is never indexed
  indexPages: false,

  // language of your documentation, see next section
  language: "en",

  // setting this to "none" will prevent the default CSS to be included. The default CSS
  // comes from autocomplete-theme-classic, which you can read more about here:
  // https://www.algolia.com/doc/ui-libraries/autocomplete/api-reference/autocomplete-theme-classic/
  // When you want to overwrite CSS variables defined by the default theme, make sure to suffix your
  // overwrites with `!important`, because they might otherwise not be applied as expected. See the
  // following comment for more information: https://github.com/cmfcmf/docusaurus-search-local/issues/107#issuecomment-1119831938.
  style: undefined,

  // The maximum number of search results shown to the user. This does _not_ affect performance of
  // searches, but simply does not display additional search results that have been found.
  maxSearchResults: 8,

  // lunr.js-specific settings
  lunr: {
    // When indexing your documents, their content is split into "tokens".
    // Text entered into the search box is also tokenized.
    // This setting configures the separator used to determine where to split the text into tokens.
    // By default, it splits the text at whitespace and dashes.
    //
    // Note: Does not work for "ja" and "th" languages, since these use a different tokenizer.
    tokenizerSeparator: /[\s\-]+/,
    // https://lunrjs.com/guides/customising.html#similarity-tuning
    //
    // This parameter controls the importance given to the length of a document and its fields. This
    // value must be between 0 and 1, and by default it has a value of 0.75. Reducing this value
    // reduces the effect of different length documents on a term’s importance to that document.
    b: 0.75,
    // This controls how quickly the boost given by a common word reaches saturation. Increasing it
    // will slow down the rate of saturation and lower values result in quicker saturation. The
    // default value is 1.2. If the collection of documents being indexed have high occurrences
    // of words that are not covered by a stop word filter, these words can quickly dominate any
    // similarity calculation. In these cases, this value can be reduced to get more balanced results.
    k1: 1.2,
    // By default, we rank pages where the search term appears in the title higher than pages where
    // the search term appears in just the text. This is done by "boosting" title matches with a
    // higher value than content matches. The concrete boosting behavior can be controlled by changing
    // the following settings.
    titleBoost: 5,
    contentBoost: 1,
    tagsBoost: 3,
    parentCategoriesBoost: 2, // Only used when indexDocSidebarParentCategories > 0
  }
}
```

You can now use the search bar to search your documentation.

**Important: Search only works for the statically built documentation (i.e., after you ran `npm run accuro-api-guide build` in your documentation folder).**

**Search does **not** work in development (i.e., when running `npm run accuro-api-guide start`).**
If you want to test search locally, first build the documentation with `npm run accuro-api-guide build`, and then serve it via `npm run accuro-api-guide serve`.


## Diagrams

Allows the creation and rendering of the diagrams shown in the documentation. [Mermaid](https://mermaid.js.org/syntax/quadrantChart.html),

### Installation

```
npm install --save @docusaurus/theme-mermaid

```

Enable Mermaid functionality by adding plugin @docusaurus/theme-mermaid and setting markdown.mermaid to true in your docusaurus.config.js.

```js
export default {
  markdown: {
    mermaid: true,
  },
  themes: ['@docusaurus/theme-mermaid'],
};

```

### Usage

Add a code block with language mermaid:

```
graph TD;
    A-->B;
    A-->C;
    B-->D;
    C-->D;
```

```mermaid
graph TD;
    A-->B;
    A-->C;
    B-->D;
    C-->D;
```


