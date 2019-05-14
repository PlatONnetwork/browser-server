#!/bin/sh
ps -ef|grep -v grep|grep 'browser-api-0.6.0-SNAPSHOT.jar --spring.profiles.active=testa28060'|awk '{print $2}'|xargs kill -9;
ps -ef|grep -v grep|grep 'browser-api-0.6.0-SNAPSHOT.jar --spring.profiles.active=testa28062'|awk '{print $2}'|xargs kill -9;
ps -elf|grep browser-api;
