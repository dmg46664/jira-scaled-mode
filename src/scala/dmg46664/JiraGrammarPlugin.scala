package dmg46664

import scaled.{EditorConfig, List, Map, Plugin, Syntax}
import scaled.grammar.GrammarPlugin

/**
  * For a more complex example see NDFGrammarPlugin
  */
@Plugin(tag="textmate-grammar")
class JiraGrammarPlugin extends GrammarPlugin {
  import EditorConfig._
  import JiraConfig._

  override def grammars = Map("source.jira" -> "JIRA.ndf")

  override def effacers = List(
    effacer("tick", jiraCssTick),
    effacer("cross", jiraCssCross)
  )

  override def syntaxers = List(
  )
}
