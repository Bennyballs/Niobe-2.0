@echo off
title Niobe Launcher
@color 2
"C:/program files/java/jre7/bin/java.exe" -Xmx512m -cp bin;lib/netty-3.5.8.Final.jar;lib/commons-compress-1.4.1.jar;lib/gson-2.2.2.jar; org.niobe.GameServer
pause