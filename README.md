# play-sagas

This is a Play application that uses Java, and communicates with an in memory database in dev environment using EBean.

The app has implemented basic user management with token authentication, user device registation and uploading assets handling.

## Play Framework

Play documentation is here:

[https://playframework.com/documentation/latest/Home](https://playframework.com/documentation/latest/Home)

## EBean

EBean is a Java ORM library that uses SQL:

[https://www.playframework.com/documentation/latest/JavaEbean](https://www.playframework.com/documentation/latest/JavaEbean)

and the documentation can be found here:

[https://ebean-orm.github.io/](https://ebean-orm.github.io/)

## S3 Usage
We use AWS S3 storage service in order to get cheap file persistence. It's necessary to create an S3 bucket for the project

The credentials of the aws account will be in DefaultAWSCredentials and the bucket in S3Adapter

## SQSUsage
If it's necessary some kind of redis, you'll find integration with AWS SQS Service, that provides fast and reliable queue service (SQSAdapter).

## SNSUsage
We use AWS SNS service to send the verification SMS, so it's necessary to have the apropiate permisions in the AWS account to use it

## Lombok
We use lombok to automatically generate getters and setters, so you'll probably have to configure it in the IDE in order to work


- InteliJ
    
    Before run tests change  the global jUnit configuration: VM options set with "-ea -javaagent:ebean-agent-11.9.1.jar"
     