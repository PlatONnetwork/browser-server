#!/bin/bash
ps -elf|grep _PROJECT_NAME_ |grep active="$1"