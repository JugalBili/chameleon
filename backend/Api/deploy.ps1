param ($projectID)
docker tag chameleon-api northamerica-northeast2-docker.pkg.dev/$projectID/chameleon-api/chameleon-api
docker push northamerica-northeast2-docker.pkg.dev/$projectID/chameleon-api/chameleon-api:latest