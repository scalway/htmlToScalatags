package com.scalway

import scala.util.matching.Regex

/**
  * Created by slovic on 26.01.16.
  */
class HtmlToScalatags(val tagPref:String ="", val attrPref:String="") {
  private val alwaysClosedTags = Seq("meta", "link")

  private val literalReg = "[a-zA-Z]+[a-zA-Z1-9-]*"
  private val attributeReg = s"""($literalReg)=('[^']*'|"[^"]*")"""
  private val innertagReg = s"""($literalReg)[\\s]*((?:${attributeReg}[\\s]*)*)"""

  private def makeTag(tag:String, attrs:String, close:Boolean) =
    ", " + tagPref + tag + "(" + flatAtributs(attrs) + (if(close) ")" else "")


  private def codeStepReplaceHeadTags(str: String) = s"<$innertagReg>".r.replaceAllIn(str, reg =>
    makeTag(reg.group(1), reg.group(2), alwaysClosedTags.contains(reg.group(1)))
  )

  private def flatAtributs(str: String) = attributeReg.r
    .findAllMatchIn(str)
    .map(attr => attrPref + normalizeAtributeName(attr.group(1)) + ":=\"" + attr.group(2).substring(1, attr.group(2).length()-1) + '"')
    .mkString(", ")

  private def normalizeAtributeName(attributeName: String) = attributeName match {
    case "class" => "cls"
    case "type" => "tpe"
    case otherwise => attributeName.replaceAll("-", ".")
  }


  private def codeStepReplaceCloseTags(str: String)     = s"</$literalReg>".r.replaceAllIn(str, s => s")")
  private def codeStepReplaceSelfClosedTags(str:String) = s"<$innertagReg/>".r.replaceAllIn(str, reg =>
    makeTag(reg.group(1), reg.group(2), true)
  )

  private def codeStepRemoveWrongCodeArtefacts(str: String) =
    "\\((\\s*)\\,".r.replaceAllIn(str, ss => {
      if (ss.group(1) != null) "(" + ss.group(1) else "("
    })

  private def codeStepWrapInnerStrings(str: String) =
    ">(\\s*[^<>\\s$]+[^<>$]*)<".r.replaceAllIn(str,
      s => {
        val text =  s.group(1)
        val trimtext = text.trim
        val wrap = if(trimtext.contains('\n') || text.contains('"') ) "\"\"\"" else "\""
        ">," + text.takeWhile(_.isWhitespace) + wrap + trimtext + wrap + text.substring(text.lastIndexWhere(!_.isWhitespace) + 1) +"<"
      }
    )

  private def codeStepWrapAllCommentsAsStrings(str: String) =
    "<!--.*-->".r.replaceAllIn(str,
      s => {
        val text =  s.group(0)
        val trimtext = text.trim
        val wrap = if(text == null || trimtext.contains('\n') || text.contains('"') ) "\"\"\"" else "\""
        "," + text.takeWhile(_.isWhitespace) + wrap + trimtext + wrap + text.substring(text.lastIndexWhere(!_.isWhitespace) + 1)
      }
    )

  val parse = {
    codeStepWrapInnerStrings _ andThen
    codeStepWrapAllCommentsAsStrings _ andThen
    codeStepReplaceSelfClosedTags _ andThen
    codeStepReplaceCloseTags _ andThen
    codeStepReplaceHeadTags _ andThen
    codeStepRemoveWrongCodeArtefacts _
  }
}

