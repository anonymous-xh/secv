#!/bin/bash
# 
# Author: anonymous-xh anonymous-xh
# adds mx to path and points java home to jdk-jvmci
#
export PATH=$PWD/mx:$PATH
export JAVA_HOME=$PWD/labsjdk-ce-11.0.13-jvmci-22.0-b02

export JVMCI_CONFIG_CHECK environment="warn"
