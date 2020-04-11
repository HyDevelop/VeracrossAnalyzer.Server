#!/usr/bin/env bash

echo "Switch to production database settings"
exit

mvn package
scp ./target/VeracrossAnalyzer.Server.jar root@hydev.org:/app/depl/veracross-analyzer/
ssh root@hydev.org "systemctl restart veracross-analyzer-api"
