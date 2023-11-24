# Notification System

> Owner & Subscriber, owner can send notification to multiple template subscribers, subscribers will
> receive the notification on their preferred options (email/phone/telegram)

**Tech Stacks:**
> `Maven Build System` `Spring boot Framework` `Spring Cloud` `JWT Authentication` `postgresSQL DB` `Kafka Broker` 
> `Docker Containers` `junit`

### Functional Todos

- [x] User Registration/Authentication/Validation
- [x] Handle Role Based Login
- [x] Fetching user info from api requests and validating if they are a registered user or not
- [x] Validating if logged-in users have enough permission or not
- [ ] Handle custom webFlux exception in API Gateway
- [x] Using FeignClient to make request to other services
- [ ] Handle FeignClient exception in template-service/subscriber-service/notification-service
- [x] Using Kafka to send events to other services
- [x] Owner can add/remove/get/update subscribers
- [x] Owner can add/remove subscribers to a template
- [x] Add Bulk Subscribers registration
- [ ] Add integration test - Template service
- [x] Add Unit test - Template service
- [ ] Add swagger Api docs
- [x] Send Notification to Subscriber via Telegram
- [ ] Send Notification to Subscriber via Email

### Non-Functional Todos

- [ ] Improve Database calls
- [ ] Improve Kafka events calls
- [ ] Hide security tokens 

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
