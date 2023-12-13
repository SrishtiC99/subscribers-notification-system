# Notification System

- In this project, I am developing a spring boot and cloud based microservices application. This project uses
technologies like Docker for virtual containers, Kafka for event driven asynchronous communication, postgreSQL 
databases. All component of this project is developed with TDD in mind.

- This application can be used in any business use case where it is crucial to receive information related
to some specific topic from any organization/person. This application allows users to register themselves 
as a USER, and to create templates for their customers. Any user can upgrade their account to OWNER in order
to use some advance features. The user can then add several subscribers to its account and to its created templates.
Subscribers will receive notification on telegram bot if any information/notification is published for their 
subscribed templates.

**Tech Stacks:**
> `Maven Build System` `Spring boot Framework` `Spring Cloud` `JWT Authentication` `postgresSQL DB` `Kafka Broker` 
> `Docker Containers` `junit`

### Functional Todos

- [x] User Registration/Authentication/Validation
- [x] creating billing account for all new user/subscriber registration event
- [x] Handle Role Based Login, update role via kafka when subscription is updated in billing
- [x] Fetching user info from api requests and validating if they are a registered user or not
- [x] Validating if logged-in users have enough permission or not, checking account expiry
- [ ] Handle custom exception in API Gateway and FeignClient exception in other services
- [x] Owner can create/get/delete a template and add/remove subscribers to a template
- [x] Using kafka to send events to other services to update subscriber list/billing account/notification
- [x] Add Bulk Subscribers registration from Excel file
- [x] Using Feign Client to get data from other services
- [ ] Add integration test - Template service
- [x] Add Unit test - Template service
- [ ] Add swagger Api docs
- [x] Send Notification to Subscriber via Telegram using TelegramBotApi
- [ ] Send Notification to Subscriber via Email
- [x] Scheduling event to suspend expired owners account
- [x] Billing account subscription renewal
- [ ] Send notification to user for account expiry/renewal
- [ ] Handle Billing for template subscribers

### Non-Functional Todos

- [ ] Improve Database calls
- [ ] Improve Kafka topic partitioning
- [ ] Hide security tokens 
- [ ] Logging and Tracing
- [ ] Dockerize this application

### Discovery Server
 - Spring Eureka server is used for service registry by which the API gateway would discover the network information of 
other services.

### API Gateway
- Spring Cloud gateway is used for validating the api requests and directing it to the correct service. 

### Auth Service
- New User Registration, User Authentication, JWT Token Validation, Fetching User details from JWT token

### Template Service
- Create/Delete/Get a Template
- Add/Remove list of Subscribers to a template
- Realtime event driven update through kafka to subscriber-service after adding/removing subscribers to a template

### Subscriber Service
- Create/Update/Get/Delete a Subscriber

### Notification Service
- Sending notification to all the subscribers of a template through Kafka

### Sender Service 
- Receiving notifications sent from notification service and forwarding them to the subscriber's email/phone/telegram
- User Registration on Telegram Bot
- Fetching chatId from Telegram Bot and saving it to the database for using it to notify user

### Billing Service
- A billing account of all users
- Track account bills for owners: if not settled disable account (Service Owner's side)
- Track account bills for Subscribers: if not settled remove from templates (Owner's side)
