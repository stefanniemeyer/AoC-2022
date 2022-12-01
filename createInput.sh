#!/bin/bash

for i in $(seq 1 25) ; do
    num=$(printf "%02d" $i)
    touch src/Day${num}.txt
    touch src/Day${num}_test.txt
done
