# Integration tests for Coffeeshop

To build & run the tests in a local docker container:

```
docker build -t coffeeshop-integration .
docker run -e COFFEESHOP_URI=http://my-coffeeshop-host coffeeshop-integration mvn verify
```
(replacing `my-coffeeshop-host` with the hostname of your coffeeshop service)
