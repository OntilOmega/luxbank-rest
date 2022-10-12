LuxBank REST Api v 1.0.0
====

### All main requirements have been done

### All optional requirements have been done

Application is up and running on Internet in AWS EC2 instance on address:


Scripts to initialize the database are in the project resources folder and run automatically by Spring boot when project starts.
 - [schema.sql](src/main/resources/schema.sql)
 - [data.sql](src/main/resources/data.sql)

### Prerequirements

1. Java
2. Git
3. Docker

### How to run the project
1. Go to folder where you want to clone the project

2. Clone the project from GitHub:
```
git clone https://github.com/OntilOmega/luxbank-rest.git 
```
3. Enter the project directory:
```
cd luxbank-rest
```
4. Run the script (git pull, maven package, docker compose):
```
./run.sh
```
