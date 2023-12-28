The script is supported to run on macos or linux and you need to have netstat, 
curl, mvn and jdk 17 installed.

How to run:
* Clone project
* Run: chmod +x run_macos.sh (macos) or chmod +x run_linux.sh (linux)
* Run: ./run_macos.sh (macos) or ./run_linux.sh (linux), you might be prompted 
for a password when changing the firewall rule.

You don't have to run the script, it also serves as documentation if you prefer 
running the commands yourself

Note that firewall rule ends up being modified, you probably want to reset them to their state prior to running the 
shell script. (Improvement - Make the script reset the firewall rule) 

The program starts up two spring boot servers, one running on port 8080 and the 
other on port 8082. When calling the endpoint /hello on the server running on 
port 8080 it calls the server running on port 8082 using a WebClient with 
reactor netty as the underlying http client, using the http2 protocol. The 
program first makes a call 
using curl and then adds a firewall rule to block more requests on that 
connection. The program then makes another request which fails, and 
then yet another one that fails if you have a macos system, but not on a linux system 
since there we have set the TCP_USER_TIMEOUT. Note that on macos requests will 
keep failing. 

client.log and server.log contains the logs from the server running on port 
8080 and 8082 respectively
