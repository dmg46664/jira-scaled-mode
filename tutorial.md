## How to create a new grammar mode:

Hopefully this is the most basic tutorial TODO confirm.
* Create a new git repo of your desired name.
* Create a package.scaled file with all the details. This is needed by pacman to download and install the module. (Use xml-mode as a template).
```
 name: my-project-name
source: git:https://github.com/me/my-project-name.git
version: 1.0
descrip: My tester project
weburl: https://github.com/me/my-project-name
license: New BSD
depend: git:https://github.com/scaled/xml-mode.git
depend: git:https://github.com/scaled/textmate-grammar.git
module: test
```
* All dependencies and directory code layout is defined here.
```
spam install git:https://github.com/dmg46664/jira-scaled-mode.git
```
* If you run scaled now, after installing this it will fail because you included the test tree as a module which means you need the following:
* Presumably this means modules required for testing (which may not be the same as source)
```
ERROR:
If you install a mode without the necessary test/module.scaled, and then add it later,
the package manager will fail to build the local one and won’t be able to get the now available udpate.
```
* Correct for this and, you’ll be able to install it locally with something like:

* You could add the following files to make it work, OR simply remove the module line from your package file
```
my-project-name/test/
my-project-name/test/module.scaled
my-project-name/test/src/
```

_NOTE This particular module.scaled file is a build files.
 Making changes to source files however WILL NOT be picked up when rerunning scaled._

In order to try and build your package:
```
spam build my-project-name
```

This should work...

Now create a file ```src/scala/mypackage/MyMainMode.scaled```
```
package gerson

import scaled.grammar.GrammarCodeMode


@Major( // Annotation stating that this is major mode
  name="jira", //Name of major mode
  //TODO bring the following back.
  //tags=Array("code", "project", "xml"),

  pats=Array(".*\\.jira"), // The pattern of files that trigger this major mode.
  desc="A major mode for editing Jira files.") //description
class JiraMode (env :Env) extends GrammarCodeMode(env) {

  override def dispose () {} // nada for now

  override def langScope = "source.xml"

  override val commenter = new Commenter() {
    override def blockOpen   = "<!--"
    override def blockClose  = "-->"
  }
}
```

After you:
 * build this again
 * and run it
 * and open a ( C-x find-files ) find a file that ends with .jira

You'll see it opens and it understands XML syntax ... hooray!

## Adding your own markup rules.

What you've created in the previous section is specifically an xml editor leveraging the
XmlMode and TextMateGrammar modules of scaled.

Next we're going to add our own TextMateGrammar rule.

* Create a file `src/resources/JIRA.ndf`
* Create the following file `src/scala/dmg46664/JiraGrammarPlugin`
```
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
  )

  override def syntaxers = List(
  )
}
```
* Alter JiraScaledMode with `override def langScope = "source.jira"`
* Create `/src/scala/dmg46664/JiraMode`
```
package dmg46664

import scaled.Config

object JiraConfig extends Config.Defs {

  val jiraCssTick = "jiraCssTick"
}
```
* Create `/src/resources/JIRA.ndf`
```# For more see NDF.ndf
   name: JIRA formatting
   scopeName: source.jira
   repository:
   patterns:
    single:
     # Tick comment
     name: comment.line.ndf
     pattern: (\(\/\))
     caps: 1=tick.ndf
```
* Create `/src/resources/jira.css`
```
/**
 * Styles for jira-scaled-mode.
 */

.jiraCssTick {
  -fx-bg-fill: LightGreen;
}
```
* Rebuild and run

You should see that the jira file when opened has a tick highlighted in light green when the text `(/)` is used.

This should get you far enough to get started. Happy coding.

*Q. So how did what we initially write a working xml version before this example?*

The `langscope` override is meaningful! It represents a name that a textmate grammar plugin. This name is then looked up
by the mode to find the grammar plugin. The grammar plugin is also resonsible for mapping this name to a file which
contains the actual grammar rules.

Given that previously we were using `source.xml`, the grammar plugin used was that from XmlMode (and hence mostly functionally the same)

*Q. Does that mean a major mode can't register and use two distinct grammars?*
 TODO it would seem so?

## Making the grammar unhighlight

In this tutorial so far you've created a major mode that highlights a Jira markup tick in green :thumbsup:
However you'll notice that when you remove any character from the tick it doesn't go back to being regular text :thumbsdown:

This is because Scaled has created an `effacer` (handler) to mark blocks of text with css as you've defined.
TODO confirm the above.

However there is no callback registered to remove the css.
*Q. Why?*

Scaled has it's own implementation of a text mate parser available here: https://github.com/scaled/textmate-grammar/
It does not rely on any other text mate libraries.

It's worth becoming a little aquainted to the processing that this library does.

* What is a `scope`? It represents every element of a match of a piece of text down an AST. I.e.
** We're in code -> in a comment block -> at the beginning of a comment block.


You can see more examples here:
scaled.grammar.GrammarTest



## Useful EMACS guides

https://github.com/noctuid/evil-guide

When you’re done, quit

http://www.gnu.org/software/emacs/refcards/pdf/refcard.pdf
Adding your own commands.



## But questions:

How to write a java code snippet?
Can we leverage org mode?
How hard to support mouse mode?

## Feature requests

Want ESC to always exit current context. Stronger than C-g which doesn’t always work.
Copy line to clipboard.




Understanding closure.
http://georgejahad.com/clojure/clojureDecompiled.html


Goal to get org-mode. BUT This is GPL not BSD!!!!


## Understanding Scaled API


Remember that all packages are stored in

/Users/dmg46664/Library/Application Support/Scaled/Packages

* How does scaled use textmate, indenters, etc?


### Understanding how XML mode is put together

First off let's understand what textmate grammars give us which is used by scaled.

Start by reading the following article:
https://macromates.com/blog/2005/introduction-to-scopes/
then
https://macromates.com/manual/en/language_grammars
additionally perhaps
https://www.apeth.com/nonblog/stories/textmatebundle.html


Note that you'll see an XmlMode.scala

This has two classes inside it: `XmlMode` and `XmlGrammarMode`.


The major mode class inherits from `GrammarCodeMode`.


### Exploring Scala-mode

???

## Problem running after breaking code

```
Daniels-MacBook-Pro:~ dmg46664$ ERRORS in /Users/dmg46664/Library/Application Support/Scaled/Packages/jira-scaled-mode/package.scaled:
- Failed to parse module test: java.nio.file.NoSuchFileException: /Users/dmg46664/Library/Application Support/Scaled/Packages/jira-scaled-mode/test/module.scaled
WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by scaled.impl.BufferArea$ (file:/Users/dmg46664/Library/Application%20Support/Scaled/Packages/scaled/editor/target/module.jar) to field javafx.scene.Node.styleHelper
WARNING: Please consider reporting this to the maintainers of scaled.impl.BufferArea$
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release
```


## How to install a mode

```
Daniels-MacBook-Pro:/Users/dmg46664/bin dmg46664$ spam install xml-mode
-- Cloning git:https://github.com/scaled/xml-mode.git into temp dir...
-- Cloning git:https://github.com/scaled/textmate-grammar.git into temp dir...
-- Updating git:https://github.com/scaled/scaled.git...
Already up-to-date.
-- Updating 1 pkgs on which scaled depends...
-- Updating git:https://github.com/scaled/pacman.git...
Already up-to-date.
-- Building textmate-grammar...
Building textmate-grammar...
MavenResolver.transferSucceeded GET SUCCEEDED http://central.maven.org/maven2/com/googlecode/plist/dd-plist/1.8/dd-plist-1.8.pom <> /Users/dmg46664/.m2/repository/com/googlecode/plist/dd-plist/1.8/dd-plist-1.8.pom
MavenResolver.transferSucceeded GET SUCCEEDED http://central.maven.org/maven2/com/eclipsesource/minimal-json/minimal-json/0.9.4/minimal-json-0.9.4.pom <> /Users/dmg46664/.m2/repository/com/eclipsesource/minimal-json/minimal-json/0.9.4/minimal-json-0.9.4.pom
MavenResolver.transferSucceeded GET SUCCEEDED http://central.maven.org/maven2/com/eclipsesource/minimal-json/minimal-json/0.9.4/minimal-json-0.9.4.jar <> /Users/dmg46664/.m2/repository/com/eclipsesource/minimal-json/minimal-json/0.9.4/minimal-json-0.9.4.jar
MavenResolver.transferSucceeded GET SUCCEEDED http://central.maven.org/maven2/com/googlecode/plist/dd-plist/1.8/dd-plist-1.8.jar <> /Users/dmg46664/.m2/repository/com/googlecode/plist/dd-plist/1.8/dd-plist-1.8.jar
Building textmate-grammar#test...
-- Installing git:https://github.com/scaled/textmate-grammar.git into Packages/textmate-grammar...
-- Building xml-mode...
Building xml-mode...
Building xml-mode#test...
-- Installing git:https://github.com/scaled/xml-mode.git into Packages/xml-mode...
Installation complete!
```

## Altering a local copy of scaled#editor

Given you can’t `spam reinstall` the following gets you passed the the fact you may already have scaled installed.

```
Daniels-MacBook-Pro:bin dmg46664$ rm -rf /Users/dmg46664/Library/Application\ Support/Scaled/Packages/scaled
Daniels-MacBook-Pro:bin dmg46664$ ls /Users/dmg46664/Library/Application\ Support/Scaled/Packages/
.DS_Store         codex/            junit-runner/     prococol/         scaledex/         xml-mode/
.idea/            java-mode/        pacman/           project-service/  textmate-grammar/
Daniels-MacBook-Pro:bin dmg46664$ spam install git:https://github.com/dmg46664/scaled.git
-- Cloning git:https://github.com/dmg46664/scaled.git into temp dir...
-- Updating git:https://github.com/scaled/pacman.git...
Already up-to-date.
-- Building scaled...
Building scaled#std...
warning: [options] bootstrap class path not set in conjunction with -source 9
1 warning
Building scaled#api...
warning: [options] bootstrap class path not set in conjunction with -source 9
1 warning
Building scaled#editor...
Building scaled#test...
warning: [options] bootstrap class path not set in conjunction with -source 9
1 warning
-- Installing git:https://github.com/dmg46664/scaled.git into Packages/scaled...
Installation complete!
Daniels-MacBook-Pro:bin dmg46664$
```

Make edits to code.

```
Daniels-MacBook-Pro:bin dmg46664$ spam build scaled
```



## Scaled basics

https://github.com/scaled/scaled

Scaled is like Emacs. For reasons to use emacs and an introduction see:
https://www.youtube.com/watch?v=B6jfrrwR10k


## Copied intro instructions:

At the moment Scaled's "UI" follows Emacs where that makes sense (pretty much all of the basic editing key bindings). Extensions like project-mode introduce new interactions and I'm not making an effort to model those on the myriad hodge-podge Emacs IDE-like extensions that exist, I'm just trying to come up with sensible bindings.
At any time, you can invoke M-x describe-mode (or C-h m) to see all of the key bindings and config vars for the the active major and minor modes. You can cross-reference that with the Emacs reference card to see basic editing commands organized more usefully than alphabetic order.

## Expanding on simple instructions

Okay...
Remember that on a mac, M (as in M-x ) is holding the command key and then pressing x.

So try M-x describe-mode

You’ll see all the commands available in the major AND minor modes.

Perhaps you’ve started typing this and want to quit the popup : C-g

We want to search for something so C-s and type in the buffer as that’s what we want to search. Search a couple more times to navigate forward.

Now describe-mode has opened up a new buffer (the original blank screen is still there). Hopefully we’ve found the  switch-to-buffer binding and run that shortcut.

* Opening files:
  * C-x f


## Spacemacs help

(Remember to type the space as below)

M-x help-with-tutorial

## Potential improvements

HELM is better than minibuffer: https://github.com/emacs-helm/helm/wiki#install
https://www.youtube.com/watch?v=k78f8NYYIto