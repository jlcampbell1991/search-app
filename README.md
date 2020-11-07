# Search App
Sample full-stack web app with `http4s` backend and `ReactJS` with `TypeScript` front-end using DuckDuckGoApi.
## Backend http4s App
Scala app with `http4s` for HTTP routing and `Circe` for JSON serialization.
### Running locally
* [Install sbt](http://www.scala-sbt.org/1.0/docs/Setup.html)
* [Install postgresql](https://www.postgresql.org/download/)
* In root directory run:
  * `sbt`
  * `project search-app`
  * `run`
* Backend should now be running on `localhost:8080`
## Front-end React App
### Running locally
* In `/search-app/src/main/react/search-app` run `npm start`
* App should now be running on `localhost:3000`
