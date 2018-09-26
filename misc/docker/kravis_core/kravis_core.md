# Rendering Container for `kravis`


## Deployment

```bash
#cd /Users/brandl/projects/kotlin/kravis/misc/docker/kravis_core

docker build -t kravis_core .

docker tag kravis_core holgerbrandl/kravis_core:3.5.1

docker login

#https://stackoverflow.com/questions/41984399/denied-requested-access-to-the-resource-is-denied-docker
docker push holgerbrandl/kravis_core:3.5.1

## create latest tag
docker tag kravis_rserve holgerbrandl/kravis_core
docker push holgerbrandl/kravis_core
```
