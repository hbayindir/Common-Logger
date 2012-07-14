/*
 * CommonLogger - A simple yet powerful general purpose local logging framework.
 * Copyright (C) 2009  Hakan Bayindir
 * 
 * Developer             : Hakan Bayindir <hakan@bayindir.org>
 * Development start date: 20 Jul 2009
 * Current state         : RC (30 Jul 2009)
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
package commons;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;


public class CommonLogger
{
	public enum messageLevels {DEBUG, INFO, WARNING, ERROR, FATAL};
	public enum logTargets    {NONE, SCREEN, FILE, BOTH};
	
	private boolean logToScreen;
	private boolean logToFile;
	private boolean logTimeAndDate;
	
	private boolean logFileIsOpen;
	
	private FileWriter logFilePointer;
	private BufferedWriter logFile;
	
	private Calendar logTime;
	
	//Every entry can be prepended with an identifier string. This is used to enable logging programs to publish their identities
	private String entryIdentifierString;
	private String absolutePathToLogFile;
	
	public CommonLogger()
	{
		//Set the default settings for the logging upon creation
		logToScreen    = true;
		logToFile      = false;
		logTimeAndDate = true;
		
		logFileIsOpen = false;
		
		entryIdentifierString = "";
	}
	
	//This is the destructor of the class. We should close all files before we destruct ourself and we do close them!
	public void finalize() throws Throwable
	{
		try
		{
			closeLogFile();
		}
		finally
		{
			super.finalize(); //The upper class should do their task too
		}
	}

	public String getEntryIdentifierString()
	{
		return this.entryIdentifierString;
	}

	public void setEntryIdentifierString(String entryIdentifierString)
	{
		this.entryIdentifierString = entryIdentifierString;
	}

	public String getAbsolutePathToLogFile()
	{
		return this.absolutePathToLogFile;
	}

	public void setAbsolutePathToLogFile(String absolutePathToLogFile)
	{
		if(logFileIsOpen)
		{
			closeLogFile(); //if the log file is open close the log file so the next log is written to correct file.
		}
		
		this.absolutePathToLogFile = absolutePathToLogFile;
	}
	
	//This function sets the absolute path to log file and then enables logging to that file automatically. This is an convenience method.
	public void setAbsolutePathToLogFileAndEnable(String absolutePathToLogFile)
	{
		setAbsolutePathToLogFile(absolutePathToLogFile);
		
		logToFile = true;
	}
	
	public void setLoggingTarget(logTargets logTarget)
	{
		if(logTarget == logTargets.NONE)
		{
			logToScreen = false;
			logToFile   = false;
		}
		else if(logTarget == logTargets.SCREEN)
		{
			//if we are no longer writing to the file, we should close it on spot
			if(logFileIsOpen)
			{
				closeLogFile();
			}
			
			logToScreen = true;
			logToFile   = false;
		}
		else if(logTarget == logTargets.FILE)
		{
			logToScreen = false;
			logToFile   = true;
		}
		else if(logTarget == logTargets.BOTH)
		{
			logToScreen = true;
			logToFile   = true;
		}
	}
	
	public logTargets getLoggingTarget()
	{
		if(logToScreen == false && logToFile == false)
		{
			return logTargets.NONE;
		}
		else if(logToScreen == true && logToFile == false)
		{
			return logTargets.SCREEN;
		}
		else if(logToScreen == false && logToFile == true)
		{
			return logTargets.FILE;
		}
		else if(logToScreen == true && logToFile == true)
		{
			return logTargets.BOTH;
		}
		
		//The code should not reach here in any circumstances because it's designed in that way. This is just to silence the compiler.
		return null;
	}
	
	public boolean getLogTimeAndDate()
	{
		return logTimeAndDate;
	}

	public void setLogTimeAndDate(boolean logTimeAndDate)
	{
		this.logTimeAndDate = logTimeAndDate;
	}

	public void log(messageLevels messageLevel, String message) //this is the actual logging function that uses the functions implemented in private section of the file.
	{
		if(logToScreen)
		{
			logToScreen(messageLevel, message);
		}
		
		if(logToFile)
		{
			logToFile(messageLevel, message);
		}
	}
	
	//This function is same as the actual log file. Instead, this file enables you to select logging target on the fly.
	public void log(messageLevels messageLevel, String message, logTargets target)
	{
		if(target == logTargets.NONE)
		{
			return;
		}
		else if(target == logTargets.SCREEN)
		{
			logToScreen(messageLevel, message);
		}
		else if(target == logTargets.FILE)
		{
			logToFile(messageLevel, message);
		}
		else if(target == logTargets.BOTH)
		{
			logToScreen(messageLevel, message);
			logToFile(messageLevel, message);
		}
	}
	
	public String getComponentVersion()
	{
		return "1.0.1";
	}
	
	//Below are private functions for the class, they are used by other public functions. Don't use them, make them public etc.
	/*
	 * Return codes for the function openLogFile
	 * -----------------------------------------
	 * 0: Everything is OK
	 * 1: File is already open
	 * 2: An I/O exception has occurred during opening file.
	 */
	private int openLogFile()
	{
		try
		{
			if (!logFileIsOpen)
			{
				//This is the most practical way of opening files in Java & isolates user from all handling.
				logFilePointer = new FileWriter(absolutePathToLogFile, true);
				logFile = new BufferedWriter(logFilePointer);
				
				logFileIsOpen = true;
				return 0;
			}

			// Code will drop here automatically if file is open already
			return 1;
		}
		catch (IOException exception)
		{
			log(messageLevels.FATAL, "[LOGGER INTERNAL SELF-LOG] - An I/O exception accured in openLogFile().\nMessage is: " + exception.getMessage() + "\nCause is: " + exception.getCause(), logTargets.SCREEN);
			return 2;
		}
	}
	
	/*
	 * Return codes for the function closeLogFile
	 * -----------------------------------------
	 * 0: Everything is OK
	 * 1: File is already closed
	 * 2: An I/O exception has occurred during closing file.
	 */
	private int closeLogFile()
	{
		try
		{
			if (!logFileIsOpen)
			{
				logFile.close();
				logFileIsOpen = false;
				return 0;
			}

			// Function will drop here if the file is already closed
			return 1;
		}
		catch (IOException exception)
		{
			log(messageLevels.FATAL, "[LOGGER INTERNAL SELF-LOG] - An I/O exception accured in closeLogFile(). Probably during close.\nMessage is: " + exception.getMessage() + "\nCause is: " + exception.getCause(), logTargets.SCREEN);
			return 2;
		}
	}
	
	private String preformatMessage(messageLevels messageLevel)
	{
		String messageToBeShown;
		
		//Program adds identifier string if it's not empty. The identifier string is omitted if the string is not set or empty
		if(!entryIdentifierString.equalsIgnoreCase(""))
		{
			messageToBeShown = entryIdentifierString + " ";
		}
		else
		{
			messageToBeShown = "";
		}
				
		if(logTimeAndDate)
		{
			logTime = Calendar.getInstance();
			messageToBeShown += "[" + logTime.getTime() + "] ";
			logTime = null;
		}
		
		if(messageLevel == messageLevels.DEBUG)
		{
			messageToBeShown += "[DEBUG] ";
		}
		else if(messageLevel == messageLevels.INFO)
		{
			messageToBeShown += "[INFO] ";
		}
		else if(messageLevel == messageLevels.WARNING)
		{
			messageToBeShown += "[WARN] ";
		}
		else if(messageLevel == messageLevels.ERROR)
		{
			messageToBeShown += "[ERROR] ";
		}
		else if(messageLevel == messageLevels.FATAL)
		{
			messageToBeShown += "[FATAL] ";
		}
		
		return messageToBeShown;
	}
	
	private void logToScreen(messageLevels messageLevel, String message)
	{
		if(messageLevel == messageLevels.DEBUG || messageLevel == messageLevels.INFO || messageLevel == messageLevels.WARNING)
		{
			System.out.println(preformatMessage(messageLevel) + message);
		}
		else if(messageLevel == messageLevels.ERROR || messageLevel == messageLevels.FATAL)
		{
			System.err.println(preformatMessage(messageLevel) + message);
		}
	}
	
	private void logToFile(messageLevels messageLevel, String message)
	{
		if(absolutePathToLogFile.equalsIgnoreCase(""))	
		{
			log(messageLevels.FATAL, "[LOGGER INTERNAL SELF-LOG] - File logging is enabled but log file is not set, not logging to file", logTargets.SCREEN);
			return;
		}
		
		try
		{
			if(!logFileIsOpen)
			{
				openLogFile();  //open the log file if it's not open yet. This is the part of the easy-to-use logger idea.
			}
			
			logFile.write(preformatMessage(messageLevel) + message + "\n");
			logFile.flush();
		}
		catch (IOException exception)
		{
			log(messageLevels.FATAL, "[LOGGER INTERNAL SELF-LOG] - An I/O exception accured in logToFile().\nMessage is: " + exception.getMessage() + "\nCause is: " + exception.getCause(), logTargets.SCREEN);
		}
	}
}
