
rem echo "Switch to production database settings"
rem exit

mvn package
scp ./target/VeracrossAnalyzer.Server.jar root@hydev.org:/app/depl/veracross-analyzer/
ssh root@hydev.org "systemctl restart va"
