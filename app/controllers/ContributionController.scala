package controllers

import play.api._
import play.api.mvc._
import org.h2.jdbc.JdbcSQLException
import play.api.libs.json._
import play.api.libs.functional.syntax._
import models.database.{Ideas, Comments, Comment, Idea}

// アイデアの投稿
object ContributionController extends Controller {

  case class jsonIdea(content: String)

  implicit val ideaReads = Json.reads[jsonIdea]
  implicit val ideaWrites = Json.writes[Idea]
  implicit val commentReads = Json.reads[Comment]
  implicit val commentWrites = Json.writes[Comment]

  def index = Action { request =>
      request.session.get("twitterAccount") match {
        case Some(x) => Ok(views.html.contribution(Some(x)))
        case None => Ok(views.html.index("Your new application is ready.", None)).withNewSession
    }
  }

  def createIdea = Action(parse.json) { request =>

    val twitterAccount = request.session.get("twitterAccount")

    val json = request.body
    json.validate[jsonIdea].fold(
      invalid = { x => { BadRequest(x.toString) } },
      valid = (js => {
        try {
          Ideas.create(new Idea(content = js.content, twitterAccount = twitterAccount.get, iine = 0))
          Ok
        } catch {
          case e: JdbcSQLException => {
            play.Logger.debug(e.getMessage())
            Conflict
          }
        }

      }))
  }

  def readIdeas = Action { request =>

//    val res = models.database.Ideas.findByAccount("dummy twitterAccount")
    val res = models.database.Ideas.findAll
    Ok(
      Json.toJson(
        res.map(idea => {
          Json.toJson(new Idea(idea.id, idea.content, idea.twitterAccount, idea.iine))
        }).toList))
  }

  def deleteIdea(id: Int) = Action { request =>
    play.Logger.debug("" + request.headers)

    try{
      models.database.Ideas.delete(id)
      Ok
    }catch{
      case e: Exception => BadRequest
    }
  }

  def commentTo = Action(parse.json) { request =>
    val json = request.body
    json.validate[Comment].fold(
      invalid = {x => BadRequest(x.toString())},
      valid = (js => {
        try{
          Comments.createComment(new Comment(js.ideaId, js.comment, js.twitterAccount))
          Ok
        }catch{
          case e: JdbcSQLException =>
          Conflict
        }
      })
    )
  }
}