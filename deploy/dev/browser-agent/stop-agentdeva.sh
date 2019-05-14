#!/bin/sh
ps -ef|grep -v grep|grep 'active=agentdeva'|awk '{print $2}'|xargs kill -9;
ps -elf|grep active=agentdeva;
