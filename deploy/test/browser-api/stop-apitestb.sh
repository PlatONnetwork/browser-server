#!/bin/sh
ps -ef|grep -v grep|grep 'active=apitestb9190'|awk '{print $2}'|xargs kill -9;
ps -elf|grep active=apitestb;
