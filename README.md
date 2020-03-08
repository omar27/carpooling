## #CAR POOLING:

Tools/Framework Used:
_) Java 8
_) Spring-boot (2+)
_) JPA
_) H2 (Disk base persistence)
_) WebSockets
_) Bootstrap

Pre-requisits:

You should have following things installed on your system before running the project

-) Java
-) Maven
-) Angular (8, +)

How to Run:

-) Clone project
-) In root directory, run ./mvnw script
-) Server would be up and running on port 8080
-) Open a browser and navigate to http://localhost:8080

## Interacting with App:

NORMAL USER (DRIVER/PASSENGER):

-) On Main page, you can go to top-right menu, you'll have to register yourself first
-) Click on Account->Register and a form would be opened, register yourself from that form
-) After successful registration, go to login page by clicking Account->Login
-) Login with your email and password
-) you would see a welcome message and 2 separate roles icons (Car referes to DRIVER, person refers to PASSENGER)
-) Click on DRIVER and you'll be navigated to "Ride" page, you can create a new ride there
-) You can add details and can add passenger (its optional, I suggest do not add passenger at this stage as passenger would book a ride on his own later on) and save the data.
-) Once you save it, you would be able to see data in the table as websockets are pushing data to UI every 5 seconds
-) A DRIVER can see ONLY those rides which he have created (He can see details of source/destinations by clicking on them or simply by clicking view button)
-) Go to home page
-) click on person icon (i.e. now your type is changed to passenger), you can also register a separate account to be used as passsenger
-) Go to top menu Entities->Fav Locations
-) Add a fav location for which you want to be notified whenever a ride is scheduled to that destination (note that a person can see ONLY his/her own added fav locations)
-) After adding fav location, go to "Ride" page by clicking user Icon from home page
-) IF any driver have created any ride with same destination, you would see that in the table along with a "book" button
-) Click on "book" button and you'll see you name will start appearing as passenger with ride (Note that passenger would be able to see only rides near to his fav location i.e. with difference of 20 points in x/y-axis and ONLY those rides whose status is SCHEDULED, once ride status is changed (BOOKED, COMPLETED, CANCELED etc), passenger would not be able to see them anymore, hence can't book them)
-) When driver is logged in again, he would be able to see this change as well, websockets are sending all this updated data every 5 seconds.

RIGHTS:
-) Any user can register him/herself and then can login, default role would be USER with type can be DRIVER/PASSENGER
-) DRIVER can add Rides, View Rides, Add FavLocation and View FavLocation
-) PASSENGER can View Scheduled Rides ONLY for his added favLocations' matching criteria
-) PASSENGER can book a ride
-) PASSENGER can ADD/View/Update/Delet his favLocations

## ADMIN:

You can log in as admin by using "admin" as username and "admin" as password

Admin can:
-) Can Temporarily block account, Change rights (i.e. giving admin right to any user) or delete user account related to any carpooling user
-) Add/View/Update/Delete carpooling users (i.e. DRIVERS or PASSENGERS)
-) Add/View/Update/Delete Rides
-) Add/View/Update/Delete Locations
-) Add/View/Update/Delete Fav Locations
-) Can use all "Administration" Modules

## Administrations Modules:

User Management:
-) Add/view/update/delete user accounts, change their usernames, email account, active/deactive, change type (i.e. make Admin etc)
Metrices:
-) Can see Application Metrices i.e. memory,threads,system details, garbage collector statistics, http requests' aggregated response time with respect to status codes, endpoints requests details and ang time etc.
Health:
-) Disk space, DB status etc
Configuration:
-) Project configuration details related to Spring and Server etc
Audits:
-) Can see user login audits with respect to dates
Logs:
-) Loging level details for all the libraries being used in the system
API:
-) Complete details of REST endpoints available (swagger implementation) in the system with per-integrated sample request and response formates. You can test any endpoint direct from browser or you can copy curl command to run in command.
Database:
-) You can directly check Database being used in this application, It'll show you a SQL client to interact with H2 Database.

## Tests:

Integration tests are available in Java for all the controllers
