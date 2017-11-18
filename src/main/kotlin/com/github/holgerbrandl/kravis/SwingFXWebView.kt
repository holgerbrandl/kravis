package com.github.holgerbrandl.kravis

/**
 * @author Holger Brandl
 */

import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.web.WebEngine
import javafx.scene.web.WebView
import javafx.stage.Stage
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities

/**
 * SwingFXWebView
 */
class SwingFXWebView : JPanel() {

    private var stage: Stage? = null
    var browser: WebView? = null
    private var jfxPanel: JFXPanel? = null
    private var swingButton: JButton? = null
    private var webEngine: WebEngine? = null

    init {
        initComponents()
    }

    private fun initComponents() {

        jfxPanel = JFXPanel()
        createScene()

        layout = BorderLayout()
        add(jfxPanel!!, BorderLayout.CENTER)

        swingButton = JButton()
        swingButton!!.addActionListener { Platform.runLater { webEngine!!.reload() } }
        swingButton!!.text = "Reload"

        add(swingButton!!, BorderLayout.SOUTH)
    }

    /**
     * createScene
     *
     * Note: Key is that Scene needs to be created and run on "FX user thread"
     * NOT on the AWT-EventQueue Thread
     *
     */
    private fun createScene() {
        //        PlatformImpl.startup {
        Platform.runLater {
            stage = Stage()

            stage!!.title = "Hello Java FX"
            stage!!.isResizable = true

            val root = Group()
            val scene = Scene(root, 80.0, 20.0)
            stage!!.scene = scene

            // Set up the embedded browser:
            browser = WebView()
            webEngine = browser!!.engine
            webEngine!!.load("http://www.google.com")

            val children = root.children
            children.add(browser)

            jfxPanel!!.scene = scene
        }
    }


    fun loadPage(content: String) {
        Platform.runLater {
            browser?.engine?.loadContent(content, "text/html")
        }
    }

    fun showInPanel() {
        SwingUtilities.invokeLater {
            val frame = JFrame()

            frame.contentPane.add(this)

            frame.minimumSize = Dimension(640, 480)
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            frame.isVisible = true
        }
    }
}

fun main(args: Array<String>) {
    // Run this later:
    val swingFXWebView by lazy { SwingFXWebView() }

    swingFXWebView.showInPanel()

    Thread.sleep(8000)

    swingFXWebView.loadPage("<html>hello, world</html>")
}


