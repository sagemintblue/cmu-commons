@ECHO OFF
SETLOCAL ENABLEDELAYEDEXPANSION

GOTO :main

REM
REM "function" definitions
REM

:usage
ECHO.
ECHO Usage: %SCRIPT_NAME% ^<action^> ^<command^> ^[args^]
ECHO.
ECHO Actions:
ECHO   exec  - executes command in foreground
ECHO   start - executes command in background
ECHO   stop  - stops command if started
ECHO   help  - prints usage information
ECHO.
ECHO Commands:
ECHO   convert - conversion utility for matrix files
ECHO.
GOTO :EOF

:init_arg
SET NAME=%~1
SET VALUE=%~2
SET %NAME%=%VALUE%
IF "%VALUE%" == "" (
  ECHO Missing required argument '%NAME%'
  EXIT /B 1
)
GOTO :EOF

REM
REM parse action
REM

:main
SET SCRIPT=%~f0
SET SCRIPT_NAME=%~n0
SET SCRIPT_DIR=%~dp0
CALL :init_arg action %1 & SHIFT
IF "%action%" == "help" (GOTO :usage)
FOR %%A IN (exec start stop redirect) DO (
  IF "%action%" == "%%A" (GOTO :valid_action)
)
ECHO Invalid action '%action%'
EXIT /B 1

REM
REM parse command
REM

:valid_action
CALL :init_arg command %1 & SHIFT
SET APP_NAME=%command%
SET MEMORY_MIN=2M
SET MEMORY_MAX=64M
SET JVMARGS=
SET MAIN_CLASS=null
IF "%command%" == "convert" (GOTO :convert)
ECHO Invalid command '%command%'
EXIT /B 1

:convert
SET MEMORY_MAX=1024M
SET MAIN_CLASS=edu.cmu.commons.mtj.util.MatrixFileConverter
GOTO :init

REM
REM initialize runtime vars
REM

:init
SET BASEDIR=%SCRIPT_DIR:\bin=%
SET CLASSPATH=
FOR /R "%BASEDIR%\lib" %%JAR IN (*.jar) DO SET CLASSPATH=!CLASSPATH!;%%JAR
SET CLASSPATH=%CLASSPATH:~1%
SET LOGDIR=%BASEDIR%\logs
SET PIDDIR=%BASEDIR%\run
FOR %%D IN ("%LOGDIR%" "%PIDDIR%") DO MKDIR %%D
SET LOGFILE=%LOGDIR%\%APP_NAME%.log
SET PIDFILE=%PIDDIR%\%APP_NAME%.pid
GOTO :%action%

REM
REM perform action for command
REM

:exec
java -cp "%CLASSPATH%" -Xms%MEMORY_MIN% -Xmx%MEMORY_MAX% %JVMARGS% %MAIN_CLASS% %1 %2 %3 %4 %5 %6 %7 %8 %9
GOTO :EOF

:redirect
javaw -cp "%CLASSPATH%" -Xms%MEMORY_MIN% -Xmx%MEMORY_MAX% %JVMARGS% %MAIN_CLASS% %1 %2 %3 %4 %5 %6 %7 %8 %9 >"%LOGFILE%" 2>&1
GOTO :EOF

:start
START "%APP_NAME%" /MIN CMD /C "%SCRIPT%" redirect %command% %1 %2 %3 %4 %5 %6 %7 %8 %9
FOR /F "tokens=2" %%PID IN ('TASKLIST /NH /FI "WINDOWTITLE eq %APP_NAME%"') DO ECHO %%PID >"%PIDFILE%"
GOTO :EOF

:stop
SET /P PID=<"%PIDFILE%"
TASKKILL /PID %PID%
