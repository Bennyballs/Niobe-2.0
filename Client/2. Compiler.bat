@echo off
title Niobe Client Compiler
echo starting...
"C:\Program Files\Java\jdk1.7.0\bin\javac.exe" -d bin; src\accounts\*.java src\constants\*.java src\interfaces\*.java src\sign\*.java src\util\*.java                                                
pause