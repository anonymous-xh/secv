#!/bin/bash

#
# Panonymous-xh:script to run Java program to generate
# synthetic benchmark polyglot programs
#

# compile java source
javac polybench/BenchMain.java


# run bench
java polybench.BenchMain