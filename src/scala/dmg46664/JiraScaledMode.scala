package dmg46664

import scaled.code.Commenter
import scaled.{Env, Major}
import scaled.grammar.GrammarCodeMode

@Major( // Annotation stating that this is major mode
  name="jira", //Name of major mode
  //TODO bring the following back.
  //tags=Array("code", "project", "xml"),

  pats=Array(".*\\.jira"), // The pattern of files that trigger this major mode.
  desc="A major mode for editing Jira files.") //description
class JiraScaledMode (env :Env) extends GrammarCodeMode(env) {

  override def dispose () {} // nada for now

  override def langScope = "source.jira"

  override def stylesheets: scaled.List[String] = stylesheetURL("/jira.css") :: super.stylesheets

  /** We'll deal with this later */
  override val commenter = new Commenter() {
    override def blockOpen   = "<!--"
    override def blockClose  = "-->"
  }
}
