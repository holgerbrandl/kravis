FROM rocker/tidyverse:3.5.1

#todo dont inherit from tidyvers but build custom image without rstudio service
#https://hub.docker.com/r/rocker/tidyverse/~/dockerfile/

#RUN install2.r --error --deps TRUE Rserve
## build from source because of https://github.com/s-u/Rserve/issues/102
RUN R -e "install.packages('Rserve',,'http://rforge.net/',type='source')"


# see https://www.rforge.net/Rserve/doc.html
#R CMD Rserve --no-save --RS-enable-remote
#CMD ["R", "CMD", "Rserve", "--no-save", "--RS-enable-remote"]
#RUN R CMD Rserve --no-save --RS-enable-remote

# you have to run Rserve with remote=TRUE otherwise
# it won't let you connect to the container
EXPOSE 6311

ENTRYPOINT R -e "Rserve::run.Rserve(remote=TRUE)"
