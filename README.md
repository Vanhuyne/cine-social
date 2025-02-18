# CineSocial

CineSocial is a social media network for movie enthusiasts inspired by IMDb. Users can explore movies, create watchlists, write reviews, and interact with other users.

## Technologies Used

- **Frontend**: Angular
- **Backend**: Spring Boot
- **Database**: MySql
- **Authentication**: JWT


## Features

- **User Authentication**: Sign up, login, and manage profiles.
- **Movie Database**: Browse and search for movies.
- **Watchlist**: Create and manage personal watchlists.
- **Reviews**: Write, edit, and delete reviews for movies.
- **Social Interaction**: Follow other users and interact with their content.

## Getting Started

### Prerequisites

- Node.js and npm
- Angular CLI
- Java 21
- Maven
- MySQL

### Setup

1. **Clone the repository**:
    ```sh
    git clone https://github.com/Vanhuyne/cine-social.git
    cd cine-social
    ```

2. **Backend Setup**:
    - Navigate to the backend directory:
        ```sh
        cd backend
        ```
    - Configure the `application.properties` file with your MySQL database details.
    - Build and run the Spring Boot application:
        ```sh
        mvn clean install
        mvn spring-boot:run
        ```

3. **Frontend Setup**:
    - Navigate to the frontend directory:
        ```sh
        cd frontend
        ```
    - Install dependencies:
        ```sh
        npm install
        ```
    - Run the Angular development server:
        ```sh
        ng serve
        ```
    - Open your browser and navigate to [http://localhost:4200/](http://_vscodecontentref_/1).


## Contributing

Contributions are welcome! Please fork the repository and submit a pull request.

## License

This project is licensed under the MIT License.

## Contact

For any inquiries, please contact [thanvanhuyy@gmail.com](mailto:thanvanhuyy@gmail.com).
