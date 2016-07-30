#!/bin/bash

HOOKS_DIR=./git/hooks/*

echo "****************GIT HOOKS SETUP**********************"
for file in $HOOKS_DIR 
do
    echo "create symlink :" $file
    ln -s -f "$(pwd)${file:1}" ./.git/hooks/${file:12}
done
echo "************INSTALLATION SUCCESSFUL*******************"
exit 0
