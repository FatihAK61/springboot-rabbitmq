# Spring Boot RabbitMQ Project

A Spring Boot application demonstrating message queue integration with RabbitMQ for asynchronous communication.

## 📋 Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [API Endpoints](#api-endpoints)
- [Message Queue Patterns](#message-queue-patterns)
- [Monitoring](#monitoring)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)

## ✨ Features

- **Message Publishing**: Send messages to RabbitMQ queues
- **Message Consumption**: Listen and process messages asynchronously
- **Multiple Exchange Types**: Support for Direct, Topic, Fanout, and Headers exchanges
- **Dead Letter Queue**: Handle failed message processing
- **Message Acknowledgment**: Reliable message processing with manual/automatic acknowledgment
- **Health Monitoring**: Spring Boot Actuator integration for monitoring
- **RESTful API**: Web endpoints for message operations

## 🔧 Prerequisites

Before running this application, make sure you have the following installed:

- **Java 21** or higher
- **Maven 3.6+**
- **RabbitMQ Server** (latest stable version)
- **Docker** (optional, for running RabbitMQ in container)

## 🚀 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/FatihAK61/springboot-rabbitmq.git
cd springboot-rabbitmq
```

### 2. Start RabbitMQ Server

#### Option A: Using Docker

```bash
docker run -d --name rabbitmq \
  -p 5672:5672 \
  -p 15672:15672 \
  -e RABBITMQ_DEFAULT_USER=guest \
  -e RABBITMQ_DEFAULT_PASS=guest \
  rabbitmq:4.1.3-management
```

#### Option B: Local Installation

- Download and install RabbitMQ from [official website](https://www.rabbitmq.com/download.html)
- Start the RabbitMQ server
- Enable management plugin: `rabbitmq-plugins enable rabbitmq_management`

### 3. Build the Application

```bash
./mvnw clean compile
```

### 4. Run the Application

```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## ⚙️ Configuration

### For Docker Container

From terminal: docker pull rabbitmq:4.1.3-management

Container Run: docker run --rm -it -p 15672:15672 -p 5672:5672 rabbitmq:4.1.3-management

### Queue Configuration

Example configuration class:

```java

@Configuration
@EnableRabbitMQ
public class RabbitConfig {

    public static final String QUEUE_NAME = "sample.queue";
    public static final String EXCHANGE_NAME = "sample.exchange";
    public static final String ROUTING_KEY = "sample.routing.key";

    @Bean
    public Queue queue() {
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue())
                .to(exchange())
                .with(ROUTING_KEY);
    }
}
```

## 📖 Usage

### Sending Messages

```java

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody String message) {
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME,
                RabbitConfig.ROUTING_KEY,
                message);
        return ResponseEntity.ok("Message sent successfully");
    }
}
```

### Consuming Messages

```java

@Component
public class MessageConsumer {

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void receiveMessage(String message) {
        log.info("Received message: {}", message);
        // Process the message
    }
}
```

## 📁 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── rabbit/
│   │           ├── RabbitmqApplication.java
│   │           ├── config/
│   │           │   └── RabbitConfig.java
│   │           ├── controller/
│   │           │   └── MessageController.java
│   │           ├── service/
│   │           │   ├── MessageProducer.java
│   │           │   └── MessageConsumer.java
│   │           └── model/
│   │               └── MessageDto.java
│   └── resources/
│       ├── application.yml
│       └── static/
└── test/
    └── java/
        └── com/
            └── rabbit/
                └── RabbitmqApplicationTests.java
```

## 🌐 API Endpoints

| Method | Endpoint            | Description             |
|--------|---------------------|-------------------------|
| POST   | `/api/v1/publish`   | Send a message to queue |
| GET    | `/api/v1/publish`   | Get queue status        |
| GET    | `/actuator/health`  | Health check            |
| GET    | `/actuator/metrics` | Application metrics     |

### Example API Calls

#### Send a Message

```bash
curl -X POST http://localhost:8080/api/v1/publish \
  -H "Content-Type: application/json" \
  -d "Hello RabbitMQ!"
```

#### Check Health

```bash
curl http://localhost:8080/actuator/health
```

## 📨 Message Queue Patterns

This project demonstrates several messaging patterns:

### 1. Work Queue Pattern

- Single producer, multiple consumers
- Load balancing between consumers

### 2. Publish/Subscribe Pattern

- Messages broadcast to multiple consumers
- Uses fanout exchange

### 3. Routing Pattern

- Selective message routing based on routing keys
- Uses direct exchange

### 4. Topic Pattern

- Pattern-based message routing
- Uses topic exchange

## 📊 Monitoring

### RabbitMQ Management UI

Access the management interface at `http://localhost:15672`

- Username: `guest`
- Password: `guest`

### Spring Boot Actuator

Monitor application health and metrics:

- Health: `http://localhost:8080/actuator/health`
- Metrics: `http://localhost:8080/actuator/metrics`

## 🧪 Testing

### Run Unit Tests

```bash
./mvnw test
```

### Run Integration Tests

```bash
./mvnw verify
```

### Manual Testing

1. Start the application
2. Send messages via REST API
3. Check RabbitMQ management UI for queue status
4. Verify message consumption in application logs

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## 📚 Additional Resources

- [Spring AMQP Documentation](https://docs.spring.io/spring-amqp/reference/)
- [RabbitMQ Tutorials](https://www.rabbitmq.com/getstarted.html)
- [Spring Boot Actuator Guide](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)

## 🐛 Troubleshooting

### Common Issues

1. **Connection refused error**
    - Ensure RabbitMQ server is running
    - Check connection parameters in application.yml

2. **Queue not found error**
    - Verify queue configuration
    - Check if queues are properly declared

3. **Messages not being consumed**
    - Check consumer configuration
    - Verify routing keys and bindings

### Getting Help

If you encounter any issues:

1. Check the application logs
2. Verify RabbitMQ server status
3. Review the configuration settings
4. Open an issue in this repository

---

**Happy Messaging! 🚀**
