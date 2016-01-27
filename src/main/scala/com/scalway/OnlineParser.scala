package com.scalway

import org.scalajs.dom
import org.scalajs.dom.raw.MouseEvent
import scalatags.JsDom.tags2.nav
import scalatags.JsDom.all._

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

/**
  * Created by slovic on 27.01.16.
  */
@JSExport
class OnlineParser extends JSApp {
  @JSExport
  override def main(): Unit = {
    val parser = new HtmlToScalatags()
    submit.onclick = (e:MouseEvent) => {
      result.value = parser.parse(htmlTextArea.value)
    }

    dom.document.body.appendChild(mainPageView.render)
  }

  val htmlTextArea = textarea(rows:="4", cols:="50").render
  val submit       = button("translate").render
  val result       = textarea(rows:="4", cols:="50").render


  def mainPageView() = div(
    nav( cls:="navbar navbar-inverse navbar-fixed-top",
      div( cls:="container",
        div( cls:="navbar-header",
          button( tpe:="button", cls:="navbar-toggle collapsed", data.toggle:="collapse", data.target:="#navbar", aria.expanded:="false", aria.controls:="navbar",
            span( cls:="sr-only", "Toggle navigation"),
            span(cls:="icon-bar"),
            span(cls:="icon-bar"),
            span(cls:="icon-bar")
          ),
          a(cls:="navbar-brand", href:="#", "Project name")
        ),
        div( id:="navbar", cls:="collapse navbar-collapse",
          ul( cls:="nav navbar-nav",
            li(cls:="active", a(href:="#","Home")),
            li( a(href:="#about", "About")),
            li( a(href:="#contact", "Contact"))
          )
        )
      )
    ),
    div( cls:="container",
      div( cls:="starter-template"
        ,h1("Bootstrap starter template")
        ,p( cls:="lead", "Use this document as a way to quickly start any new project. All you get is this text and a mostly barebones HTML document.")
        ,htmlTextArea
        ,submit
        ,result
      )
    )
  )
}
