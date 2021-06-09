## Micronaut Hasher

A simple demo application to showcase some features Micronaut provides.

So far the scope of this pet project is to show off:
- caching (using Caffeine integration)
- kubernetes deployment (including health probes)
- openapi integration

This list might of course grow over time, but given that this project has no tangible real-world use  
it is only to be considered as a reference

## Usage
Build and launch:
```shell
./gradlew run
```
Open in browser:  
http://localhost:8080/swagger-ui/

Swagger-UI should help you get an overview of the available endpoints quickly.

### Testing cache functionality

The /cache/sha512/ endpoint uses the caffeine cache and will pull hashes for messages it has seen before from there.  

We can verify this by running `curl` against the endpoint once:   
```shell
curl localhost:8080/cache/sha512/yourmessagestringhere -w " connect:%{time_connect} total:%{time_total}"
```
Which will give us the response plus some info about connect time and total time for the request like this:
```shell
{
  "algorithm":"SHA512",
  "hash":"<truncated>"
} connect:0.001002 total:0.008505
```
So the request took 8.5ms to be served, including a negligible 1ms connect time.

Running the same command again will now draw from the cache instead of recomputing the hash,  
and the result should look similar to this:
```shell
{
  "algorithm":"SHA512",
  "hash":"<truncated>"
} connect:0.000905 total:0.003337
```

Granted this is a contrived example, as the endpoint that doesn't query the cache also serves requests in less than 4ms on my machine.  

Caching really only makes sense on operations that take 10s or even 100s of ms where the overhead of the cache lookup is small compared to the operation.  

## Micronaut Docs

Leaving these here as they can be useful to get up and running

### Micronaut 2.5.5 Documentation

- [User Guide](https://docs.micronaut.io/2.5.5/guide/index.html)
- [API Reference](https://docs.micronaut.io/2.5.5/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/2.5.5/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---

### Feature management documentation

- [Micronaut Management documentation](https://docs.micronaut.io/latest/guide/index.html#management)

### Feature kubernetes documentation

- [Micronaut Kubernetes Support documentation](https://micronaut-projects.github.io/micronaut-kubernetes/latest/guide/index.html)

- [https://kubernetes.io/docs/home/](https://kubernetes.io/docs/home/)

### Feature cache-caffeine documentation

- [Micronaut Caffeine Cache documentation](https://micronaut-projects.github.io/micronaut-cache/latest/guide/index.html)

- [https://github.com/ben-manes/caffeine](https://github.com/ben-manes/caffeine)

### Feature openapi documentation

- [Micronaut OpenAPI Support documentation](https://micronaut-projects.github.io/micronaut-openapi/latest/guide/index.html)

- [https://www.openapis.org](https://www.openapis.org)
