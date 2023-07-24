# Instructions

The infrastructure is in the directory `infrastructure`. It's a docker compose file.
To run this run the below command in `infrastructure` directory:

```shell
docker-compose up -d
```

Once all the containers are running then you can start the BE application. To build the dockerfile run below command in
the `acc` directory to build the image.

```shell
docker build -t jaelse/acc-app:v1 .  
```

Once you can successfully build the image, run below command to run the BE app container.
NOTE:

- Please check that the container network is specified correctly and the environment variable is specified correctly.
- `POSTGRES_HOST` variable should be initialized with the container name of postgres DB.

```shell
docker run --name acc-app -p 8080:8080 --network=acc-network -e POSTGRES_HOST='accDB' jaelse/acc-app:v1
```

Below is th list of all the environment variables. In case, the default values are not compatible with your local
machine
environment, you can change as required.

| Environment Variable | Default Value                                                                      |
|----------------------|------------------------------------------------------------------------------------|
| POSTGRES_HOST        | localhost                                                                          |
| POSTGRES_PORT        | 5432                                                                               |
| POSTGRES_DB          | postgres                                                                           |
| POSTGRES_USER        | postgres                                                                           |
| POSTGRES_PASSWORD    | acc                                                                                |
| SECRET               | `supersecretblahblahblahssssseeecretttasdfiieeih@*$(*$*(@#*(*HR#IFIUEAIHUEFAEFIHU` |

# Considerations

This project is built over Reactive stack which I find a good fit considering two main reasons. First, Reactive
stack make the complete application asynchronous and non-blocking which I believe is the main object of this
application. This stack uses Netty server which is asynchronous and event driven. For database, I used R2dbc with
r2dbc-postgres driver which provides reactive data connectivity. Second, I am more familiar with this stack so I think
it will give me a better position to satisfy most of the point mentioned in the requirement. The requirement gave
suggestion of Node.js or Python to meet the asynchronicity. I guess this stack still fulfills all those requirements. I
have tried to put comments in some places just for the sake of explain-ability

# Trade-offs

Below are few of the trade-offs that is made this the current choice of tech stack.

## Memory consumption

Java applications are known for its memory hogging characteristics. I tried to use GraalVM to help that.

## Developer Familiarity

Developers are not so familiar with Reactive stack. And nowadays, Node.js seems to be widely used by developers.

# List of Endpoints

| Endpoint              | Description                                      |
|-----------------------|--------------------------------------------------|
| POST /v1/accounts     | Create a new Account with name and email address |
| GET /v1/accounts/{id} | Get account details by id                        |
| PUT /v1/accounts/{id} | Update user information by ID                    |
| POST /v1/login        | Login with email and password                    |

Below are some curl requests just for example:

## Create Account

```shell
curl --location 'http://localhost:8080/v1/accounts' \
  --header 'Content-Type: application/json' \
  --data-raw '{
    "name": "jaelse3sdf",
    "email": "jaelse2@gmail.com",
    "password": "jaelse!1234"
}'
```

## Get account

```shell
curl --location 'http://localhost:8080/v1/accounts/4' \
  --header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWQiOjQsImVtYWlsIjoiamFlbHNlMkBnbWFpbC5jb20iLCJzdWIiOiJqYWVsc2UyQGdtYWlsLmNvbSIsImlhdCI6MTY5MDExMzA1NSwiZXhwIjoxNjkwMTk5NDU1fQ.V2-Re5jnbblotH5ffzmoESC26SjdzoV3s5sP7hOkgfxQM-5zNYtEHNwwRhkQsiNiaQafg0hdxz5opUB8CGaUUA'

```

## Update account

```shell
curl --location --request PUT 'http://localhost:8080/v1/accounts/8' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWQiOjQsImVtYWlsIjoiamFlbHNlMkBnbWFpbC5jb20iLCJzdWIiOiJqYWVsc2UyQGdtYWlsLmNvbSIsImlhdCI6MTY5MDExMzA1NSwiZXhwIjoxNjkwMTk5NDU1fQ.V2-Re5jnbblotH5ffzmoESC26SjdzoV3s5sP7hOkgfxQM-5zNYtEHNwwRhkQsiNiaQafg0hdxz5opUB8CGaUUA' \
--data '{
    "name": "re"
}'

```

## Search account

```shell
curl --location --request GET 'http://localhost:8080/v1/accounts' \
  --header 'Content-Type: application/json' \
  --data '{
    "text": "jae"
}'

```

## Login

```shell
curl --location 'http://localhost:8080/v1/login' \
  --header 'Content-Type: application/json' \
  --data-raw '{
    "email": "jaelse2@gmail.com",
    "password": "jaelse!1234"
}'

```

# Tests

You will only see tests for the endpoints. All the errors that might happen in Repository or Service layer are handled
in the endpoint.