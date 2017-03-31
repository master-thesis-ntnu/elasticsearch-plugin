#!/usr/bin/env bash

# Uninstall plugin
~/programs/elasticsearch-5.2.2/bin/elasticsearch-plugin remove QueryExpansionPlugin

# Install plugin
~/programs/elasticsearch-5.2.2/bin/elasticsearch-plugin install file:///home/jonas/git/elasticsearch-plugin/target/releases/query-exponsion-plugin-0.0.1.zip
