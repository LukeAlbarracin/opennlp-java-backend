#!/bin/bash

# set default ENV vars

export PROJECT=nla-cs125x-276023

gcloud config set project ${PROJECT}

export REGION=us-central
gcloud config set compute/region ${REGION}

export ZONE=us-central1-c
gcloud config set compute/zone ${ZONE}

gcloud config list
