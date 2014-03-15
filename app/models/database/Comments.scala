package models.database

import scala.slick.driver.H2Driver.simple._
import play.api.db.DB
import play.api.Play.current

/**
 * Created by FScoward on 14/03/05.
 */
case class Comment(ideaId: Int, comment: String, twitterAccount: String)
object Comments {

  val database = Database.forDataSource(DB.getDataSource())

  class Comments(tag: Tag) extends Table[Comment](tag, "COMMENTS") {
    def ideaId = column[Int]("IDEA_ID")
    def comment = column[String]("COMMENT")
    def twitterAccount = column[String]("TWITTER_ACCOUNT")
    def * = (ideaId, comment, twitterAccount) <> (Comment.tupled, Comment.unapply)
  }

  val comments = TableQuery[Comments]

  def createComment(comment: Comment) = database.withTransaction { implicit session: Session =>
    comments.ddl.create
    comments.insert(comment)
  }

}
