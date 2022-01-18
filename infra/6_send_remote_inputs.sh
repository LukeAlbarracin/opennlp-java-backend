#!/bin/bash

INPUT1=Donald%20Trump%20is%20president.
INPUT2=Barack%20Obama%20was%20president.
INPUT3=George%20Washington%20was%20first%20president.

URL=https://nla-cs125x-276023.uc.r.appspot.com/

curl $URL/process/?input=$INPUT1
curl $URL/process/?input=$INPUT2
curl $URL/process/?input=$INPUT3
