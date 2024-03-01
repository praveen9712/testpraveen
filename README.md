# Accuro REST API

Accuro REST API aims to provide a [RESTFul](https://en.wikipedia.org/wiki/Representational_state_transfer) interface through
which third party partners can integrate with Accuro clients.

## Development Set Up

* Clone this repository: `git clone git@github.com:qhrtech/accuro-api`
* `cd accuro-api/src/main/resources`
* `cp jdbc.properties.example jdbc.properties`
* Configure `jdbc.properties` to point to an Accuro Database that is 2017.01 or higher
* `cp config.properties.example config.properties`
* Configure `config.properties` to point to an appropriate Medeo environment with the right id, secret and keys
* Build and Run the project (Requires JDK 1.8).

For more information on Development Set Up see the [Confluence Page](https://confluence.qhrtech.com/display/Exchange/Development+Information).

## ACCAPI-Documentation Postman Collection

The ACCAPI-Documentation collection can be imported into your postman UI and used to test the ACCAPI endpoints. 
For more information visit the [Accuro API Postman Collection Documents](https://confluence.qhrtech.com/display/Exchange/Testing+With+Postman).

## Swagger documentation for Accuro API

* Installation

cd openapi-ui

npm install
Run swagger:resolve or compile -P swagger maven task to ensure that openapi.json exists in target/openapi/.

* Building
npm run build

* Running
Open index.html in a browser

## More Information

For more information visit the [Accuro API Confluence Page](https://confluence.qhrtech.com/display/Exchange/Accuro+API).

