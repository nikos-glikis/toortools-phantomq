#!/usr/bin/env bash
#mvn install -DperformRelease=true -DcreateChecksum=true
mvn install -DcreateChecksum=true -DperformRelease=true -Dgpg.passphrase=password