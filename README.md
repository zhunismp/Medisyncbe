# Medisyncbe 💊
- *Serving service for durg operational in Medisync application*
## Prerequisite
- Installed docker (check [here](https://www.docker.com/products/docker-desktop/) to download.)
## Spinning project up 🌌
1. Clone the project by using `git clone https://github.com/Zhuuuun/Medisyncbe.git`
2. Add environment variable into project
    - **.env** and **backend.env** inside **/docker**
    - **.env** and **keystore.p12** for Java application inside **Medisyncbe\drugapi\src\main\resources** 
3. Go inside folder **/docker** and run these command to run/stop server
   ```
   docker-compose up --build // run server
   docker-compose down // stop server
   ```
## API documentation 🗃️
- This project using *Openapi* for documentation. After project is up and running, you can go check API documentation at [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
  ![image](https://github.com/user-attachments/assets/2d8e3894-d427-4b05-84d7-ce84bf8ca632)
