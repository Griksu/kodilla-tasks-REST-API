echo Starting Show tasks
echo.
echo Starting Run crud
call runcrud.bat
if "ERRORLEVEL" == "0" goto runwebbrowser
echo.
echo RUNCRUD has errors - breaking work
goto fail

:runwebbrowser
echo Starting web browser
set url="http://localhost:8080/crud/v1/task/getTasks"
start "" %url%
goto end

:fail
echo.
echo There were errors

:end
echo.
echo Work is finished