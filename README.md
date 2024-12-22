# Library Management System API Documentation

This API provides functionalities to manage a simple library system, including registering borrowers and books, listing all books, borrowing books, and returning borrowed books.



## Features
- Register borrowers
- Register books
- Borrow and return books
- Unique ISBN validation
- Swagger API documentation
- H2 database integration
- Dockerized setup for all environments

## Base URL

All endpoints are prefixed with /api 

## Data Models

### **Borrower**
Represents a person who can borrow books from the library.

Fields:

```
id: Unique identifier (generated automatically).
name: Borrower's name (mandatory, non-blank).	
email: Borrower's email (mandatory, must be valid).
```

### **Book**
Represents a book from the library.

Fields:

```
id: Unique identifier (generated automatically).
isbn: ISBN number of the book (mandatory, non-blank).
title: Title of the book (mandatory, non-blank).
author: Author of the book (mandatory, non-blank).
borrowed: Indicates if the book is currently borrowed (default: false).
```


## API Endpoints

##### `Register a Book`

##### Endpoint: POST /api/books/request
##### Description: Registers a new book to the library.
##### Request Body:

```
{
  "isbn": "978-3-16-148410-0",
  "title": "Book Title",
  "author": "Author Name",
  "borrowed": false
}
```
##### Response:
* 201 Created: Book registered successfully, returns the book object.
* 400 Bad Request: Validation error.

##### `Register a Borrower`

##### Endpoint: POST /api/borrowers/request
##### Description: Registers a new borrower to the library.
##### Request Body:

```

{
  "name": "mahesh guruge",
  "email": "mahesh@example.com"
}

```

##### `Get All Books`
##### Endpoint: GET /api/books
Description: Retrieves a list of all books in the library.

##### Response:

* 200 OK: Returns a list of all books.

```
[
  {
    "id": 1a7a0ce1-60db-4400-be94-09c6ead676fe,
    "isbn": "978-3-16-148410-0",
    "title": "Book Title",
    "author": "Author Name",
    "borrowed": false
  }
]
```

##### `Borrow a Book`

##### Endpoint: POST /api/borrowers/{borrowerId}/borrow/{bookId}
##### Description: Allows a borrower to borrow a book.
##### Response:

* 200 OK: Book borrowed successfully.
* 404 Not Found: Borrower or book not found.
* 409 Conflict: Book is already borrowed.

##### `Return a Book`

##### Endpoint: POST /api/borrowers/{borrowerId}/return/{bookId}
##### Description: Allows a borrower to return a borrowed book.
##### Response:

* 200 OK: Book returned successfully.
* 404 Not Found: Borrower or book not found.
* 409 Conflict: Book is not currently borrowed.

## Error Handling

##### Validation Errors:
Return `400 Bad Request` with a JSON object containing field-specific error messages.

```
{
  "errors": [
    "email: Email should be valid"
  ],
  "status": 400,
  "timestamp": "2024-12-22T19:13:23.651904804"
}
```

##### Not Found Errors: 
Return `404 Not Found` with an appropriate message.

##### Conflict Errors: 
Return `409 Conflict` when trying to borrow an already borrowed book or return a book that isn�t borrowed.

## ISBN Rules and Assumptions
* Two books with the same ISBN must have the same title and author.
* Two books with the same title and author but different ISBNs are treated as different books.
* Multiple copies of books with the same ISBN are allowed.
* Borrowers and books must be registered before borrowing or returning operations.
* A book can only be borrowed by one borrower at a time.
* Validation ensures meaningful data is provided for borrowers and books.

## Running the Application

##### Prerequisites

* Java 17
* Gradle

##### Steps
* Clone the repository.
* Build the project.

```
./gradlew build
```

* Run the application.

```
docker-compose up --build
````

##### Access the H2 database console

```
http://localhost:8080/h2-console
```

```
JDBC URL: jdbc:h2:mem:librarydb
Username: root
Password: root
```

## Testing the API
Refer to the `Swagger UI` for a full list of endpoints.

```
http://localhost:8080/swagger-ui/index.html
```
