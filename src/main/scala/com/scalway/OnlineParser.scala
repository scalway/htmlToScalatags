package com.scalway

import org.scalajs.dom
import org.scalajs.dom.raw.MouseEvent
import scalatags.JsDom.tags2.nav
import scalatags.JsDom.all._
import scalatags.JsDom.{attrs => ^, tags2 => <, TypedTag}
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

  val htmlTextArea = textarea(cls:="col-md-6", style:="min-height:300px").render
  val result       = textarea(cls:="col-md-6", style:="min-height:300px").render

  val submit       = button(
      cls:="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect mdl-button--accent" ,
      "translate"
    ).render



  def b3HeaderMenu(header:Tag, menuItems:TypedTag[dom.html.LI]*) = {
    nav( cls:="navbar navbar-inverse navbar-fixed-top",
      div( cls:="container",
        div( cls:="navbar-header",
          button( tpe:="button", cls:="navbar-toggle collapsed", data.toggle:="collapse", data.target:="#navbar", aria.expanded:="false", aria.controls:="navbar",
            span( cls:="sr-only", "Toggle navigation"),
            span(cls:="icon-bar"),
            span(cls:="icon-bar"),
            span(cls:="icon-bar")
          ),
          header(cls:="navbar-brand")
        ),
        div( id:="navbar", cls:="collapse navbar-collapse",
          ul( cls:="nav navbar-nav",
            menuItems.toSeq
          )
        )
      )
    )
  }

  def mainPageView() = div(
    b3HeaderMenu(
      a(href:="#", "HTML => SCALATAGS")
//      ,li(cls:="active", a(href:="#","Home")),
//      ,li(a(href:="#about", "About")),
//      ,li(a(href:="#contact", "Contact"))
    ),
    div( cls:="container"
      ,style:="padding-top:60px;"
      ,div( cls:="row"
        ,h1("HTML to SCALATAGS ONLINE CONVERTER", br(), small("Everyone sometimes want's to copy/paste something :)"))
        ,p( cls:="lead", "Use this console to create scalatags code just from html tags. It isn't perfect tool but it works pretty well. " +
          "You'll be probably obligated to make some additionals changes after convertion.")
      )
      ,div(cls:="row"
         ,htmlTextArea
         ,result
      )
      ,div(cls:="row",(div(cls:="text-center",submit)))
    )
  )
}
