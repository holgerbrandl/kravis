
## Api Pointers


layer with https://vega.github.io/vega-lite/docs/layer.html


https://vega.github.io/vega/examples/box-plot/


https://stackoverflow.com/questions/41861449/kotlin-dsl-for-creating-json-objects-without-creating-garbage


---
Generate Plain Old Java Objects from JSON or JSON-Schema.

http://www.jsonschema2pojo.org/


So with https://vega.github.io/vega-lite/docs/spec.html we can use https://vega.github.io/schema/vega-lite/v2.json

```bash
cd /Users/brandl/projects/kotlin/kravis/misc/vega_playground
wget  https://vega.github.io/schema/vega-lite/v2.json
brew install jsonschema2pojo
jsonschema2pojo https://vega.github.io/schema/vega-lite/v2.json

jsonschema2pojo --source v2.json --target java-gen


```

---
other way round
https://github.com/FasterXML/jackson-module-jsonSchema Module for generating JSON Schema (v3) definitions from POJOs
                                                       *



---
https://github.com/vegas-viz/Vegas


---
Vegas-vis

https://github.com/vegas-viz/Vegas

can be used with https://github.com/jupyter-scala/jupyter-scala



```
sbt publishLocal

./jupyter-scala --id scala --name "Scala" --force

#cool
jupyter console --kernel scala

jupyter console --kernel scala



## interactive plot development with
http://visdown.com/

```

api entrypoint for testing vegas.util.Look


## plot examples

nice grouped barchart
https://vega.github.io/vega/examples/grouped-bar-chart/



## misc


Is it possible to embed chrome browser in JavaFx application? Yes JCEF

https://stackoverflow.com/questions/32200511/is-it-possible-to-change-webview-of-javafx-to-chrome