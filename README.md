Author: Adrian LIU
# microservice-saga-pattern

This is a simple demo for building saga pattern using spring boot and ActiveMQ. 
Flight booking, hotel booking and car booking together form an atomic travel booking transaction.

In general, these microservices are supposed to connect with their dedicated database, respectively. For demo purpose, I connect them to the same database here.