#!/usr/bin/zsh

k create -f ./yml/postgresql-config.yaml
k create -f ./yml/postgresql-storage.yaml
k create -f ./yml/postgresql-deployments.yaml
k create -f ./yml/postgresql-service.yaml

k port-forward postgresql 5432:5432