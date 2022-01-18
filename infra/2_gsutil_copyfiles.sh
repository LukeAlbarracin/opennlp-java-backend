#!/bin/bash

export BUCKET=nla-cs125x-opennlp-java-backend/quiz1
export FILE1=question1.txt
export FILE2=question2.txt
export FILE3=question3.txt

gsutil rm gs://$BUCKET/$FILE1
gsutil rm gs://$BUCKET/$FILE2
gsutil rm gs://$BUCKET/$FILE3

gsutil copy $FILE1 gs://$BUCKET
gsutil copy $FILE2 gs://$BUCKET
gsutil copy $FILE3 gs://$BUCKET
