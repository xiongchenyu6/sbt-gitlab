ThisBuild / name := """sbt-gitlab"""
ThisBuild / organization := "com.gilandose"

sbtPlugin := true


// ScalaTest
//libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.1" % "test"
//libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"

//bintrayPackageLabels := Seq("sbt","plugin")
//bintrayVcsUrl := Some("""git@github.com:com.gilcloud/sbt-gitlab.git""")

initialCommands in console := """import com.gilcloud.sbt._"""


ThisBuild / git.useGitDescribe := true
ThisBuild / git.gitTagToVersionNumber := { tag: String =>
  if (tag matches "^0+(?:\\.\\d+){2,4}$") Some(s"$tag-SNAPSHOT")    // If the version number start with zero then we should add snapshot postfix
  else if (tag matches "^[1-9]\\d*(?:\\.\\d+){2,4}$") Some(s"$tag") // Else use the origin git number
  else Some(s"$tag")
}

enablePlugins(ScriptedPlugin, GitVersioning)

// set up 'scripted; sbt plugin for testing sbt plugins
// scriptedLaunchOpts ++=
//  Seq("-Xmx1024M", "-Dplugin.version=" + version.value)


credentials += Credentials("gitlab", "git.sysop.bigo.sg", "Private-Token", "Zs56zXYa6BHFpvUaru7D")

publishMavenStyle := true
publishTo := {
  val nexus = "http://git.sysop.bigo.sg/api/v4/projects/3841/packages/maven"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("gitlab" at s"${nexus}")
  else
    Some("gitlab" at s"${nexus}")
}.map(_.withAllowInsecureProtocol(true))

pomIncludeRepository := { _ => false }
updateOptions := updateOptions.value.withGigahorse(false)
// Use publish to publish to our maven repo,  and use publishLocal to publish to local ivy, if you want to use local m2 change to setting below
// publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository")))

resolvers ++= Seq(
  Resolver.mavenLocal, // Search local maven first for dev
  "gitlab-dowload" at "https://gitlab.com/api/v4/packages/maven",
).map(_.withAllowInsecureProtocol(true))
