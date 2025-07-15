#/bin/sh

curl -X POST \
  http://localhost:7200/rest/repositories \
  -H "Content-Type: multipart/form-data" \
  -F "config=@config.ttl"


jbang camel@apache/camel run load-data.yaml --camel-version=4.10.0 --dep=mvn:com.cefriel:camel-chimera-mapping-template:4.5.0
