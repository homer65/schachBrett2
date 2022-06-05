#
# schachBrett2
#
$Dir = $PSScriptRoot
$Lib = $Dir + "\lib\*.jar"
$jars = Get-ChildItem -Path $Lib
foreach ($jar in $jars)
{
 $Env:CLASSPATH=$Env:CLASSPATH + ";" + $jar
}
#
$Lib = $Dir + "\dll\"
$Env:PATH = $Env:PATH + ";" + $Lib
#
java -Xmx4096M -Xms1280M pack.Main
java pack.Pause