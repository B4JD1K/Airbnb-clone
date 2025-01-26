# 🌟 Airbnb Clone - Spring Boot 3, Angular 17, PrimeNG, PostgreSQL, Auth0

A full-stack Airbnb clone project developed using **Angular** (TypeScript) for the frontend and **Spring Boot** (Java) for the backend, secured with **Auth0** (OAuth2).

---

## ✨ Features

The project includes key features such as:
- **📅 Booking Management**: Travelers can book properties seamlessly.
- **🏠 Landlord Management**: Landlords can manage property reservations.
- **🔍 Advanced Search**: Search for houses by location, date, number of guests, beds, and other criteria.
- **🔐 Secure Authentication**: Role-based access control implemented with **Auth0** (OAuth2).
- **🏢 Domain-Driven Design**: Ensures maintainable and scalable architecture.

---

## ⚙️ Prerequisites

### 🖥️ Frontend
- [Node.js v20.11 LTS](https://nodejs.org/en/)
- [Angular CLI v17](https://angular.io/cli)
- [PrimeNG v17](https://primeng.org/)
- IDE: [IntelliJ IDEA Ultimate](https://www.jetbrains.com/idea/), [WebStorm](https://www.jetbrains.com/webstorm/), or [VSCode](https://code.visualstudio.com/)

### 💾 Backend
- [JDK 21](https://jdk.java.net/21/)
- [Spring Boot 3](https://spring.io/projects/spring-boot)
- [PostgreSQL](https://www.postgresql.org/)
- IDE: [IntelliJ IDEA](https://www.jetbrains.com/idea/)

---

## 🚀 Getting Started

### 1️⃣ Clone the Repository

Clone the fullstack project using the following command:
```bash
git clone https://github.com/B4JD1K/Airbnb-clone
```

The project structure will include:
```
Airbnb-clone/
│
├── airbnb-clone-backend/   # Backend code (Spring Boot)
└── airbnb-clone-frontend/  # Frontend code (Angular)
```

---

### 2️⃣ Frontend Setup

1. Navigate to the `airbnb-clone-frontend` directory:
    ```bash
    cd Airbnb-clone/airbnb-clone-frontend
    ```
2. Install the dependencies:
    ```bash
    npm install
    ```
3. Start the development server:
    ```bash
    ng serve
    ```
4. Open the application in your browser at:
    ```text
    http://localhost:4200
    ```

---

### 3️⃣ Backend Setup

1. Navigate to the `airbnb-clone-backend` directory:
    ```bash
    cd Airbnb-clone/airbnb-clone-backend
    ```
2. Open the backend project in IntelliJ or your preferred IDE.
3. Configure the PostgreSQL database:
  - Ensure that PostgreSQL is running.
  - Update the database credentials in the `application.properties` or `application.yml` file.
4. Start the Spring Boot application:
  - Using the terminal:
    ```bash
    ./mvnw spring-boot:run -Dspring-boot.run.arguments="--AUTH0_CLIENT_ID=<client-id> --AUTH0_CLIENT_SECRET=<client-secret>"
    ```
  - Or, run the application directly from IntelliJ.
5. The backend server will run on:
    ```text
    http://localhost:8080
    ```

---

## 🛠️ Technologies Used

- **Frontend**:
  - Angular 17
  - PrimeNG 17
  - PrimeFlex 3


- **Backend**:
  - Spring Boot 3
  - PostgreSQL 16.6
  - Auth0 (OAuth2)

---

## 📄 License

This project is licensed under the **MIT License**. See the [LICENSE](https://opensource.org/license/mit) for details.

---

## 🙌 Acknowledgements

- Special thanks to the [@C0de-cake](https://github.com/@C0de-cake) - creator of [the tutorial](https://youtu.be/XriUV06Hkow).
