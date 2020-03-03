# Local Managed Identity Server for development

[Managed identities for Azure resources](https://docs.microsoft.com/en-us/azure/active-directory/managed-identities-azure-resources/overview) allow app builders to connect to cloud services without using credentials. The identity is managed by the Azure platform and does not require you to provision or rotate any secrets. This method enables you to secure your application in production, but poses some minor challenges in development. Although often times developers use mock services or emulators for cloud services, there's virtue in connecting to real cloud services. It's possible to contain different strategies for authentication depending on configuration (dev vs prod), but alternatively you could run a sample server that can act like an endpoint for managed identities. This way your application will be able to access the (development) cloud services without any changes in your code.

Note that this server is designed to work with App Service managed identities.

## Getting access to cloud services

First of all you'll need to make sure that you've configured an identity (service principal) to connect
to a cloud service. If you don't have one, try the following command to create a new one

```bash
ID_NAME="http://spn-my-local-msi"  # needs to be a URI
CLIENT_SECRET=`az ad sp create-for-rbac --name $ID_NAME --skip-assignment --query password -o tsv`
```

Note that this server expects SPN information to be set in certain environment variables, so in addition to
the `CLIENT_SECRET`, you need to set the `CLIENT_ID` and `TENANT_ID`

```bash
CLIENT_ID=`az ad sp show --id $ID_NAME --query appId -o tsv`
TENANT_ID=`az ad sp show --id $ID_NAME --query appOwnerTenantId -o tsv`
```

Once the environment variables are set, you can assign permissions to the service principal to access
resources. For example, in order to be able to manage (send/listen) all Event Hubs in an Azure Event
Hubs Namespace, you need to run the following command

```bash
# First get the scope of the Azure Event Hubs Namespace
RG=...  # the resource group in which the Azure Event Hubs Namespace is created
NS=...  # the name of the Azure Event Hubs Namespace
SCOPE=`az eventhubs namespace show -g $RG -n $NS --query id -o tsv`
# Now the you can assign the service principal the required role
az role assignment create --assignee $CLIENT_ID --role "Azure Event Hubs Data Owner" --scope $SCOPE
```

Note that some cloud services (i.e. [Azure SQL Database](https://docs.microsoft.com/en-us/azure/app-service/app-service-web-tutorial-connect-msi)) might require different/additional steps to enable
access from service principals.

## Running the server

After cloning this repository, build the code

```bash
mvn clean package -DskipTests
```

Assuming that the environment variables `CLIENT_ID`, `CLIENT_SECRET` and `TENANT_ID` has been set, you can now run the server

```bash
java -jar target/app.jar
```

By default the server runs on port `1509`, you can change that through the standard methods of configuring
the `server.port` property for Spring Boot.

## Running your application

App Services expect the environment variables `MSI_ENDPOINT` and `MSI_SECRET` to be set. Assumint that the
local MSI server is running on localhost and the default port, you can use the following to configure
these variables

```bash
MSI_ENDPOINT=http://localhost:1509/msi/token
MSI_SECRET=foobar  # the value doesn't matter, it could be empty as well
```

Now if you run your application (which uses [AppServiceMSICredentials](http://azure.github.io/ref-docs/java/com/microsoft/azure/credentials/AppServiceMSICredentials.html)), the local MSI server will be accessed with
the request to provide a token, which is then retrieved using the service principal that has been configured.