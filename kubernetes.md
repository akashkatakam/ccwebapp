---
layout: default
---

## Kubernetes ( CSYE 7374- Advanced Cloud computing )

### Summary

Simulation on production grade kubernetes deployment on AWS

* Backend application is built on java using spring boot framework
* Created a minimal frontend application with react
* Containerized the applications with docker and stored them in private repository
* Setup the jenkins infrastructure on a custom domain using ansible 
* Created webhooks to Jenkins to create,tagged and pushed docker images on each commit 
* Created a kubernetes server in HA mode and deployed the microservices
* Service discovery for frontend and backend
* Created helm charts to deploy the application 

### [Infrastructure](#csye7374Infra)

#### [Jenkins setup](#JenkinsSetup)

* Created [ansible](https://docs.ansible.com/ansible/latest/index.html) playbooks to create infrastructure
* Hosted [jenkins](https://jenkins.io/doc/) on a custom domain `jenkins.username.com`
* The ansible playbooks launches the EC2 instances and bootstraps the instance to start a secured jenkins server
* Secured the jenkins server by enabling SSL and periodically updating the [let's encrypt](https://letsencrypt.org/) certificates 
* Configured a custom custom domain in Route53 and updating the Route53 A record with the launched EC2 instances public dns
* Clone the repo 
* Execute `ansible-playbook -v -i hosts launchInstance.yml --extra-vars "@extra_vars.json"  --ask-vault-pass`
```
{
    "profile":"", 
    "domain_name":"",
    "key_name": "",
    "email_id" : ""
}
```
|Key| Description|
|---|---|
|profile| AWS Profile to create resources
|domain_name| `<YOUR_DOMAIN_NAME>.TLD`
|key_name| Key pair for EC2 instance
|email_id| To get Let's encrypt certificates

#### [Clusters](#clusters)

* Created ansible play-book to launch the cluster in Dev and Prod environments
* Playbooks handle the number of nodes and sizes of compute and master nodes depending on the type of environment
* Created kubernetes clustes in HA setup in 3 different availability zones picking the zones automatically in production setup
* Used ansible [k8s](https://docs.ansible.com/ansible/latest/modules/k8s_module.html) module to deploy the application on the clusters

### [Microservices](#Microservices)

#### [Backend](#backend)

* The Recipe Management Web application is developed using Java Spring Boot framework that uses the REST architecture
* Secured the application with  [Spring Security](https://spring.io/projects/spring-security) Basic [authentication](https://developer.mozilla.org/en-US/docs/Web/HTTP/Authentication) to retrieve user information
* Storing the images of recipes in S3
* The data is cached in Redis for 10 minutes for faster IO
* Generating [Pre-signed URL](https://docs.aws.amazon.com/AmazonS3/latest/dev/PresignedUrlUploadObjectJavaSDK.html) to with expiration of 2 minutes
* Containerized the application 

#### [Frontend](#frontend)

* A minimal frontend built on react to view the recipes
* The frontend app uses a public endpoint exposed by backend
* Containerized the application 

#### [Jenkins CI](#jenkinsci)

* Created webhooks on github to trigger a jenkins build on each commit
* Created Jenkins [pipeline](https://jenkins.io/doc/pipeline/tour/hello-world/) script to run the unit tests, tag the image with push the image to docker

### [Deployments](#deployments)

* Deployed the application in the kubernetes clusters using ansible k8s module
* Created Replica sets from frontend and backend
* Pulled the image from private docker repository using imagePullSecrets
* Paramerterized all values to facilitate running the same playbook for different users
* Did service discovery to discover the pods 
* Passed db, aws secret and donfig data to the application with configMap and secrets
* Created a health endpoint for the backend to perform the readiness probe
* Created Liveness probes for both the containers
* Backend container has an init-container to waits for the redis service to come up
* Frontend container waits till the backend is up and running
* Created Loadbalancer services for the frontend and backend
* Run `ansible-playbook -v webservers.yml --extra-vars "@extra_vars.json"` command to deploy the applciations to the clusters
```
{
    "region" : "",
    "profile": "",
    "cluster_name" : "",
    "state_store" : "",
    "type" : "",
    "ssh_path": "",
    "command" : "",
    "dns_zone_id" : "",
    "db_password" : "",
    "home_path" : "",
    "frontend_image" : "",
    "backend_image" : "",
    "access_key":"",
    "secret_key": "",
    "bucket_name" : ""
}

```


[back](./)