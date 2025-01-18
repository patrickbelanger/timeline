# Timeline
Timeline - The art of storytelling, reimagined - Powered by AI

# Requirement
* A Groq® Cloud account. Sign up at [Groq Cloud Console](https://console.groq.com/login).
* A Groq® API key, which can be created in the Groq Cloud Console, [API keys section](https://console.groq.com/keys).
* Set your Groq® API key in the ``GROQ_API_KEY`` environment variable to enable the client to use it for making requests with the Groq client.
* Docker (or Podman)

📢 Notice: Groq® is a trademark of Groq, Inc. [Groq CLI and the Groq Client](https://github.com/patrickbelanger/groq-client) are not affiliated with Groq, Inc. These library and client
are provided as interfaces to communicate with the Groq API. For more information, refer to Groq's [Trademark Policy](https://groq.com/trademark-policy/).

## How to set the ``GROQ_API_KEY`` environment variable on Windows, macOS, and Linux?

### Windows

* Open the Start Menu and search for Environment Variables.
* Click on Edit the system environment variables.
* In the System Properties window, click Environment Variables.
* Under User variables, click New, and enter:
* Variable Name: ``GROQ_API_KEY``
* Variable Value: ``your-api-key-here`` (replace this value with generated one)
* Click OK to save and close.

### macOS/Linux

Open your shell configuration file in a text editor:

#### For bash:

```bash
~/.bashrc
```
or
```bash
~/.bash_profile
```

#### For zsh:
```bash
~/.zshrc
```

Add the following line to the file:
```bash
export GROQ_API_KEY=your-api-key-here
```

Save the file and run:
```bash
source ~/.bashrc
```
or
```bash
source ~/.zshrc
```

depending on your shell.


# Setup 🌮

## Using Docker
Run the following command to start the services:

```bash
docker compose up -d
```

## Using Podman
If you're using Podman, use this command instead:

```
podman compose up -d
```

This will start all the required service dependencies for the application, including Redis and Postgres.

# Launch the application

## Launching unit and integration tests

Make sure to copy these properties in the `application-local.properties` before executing the command:

```bash
mvn test
```

Properties to add:

```properties
#H2 (test) Datasource
spring.datasource.test.url=jdbc:h2:mem/unit_test_db
spring.datasource.test.diver-class-name=org.h2.Driver
spring.datasource.test.username=sa
spring.datasource.test.password=<replace by H2 default password>
spring.jpa.test.hibernate.ddl-auto=create-drop
```