package models.database

import scala.slick.driver.H2Driver.simple._
import play.api.db.DB
import play.api.Play.current
import play.Logger
import org.h2.jdbc.JdbcSQLException

case class Idea(id: Int = -1, content: String, twitterAccount: String)
object Ideas {
  
  val database = Database.forDataSource(DB.getDataSource())
  
  class Ideas(tag: Tag) extends Table[Idea](tag, "IDEAS") {
    def id = column[Int]("IDEA_ID", O.PrimaryKey, O.AutoInc)
    def content = column[String]("CONTENTS", O.NotNull)
    def twitterAccount = column[String]("TWITTER_ACCOUNT", O.NotNull)
    def * = (id, content, twitterAccount) <> (Idea.tupled, Idea.unapply)
    def idx = index("ideas_id_key", (content, twitterAccount), unique= true)
  }
  
  val ideas = TableQuery[Ideas]
    
  def create(idea: Idea) = database.withTransaction { implicit session: Session =>
    ideas.insert(idea)
  }
  
  def findByAccount(twitterAccount: String) = database.withTransaction{ implicit session: Session => 
    val q = ideas.filter(_.twitterAccount === twitterAccount)
    q.iterator
  }
	
}