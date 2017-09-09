#/bin/bash

cd angular
npm install
ng build env=prod

cd ..
mvn install