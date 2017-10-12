#!/bin/sh

export LD_PRELOAD=./nwnx2.so
#export LD_LIBRARY_PATH=/usr/lib/jvm/java-7-openjdk-i386/jre/lib/i386/client/libjvm.so
export LD_LIBRARY_PATH=lib/:$LD_LIBRARY_PATH:/usr/lib/jvm/java-8-openjdk-i386/jre/lib/i386/server/libjvm.so


./nwserver \
	-publicserver 1 \
	-servername "Cyberpunk Zombie Survival" \
	-dmpassword dmpassword \
	-oneparty 0 \
	-pvp 0 \
	-difficulty 2 \
	-elc 1 \
	-reloadwhenempty 0 \
	-module "Cyberpunk Zombie Survival" \
	-maxclients 32 \
	-servervault 1 \
	-maxlevel 40 \
	-gametype 0 \
	-autosaveinterval 0 \
	"$@"

