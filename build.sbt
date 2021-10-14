import laika.theme.Theme

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.github.daddykotex",
      scalaVersion := "2.13.3",

      githubWorkflowBuild := Seq(WorkflowStep.Sbt(List("blog/laikaSite"))),
      githubWorkflowPublish := Seq(WorkflowStep.Sbt(List("blog/ghpagesPushSite")))
    )),
    name := "blog-sbt"
  )

lazy val blog = project
  .enablePlugins(LaikaPlugin)
  .enablePlugins(GhpagesPlugin)
  .settings(
    laikaTheme := Theme.empty,

    git.remoteRepo := {
      val repo = "blog-sbt"
      val username = "daddykotex"

      if (insideCI.value) {
        val token = sys.env("GITHUB_TOKEN")
        s"https://$username:$token@github.com/$username/$repo.git"
      } else {
        s"git@github.com:$username/$repo.git"
      }
    },
    ghpagesNoJekyll := true,
    ghpagesPrivateMappings  := {
      val targetF = (laikaSite / target).value
      val allFiles: Seq[File] = targetF.**(AllPassFilter).get
      allFiles.pair(Path.relativeTo(targetF))
    }
  )