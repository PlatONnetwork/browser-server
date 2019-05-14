#!/bin/sh
ps -ef|grep -v grep|grep 'active=agenttesta'|awk '{print $2}'|xargs kill -9;
ps -elf|grep active=agenttesta;
