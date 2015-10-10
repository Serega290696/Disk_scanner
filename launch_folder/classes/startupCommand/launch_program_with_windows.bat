Set pathBat="D:\1-Programming\1-Projects\Part1\BigProjects\Disk_scanner\out\launch.bat"
Reg Add "HKLM\SOFTWARE\Microsoft\Windows\CurrentVersion\Run" /v "MyBat" /t REG_SZ /d "%pathBat%" /f
echo "Done!"
 