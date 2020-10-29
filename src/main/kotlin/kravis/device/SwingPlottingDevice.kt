package kravis.device

import kravis.GGPlot
import kravis.render.PlotFormat
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JDialog
import javax.swing.Timer
import javax.swing.WindowConstants


abstract class OutputDevice {
    protected abstract fun getPreferredFormat(): PlotFormat

    protected open fun getPreferredSize(): Dimension? = null

    abstract fun show(plot: GGPlot): Any
}


class SwingPlottingDevice : OutputDevice() {

    val history = listOf<GGPlot>().toMutableList()

    var curPlot: GGPlot? = null

    override fun getPreferredSize(): Dimension? = with(panel.imagePanel) { Dimension(width, height) }

    override fun getPreferredFormat() = PlotFormat.PNG

    override fun show(plot: GGPlot) {
        history.apply { add(plot); while (size > 10) removeAt(0) }

        renderInternally(plot)
    }

    fun renderInternally(plot: GGPlot) {
        curPlot = plot

        val imageFile = plot.save(createTempFile(suffix = getPreferredFormat().toString()), getPreferredSize())
        require(imageFile.exists()) { "Visualization Failed. Could not render image." }

        showImage(imageFile)
    }

    val panel by lazy {
        val plotResultPanel = PlotResultPanel()

        plotResultPanel.nextButton.addActionListener {
            renderInternally(history[Math.min(history.size - 1, history.indexOf(curPlot) + 1)])
        }

        plotResultPanel.lastButton.addActionListener {
            renderInternally(history[Math.max(0, history.indexOf(curPlot) - 1)])
        }


        // https://stackoverflow.com/questions/1281582/how-to-find-out-the-instance-when-component-resize-is-complete
        plotResultPanel.imagePanel.addComponentListener(object : ComponentAdapter() {

            /** Time to wait  */
            private val DELAY = 1000
            /** Waiting timer  */
            private var waitingTimer: javax.swing.Timer? = null

            /**
             * Handle resize event.
             */
            override fun componentResized(e: ComponentEvent) {
                if (this.waitingTimer == null) {
                    /* Start waiting for DELAY to elapse. */
                    this.waitingTimer = Timer(DELAY) { actionPerformed(it) }
                    this.waitingTimer!!.start()
                } else {
                    /* Event came too soon, swallow it by resetting the timer.. */
                    this.waitingTimer!!.restart()
                    // todo may we could show some "rerendering plot..." in the middle of the screen"
                }
            }

            var ignoredFirst = false
            /**
             * Actual resize method
             */
            fun applyResize() {
                if(!ignoredFirst){
                    ignoredFirst = true
                    return
                }
                if (curPlot != null) renderInternally(curPlot!!)
            }

            /**
             * Handle waitingTimer event
             */
            fun actionPerformed(ae: ActionEvent) {
                /* Timer finished? */
                if (ae.getSource() === this.waitingTimer) {
                    /* Stop timer */
                    this.waitingTimer!!.stop()
                    this.waitingTimer = null
                    /* Resize */
                    this.applyResize()
                }
            }
        })

        val frame = JDialog()
        frame.defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE

        frame.contentPane.add(plotResultPanel.mainPanel)
        frame.isVisible = true

        frame.setSize(600, 500)

        plotResultPanel
    }

    internal fun showImage(imageFile: File) {
        val img = ImageIO.read(imageFile); // eventually C:\\ImageTest\\pic2.jpg

        panel.imagePanel.setImage(img)
    }
}


fun main() {
    SwingPlottingDevice().showImage(File("/Users/brandl/Dropbox/sharedDB/fotos/2017-07-01 14.35.05.jpg"))
}