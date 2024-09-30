
# Hello
Thanks for the challenge and the time you will allocate to review it.  
Time spent: 2h

Important: I assumed that the application would run in a dockerized environment. The Docker daemon needs to run to execute the tests and run the docker container.

# Description
I took the most forward approach to solve the challenge, using a REST API:
- Accept a request with a long URL
  - Generate a random code and save it along the long URL, in a database
- Accept a request with the code, that returns the long URL

I introduced the concept of `TinyURL`, that represents the association between a generated code and the initial long URL.  
A `shortCode` is a String of 10 characters.

# Assumption
- I only return the generated code and not a full URL. I assumed that the full URL would be built client-side (appending the server name + generated code)
  - I could have saved the tiny URL directly in the database instead, if the API needed to return a fully built URL

# Installation
First, we need to build the project. Execute the following command:

```./gradlew clean build```

Then, let's build the Docker image

```docker build -t challenge-fit .```

Finally, once the image is ready, you can run the service using the provided docker-compose:

```docker compose up```

This will spin up the service and the database. Once ready, you should be able to interact with app.

# Endpoints
The service exposes 2 endpoints

## Create a tiny url
URL: `http://localhost:8080/url`
Verb: `POST`
Content-type: `application/json`
Description: Takes a long URL in the body and return a short code that can be used to retrieve the long URL later.
Payload:
```
{
	"initialURL": "https://you-long-url.com" 
}
```
Response:
```
{
	"shortCode": "q2dIyP096G" 
}
```

### Try it
```
curl --location 'http://localhost:8080/url' --header 'Content-Type: application/json' --data  '{"initialURL": "https://you-long-url.com"}'
```

## Retrieve the initial long URL

URL: `http://localhost:8080/{shortCode}`
Verb: `GET`
Content-type: `application/json`
Description: Accepts a shortCode in the path and return the long URL associated to it.
Response:
```
	"initialURL": "https://you-long-url.com" 
```

### Try it
```
curl --location 'http://localhost:8080/q2dIyP096G' --header 'Content-Type: application/json'
```

## Advanced configuration

### Change REST API port
By default, the REST API is exposed on the port `8080`. If you already use this port on your machine, you modify this using the following process:

1. Update `docker-compose.yml`
   In the `challenge` service, update the first port in the association.
   (e.g `8080:8080` -> `8081:8080`)

# Go further
Here are a few ideas to improve the solution, if I had to allocate more time

## Business idea
- Set an expiry date for the tiny URL. Currently they will live forever in the database. We might want then to expire after X days.
- Redirects directly to the initial URL.
  - Instead of returning the Initial URL, the `GET` endpoint could return an HTTP 3xx and redirect the User-Agent to the initial long URL.

## Technical
- If required, we could improve performance by caching some `TinyURL`. That would help to return the initial URL faster.
- We could load some already generated `shortCode` in memory, even before receiving the `POST` request. That would speed up the creation of `TinyURL`
- Currently, the system would fail in case of `shortCode` duplicate (as it's the primary key in the database). We would need to find a way to recover (maybe just retry based on the error)
- For a production-ready service we could:
  - Increase the monitoring. It would be great to generate some metrics to check the time taken to generate and retrieve URL.
  - Remove those static secrets from the configuration. They should be injected later, when running the Docker container (using env variables for example)
  - Better error formatting. Currently, we only return a String but there are better ways (e.g. RFC 7807)
  - ...
