
```r
require(Rserve)
# ?Rserve
Rserve()

install.packages('Rserve',,"http://rforge.net/",type="source")

```

```bash
killall -INT Rserve

```


https://github.com/s-u/Rserve/issues/102


return types

```r
foo = try({ binImage <- readBin('/Users/brandl/Desktop/test.txt','raw',2024*2024); unlink('" + tempFileName + "'); binImage })

foo = readBin('/Users/brandl/Desktop/test.txt','raw',2024*2024)
class(foo)
```


https://github.com/stevenpollack/docker-rserve

In particualar
https://github.com/stevenpollack/docker-rserve/blob/master/dockerfiles/docker-rserve