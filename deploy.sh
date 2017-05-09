#!/usr/bin/env bash

echo "Deploying plugin to server..."

# Build plugin with maven
mvn clean install

# Upload plugin file
rsync -azPh target/releases/query-exponsion-plugin-0.0.1.zip user@0.0.0.0:~/

# Run commands on the server
ssh user@0.0.0.0 /bin/bash << EOF
  ls
  # Uninstall plugin
  sudo /usr/share/elasticsearch/bin/elasticsearch-plugin remove QueryExpansionPlugin

  # Install plugin
  sudo /usr/share/elasticsearch/bin/elasticsearch-plugin install file:///home/user/query-exponsion-plugin-0.0.1.zip
EOF

echo "Done"
