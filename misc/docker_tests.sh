#!/usr/bin/env bash

docker pull rocker/tidyverse:3.5.1
docker run --rm -it rocker/tidyverse /bin/bash

cd /Users/brandl/projects/kotlin/kravis/misc/test_data

cat - <<"EOF" > test_plot.R
require(ggplot2)

gg = ggplot(mpg, aes(class, hwy)) + geom_boxplot(notch = TRUE, fill = "orchid3", colour = "#3366FF")
ggsave("results/plot.png", gg)
EOF




mkdir results

#https://stackoverflow.com/questions/42248198/how-to-mount-a-single-file-in-a-volume
docker run -v /Users/brandl/Desktop/Epigenomics_Roadmap_HM_ChIP-seq_table.txt:/data/table.txt --rm -it rocker/tidyverse:3.5.1 R --nosave -f test_plot.R

docker run \
    -v $(PWD):/scripts \
    -v $PWD/results:/results \
    -v /Users/brandl/Desktop/Epigenomics_Roadmap_HM_ChIP-seq_table.txt:/data/table.txt \
    --rm -it rocker/tidyverse:3.5.1 \
    R -f /scripts/test_plot.R
#    --rm -it rocker/tidyverse:3.5.1 bash


cat - <<"EOF" > test_calc.R
#print(1+1)
cat(1+1)
EOF

docker run \
    -v $(PWD):/scripts \
    --rm -it rocker/tidyverse:3.5.1 \
    R -f /scripts/test_calc.R


########################################################################################################################
## check presence of required docker image

## https://stackoverflow.com/questions/30543409/how-to-check-if-a-docker-image-with-a-specific-tag-exist-locally
if [[ "$(docker images -q rocker/tidyverse:3.5.2 2> /dev/null)" == "" ]]; then
echo missing image
fi