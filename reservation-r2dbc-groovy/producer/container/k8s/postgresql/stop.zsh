#!/usr/bin/zsh

k delete service postgres
k delete deployment postgres
k delete configmap postgres-config
k delete persistentvolumeclaim postgres-pv-claim
k delete persistentvolume postgres-pv-volume