# Integration tests for Coffeeshop

These tests assume you've deployed the coffeeshop demo from https://github.ibm.com/appsody-coffeeshop/gitops-dev

To build the tests locally:

```
docker build -t coffeeshop-integration .
```

To run the tests locally, you can start a Selenium host in a local docker container, then run the tests in a container:

```
docker run -d -p 4444:4444 -v /dev/shm:/dev/shm selenium/standalone-chrome:3.141.59-20200409
docker run -e COFFEESHOP_URI=http://my-coffeeshop-host coffeeshop-integration mvn -f /project/pom.xml verify
```
(replacing `my-coffeeshop-host` with the hostname of your coffeeshop service)

With the coffeeshop demo deployed on OpenShift you can find out the hostname by running:
```
oc get route coffeeshop-ui -n coffeeshop
```