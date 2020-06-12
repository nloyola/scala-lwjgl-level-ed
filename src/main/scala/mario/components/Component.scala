package mario.components

import imgui.ImGui
import mario.GameObject
import play.api.libs.json._
import org.joml.{Vector3f, Vector4f}
import org.slf4j.Logger
import scala.reflect.runtime.{universe => ru}

trait Component {

  protected val log: Logger

  protected val typeName: String

  var gameObject: Option[GameObject]

  def start(): Unit

  def update(dt: Float): Unit = {}

  def imgui(): Unit = {
    val typeMirror     = ru.runtimeMirror(this.getClass.getClassLoader)
    val instanceMirror = typeMirror.reflect(this)

    instanceMirror.symbol.typeSignature.members.foreach { field =>
      val fieldName = field.name.toString

      field.typeSignature match {
        case tpe if tpe =:= ru.typeOf[Int] =>
          val fieldMirror = instanceMirror.reflectField(field.asTerm)
          val v           = fieldMirror.get.asInstanceOf[Int]

          val imInt = Array[Int](v)
          if (ImGui.dragInt(s"$fieldName: ", imInt)) {
            fieldMirror.set(imInt(0))
          }

        case tpe if tpe =:= ru.typeOf[Float] =>
          val fieldMirror = instanceMirror.reflectField(field.asTerm)
          val v           = fieldMirror.get.asInstanceOf[Float]

          val imFloat = Array[Float](v)
          if (ImGui.dragFloat(s"$fieldName: ", imFloat)) {
            fieldMirror.set(imFloat(0))
          }

        case tpe if tpe =:= ru.typeOf[Boolean] =>
          val fieldMirror = instanceMirror.reflectField(field.asTerm)
          val v           = fieldMirror.get.asInstanceOf[Boolean]

          if (ImGui.checkbox(fieldName + ": ", v)) {
            fieldMirror.set(!v)
          }

        case tpe if tpe =:= ru.typeOf[Vector3f] =>
          val fieldMirror = instanceMirror.reflectField(field.asTerm)
          val v           = fieldMirror.get.asInstanceOf[Vector3f]

          val imVec = Array[Float](v.x, v.y, v.z)
          if (ImGui.dragFloat3(s"$fieldName: ", imVec)) {
            v.set(imVec(0), imVec(1), imVec(2))
          }

        case tpe if tpe =:= ru.typeOf[Vector4f] =>
          val fieldMirror = instanceMirror.reflectField(field.asTerm)
          val v           = fieldMirror.get.asInstanceOf[Vector4f]

          val imVec = Array[Float](v.x, v.y, v.z, v.w)
          if (ImGui.dragFloat4(s"$fieldName: ", imVec)) {
            v.set(imVec(0), imVec(1), imVec(2), imVec(3))
          }

        case _ =>
      }

    }
  }
}

object Component {

  implicit val componentFormat: Format[Component] = new Format[Component] {

    override def writes(c: Component): JsValue = {
      val objJson = c match {
        case sc: SpriteRenderer => Json.toJson(sc)
        case rb: RigidBody      => Json.toJson(rb)
      }

      Json.obj("typeName" -> c.typeName) ++ objJson.as[JsObject]
    }

    override def reads(json: JsValue): JsResult[Component] = (json \ "typeName") match {
      case JsDefined(JsString("spriteRenderer")) => json.validate[SpriteRenderer]
      case JsDefined(JsString("rigidBody"))      => json.validate[RigidBody]
      case _                                     => JsError("error")
    }
  }
}
