## Car Rental Service - frontend

Kodilla course - final project. WEB application of car rental service.

- To acquire backend go to:
https://github.com/hubertgorczynski/final-project-backend

- Application deployed also on Heroku under link below:
https://salty-mountain-66251.herokuapp.com/

### Technologies
- Spring Boot 
- Hibernate
- REST API
- JDBC
- JUnit5
- Mockito
- Vaadin (frontend)
- Lombok

### Features
- Test coverage > 80%
- Three external API's:
    - HereApi (for searching car agencies by zip-code)
    - VinDecoder (for decode vin numbers)
    - EmailVerification (verifying correctness of email used during registration)
- Email sender scheduler (for admin daily information about actual rentals and cars)
- Design patterns:
    - Strategy
    - Facade

### Instruction
1. Firstly, please create Your account. After that You will receive an email with information that Your account has been created.
2. Log in. You can now rent a car from the table in page called "Cars".
3. In page called "Rentals" You can check Your actual rentals and modify (for example by extending duration) or closing them. You will also receive email with confirmation about Your actions.
4. In page called "User account", You will find Your personal data (You can also modify them here). In this section You can delete Your account.
5. In page called "Agency searcher" You can find the nearest car agencies in Your area. You just have to enter Your zip code.
6. In page called "Vin decoder" You can check any car (not only from our database) information by entering its unique VIN number.

### Extra feature
You can log in as Administrator by using below login and pass. In admin mode You can see all users and theirs rentals.
- Login: admin
- Password: admin

