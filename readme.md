The script is supported to run on macos and you need to have mvn installed

How to run:
* Clone project
* Run: chmod +x run.sh 
* Run: ./run.sh, you might be prompted for a password when changing the 
firewall rule

The program starts up two spring boot servers, one running on port 8080 and the 
other on port 8082. When calling the endpoint /hello on the server running on 
port 8080 it calls the server running on port 8082 using a WebClient with 
reactor netty as the underlying http client. The program first makes a call 
using curl and then adds a firewall rule to block more requests on that 
connection. The program then makes another request which fails, and then yet 
another one that also fails. 

client.log and server.log contains the logs from the server running on port 
8080 and 8082 respectively
