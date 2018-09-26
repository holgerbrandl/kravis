FROM rocker/tidyverse:3.5.1


# svg-support we need cairo
RUN apt-get update && apt-get install --yes libcairo2-dev
RUN R -e "devtools::install_github('davidgohel/gdtools')"
RUN R -e "install.packages('svglite')"

#ENTRYPOINT R
