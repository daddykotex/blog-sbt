import laika.theme.Theme

lazy val configureGit = taskKey[Unit]("Configure git to push from Github actions over https")
lazy val botUsername = "bot@davidfrancoeur.com"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.github.daddykotex",
      scalaVersion := "2.13.3",

      githubWorkflowBuild := Seq(WorkflowStep.Sbt(List("blog/laikaSite"))),
      githubWorkflowPublish := {
        val generated = Path.relativeTo(file("."))((blog / laikaSite / target).value).getOrElse(throw new RuntimeException("Expecting a path."))
        Seq(
          WorkflowStep.Sbt(List("blog/configureGit", "blog/ghpagesPushSite")),
          WorkflowStep.Use(
            UseRef.Public("cpina", "github-action-push-to-another-repository", "2ebe0cc15fc6a8e63c3658c119525bf1aead4418"),
            name = Some("Push to other repository"),
            params = Map(
              "source-directory" -> generated,
              "destination-github-username" -> "daddykotex",
              "destination-repository-name" -> "blog-sbt-generated",
              "user-email" -> botUsername,
              "target-branch" -> "main",
            ),
            env = Map("API_TOKEN_GITHUB" -> "${{ secrets.API_TOKEN_GITHUB }}")
          )
        )
      }
    )),
    name := "blog-sbt"
  )

lazy val blog = project
  .enablePlugins(LaikaPlugin)
  .enablePlugins(GhpagesPlugin)
  .settings(
    laikaTheme := Theme.empty,
    laikaExtensions += CustomDirective,

    configureGit := {
      import sys.process._

      val logger = sLog.value

      val commands = Seq(
        Seq("git", "config", "--global", "user.email", botUsername),
        Seq("git", "config", "--global", "user.name", "Github Actions")
      )

      commands.foreach(_.!!(logger))
    },

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