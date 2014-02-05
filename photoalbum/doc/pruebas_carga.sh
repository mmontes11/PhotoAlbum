#!/bin/bash
for i in `seq 1 4`
do
	for i in `seq 1 60`
	do
		for j in `seq 0 2` 
		do
			curl -s "http://localhost:8080/PhotoAlbum02_webservice/ws/search?num=200&keywordName=a&resource=$j&hotCriterion=$j&initDate=08-12-2010&endDate=16-12-2020&orderCriterion=0" &
			curl -s "http://localhost:8080/PhotoAlbum02_webservice/ws/search?num=200&keywordComment=a&resource=$j&hotCriterion=$j&initDate=08-12-2010&endDate=16-12-2020&orderCriterion=1" &	
			curl -s "http://localhost:8080/PhotoAlbum02_webservice/ws/search?num=200&keywordName=a&resource=$j&hotCriterion=$j&initDate=08-12-2010&endDate=16-12-2020&orderCriterion=0" &
			curl -s "http://localhost:8080/PhotoAlbum02_webservice/ws/search?num=1000&resource=2&hotCriterion=$j&initDate=08-12-2010&endDate=16-12-2020&orderCriterion=1" &
		done	
	done
	sleep 18s
done
echo "Fin";
