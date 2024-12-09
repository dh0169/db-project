rsync -Pav -e "ssh -i $HOME/.ssh/rpi5_rsa" $HOME/Dev/db-project/ bebop831@10.0.0.131:/opt/tomcat/webapps/ROOT --exclude=".git" --exclude="*.sh"
