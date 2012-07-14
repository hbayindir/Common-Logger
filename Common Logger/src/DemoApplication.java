/*
 * CommonLogger - A simple yet powerful general purpose local logging framework, demo application.
 * Copyright (C) 2009  Hakan Bayindir
 * 
 * Developer             : Hakan Bayindir <hakan@bayindir.org>
 * Development start date: 14 Aug 2009
 * Current state         : Development (14 Aug 2009)
 * Codename              : The historian
 * License               : GNU/GPLv3
 * 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import commons.CommonLogger;
import commons.CommonLogger.logTargets;
import commons.CommonLogger.messageLevels;

public class DemoApplication
{
	public static void main(String[] args)
	{
		System.out.println("Hello, this little program demoes the abilities of common logger by Hakan Bayindir <hakan@bayindir.org>");
		System.out.println("This logger is designed to be user friendly and be practical to use, it's not designed to compete with full blown loggers around.");
		
		//Instead of putting a \n above line, use a println() to ease the jobs of the people who is new to programming.
		System.out.println();
		
		//Let's start by creating a new CommonLogger so we can show its abilities to the tester.
		CommonLogger historian = new CommonLogger();
		
		historian.log(messageLevels.INFO, "You are running this demo with the common logger version " + historian.getComponentVersion());
		
		historian.log(messageLevels.INFO, "This is the output of vanilla logger right after initialization. While it supports logging to file, when initialized, only to screen logging is initialized." +
				                          " Please note that severities ERROR and FATAL is directed to STDERR, not STDOUT.");
		
		System.out.println(); //Demo applications should look nice.
		
		//Vanilla Logger.
		historian.log(messageLevels.DEBUG, "This is a DEBUG message after intializing logger");
		historian.log(messageLevels.INFO, "This is an INFO message after intializing logger");
		historian.log(messageLevels.WARNING, "This a WARNING message after intializing logger");
		historian.log(messageLevels.ERROR, "This is an ERROR message after intializing logger");
		historian.log(messageLevels.FATAL, "This is a FATAL message after intializing logger");
		
		System.out.println();
		
		//This is how we set the identifier of the program. Formatting is left free intentionally
		historian.setEntryIdentifierString("[The Historian Demo]");
		
		historian.log(messageLevels.INFO, "This is the output of the logger after setting entry identifier string.");
		
		System.out.println();
		
		historian.log(messageLevels.DEBUG, "This is a DEBUG message after setting entry identifier string");
		historian.log(messageLevels.INFO, "This is an INFO message after setting entry identifier string");
		historian.log(messageLevels.WARNING, "This a WARNING message after setting entry identifier string");
		historian.log(messageLevels.ERROR, "This is an ERROR message after setting entry identifier string");
		historian.log(messageLevels.FATAL, "This is a FATAL message after setting entry identifier string");
		
		System.out.println();
		
		//Let's play with the dating system a bit.
		historian.setLogTimeAndDate(false);
		
		historian.log(messageLevels.INFO, "You can disable date and time in logs.");
		
		System.out.println();
		
		historian.log(messageLevels.DEBUG, "This is a DEBUG message without date & time");
		historian.log(messageLevels.INFO, "This is an INFO message without date & time");
		historian.log(messageLevels.WARNING, "This a WARNING message without date & time");
		historian.log(messageLevels.ERROR, "This is an ERROR message without date & time");
		historian.log(messageLevels.FATAL, "This is a FATAL message without date & time");
		
		System.out.println();
		
		historian.setLogTimeAndDate(true);
		
		//You can enable file logging with a single function call.
		historian.setAbsolutePathToLogFileAndEnable("./historian_demo.log");
		
		historian.log(messageLevels.INFO, "You can enable file logging with a single function call. Look for file historian_demo.log.\n"); //changing to \n to make log file pretty too.
		
		historian.log(messageLevels.DEBUG, "This is a DEBUG message logged both to file and screen after enabling file logging");
		historian.log(messageLevels.INFO, "This is an INFO message logged both to file and screen after enabling file logging");
		historian.log(messageLevels.WARNING, "This a WARNING message logged both to file and screen after enabling file logging");
		historian.log(messageLevels.ERROR, "This is an ERROR message logged both to file and screen after enabling file logging");
		historian.log(messageLevels.FATAL, "This is a FATAL message logged both to file and screen after enabling file logging\n");
		
		historian.log(messageLevels.INFO, "You can selectively redirect log entries to file or the screen\n");
		
		historian.log(messageLevels.DEBUG, "This is a DEBUG message logged only to screen by overriding", logTargets.SCREEN);
		historian.log(messageLevels.INFO, "This is an INFO message logged only to file by overriding", logTargets.FILE);
		historian.log(messageLevels.WARNING, "This a WARNING message logged to both by overriding", logTargets.BOTH);
		
		historian.setLoggingTarget(logTargets.SCREEN);
		historian.log(messageLevels.ERROR, "This is an ERROR message logged only to screen by selecting default log target");
		
		historian.setLoggingTarget(logTargets.FILE);
		historian.log(messageLevels.FATAL, "This is a FATAL message logged only to file by selecting default log target");

	}

}
