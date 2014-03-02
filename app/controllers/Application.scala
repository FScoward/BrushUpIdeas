package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {

  def index = Action { request =>
    request.session.get("twitterAccount") match {
      case Some(x) => Ok(views.html.contribution(Some(x)))
      case None => Ok(views.html.index("Your new application is ready.", None)).withNewSession
    }
  }
  
  def login = Action {
//    Ok(views.html.contribution()).withSession("twitterAccount" -> "dummy")
    // TODO
    Ok(views.html.ideas(None)).withSession("twitterAccount" -> "FScoward")
  }
  
  // TODO
  def logout = Action {
    Ok(views.html.index("", None)).withNewSession
  }

  def ideas = Action { request =>
    request.session.get("twitterAccount") match {
      case Some(x) => Ok(views.html.ideas(Some(x)))
      case None => Redirect("/")
    }
  }

}