package com.scalway



import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

/**
  * Created by slovic on 27.01.16.
  */
@JSExport
class ConsoleParser extends JSApp {
  @JSExport
  override def main(): Unit = {
    val parser = new HtmlToScalatags()
    val text =
      """
      |<!doctype html>
      |<html>
      |
      |<head>
      |    <meta charset="utf-8">
      |    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
      |    <title>HTML 2 Jade - a converter for HTML</title>
      |    <link
      |       type="text/css"
      |       rel="stylesheet"
      |       href="/site.css"
      |    />
      |    <title></title>
      |</head>
      |
      |<body>
      |    <h1>Hello world!</h1>
      |</body>
      |
      |</html>
      |
    """.stripMargin

    println(parser.parse(text))
  }
}
