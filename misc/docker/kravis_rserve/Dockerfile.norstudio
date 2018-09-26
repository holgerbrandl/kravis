# copy of https://github.com/stevenpollack/docker-rserve/blob/master/dockerfiles/docker-rserve
# vim:set ft=dockerfile:
FROM rocker/r-base

MAINTAINER "Steven Pollack" steven@gnobel.com

RUN install2.r --error \
    -r "https://cran.rstudio.com" \
    Rserve && \
    rm -rf /tmp/downloaded_packages/ /tmp/*.rds

# you have to run Rserve with remote=TRUE otherwise
# it won't let you connect to the container
EXPOSE 6311
ENTRYPOINT R -e "Rserve::run.Rserve(remote=TRUE)" 
