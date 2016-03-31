#!/usr/bin/env bash
set -ex

sbt pack-archive
cd target/samza-demo
if [[ -z $1 ]]; then
    archiveName="samza-demo-1.0"
else
    archiveName=$1
fi

tar -czvf ../${archiveName}.tar.gz bin config lib
