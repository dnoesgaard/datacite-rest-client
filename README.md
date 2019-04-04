# datacite-rest-client

This library provides a simple implmentation of a Java client for the REST Datacite API.
Initially it will provide the basic DOI operations described [here](https://support.datacite.org/reference/dois-2) to perform CRUD and query operations on DOIs.
 
# Dependencies
The implementation of this library will depend on the following commonly used libraries:
 - [Retrofit](https://square.github.io/retrofit/) and [OkHtpp](https://square.github.io/okhttp/) as HTTP clients.
 - [Jackson](https://github.com/FasterXML/jackson) for JSON data handling.
 - [SLF4J](https://www.slf4j.org/) as logging facade.
 - [error-prone](https://github.com/google/error-prone) as static analysis tool.
 