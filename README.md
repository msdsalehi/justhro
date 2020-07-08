# justhro
justhro.io

# Justhro - Unified microservice exception handling mechanism using Java, Spring Boot and Feign


# 1 Preface

Most microservice environments have practiced difficulties in inter service communication exception handling, mainly when communications are synchronized using restful APIs. 

Developers in a microservice environment usually find this as an annoying part of their tasks. As a developer, I prefer to stay focused on taking care of the business logic, corner cases and checking exceptional situations which may happen during compile or run time. We never find it annoying to catch an exception which is probable to be thrown, or on the other side we always throw relevant exceptions easily while implementing a service.

But the reason for these difficulties is not lacking technical solutions to the problem in our environment. It’s the lack of clarity and presence of different aspects of this specific area which a developer has to deal with. It is mainly the lack of a simple and clear operational guideline to producing and handling exceptions.

In this short document I try to represent a solution for those who use Java, Spring Boot and Feign to manage their synchronized restful inter service communications. I tried to hide some difficulties from the service developer point of view. I named the project as Justhro which stands for `_Just Throw_`.


# 2 Key components

Key components designed in this solution are:



1. Service (server business logic implementation, provided by service developer)
2. Abstract exception class (base exception class called JustAPIException, provided by Justhro)
3. Spring controller advice (error composer class called JustAdvice, provided by Justhro)
4. Service API library (jar file containing feign clients and business exception classes, provided by service developer)
5. Error decoder (error decoder class responsible to deserialize server’s response to relevant java exception called JustErrorDecoder, provided by Justhro)
6. Client (client business logic implementation, provided by client developer)

The only notable item may be item number four. You may be surprised but yes, I asked the service developer to provide a feign Interface containing service methods’ signatures. This is what I have been doing for years and has a lot of benefits in a microservice environment compared to the normal way of working with feign clients, where the client is responsible for creating the feign Interface. Something like EJB’s remote interfaces.


# 3 Facts

Following facts have been tried to be met by Justhro to make the whole process operational and simple.


## Developer of the service must not be confused



*   Exceptional conditions which are being reported to the service's client, must be done simply by throwing subtypes of JustAPIException class. JustAPIException is extending RuntimeException and therefore is unchecked.
*   If a new exception type is required in order to represent a new error case, no registration of these types has to be required in Justhro.
*   No exception has to leak from JustAdvice.
*   Exceptions have to be returned to the client in JSON format. JustAdvice is responsible for serializing them.


## Service API must be clear and self explanatory



*   Service API must contain feign client Interface for each published controller.
*   Feign Interface methods must use the “_throws_” keyword to represent their possible error conditions.
*   Exception types need to have “_httpStatus_”, “_code_”, “_path_”, “api_Message_”, “_causes_” and "_timestamp_" properties which have to be provided by JustAPIException.


## Developer of the client must not generate boilerplate code

JustErrorDecoder has to deserialize error responses which are in the form of JSON, to correct JustAPIException subtypes and throw them. So that client can use java “catch” expressions to handle them.


# 4 Implementation


## 4-1 Service side



1. Add following dependency to include JustAPIException and creation of JustAdvice component:
    
        <dependency>
            <groupId>io.justhro</groupId>
            <artifactId>service-spring-boot-starter</artifactId>
            <version>1.0.0</version>
        </dependency>
2. Create business exception classes and extend JustAPIException class. You will be forced to implement abstract methods to provide specific data about each error condition which the exception class is related to.
3. Throw your business exceptions where ever you like and let them be thrown from your controller tier.
4. Create SERVICE_NAME-api project which has “jar” as it’s packaging tag in it’s POM file. Put Feign clients, DTOs used by your service interfaces and also all business exceptions (which inherit JustAPIException) in the jar file. 
    Also you need to put the following dependency in SERVICE_NAME-api’s pom file:

        <dependency>
            <groupId>io.justhro</groupId>
            <artifactId>core</artifactId>
            <version>1.0.0</version>
        </dependency>
5. Add SERVICE_NAME-api to your main project’s POM file as a dependency to be able to throw these Exceptions inside your business logic. DTOs also will be available to the main project to be used by controllers and services.


## 4-2 Client side



1. Add following dependency to include JustException and creation of JustErrorDecoder component:
    
        <dependency>
            <groupId>io.justhro</groupId>
            <artifactId>client-spring-boot-starter</artifactId>
            <version>1.0.0</version>
        </dependency>

2. Add SERVICE_NAME-api dependency to your POM file as a dependency. This way you do not have to create the service’s feign client and will simply inject it to your code. Also this will include service’s specific exception classes so that you will be able to catch them in your code when you call feign methods. 

Just Decoder will automatically construct all the subtypes of JustAPIException available in the “service API” library at client service startup. This way it throws a specific exception type for each error code received from the server. So you won’t write any ErrorDecoder.


# 5 Advance topics
## 5-1 Internationalization
To enable localized messages for “apiMessage” property, put “errors.properties” message bundle in the “resources” folder of the service application. You may support different languages by adding more bundles having “_lang” postfix in file name. For instance, to support French, the bundle name will be “errors_fr.properties”.

## 5-2 Checked exceptions
Sometimes it is hard to find available exceptions which are possible to be thrown from a method, due to the large number of low level API invocations. Besides that, sometimes a developer decides to use checked exceptions in order to force the caller to handle the specific exceptional situation.

Fortunately there is another base Exception class namely JustAPICheckedException you can extend to achieve this.
