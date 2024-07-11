Copy-Item -Recurse -Force ..\shared ./
docker build -t chameleon-api .
Remove-Item -Recurse -Force .\shared