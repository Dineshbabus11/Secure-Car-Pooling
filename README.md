# Secure Carpooling - Blockchain-Based Ride Sharing Platform

## 📋 Description

A decentralized carpooling application that leverages blockchain technology to ensure transparency, security, and trust in ride-sharing transactions. Built with Spring Boot and Ethereum blockchain integration, this platform provides a tamper-proof record of all ride transactions while maintaining efficiency and scalability.

## 🚀 Features

- **Blockchain Integration**: All ride transactions are recorded on Ethereum blockchain for immutability and transparency
- **Secure Ride Management**: Create, view, and manage carpooling rides with cryptographic verification
- **Smart Contract Integration**: Automated ride recording using Solidity smart contracts
- **Transaction Tracking**: Real-time blockchain transaction status monitoring
- **Gas Optimization**: Efficient data storage strategies to minimize blockchain gas costs
- **Batch Processing**: Queue system for handling multiple transactions efficiently
- **Fallback Mechanism**: Graceful handling of blockchain failures with retry logic
- **RESTful API**: Clean and well-documented API endpoints for all operations

## 🛠️ Technology Stack

### Backend
- **Java 17+**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **Spring Web**
- **Spring Security**

### Blockchain
- **Web3j 4.10.3** - Ethereum Java integration library
- **Solidity 0.8.x** - Smart contract development
- **Ganache** - Local blockchain development network
- **MetaMask** - Wallet integration (optional)

### Database
- **MySQL / PostgreSQL** - Primary data storage
- **H2** - Development and testing

### Tools & Libraries
- **Maven** - Dependency management
- **SLF4J** - Logging
- **Apache Commons Codec** - Cryptographic operations
- **Jakarta Persistence API** - ORM

## 📁 Project Structure

```
secure-carpooling/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/carpooling/securecarpooling/
│   │   │       ├── controller/
│   │   │       │   └── RideController.java
│   │   │       ├── service/
│   │   │       │   ├── BlockchainService.java
│   │   │       │   ├── RideService.java
│   │   │       │   └── BatchBlockchainService.java
│   │   │       ├── entity/
│   │   │       │   └── Ride.java
│   │   │       ├── model/
│   │   │       │   ├── TransactionStatus.java
│   │   │       │   └── RideTransaction.java
│   │   │       ├── repository/
│   │   │       │   └── RideRepository.java
│   │   │       └── SecureCarpoolingApplication.java
│   │   ├── resources/
│   │   │   ├── application.properties
│   │   │   └── contracts/
│   │   │       └── RideContract.sol
│   └── test/
├── pom.xml
└── README.md
```

## 🔧 Installation & Setup

### Prerequisites

- Java JDK 17 or higher
- Maven 3.6+
- MySQL 8.0+ or PostgreSQL 13+
- Node.js 14+ (for Ganache)
- Ganache CLI or Ganache GUI

### Step 1: Clone the Repository

```bash
git clone https://github.com/yourusername/secure-carpooling.git
cd secure-carpooling
```

### Step 2: Database Setup

```sql
CREATE DATABASE carpooling_db;
CREATE USER 'carpooling_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON carpooling_db.* TO 'carpooling_user'@'localhost';
FLUSH PRIVILEGES;
```

### Step 3: Configure Application Properties

Edit `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/carpooling_db
spring.datasource.username=carpooling_user
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

# Blockchain Configuration
blockchain.node.url=http://localhost:8545
blockchain.private.key=YOUR_GANACHE_PRIVATE_KEY
blockchain.contract.address=YOUR_DEPLOYED_CONTRACT_ADDRESS
blockchain.gas.limit=3000000
blockchain.gas.price=20000000000

# Batch Processing
blockchain.batch.size=10
blockchain.batch.interval=30000
```

### Step 4: Start Ganache (Local Blockchain)

```bash
# Using Ganache CLI
ganache-cli --gasLimit 10000000 --gasPrice 20000000000 --port 8545

# Or use Ganache GUI and configure:
# - Port: 8545
# - Gas Limit: 10000000
# - Gas Price: 20000000000
```

### Step 5: Deploy Smart Contract

```bash
# Install Truffle
npm install -g truffle

# Compile and deploy
truffle compile
truffle migrate --network development
```

Copy the deployed contract address to `application.properties`

### Step 6: Build and Run

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run

# Or run the JAR
java -jar target/secure-carpooling-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## 📡 API Endpoints

### Ride Management

#### Create a Ride
```http
POST /api/rides
Content-Type: application/json

{
  "driverId": "driver123",
  "origin": "New York",
  "destination": "Boston",
  "price": 45.50,
  "departureTime": "2024-01-20T10:00:00",
  "availableSeats": 3
}
```

#### Get Ride by ID
```http
GET /api/rides/{id}
```

#### Get All Rides
```http
GET /api/rides
```

#### Update Ride
```http
PUT /api/rides/{id}
Content-Type: application/json

{
  "availableSeats": 2,
  "status": "ACTIVE"
}
```

### Blockchain Operations

#### Get Transaction Status
```http
GET /api/blockchain/transaction/{txHash}
```

#### Get Pending Transactions
```http
GET /api/blockchain/pending
```

## 🔐 Smart Contract

### RideContract.sol

```solidity
// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract RideContract {
    struct Ride {
        uint256 rideId;
        string rideHash;
        uint256 timestamp;
    }
    
    mapping(uint256 => Ride) public rides;
    
    event RideRecorded(uint256 indexed rideId, string rideHash, uint256 timestamp);
    
    function recordRide(uint256 _rideId, string memory _rideHash) public {
        rides[_rideId] = Ride({
            rideId: _rideId,
            rideHash: _rideHash,
            timestamp: block.timestamp
        });
        
        emit RideRecorded(_rideId, _rideHash, block.timestamp);
    }
    
    function getRide(uint256 _rideId) public view returns (Ride memory) {
        return rides[_rideId];
    }
}
```

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=BlockchainServiceTest

# Run with coverage
mvn clean test jacoco:report
```

## 📊 Database Schema

### Rides Table
```sql
CREATE TABLE rides (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    driver_id VARCHAR(255) NOT NULL,
    origin VARCHAR(255) NOT NULL,
    destination VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    departure_time DATETIME,
    available_seats INT,
    blockchain_tx_hash VARCHAR(255),
    blockchain_status VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

## 🐛 Troubleshooting

### Gas Limit Exceeded Error
```
Solution: Reduce data being sent to blockchain or increase Ganache gas limit
ganache-cli --gasLimit 10000000
```

### Connection Refused to Blockchain
```
Solution: Ensure Ganache is running on correct port (8545)
Check blockchain.node.url in application.properties
```

### Transaction Hash is Null
```
Solution: Verify private key and contract address are correct
Ensure wallet has sufficient ETH for gas fees
```

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Coding Standards
- Follow Java naming conventions
- Write unit tests for new features
- Document public APIs with JavaDoc
- Keep methods focused and concise

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- **Your Name** - *Initial work* - [YourGitHub](https://github.com/yourusername)

## 🙏 Acknowledgments

- Web3j documentation and community
- Spring Boot team
- Ethereum Foundation
- OpenZeppelin for smart contract best practices

## 📞 Contact

- **Email**: your.email@example.com
- **LinkedIn**: [Your LinkedIn](https://linkedin.com/in/yourprofile)
- **Twitter**: [@yourhandle](https://twitter.com/yourhandle)

## 🗺️ Roadmap

- [ ] User authentication and authorization
- [ ] Ride matching algorithm
- [ ] Real-time notifications
- [ ] Mobile application (React Native)
- [ ] Payment integration
- [ ] Rating and review system
- [ ] Advanced analytics dashboard
- [ ] Multi-chain support (Polygon, BSC)
- [ ] IPFS integration for data storage
- [ ] Docker containerization
- [ ] Kubernetes deployment
- [ ] CI/CD pipeline

## 📈 Project Status

**Current Version**: 1.0.0-BETA

This project is currently in beta phase. Features are being actively developed and tested.

---

**Note**: This is a educational/demonstration project. For production use, additional security measures, thorough testing, and auditing are required.

⭐ **Star this repository if you found it helpful!**

---

## 📚 Additional Resources

- [Web3j Documentation](https://docs.web3j.io/)
- [Spring Boot Guide](https://spring.io/guides)
- [Solidity Documentation](https://docs.soliditylang.org/)
- [Ethereum Development Tutorial](https://ethereum.org/en/developers/docs/)
