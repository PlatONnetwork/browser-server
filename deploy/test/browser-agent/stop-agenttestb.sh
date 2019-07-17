#!/bin/sh
ps -ef|grep -v grep|grep 'active=agenttestb'|awk '{print $2}'|xargs kill -9;
ps -elf|grep active=agenttestb;
