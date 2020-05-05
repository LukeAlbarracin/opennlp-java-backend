#!/bin/bash

INPUT1=Donald%20Trump%20is%20president.
INPUT2=Barack%20Obama%20was%20president.
INPUT3=George%20Washington%20was%20first%20president.

curl localhost:8080/process/?input=$INPUT1
curl localhost:8080/process/?input=$INPUT2
curl localhost:8080/process/?input=$INPUT3
