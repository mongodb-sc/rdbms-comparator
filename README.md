# RDBMS Comparator

RDBMS Comparator is a simple app based on the concept of a logistics company that has customers and orders. 
The app uses a Spring-Boot backend to connect to both a MongoDB and Postgres DB. 
Using an Angular CLI created web app (hosted inside the spring-boot app) the user can show the approach to query different 
sets of records from each of the different backend sources using a toggle switch in the navbar

## Concept

Create a side-by-side demo that leverages both MongoDB and Postgres to help show the benefits of MongoDB for workloads that traditionally
would gone to RDBMS systems. 

### Dependencies

- Docker
- Docker Compose
- Postgres (via Oocker)
- Atlas (via Atlas CLI & Docker)

If you want to run the app locally you will need the following

- Java 23
- npm
- [Angular CLI](https://angular.dev/tools/cli)
- Postgres
- Atlas cluster
- Python

This guide does not cover setup and installation of all of those tools. 

## Docker Setup


### Build Docker Image
Once you have cloned the repository, build the docker image. 
``` 
docker build -t jesmith17/rdbms_comparator .
```

Start up docker images. 
```
docker-compose up
```


### Load Data

Long term this will be put into the docker build as well, but for now you need to load data into the cluster after its up and running. The load scripts simply cally 
the API endpoints to create objects. This ensures that both database have identical data. 

```
cd data
python -m venv venv
source ./venv/bin/activate

python data_loader.py

```

The script assumes that your application is running at localhost:8080/api. If that URL is changed then you will need to update the
python script to point to the correct place.

Data loading can take 10-20 minutes due to the number of records that it's attempting to load. You should be able to access the app during this data loading process.


### Create mongo indexes

There are mongo indexes defined in the Spring-data objects, but spring-data doesn't natively create them. To setup the indexes in Mongo, including the 
search index you need to run a command. 

```
mongosh -f init_mongo.js
```

If this comes back with no output, then you should be good. 


### Validate

To double check that things are working correct you can connect directly to the Atlas CLI DB to validate it's setup. 
In my experience the external connection requires this configuration

```
mongosh "mongodb://yoda:starwars@localhost:27018/rdbms?directConnection=true&authSource=admin"
```







