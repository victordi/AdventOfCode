#!/bin/bash

if [ -z "$1" ]; then
  echo "Usage: $0 <new_package>"
  exit 1
fi

dest_dir="src/main/kotlin/$1"

cp -r "src/main/kotlin/template" "$dest_dir"

find "$dest_dir" -name "main.kt" | while read -r file; do
  sed -i '' "1s/package template/package $1/" "$file"
done
