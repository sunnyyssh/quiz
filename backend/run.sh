#!/bin/bash

docker run -d \
  --name pg-quiz \
  -e POSTGRES_USER=quiz \
  -e POSTGRES_PASSWORD=secret \
  -e POSTGRES_DB=quiz \
  -p 6432:5432 \
  postgres:latest

export PG_CONN_STRING="postgres://quiz:secret@0.0.0.0:6432/quiz"

go run .