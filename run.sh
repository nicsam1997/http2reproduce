#!/bin/bash
echo "Build programs"
mvn -f http2client/pom.xml clean install
mvn -f http2server/pom.xml clean install

echo "Start client program"
mvn -f http2client/pom.xml --quiet clean spring-boot:run > client.log &
echo "Start server program"
mvn -f http2server/pom.xml --quiet clean spring-boot:run > server.log &

echo "Sleep 10 seconds, this might have to be adjusted"
sleep 10

echo "Send request"
response1=$(curl -s http://localhost:8080/hello)
echo "Response: ${response1}"
echo -e "\nShow active tcp connection"
netstat -anp tcp | grep 127.0.0.1.8082
connection=$(netstat -anp tcp | grep 127.0.0.1.8082 | awk 'NR==1 {print $5}')
port=${connection##*.}

echo "Setup a firewall rule to block the connection on ${port}"
echo "block drop in proto tcp from any to any port ${port}" | sudo pfctl -ef -

echo "Send another request to the same endpoint"
response2=$(curl -s http://localhost:8080/hello)
echo "Response: ${response2}"

echo "Send yet another request to the same endpoint"
response3=$(curl -s http://localhost:8080/hello)
echo "Response: ${response3}"

echo "Clean up"
kill $(lsof -i :8082 | grep LISTEN | awk '{print $2}')
kill $(lsof -i :8080 | grep LISTEN | awk '{print $2}')
