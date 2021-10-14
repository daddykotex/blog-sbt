import laika.directive.Templates
import laika.directive.DirectiveRegistry
import laika.ast._

object CustomDirective extends DirectiveRegistry {
  import Templates.dsl._

  val ticketDirective = Templates.create("ticket") {
    attribute(0).as[String].map { ticketNo =>
      val base = "http://tickets.service.com/"+ticketNo
      val link = SpanLink.external(base)("Ticket "+ticketNo).withOptions(Styles("ticket"))
      TemplateElement(link)
    }
  }

  val spanDirectives = Seq.empty
  val blockDirectives = Seq()
  val templateDirectives = Seq(ticketDirective)
  val linkDirectives = Seq()
}