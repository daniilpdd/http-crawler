## Simple http crawler written in Scala using Zio

Service has 2 endpoints:
- POST /api/urls. Request body contains a list of URLs separated by line breaks
- GET /api/urls. URLs are passed in the query parameter with the name 'urls' separated by commas

### Building docker image 
`sbt docker:publishLocal`

### Run docker image 
`docker run -p 8081:8080 http-crawler:0.1`
