#!/bin/bash

deps=org.opennebula.client-2.0.1.jar:sigar.jar
jarname=cloudmiddleware-0.0.1-SNAPSHOT-jar-with-dependencies.jar

for i in {500,1000,2000} ; do
	for j in {1, 250, 500} ; do
		java -classpath $deps:$jarname br.ufpe.cin.middleware.app.statistics.StatisticsClient -host 10.0.0.1 -time 15 \
			-load $i -service-delay $j
	done
done

