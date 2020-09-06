package yncrea.isen.cir3.view.impl

import org.apache.logging.log4j.kotlin.Logging
import yncrea.isen.cir3.ctrl.MyArticleDefaultController
import yncrea.isen.cir3.model.data.ArticleData
import yncrea.isen.cir3.model.data.LeMondeData
import yncrea.isen.cir3.view.IMyArticlesView
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.GridLayout
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.net.URL
import javax.imageio.ImageIO
import javax.swing.*
import javax.swing.SpinnerNumberModel
import javax.swing.JSpinner
import com.sun.deploy.uitoolkit.ToolkitStore.dispose
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.awt.Image.SCALE_SMOOTH
import javax.swing.AbstractButton









class MyView : IMyArticlesView,ActionListener {
    companion object : Logging

    private val frame: JFrame
    private val controller: MyArticleDefaultController

    private var headlinesInfos : JPanel
    private var articlesInfos : JPanel

    private val pageField : JTextField

    constructor(ctrl: MyArticleDefaultController, title: String) {
        this.pageField = JTextField()
        this.headlinesInfos = JPanel()
        this.articlesInfos = JPanel()

        this.frame = JFrame()
        this.frame.contentPane = makeUI()
        this.frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        this.frame.preferredSize = Dimension(600, 500)
        this.frame.title = title
        this.frame.pack()

        this.controller = ctrl
        this.controller.registerView(this)
        //this.headlinesInfos = JPanel(BorderLayout())
        //this.articlesInfos = JPanel(BorderLayout())

    }
    override fun display() {
        this.frame.isVisible = true
    }

    override fun close() {
        this.frame.isVisible = false
        this.frame.dispose()
    }

    private fun makeUI(): JPanel{
        val contentPane = JPanel()

        contentPane.layout = BorderLayout()
        contentPane.add(createUIForUpdateArticles(), BorderLayout.NORTH)
        contentPane.add(createUIForHeadlines(), BorderLayout.WEST)
        contentPane.add(createUIForPageableArticles(), BorderLayout.EAST)

        return contentPane
    }

    private fun createUIForUpdateArticles(): JPanel{
        val panelDataEnter = JPanel()
        panelDataEnter.layout = GridLayout(1,3)

        val button1 = JButton("Get Headlines")
        button1.addActionListener(this)
        panelDataEnter.add(button1)

        panelDataEnter.add(JLabel("page n°"))
        System.out.println(pageField)
        panelDataEnter.add(pageField)
        val button2 = JButton("Get Articles")
        button2.addActionListener(this)
        panelDataEnter.add(button2)

        return panelDataEnter
    }

    private fun createUIForHeadlines(): JPanel{
        return headlinesInfos
    }

    private fun createUIForPageableArticles(): JPanel{
        return articlesInfos
    }

    private fun createItemOfHeadlines(data: ArticleData): JPanel {
        val result = JPanel(FlowLayout())

        val url = URL(data.urlToImage)
        val img : BufferedImage = ImageIO.read(url)
        var imgResized = this.resize(img,30,80)

        val label = JLabel(ImageIcon(imgResized))
        result.add(label)

        val gridinfo = JPanel(GridLayout(5,1))
        gridinfo.add(JLabel("${data.title}"))
        gridinfo.add(JLabel("Auteur : ${data.author}"))
        gridinfo.add(JLabel("Le ${data.publishedAt}"))
        val content : JLabel = JLabel("${data.content}")
        content.setSize(150,10)
        gridinfo.add(content)
        gridinfo.add(JLabel("Lien : ${data.url}"))
        result.add(gridinfo)
        return result
    }

    private fun createItemOfPageableArticles(data: ArticleData): JPanel {
        val result = JPanel(FlowLayout())

        val url = URL(data.urlToImage)
        val img : BufferedImage = ImageIO.read(url)
        var imgResized = this.resize(img,30,80)

        val label = JLabel(ImageIcon(imgResized))
        result.add(label)

        val gridinfo = JPanel(GridLayout(5,1))
        gridinfo.add(JLabel("${data.title}"))
        gridinfo.add(JLabel("Auteur : ${data.author}"))
        gridinfo.add(JLabel("Le ${data.publishedAt}"))
        gridinfo.add(JTextArea("${data.content}"))
        gridinfo.add(JLabel("Lien : ${data.url}"))
        result.add(gridinfo)
        return result
    }

    private fun updateArticlesInfos(data: LeMondeData) {
        val paneAllItem = JPanel(GridLayout(data.articles.size,1))

        for(item:ArticleData in data.articles){
            logger.debug("updateArticles $item")
            paneAllItem.add(createItemOfPageableArticles(item))
        }

        val scrollPane = JScrollPane(paneAllItem)
        articlesInfos.add(scrollPane, BorderLayout.EAST)
        articlesInfos.revalidate()
    }

    private fun updateArticlesHeadlinesInfos(data: LeMondeData) {
        val paneAllItem = JPanel(GridLayout(data.articles.size,1))
        System.out.println("coucou")
        for(item:ArticleData in data.articles){
            logger.debug("updateArticlesHeadlines $item")
            paneAllItem.add(createItemOfHeadlines(item))
        }

        val scrollPane = JScrollPane(paneAllItem)
        headlinesInfos.add(scrollPane, BorderLayout.CENTER)

        headlinesInfos.revalidate()
    }

    override fun actionPerformed(e: ActionEvent?) {
        logger.debug("ActionEvent :  ${e.toString()}")
        val sourceJButton  = e?.source as JButton
        System.out.println(sourceJButton.toString())
        if(sourceJButton.text.equals("Get Headlines"))
            controller.findHeadlines()
        else if(sourceJButton.text.equals("Get Articles"))
        {
            when (pageField.getText()) {
                "1","2","3","4","5" -> controller.findArticles(pageField.getText().toInt())
                else -> { // Note the block
                    logger.warn("Page impossible = ${pageField.getText()}")
                }
            }
        }

    }

    override fun updateArticles(data: Any) {
        if (data is LeMondeData) {
            logger.info("receive articles data")
            updateArticlesInfos(data)
        }
    }

    override fun updateArticlesHeadlines(data: Any) {
        if (data is LeMondeData) {
            logger.info("receive articles data")
            updateArticlesHeadlinesInfos(data)
        }
    }

    private fun resize(img: BufferedImage, height: Int, width: Int): BufferedImage {
        val tmp = img.getScaledInstance(width, height,SCALE_SMOOTH)
        val resized = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val g2d = resized.createGraphics()
        g2d.drawImage(tmp, 0, 0, null)
        g2d.dispose()
        return resized
    }
}