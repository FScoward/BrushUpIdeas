package controllers

import play.api._
import play.api.mvc._

// アイデアの投稿
object ContributionController extends Controller {
  def index = Action {
    Ok(views.html.contribution.render())
  }
}