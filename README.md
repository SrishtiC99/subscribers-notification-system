# Notification System

> Owner & Subscriber, owner can send notification to multiple template subscribers, subscribers will
> receive the notification on their preferred options (email/phone/telegram)

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
- [x] Using Kafka to send events to other services
- [x] Owner can add/remove/get/update subscribers
- [x] Owner can add/remove subscribers to a template
- [x] Add Bulk Subscribers registrationg
- [ ] Add integration test - Template service
- [x] Add Unit test - Template service
- [ ] Add swagger Api docs
- [x] Send Notification to Subscriber via Telegram
- [ ] Send Notification to Subscriber via Email
- [x] Scheduling event to suspend expired owners
- [x] Billing account subscription renewal

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

### Billing Service
- A billing account of all users
- Track account bills for owners: if not settled disable account (Service Owner's side)
- Track account bills for Subscribers: if not settled remove from templates (Owner's side)

> &ensp; TODOs
> - [ ] &ensp; Send Notification to user after renewing notification
> - [ ] &ensp; Add other aspects of account billing (like bank details)
> - [ ] &ensp; Handle billing for template subscribers
> - [ ] &ensp; update template subscriber's list based on bill settlement