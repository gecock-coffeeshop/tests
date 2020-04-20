# Integration tests for Coffeeshop

These tests assume you've deployed the coffeeshop demo from https://github.ibm.com/appsody-coffeeshop/gitops-dev

To build & run the tests in a local docker container:

```
docker build -t coffeeshop-integration .
docker run -e COFFEESHOP_URI=http://my-coffeeshop-host coffeeshop-integration mvn -f /project/pom.xml verify
```
(replacing `my-coffeeshop-host` with the hostname of your coffeeshop service)

With the coffeeshop demo deployed on OpenShift you can find out the hostname by running:
```
oc get route coffeshop-ui
```