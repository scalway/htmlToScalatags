package com.scalway

import scala.util.matching.Regex

/**
  * Created by slovic on 26.01.16.
  */
class HtmlToScalatags(val tagPref:String ="", val attrPref:String="", commaOnEndOfLine:Boolean=true) {
  private val alwaysClosedTags = Seq("meta", "link", "img")

  private val literalReg = "[a-zA-Z]+[a-zA-Z1-9-]*"
  private val attributeReg = s"""($literalReg)=('[^']*'|"[^"]*")"""
  private val innertagReg = s"""($literalReg)(\\s*(?:${attributeReg}\\s*)*)"""
  private val preModifier = if (commaOnEndOfLine) "" else ","
  private val postModifier = if (commaOnEndOfLine) "," else ""


  private def makeTag(tag:String, attrs:String, closeTag:Boolean) = {
    println(s"makeTag($tag, $attrs, $closeTag)")
    var closeText = ""
    if (closeTag ==true) closeText = ")" + postModifier

    preModifier + tagPref + tag + "(" + flatAtributs(attrs) + closeText
  }

  private def codeStepReplaceHeadTags(str: String) = s"<$innertagReg>".r.replaceAllIn(str, reg =>
    makeTag(reg.group(1), reg.group(2), alwaysClosedTags.contains(reg.group(1)))
  )

  private def flatAtributs(str: String) = attributeReg.r
    .replaceAllIn(str,
      attr => preModifier + attrPref + normalizeAtributeName(attr.group(1)) + ":=\"" + attr.group(2).substring(1, attr.group(2).length()-1) +  '"' + postModifier)

  private def normalizeAtributeName(attributeName: String) = attributeName match {
    case "class" => "cls"
    case "type" => "tpe"
    case "for" => "`for`"
    case otherwise => attributeName.replaceAll("-", ".")
  }


  private def codeStepReplaceCloseTags(str: String)     = {
    s"</($literalReg)>".r.replaceAllIn(str, s => {
      println(s"codeStepReplaceCloseTags(${s.group(1)})")
      s")" + postModifier
    })
  }
  private def codeStepReplaceSelfClosedTags(str:String) = {
    s"<$innertagReg/>".r.replaceAllIn(str, reg => {
      println(s"codeStepReplaceSelfClosedTags(${reg.group(1)}, ${reg.group(2)})")
      makeTag(reg.group(1), reg.group(2), true)
    })
  }

  private def codeStepRemoveCodeAnomalies(str: String) = {
    //remove all "(," or ",)" anomalies

    val str2 = (s"$postModifier(\\s*)\\)").r.replaceAllIn(str, ss => {
      println(s"codeStepRemoveCodeAnomaliesPost(${ss.group(1)})")
        if (ss.group(1) != null) ss.group(1) + ")" else ")"
      })

      (s"\\((\\s*)$preModifier").r.replaceAllIn(str2, ss => {
        println(s"codeStepRemoveCodeAnomaliesPre(${ss.group(1)})")
        if (ss.group(1) != null) "(" + ss.group(1) else "("
      })
  }

  private def codeStepWrapInnerStrings(str: String) =
    ">(\\s*[^<>\\s$]+[^<>$]*)<".r.replaceAllIn(str,
      s => {
        println(s"codeStepWrapInnerStrings(${s.group(1)})")
        val text =  s.group(1)
        val trimtext = text.trim
        val wrap = if(trimtext.contains('\n') || text.contains('"') ) "\"\"\"" else "\""
        ">" + preModifier + text.takeWhile(_.isWhitespace) + wrap + trimtext + wrap + text.substring(text.lastIndexWhere(!_.isWhitespace) + 1) + postModifier +"<"
      }
    )

  private def codeStepWrapAllCommentsAsStrings(str: String) =
    "<!--.*-->".r.replaceAllIn(str,
      s => {
        val text =  s.group(0)
        val trimtext = text.trim
        val wrap = if(text == null || trimtext.contains('\n') || text.contains('"') ) "\"\"\"" else "\""
        preModifier + text.takeWhile(_.isWhitespace) + wrap + trimtext + wrap + text.substring(text.lastIndexWhere(!_.isWhitespace) + 1) + postModifier
      }
    )

  val parse = {
    codeStepWrapInnerStrings _ andThen
    codeStepWrapAllCommentsAsStrings _ andThen
    codeStepReplaceCloseTags _ andThen
    codeStepReplaceSelfClosedTags _ andThen
    codeStepReplaceHeadTags _ andThen
    codeStepRemoveCodeAnomalies _
  }
}

