package main.yiqirun.domain

import java.util.Date

import spray.json.DefaultJsonProtocol

/**
 * Created by zhanghao on 2015/6/11.
 */
class Domain {

  case class User(email:String,userId:String,password:String)
  case class Run(author:String,createTime:Date,context:String,tag:String)

  case object UserLogin
  case object UserLogout
  case object UserRegister
  case object RunInput
  case object RunDelete
  case object RunUpdate

  object  User extends DefaultJsonProtocol{
    implicit  val format = jsonFormat3(User.apply)
  }

  object Run extends DefaultJsonProtocol{
    implicit val format = jsonFormat4(Run.apply)
  }
}
