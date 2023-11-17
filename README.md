# Notification System

> Owner & Subscriber, owner can send notification to multiple template subscribers, subscribers will
> receive the notification on their preferred options (email/phone/telegram)

**Tech Stacks:**
> `Maven Build System` `Spring boot Framework` `JWT Authentication` `postgresSQL DB` `Kafka Broker`

- [ ] Handle Auth validation exception in API Gateway
- [ ] Handle FeignClient exception in template-service/subscriber-service/notification-service
- [x] Add Bulk Subscribers registration
- [ ] Add integration test
- [ ] Add swagger Api docs
- [ ] Notification with Email
- [ ] Notification with Telegram bot

### Discovery Server
Spring Eureka server is used for service registry by which the API gateway would discover the network information of 
other services.

### API Gateway
Spring Cloud gateway is used for validating the api requests and directing it to the correct service. 

### Auth Service
- New User Registration, User Authentication, JWT Token Validation

### Template Service
- Create/Delete/Get a Template
- Add/Remove list of Subscribers to a template
- Realtime event driven update through kafka to subscriber-service after adding/removing subscribers to a template

### Subscriber Service
- Create/Update/Get/Delete a Subscriber

### Notification Service
- Sending notification to all the subscribers of a template through Kafka
