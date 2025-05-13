---
sidebar_position: 3
---



# Local Setup

The Mongo Logistics demo is available to any one within MongoDB at [Mongo Logistics](https://rdbms-comparator.sa-demo.staging.corp.mongodb.com/)
but the tool also has the ability to demo locally if needed. This setup can take some due to the data creation steps. If you need to demo locally, or want to make changes and demo those, follow the instructions below. 

## Dependencies

- Docker
- Docker Compose
- Postgres (via Docker)
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

The script inserts records into both databases. It establishes connections to the DB using the public ports defined in the docker compose file.
If you adjust the docker compose file then you will need to also adjust this script.

:::note
docker compose is set to move the default ports for postgres and mongo to ensure that they don't conflict with instances you might
already have locally
- Postgres is moved to 5433
- Mongo is moved to 27018
  :::

:::important
The data loading process can take a few minutes. Customers, Products, and Stores all load pretty fast (10K of each being created)
Orders however is creating 500K by default, so it may take 5-10 mimutes.
:::


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

## Additional Features

* Ability to display count of SQL statements need to return results from both Postgres and MongoDB
* 
