mvn clean package spring-boot:repackage

if [[ "$?" -ne 0 ]] ; then
  echo 'Maven build failed'; exit $rc
fi

docker-compose up --build