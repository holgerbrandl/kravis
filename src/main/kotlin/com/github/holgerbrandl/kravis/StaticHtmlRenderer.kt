package com.github.holgerbrandl.kravis

/**
 * Adopted from vegas.render.StaticHTMLRenderer
 * @author HolgerAish Fenton.
 */
class StaticHTMLRenderer(val specJson: String) {

    fun importsHTML(additionalImports: List<String>): String {
        return (JSImports + additionalImports).map {
            "<script src=\"" + it + "\" charset=\"utf-8\"></script>"
        }.joinToString("\n")
    }

    fun headerHTML(vararg additionalImports: String) =
        """
       |<html>
       |  <head>
       |    ${importsHTML(additionalImports.toList())}
       |  </head>
       |  <body>
    """.trimMargin("|")

    fun plotHTML(name: String = this.defaultName): String {
        return """
           | <div id='$name'></div>
           | <script>
           |   var embedSpec = {
           |     mode: "vega-lite",
           |     spec: $specJson
           |   }
           |   vg.embed("#$name", embedSpec, function(error, result) {});
           | </script>

        """.trimMargin("|")
    }

    val footerHTML =
        """
      |  </body>
      |</html>
    """.trimMargin("|")

    fun pageHTML(name: String = defaultName) = headerHTML().trim() + plotHTML(name) + footerHTML.trim()

    /**
     * Typically you'll want to use this method to render your chart. Returns a full page of HTML wrapped in an iFrame
     * for embedding within existing HTML pages (such as Jupyter).
     * XXX Also contains an ugly hack to resize iFrame height to fit chart, if anyone knows a better way open to suggestions
     * @param name The name of the chart to use as an HTML id. Defaults to a UUID.
     * @return HTML containing iFrame for embedding
     */
    fun frameHTML(name: String = defaultName) {
        // https://stackoverflow.com/questions/1265282/recommended-method-for-escaping-html-in-java
        val frameName = "frame-" + name
        """
        <iframe id="${frameName}" sandbox="allow-scripts allow-same-origin" style="border: none; width: 100%" srcdoc="${escapeHTML(pageHTML(name))}"></iframe>
        <script>
          (function() {
            function resizeIFrame(el, k) {
              var height = el.contentWindow.document.body.scrollHeight || '400'; // Fallback in case of no scroll height
              el.style.height = height + 'px';
              if (k <= 10) { setTimeout(function() { resizeIFrame(el, k+1) }, 1000 + (k * 250)) };
            }
            resizeIFrame(document.querySelector('#${frameName}'), 1);
          })(); // IIFE
        </script>
    """.trimMargin()
    }

    fun escapeHTML(s: String): String {
        val out = StringBuilder(Math.max(16, s.length))
        for (i in 0 until s.length) {
            val c = s[i]
            if (c.toInt() > 127 || c == '"' || c == '<' || c == '>' || c == '&') {
                out.append("&#")
                out.append(c.toInt())
                out.append(';')
            } else {
                out.append(c)
            }
        }
        return out.toString()
    }


    private val WebJars = mapOf(
        "vega-lite" to "1.2.0",
        "vega" to "2.6.3",
        "d3" to "3.5.17"
    )

    private fun CDN(artifact: String, file: String) = "https://cdn.jsdelivr.net/webjars/org.webjars.bower/$artifact/${WebJars[artifact]}/$file"

    val JSImports = listOf(
        CDN("d3", "d3.min.js"),
        CDN("vega", "vega.min.js"),
        CDN("vega-lite", "vega-lite.min.js"),
        "https://vega.github.io/vega-editor/vendor/vega-embed.js"
    )

    val defaultName = "vegas-" + java.util.UUID.randomUUID().toString()

}

fun main(args: Array<String>) {
    val pageHTML = StaticHTMLRenderer("""
        {
  "data": {
    "values": [
      {"a": "C", "b": 2}, {"a": "C", "b": 7}, {"a": "C", "b": 4},
      {"a": "D", "b": 1}, {"a": "D", "b": 2}, {"a": "D", "b": 6},
      {"a": "E", "b": 8}, {"a": "E", "b": 4}, {"a": "E", "b": 7}
    ]
  },
  "mark": "bar",
  "encoding": {
    "x": {"field": "a", "type": "nominal"},
    "y": {"aggregate": "average", "field": "b", "type": "quantitative"}
  }
}
        """).pageHTML()

    System.err.println(pageHTML)

    SwingFXWebView().apply {
        showInPanel()
        Thread.sleep(3000)
        loadPage(pageHTML)
    }
}
