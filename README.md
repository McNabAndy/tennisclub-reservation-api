# Tennis Club Reservation System
This is a basic Spring Boot application for managing tennis court reservations. It supports creating, retrieving, and managing surface types, courts, and reservations.

## Features
- Create and manage surface types (e.g., clay, grass)
- Create and manage court
- Create and manage reservation
- Soft-delete support for entities
- Basic validation and error handling
- H2 in-memory database (for testing)
- RESTful API with DTO responses
- Automatic database initialization (can be enabled in `application.properties`)

## Requirements
- Java 21
- Maven

## Dependencies
- Spring Boot
- H2 Database
- Lombok
- JUnit 5 and Mockito

## UML Diagrams
UML diagrams of the app are in the path `docs/uml`

## Notes
The application uses JPA with custom DAO implementations __no JpaRepository or CrudRepository__.
Data initialization (surfaces and courts) is configurable via `application.properties` 
```
app.init.data=true
```


# API Endpoints - Surface Type

### POST Request - Create Surface type 
`/api/surfaces/create`
```
{
    "name": "Antuka",
    "minutePrice": 2.50
}
```
#### Response
```
{
    "id": 1,
    "name": "Antuka",
    "minutePrice": 2.50
}
```
### GET Request - Retrive Surface type by id 
`/api/surfaces/{id}`
#### Response
```
{
    "id": 1,
    "name": "Clay",
    "minutePrice": 1.50
}
```


### GET Request - Retrive All Surface type
`/api/surfaces`
#### Response
```
[
    {
        "id": 1,
        "name": "Clay",
        "minutePrice": 1.50
    },
    {
        "id": 2,
        "name": "Grass",
        "minutePrice": 3.50
    },
    {
        "id": 3,
        "name": "Antuka",
        "minutePrice": 10.00
    }
]
```


### PUT Request - Update Surface type by id
`/api/surfaces/{id}`
```
{
    "name": "Clay",
    "minutePrice": 1.50
}
```
#### Response
```
{
    "id": 2,
    "name": "Clay",
    "minutePrice": 1.50
}
```


### DELETE Request - Soft Delete Surface type by id 
`/api/surfaces/{id}`
#### Response
```
{
    "message": "Surface Type with ID 3 was deleted."
}
```




# API Endpoints - Court

### POST Request - Create Court
`/api/courts/create`
```
{
    "courtNumber": 105,
    "surfaceTypeId": 2
}
```
#### Response
```
{
    "id": 5,
    "courtNumber": 105,
    "surfaceType": {
        "id": 2,
        "name": "Clay",
        "minutePrice": 1.50
    }
}
```
### GET Request - Retrive Court by id 
`/api/courts/{id}`
#### Response
```
{
    "id": 1,
    "courtNumber": 101,
    "surfaceType": {
        "id": 2,
        "name": "Clay",
        "minutePrice": 1.50
    }
}
```


### GET Request - Retrive All Courts
`/api/courts`
#### Response
```
[
    {
        "id": 1,
        "courtNumber": 101,
        "surfaceType": {
            "id": 1,
            "name": "Clay",
            "minutePrice": 1.50
        }
    },
    {
        "id": 2,
        "courtNumber": 102,
        "surfaceType": {
            "id": 1,
            "name": "Clay",
            "minutePrice": 1.50
        }
    },
    {
        "id": 3,
        "courtNumber": 103,
        "surfaceType": {
            "id": 1,
            "name": "Clay",
            "minutePrice": 1.50
        }
    }
]
```


### PUT Request - Update Courts by id
`/api/courts/{id}`
```
{
    "courtNumber": 105,
    "surfaceTypeId": 2
}
```
#### Response
```
{
    "id": 5,
    "courtNumber": 105,
    "surfaceType": {
        "id": 2,
        "name": "Clay",
        "minutePrice": 1.50
    }
}
```


### DELETE Request - Soft Delete Court by id 
`/api/courts/{id}`
#### Response
```
{
    "message": "Court with id 4 was deleted"
}
```
  



# API Endpoints - Reservation

### POST Request - Create Reservation
`/api/reservations/create`
The user is saved based on their phone number. If no user exists in the database with that phone number, a new one will be created.
At the same time, validation must be enforced: the server will return an error if the date fails validation. 
A reservation can only be made between __10:00 and 22:00__ for up to __120 minutes__, and it must __not overlap__ with any existing reservation for the same court.
```
{
    "userName": "Andy McNab",
    "phoneNumber": "777888999",
    "startTime": "2026-05-28T11:30",
    "endTime": "2026-05-28T12:30",
    "courtNumber": 109,
    "gameType": "SINGLES"
}
```
#### Response
```
{
    "id": 1,
    "courtNumber": 109,
    "userName": "Andy McNab",
    "phoneNumber": "777888999",
    "startTime": "11:30",
    "endTime": "12:30",
    "gameDate": "28.05.26",
    "gameType": "SINGLES",
    "price": 90.00,
    "createdAt": "02.06.25"
}
```
### GET Request - Retrive Reservation by id 
`/api/reservations/{id}`
#### Response
```
{
    "id": 2,
    "courtNumber": 102,
    "userName": "John Doe",
    "phoneNumber": "123123123",
    "startTime": "11:00",
    "endTime": "12:30",
    "gameDate": "05.06.26",
    "gameType": "DOUBLES",
    "price": 270.00,
    "createdAt": "02.06.25"
}
```

### GET Request - Retrive Reservation by court number
`/api/reservations/{courtNumber}`
In this case, reservations are sorted by creation date.
#### Response
```
[
    {
        "id": 3,
        "courtNumber": 109,
        "userName": "John Doe",
        "phoneNumber": "123123123",
        "startTime": "11:00",
        "endTime": "12:30",
        "gameDate": "05.06.26",
        "gameType": "DOUBLES",
        "price": 270.00,
        "createdAt": "02.06.25"
    },
    {
        "id": 1,
        "courtNumber": 109,
        "userName": "Andy McNab",
        "phoneNumber": "777888999",
        "startTime": "11:30",
        "endTime": "12:30",
        "gameDate": "28.05.26",
        "gameType": "SINGLES",
        "price": 90.00,
        "createdAt": "02.06.25"
    }
]
```

### GET Request - Retrive Reservation by phone number
`/api/reservations/{phone}?futureOnly=true`
The /api/reservations/phone/{phoneNumber} endpoint accepts an optional `futureOnly` query parameter, which defaults to `false` if not provided. 
When `futureOnly` is set to `true`, the server returns only the reservations for the specified phone number that occur in the future. 
If the parameter is omitted or explicitly set to false, the server returns all reservations associated with that phone number, regardless of their date.
#### Response
```
[
    {
        "id": 3,
        "courtNumber": 109,
        "userName": "John Doe",
        "phoneNumber": "123123123",
        "startTime": "11:00",
        "endTime": "12:30",
        "gameDate": "05.06.26",
        "gameType": "DOUBLES",
        "price": 270.00,
        "createdAt": "02.06.25"
    },
    {
        "id": 1,
        "courtNumber": 109,
        "userName": "Andy McNab",
        "phoneNumber": "777888999",
        "startTime": "11:30",
        "endTime": "12:30",
        "gameDate": "28.05.26",
        "gameType": "SINGLES",
        "price": 90.00,
        "createdAt": "02.06.25"
    }
]
```

### GET Request - Retrive All Reservation
`/api/reservations`
#### Response
```
[
    {
        "id": 4,
        "courtNumber": 109,
        "userName": "Tomas Adamec",
        "phoneNumber": "111222333",
        "startTime": "15:00",
        "endTime": "16:30",
        "gameDate": "05.06.25",
        "gameType": "DOUBLES",
        "price": 270.00,
        "createdAt": "02.06.25"
    },
    {
        "id": 5,
        "courtNumber": 102,
        "userName": "Tomas Adamec",
        "phoneNumber": "111222333",
        "startTime": "13:00",
        "endTime": "14:00",
        "gameDate": "11.06.25",
        "gameType": "SINGLES",
        "price": 90.00,
        "createdAt": "02.06.25"
    },
    {
        "id": 1,
        "courtNumber": 109,
        "userName": "Andy McNab",
        "phoneNumber": "777888999",
        "startTime": "11:30",
        "endTime": "12:30",
        "gameDate": "28.05.26",
        "gameType": "SINGLES",
        "price": 90.00,
        "createdAt": "02.06.25"
    }
]
```

### PUT Request - Update Reservation by id
`/api/reservations/{id}`
You can modify any field in the request body—if you change the phone number and there is no user with that number, a new user record will be created and the reservation will be reassigned to that new user. 
If you change the name for an existing user (identified by phone number), their name will be updated on all reservations associated with that phone number. 
Additionally, the server will recalculate the total price based on any altered time, court or duration and will verify that the updated timeslot does not overlap with any other reservation on the same court.
```
{
    "userName": "Tomas Adamec",
    "phoneNumber": "111222333",
    "startTime": "2025-07-28T12:30",
    "endTime": "2025-07-28T13:30",
    "courtNumber": 109,
    "gameType": "DOUBLES"
}
```
#### Response
```
{
    "id": 5,
    "courtNumber": 109,
    "userName": "Tomas Adamec",
    "phoneNumber": "111222333",
    "startTime": "12:30",
    "endTime": "13:30",
    "gameDate": "28.07.25",
    "gameType": "DOUBLES",
    "price": 180.00,
    "createdAt": "02.06.25"
}
```

### DELETE Request - Soft Delete Surface type by id 
`/api/reservations{id}`
#### Response
```
{
    "message": "Reservation with id 5 was deleted"
}
```


  
