# Subway Sandwich Truck Order Management System


## Technology Stack

- Java 17
- Kafka
- Spring Boot
- Spring MVC
- Spring Security
- Docker
- SQL (PostgreSQL, MS SQL)
- Elasticsearch

## User Stories

1. **View Ingredients:** Users can view the list of available ingredients, their prices, and quantities to choose what they want in their sandwich.

2. **Place Orders:** Users can place orders for sandwiches by specifying the ingredients and quantity for each sandwich. The sandwich cost is calculated as the sum of ingredient costs plus a 30% profit margin.

3. **Access Statistics:** Admins can access statistics on best-selling ingredients, number of sandwiches sold, and profit for a specific day to analyze the business performance.

4. **Containerization:** The application is containerized for ease of setup and deployment. It also has reasonable test coverage.

## Optional Features

- **Elasticsearch:** Leverage Elasticsearch to perform advanced search queries, such as ranking ingredients based on popularity or sales performance during a specific time frame.

- **Kafka:**
  - Producer: Continuously sends sandwich orders to a Kafka topic.
  - Consumer: Consumes the orders from the Kafka topic, validates them against available ingredients, calculates the total cost, and stores the validated orders.

## Data Model

**Ingredients Table:**
- Stores ingredient metadata including ID, name, price, available quantity, and type (e.g., meat, vegetable).

## Implementation

The implementation includes the following components:

- **Ingredient Service:** Manages ingredient-related operations such as saving, updating, deleting, and retrieving ingredients.
  
- **Order Service:** Handles order-related operations such as placing orders, calculating total costs, and validating orders against available ingredients.
  
- **Statistic Service:** Provides methods to access sales statistics such as best-selling ingredients, number of sandwiches sold, and profit for a specific day.
  
- **Kafka Integration:** Includes a producer to send sandwich orders to a Kafka topic and a consumer to consume orders, validate them, and store them.

## Repository Structure

The repository includes the following directories:

- `src/main/java/tn/example/sst/services`: Contains service classes responsible for business logic implementation.
  
- `src/main/java/tn/example/sst/repository`: Contains repository classes responsible for database interactions.
  
- `src/main/resources`: Contains application configuration files such as `application.properties`.
  
- `src/test`: Contains unit and integration tests for the application.

## Getting Started

To set up and run the application locally, follow these steps:

1. Clone the repository to your local machine.
2. Navigate to the project directory.
3. Build the project using Maven or Gradle.
4. Set up a database according to the configuration specified in `application.properties`.
5. Run the application using Spring Boot.

## Integration with Kafka

To enable Kafka integration:

1. Set up Kafka brokers and topics.
2. Configure Kafka properties in `application.properties`.
3. Start the Kafka producer and consumer components alongside the application.

## Elasticsearch Integration (Optional)

To enable Elasticsearch integration:

1. Set up an Elasticsearch cluster.
2. Configure Elasticsearch properties in `application.properties`.
3. Implement Elasticsearch repositories and services for advanced search functionality.

## Contributing

Contributions to the project are welcome. You can contribute by reporting issues, suggesting features, or submitting pull requests.

## License

This project is licensed under the [MIT License](LICENSE).

For more information, please refer to the [CONTRIBUTING.md](CONTRIBUTING.md) file.

## Contact

For questions or support, please contact [Elyes Ben Salah] at [bselyes@gmail.com].

---

Feel free to customize this README template further to include specific details about your project's architecture, configuration, and deployment instructions.
