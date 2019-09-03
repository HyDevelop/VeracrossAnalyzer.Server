scp ../target/VeracrossAnalyzer.Server.jar root@cn2.hydev.org:/app/depl/veracross-analyzer/
ssh root@cn2.hydev.org "systemctl restart veracross-analyzer-api"
