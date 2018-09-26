# Rserve Container for `kravis`


## How to start it?

```bash
# docker run -p <public_port>:<private_port> -d <image>  
docker run -dp 6302:6311 kravis_rserve

## and make sure that it is up and running
docker ps -a
```

Or reroute exposed port to a different port as needed.



## Deployment

```bash
#cd /Users/brandl/projects/kotlin/kravis/misc/docker/kravis_rserve

docker build -t kravis_rserve .

docker tag kravis_rserve holgerbrandl/kravis_rserve:3.5.1

docker login

#https://stackoverflow.com/questions/41984399/denied-requested-access-to-the-resource-is-denied-docker
docker push holgerbrandl/kravis_rserve:3.5.1

## create latest tag
docker tag kravis_rserve holgerbrandl/kravis_rserve
docker push holgerbrandl/kravis_rserve
```

## Misc

### Overriding Entrypoint

You can override entrypoint instructions using the [docker run --entrypoint](https://docs.docker.com/engine/reference/run/#entrypoint-default-command-to-execute-at-runtime) flag.

### How to keep it run running after PID is done

https://stackoverflow.com/questions/25775266/how-to-keep-docker-container-running-after-starting-services


### Other docker images running Rserve

https://github.com/stevenpollack/docker-rserve


### Expose vs publish

https://medium.freecodecamp.org/expose-vs-publish-docker-port-commands-explained-simply-434593dbc9a3

EXPOSE tells Docker the running container listens on specific network ports. This acts as a kind of port mapping documentation that can then be used when publishing the
You can also specify this within a docker [run](https://docs.docker.com/engine/reference/run/) command, such as:{#d1d7}
```
docker run `--expose=1234 my_app`
```
But **EXPOSE will not** allow communication via the defined ports to containers outside of the same [network](https://docs.docker.com/network/) or to the host machine. To allow this to happen you need to *publish* the ports.

To publish all the ports you define in your Dockerfile with `EXPOSE` and bind them to the host machine, you can use the `-P` flag.{#3aa4}

https://stackoverflow.com/questions/22111060/what-is-the-difference-between-expose-and-publish-in-docker

1) If you do not specify neither `EXPOSE` nor `-p`, the service in the container will only be accessible from *inside* the container itself.

2) If you `EXPOSE` a port, the service in the container is not accessible from outside Docker, but from inside other Docker containers. So this is good for inter-container communication.

3) If you `EXPOSE` and `-p` a port, the service in the container is accessible from anywhere, even outside Docker.


## Todo

* ensure proper sandboxing similar to https://github.com/JanWielemaker/rserve-sandbox


## Testing

```bash
#docker run --rm -it rocker/tidyverse:3.5.1 /bin/bash --login
#docker run --rm -it kravis_rserve /bin/bash --login

docker run --rm -it  -p 6302:6311 --entrypoint /bin/bash kravis_rserve --login
docker run --rm -it  -p 6311:6311 --entrypoint /bin/bash kravis_rserve --login
docker run --rm -it  --entrypoint /bin/bash kravis_rserve --login
# [R CMD Rserve --no-save --RS-enable-remote]()

docker logs $(docker ps -a -q | head -n1)

docker run -d -p 6302:6311 kravis_rserve
#docker run -d -p 6302:6311 kravis_rserve:latest

#docker run -d -p 6311:6300  --restart=always --name kravis_rserve kravis_rserve

lsof -nP -i4TCP:6311
```


