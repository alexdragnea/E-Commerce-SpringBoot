# E-Commerce - SpringBoot
Simple demo E-Commerce application with Spring boot.

# About
E-Commerce is a demo project which puts in practice what I learned during School-For-Java Bootcamp from Endava and on my own.This projects is based on Spring Boot and uses other spring modules like Spring Security.

For demonstration purpose I created a Rest Controller with diffrent endpoints like adding products/users from the body to retrieving lists of users, products based on the primary keys and more.

There are two types of users :
 - [USER] who can perform basic operations specific to an online shopping platform like registration/reseting password with confirmation on email for activating the account/ reseting the password, adding products to the cart, making orders, etc..
 - [ADMIN] who have full access, from adding products to managing users, orders.Once a order is made by a user, in the admin panel section the orders will be approved or declined manually by the ADMIN user.

 # In Memory Auth 
  - username : admin@admin.com
  - password : admin

# Technologies
- Spring Boot 2.4.3
- Spring Data Jpa
- Spring Security
- Java 11
- Maven 
- Thymeleaf Engine 
- Spring Security with two configuration for admin and user role
- Html & CSS 
- Bootstrap 5.0
- Hibernate 
- JUnit 5 & Mockito

# Features 
- Using Spring-boot-starter-mail for seding emails with unique tokens for account activation / reseting password, for order confirmations.
- Using Javax.validation API for defining constraints for user entity in the registration process.
- Using Lombok for the boilerplate code.
- Using Sl4j for loggin the process of adding the products.
- Using Multipart to upload only a single photo of a product.
- Using SonarLint for eliminating bad practices and most coding smells problems.
- Using MVC design pattern.
- Using JpaRepository for implementing pagination of the products.
- Using BCrypt for hasing the passwords.
- Using a query for a simple search functionality.

# Pictures
https://imgur.com/a/Aw9rOJz
