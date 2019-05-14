#!/bin/sh
ps -ef|grep -v grep|grep 'active=apitesta9090'|awk '{print $2}'|xargs kill -9;
ps -ef|grep -v grep|grep 'active=apitesta9091'|awk '{print $2}'|xargs kill -9;
ps -elf|grep active=apitesta;
