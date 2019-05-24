#!/bin/sh
ps -ef|grep -v grep|grep 'active=appdeva10000'|awk '{print $2}'|xargs kill -9;
ps -ef|grep -v grep|grep 'active=appdeva10001'|awk '{print $2}'|xargs kill -9;
ps -elf|grep active=appdeva;
