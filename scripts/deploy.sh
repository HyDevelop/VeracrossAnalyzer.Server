scp ../target/VeracrossAnalyzer.Server.jar root@hydev.org:/app/depl/veracross-analyzer/
ssh root@hydev.org "systemctl restart veracross-analyzer-api"
