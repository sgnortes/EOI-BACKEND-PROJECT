# EOI-BACKEND-PROJECT
This is the final Backend Project I did when I was enrolled in the FullStack EOI course.

## What did I do?
I created a REST service for a Marketplace using the Spring Framework (Java). 

## What I've learnt in this part of the course?
I have learnt tons of things, these include the following ones:
- How to build a local database
- Understand the different types of relations between tables (1:1, 1:N, N:N)
- Fundamental SQL queries and how to work with MySQL Workbench
- How to setup a Maven project
- How to setup a Springboot project
- Different ways of reading data from a database: JPA and JDBC
- How to structure a project using the MVC design pattern
- Develop a REST service
- Create an embedded database using H2
- Use swagger and postman to easy develop and test the REST service
- Implement basic security

## Functionalities
With this REST service you can do the following:
- Create, list and update the Users/Customers.
- Create, list by id, list by partially introduced name(product) and update the products the Marketplace sells.
- Create, list by id, list by partially introduced name(order), update and delete the orders.

## How to test it
1. Clone the repository
2. Run the REST service (you can use an IDE like Eclipse or use the command line)
3. Open a browser and type this: http://localhost:8080/swagger-ui.html . By doing so you will be able to easily test the calls to the REST service.
4. To see the changes you are making in the database you can open the h2 console. To do so, you should open a new tab in the browser and write the following: http://localhost:8080/h2-console. Once you open it, in order to have access, you must enter '1234' as the password. 
5. ¡Voilà! you are ready make calls and test crazy things.

## Technology used
1. Spring framework
2. Springboot
3. H2 as my embedded database
4. Swagger to test the service

