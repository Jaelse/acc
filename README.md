# Instructions

The infrastructure is in the directory `infra`. It's a docker compose file.
To run this run the below command in `infra` directory:

```shell
docker-compose up -d
```

Once all the containers are running then you can start the BE application.

This project is built with Reactive Programing and use using kafka because it is a good fit for a
system that is required handle high number of transactions. kafka configuration is too basic. It can
be fine-tuned, but I didn't do it because it will require a lot of time. Consumer takes a while to
start consuming, so if you see the consumption hasn't started right away please wait a while.

# List of Endpoints

| Endpoint              | Description                                      |
|-----------------------|--------------------------------------------------|
| POST /v1/accounts     | Create a new Account with name and email address |
| GET /v1/accounts/{id} | Get account details by id                        |
| PUT /v1/accounts/{id} | Update user information by ID                    |

# Run load test

Go to the load_testing directory. Run the following command to start the program.
First arg is the wallet id and the second is the number of senders.

```shell
go run main.go 64339ea06120341bad15586d 1
```

To stop the senders, press `cmd+c` on macbook.
It will take a while to stop if too many senders are there.
