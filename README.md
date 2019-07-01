This file is provided together with:
* jacoco folder
* realm_db.sql file
* realm-0.0.1-SNAPSHOT.jar file

How to deploy:
1. Install MySQL server with MySQL Workbench for convenience (follow the installation instructions provided by MySQL server itself). MySQL 8.0 was used during the development of this application
2. In MySQL Workbench, login as root or any other user created during the installation to the local instance of MySQL. The user must have enough priveleges to create a schema, create and drop tables (see realm_db.sql for reference)
3. Select Server -> Data Import in the main menu. In the Data Import dialog select "Import from Self-Contained File" and provide a path to realm_db.sql. Click on "Start Import" button.
4. When the import is completed, click the "Refresh" button in the Navigator window, realm_db should appear in the list of schemas.
5. Create a user named "user" with a password "user" that have enough privileges to select and insert rows ("Object Rights" in "Schema Privileges" tab of "Server -> User and Privileges" dialog) for the realm_db schema. The web application is designed to work with the user with exactly the same name and password, as it is not the business-ready solution.
6. Make sure that your MySQL server is up and running. "Server -> Server Status" dialog indicates it.
7. Run realm-0.0.1-SNAPSHOT.jar (java -jar realm-0.0.1-SNAPSHOT.jar)
8. Test that the application is up and running by sending a request. You can use CURL (shipped with Windows 10) or any other application designed for HTTP communication. For CURL the sample command is:

curl -i -H "Accept: application/json" "http://localhost:8080/service/user/realm/1"

The expected response is:

HTTP/1.1 404
Content-Type: application/xml
Transfer-Encoding: chunked
Date: Mon, 01 Jul 2019 20:16:19 GMT

<error>
  <code>RealmNotFound</code>
</error>

Summary:
The application is developed according to the description, no additional checks are done (no DB column limitations checks, no security checks, etc). The code hasn't been reviewed.

Test coverage:
See jacoco/index.html

Time constraints:
Do not work with the client for more than 2 hours without a break. Do some eyes exercises during the break.


