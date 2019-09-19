
# Upload libraries
ssh root@hydev.org "rm -rf /app/depl/veracross-analyzer/lib/*"
rm /app/depl/veracross-analyzer/lib/*
scp -r ../target/lib root@hydev.org:/app/depl/veracross-analyzer/
