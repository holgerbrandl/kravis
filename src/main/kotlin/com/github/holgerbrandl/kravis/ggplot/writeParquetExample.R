devtools::source_url("https://raw.githubusercontent.com/holgerbrandl/datautils/v1.50/R/core_commons.R")


load_pack(sparklyr)

# https://spark.rstudio.com/reference/spark_write_parquet/

spark_write_parquet(iris, "test.pqt")