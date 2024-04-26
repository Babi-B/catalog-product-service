# catalog-project-service-03
   _A spring boot version 2.5.5 app_
   

### @Babi-B and @LeleVaneck10 worked on this


## Some info to know
- The ***deploy branch*** of this repository has been deployed on heroku
- This app has been containerized(docker) 

## To run this app
- Prefered IDE: Eclipse version 2021-09 (4.21.0). Other IDEs (including intelliJ) might throw errors you'll have to resolve
- Download lombok in the eclipse (For help, use: https://stackoverflow.com/a/46520849/17065095)

- The catalog-product-service-03.sql file creates the database. The app automatically recreates the db everytime it is run.

### N.B!
   Donot change the application.properties file **spring.jpa.hibernate.ddl-auto=create** to **spring.jpa.hibernate.ddl-auto=update**. Doing that will interrupt the proper functioning of the pre-defined table(roles).
      

## APIs
### Public APIs 
  - /api/public/landing-page

### Private APIs
  - /api/**
  - /api/auth/**
 
### Test APIs
  To be used during testing
   - /api/test/**
   
 #### For complete info on the various APIs check the documentations below
 Run the app first to access these URIs
  - Swagger API Documentation -> http://localhost:8000/swagger-ui/index.html
  - API docs ->  http://localhost:8000/v3/api-docs
