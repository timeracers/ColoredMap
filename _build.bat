@echo off
call mvn package
cp target/ColoredMap.jar ../_ModTheSpire/mods
pause