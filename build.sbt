import laika.theme.Theme

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.github.daddykotex",
      scalaVersion := "2.13.3"
    )),
    name := "blog-sbt"
  )

lazy val blog = project
  .enablePlugins(LaikaPlugin)
  .settings(
    laikaTheme := Theme.empty
  )