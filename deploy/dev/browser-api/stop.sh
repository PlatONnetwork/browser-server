#!/bin/sh
ps -ef|grep -v grep|grep '--spring.profiles.active=apidev8080'|awk '{print $2}'|xargs kill -9;
ps -ef|grep -v grep|grep '--spring.profiles.active=apidev8081'|awk '{print $2}'|xargs kill -9;
ps -elf|grep browser-api;
