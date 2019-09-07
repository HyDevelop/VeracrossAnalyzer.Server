# Mvn package first

# Server
server="root@hydev.org"

# Upload libraries
scp -r ../target/lib $server:/app/depl/veracross-analyzer/

# Execute command
ssh $server << EOF
  # Make dirs
  mkdir /app
  mkdir /app/depl
  mkdir /app/depl/veracross-analyzer
  cd /app/depl/veracross-analyzer/
  mkdir logs
  mkdir lib

  # Make launch script
  echo "/usr/lib/jvm/java-8-openjdk-amd64/jre/bin/java -cp "VeracrossAnalyzer.Server.jar:./lib/*" -Xms256M -Xmx512M org.hydev.veracross.analyzer.api.VAApiServer" > launch.sh
  chmod +x launch.sh

  # Set permissions
  cd ..
  chown -R jvmapps:appmgr veracross-analyzer

  # Make systemctl script
  echo "" > /etc/systemd/system/veracross-analyzer-api.service
  cat << EOT >> /etc/systemd/system/veracross-analyzer-api.service
[Unit]
Description=VeracrossAnalyzer API Server

[Service]
WorkingDirectory=/app/depl/veracross-analyzer/
ExecStart=/bin/bash launch.sh
User=jvmapps
Type=simple
Restart=on-failure
RestartSec=5

[Install]
WantedBy=multi-user.target
EOF

# https://computingforgeeks.com/how-to-run-java-jar-application-with-systemd-on-linux/
