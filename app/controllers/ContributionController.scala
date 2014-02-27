package controllers

import play.api._
import play.api.mvc._
import org.h2.jdbc.JdbcSQLException
import play.api.libs.json._
import play.api.libs.functional.syntax._
import models.database.Idea

// アイデアの投稿
object ContributionController extends Controller {

  case class jsonIdea(content: String)

  implicit val ideaReads = Json.reads[jsonIdea]
  implicit val ideaWrites = Json.writes[Idea]

  def index = Action {
    Ok(views.html.contribution.render())
  }

  def createIdea = Action(parse.json) { request =>
    import models.database._

    val json = request.body
    json.validate[jsonIdea].fold(
      invalid = { x => { BadRequest(x.toString) } },
      valid = (js => {
        try {
          Ideas.create(new Idea(content = js.content, twitterAccount = "dummy twitterAccount"))
          Ok
        } catch {
          case e: JdbcSQLException => {
            play.Logger.debug(e.getMessage())
            Conflict
          }
        }

      }))
  }

  def readIdeas = Action {
    val res = models.database.Ideas.findByAccount("dummy twitterAccount")
    Ok(
      Json.toJson(
        res.map(idea => {
          Json.toJson(new Idea(idea.id, idea.content, idea.twitterAccount))
        }).toList))
  }
}