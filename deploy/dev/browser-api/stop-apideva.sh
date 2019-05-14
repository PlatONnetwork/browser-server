#!/bin/sh
ps -ef|grep -v grep|grep 'active=apideva8080'|awk '{print $2}'|xargs kill -9;
ps -ef|grep -v grep|grep 'active=apideva8081'|awk '{print $2}'|xargs kill -9;
ps -elf|grep active=apidev;
