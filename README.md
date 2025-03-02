# Medisyncbe 💊
- *Serving service for durg operational in Medisync application*
## Prerequisite
- Installed docker (check [here](https://www.docker.com/products/docker-desktop/) to download.)
## Spinning project up 🌌
1. Clone the project by using `git clone https://github.com/Zhuuuun/Medisyncbe.git`
2. Add environment variable into project
    - **.env**, **backend.env**, and **cron.env** inside **/docker**
    - **.env** for Java application inside **Medisyncbe\drugapi\src\main\resources** 
3. Running these command to start application
    - `make build` for running application with fresh build.
    - `make up` for running application without build.
    - `make down` for stop application.

## Inspect data in database
1. Checkout `http://localhost:8080` 
2. Enter these credentials `email=admin@example.com` and `password=admin`
3. Going to **Query tool work space** (second item on side bar.)
4. Connect with drug_db by entered these provided value
    - Server name = `mdbe`
    - Host name = `drugdb`
    - Port = `5432`
    - Database = `drug_db`
    - User = `$POSTGRES_USER`
    - Password = `$POSTGRES_PASSWORD`
5. Start querying
![image](/docs/image.png)
> **Server name** can typically be any name. **User** and **Password** can found in .env file

## API documentation 🗃️
- This project using *Openapi* for documentation. After project is up and running, you can go check API documentation at [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
  ![image](https://github.com/user-attachments/assets/2d8e3894-d427-4b05-84d7-ce84bf8ca632)
