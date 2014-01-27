#!/bin/bash

DEPENDENCIES=org.opennebula.client-2.0.1.jar:org.opennebula.client-3.4.jar:sigar.jar
JARNAME=cloudmiddleware-0.0.1-SNAPSHOT-jar-with-dependencies.jar

java -classpath .:${DEPENDENCIES}:${JARNAME} br.ufpe.cin.middleware.facade.Middleware -file middleware.properties  