### Summary

 This is a library management system web application build with spring boot and deployed on AWS

* EC2 instances are built on a custom [AMI](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/AMIs.html) using [packer](https://packer.io/)
* Setting up the network and creation of resources is automated with Cloud formation, aws cli and shell scripts
* Instances are autoscaled with [ELB](https://aws.amazon.com/elasticloadbalancing/) to handle the web traffic
* Created a [serverless](https://aws.amazon.com/lambda/) application to facilitate the password reset functionality using [SES](https://aws.amazon.com/ses/) and [SNS](https://aws.amazon.com/sns/)
* The application is deployed with Circle CI and AWS Code Deploy

### Architecture Diagram

![alt text](https://s3.amazonaws.com/github.akashkatakam.com/aws_full.png "AWS Architecture diagram")

## Tools and Technologies

| | |
|--- | :---|
| Infrastructure | VPC, ELB, EC2, Route53, Cloud formation, Shell, Packer|
| Webapp | Java, Spring Boot, MySQL, Maven |
| CI/CD  | Circle CI, AWS Code Deploy |
| Alerting and logging| statsd, Cloud Watch, SNS, SES, Lambda |
| Security| WAF |

## Infrastructure-setup

* Create the networking setup using cloud formation and aws cli 
* Create the required IAM policies and users
* Setup Load Balancers, Route53, DynamoDB, SNS, SES, RDS, WAF

## CI/CD

* Created a webhook from github to CircleCI
* Bootstrapped the docker container in CircleCI to run the unit tests, integration tests and generate the artifact
* The artifact generated is stored S3 bucket and deployed to an autoscaling group.
![alt text](https://s3.amazonaws.com/github.akashkatakam.com/ci-cd.png "CI/CD")

## Auto scaling groups

* Created auto scaling groups to scale to the application to handle the webtraffic and keep the costs low when traffic is low
* Created cloud watch alarms to scale up and scale down the EC2 instances

## Serverless computing

* Created a pub/sub system with SNS and lambda function
* When the user request for a password reset a message is published to the SNS topic.
* The lambda function checks for the entry of the email in DynamoDB if it has no entry then it inserts a record with a TTL of 15 minutes and sends the notification to the user with SES
![alt text](https://s3.amazonaws.com/github.akashkatakam.com/lambda.png "Serverless computing")

## [Packer](https://packer.io/)

* Implemented CI to build out an AMI and share it between organization on AWS
* Created provisioners and bootstrapped the EC2 instance with required tools like Tomcat, JAVA, Python

## Team Information

| Name | NEU ID | Email Address |
| --- | --- | --- |
| Aditi Jalkote| 001404314 | jalkote.a@husky.neu.edu |
| Akash Katakam| 001400025 | katakam.a@husky.neu.edu |
| Tanmayee Kalluri| 001400957 | kalluri.t@husky.neu.edu |
