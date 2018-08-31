# cinema
1. Description of solution
All api methods mentioned in task description were created.

Application was created using spring framework and scala language.
H2 and slick where used as database and as data access layer

Application code was separated into few packages:
api - data models for public api, public api versions descriptions
applications - public endpoints separated into different versions of applications
domain - application logic divided into movie domain and review domain
infrastructure - implementations of database repositories, application configurations

All data for particular page is returned in one http request. This solutions for mobile application could
reduce time of getting data and optimalize its size. If api would be created for "desktop users" I would consider
to separate api into few request. It could cause better domain separation and give possibility to render parts
of page independently. There is also possible to implement graphQL api to give all of described advantages.

Problem of A/B tasting was resolved by preparing endpoints in different variants. Variant choosing is done by
sending different version in content type header. Both versions of movie page use as much common logic as possible.
Differences are applied while building final response model in endpoint so there was no necessity to implement
few versions of application domain logic.

2. How to run application
- checkout code from
- got to project directory "cd cinema"
- copy create database file to home directory "cp src/main/resources/create.sql ~/"
- run application "./gradlew run"

how to get home page data:
curl 'http://localhost:8080/main?reviews.limit=3&movies.limit=4' -H "Content-Type: application/vnd.puffin.public.v1+json"

how to get category page data:
curl 'http://localhost:8080/categories/DRAMA' -H "Content-Type: application/vnd.puffin.public.v1+json"

how to get move page data in first variant:
curl 'http://localhost:8080/movies/1' -H "Content-Type: application/vnd.puffin.public.v1+json"

how to get move page data in second varinat:
curl 'http://localhost:8080/movies/1' -H "Content-Type: application/vnd.puffin.public.v2+json"

how to put new review to service:
curl -X POST 'http://localhost:8080/reviews' -H "Content-Type: application/vnd.puffin.public.v1+json" -d '{"id":"1","movieId":"1","rate":3,"description":"rating-body"}'

3. Future things worth to do in next iterations
- use flyway or similar library to initialize database
- create error handler to response with custom errors and proper error codes
- add more data validation - for example to not let put reviews with ratings less then 0 or greater then 5
- add integration tests (now only unit tests for methods containing application logic are written)
- optimalize getting data from database