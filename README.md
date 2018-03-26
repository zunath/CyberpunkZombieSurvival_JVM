# CyberpunkZombieSurvival_JVM
Server-side Java code used in the Neverwinter Nights Cyberpunk Zombie Survival module.

Game: Neverwinter Nights 1

Website: https://czs-web.azurewebsites.net/
Discord: https://discord.gg/Mt25sk8

# Project Description

This project contains the Java source code used on the Cyberpunk Zombie Survival server. 

It serves as a replacement for NWScript and handles most server features and functions. This is possible by using the NWNX_JVM plugin for NWNX. Refer to this forum for more info on what this is: http://www.nwnx.org/phpBB2/viewtopic.php?t=1478

This can ONLY be run on a Linux server due to the fact that there is no equivalent Windows plugin.

Refer to the quick start guide below and be sure to post any issues on our forums. The link to the forums is above.

# Quick-start guide for CZS server developers

1.) Install Git from here: https://git-scm.com/downloads

2.) Install IntelliJ Community Edition (NOT Ultimate!) from here: https://www.jetbrains.com/idea/download/

3.) Start up IntelliJ

4.) Click the "Check out from Version Control" option.

5.) Select GitHub

6.) Enter in the following:

Git Repository URL: https://github.com/zunath/CyberpunkZombieSurvival_JVM.git

Parent Directory: Select a location on your hard drive where you want to store your projects or leave it as the default.

Directory Name: CyberpunkZombieSurvival_JVM

7.) Click Clone and wait until it finishes downloading the files.

8.) IntelliJ will load the project automatically.



# Hosting Dependencies

1.) Neverwinter Nights: Enhanced Edition Server Files

2.) Linux OS

3.) NWNX + the following plugins: Chat, Creature, Events, JVM, Object, Player

4.) OpenJDK 8 JRE

5.) Microsoft SQL Server 2017

# Using Docker

I highly recommend using Docker to speed up development time. You can read about how to use it for NWN:EE here: https://hub.docker.com/r/nwnxee/nwserver/

It'll take care of hosting dependencies #1-#4. You only need to set up the SQL Server database at that point.

# Getting Help

If you need help with anything related to Cyberpunk Zombie Survival please feel free to contact us on our Discord.

For NWNX and Docker related issues please look for help in the NWNX Discord channel here: https://discord.gg/m2hJPDE