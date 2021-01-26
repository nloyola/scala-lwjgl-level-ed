package mario

import mario.components.Component
import scala.collection.mutable.ListBuffer
import scala.reflect.ClassTag
import play.api.libs.json._

case class GameObject(name: String, transform: Transform, zIndex: Int, components: ListBuffer[Component]) {

  def getComponent[T]()(implicit tag: ClassTag[T]): Option[T] = {
    components.find {
      case e: T => true
      case _ => false
    } map { _.asInstanceOf[T] }
  }

  def removeComponent[T]()(implicit tag: ClassTag[T]): Unit = {
    components.filter {
      case e: T => true
      case _ => false
    }
    ()
  }

  def addComponent(c: Component): Unit = {
    components += c
    c.gameObject = Some(this)
  }

  def update(dt: Float): Unit = components.foreach(_.update(dt))

  def start(): Unit = components.foreach(_.start())

  def imgui(): Unit = components.foreach(_.imgui())

  def getZIndex(): Int = zIndex

  override def toString: String = s"name: $name, components: ${components.length}, zIndex: $zIndex"

}

object GameObject {

  def apply(name: String): GameObject = GameObject(name, Transform(), 0, ListBuffer.empty[Component])

  def apply(name: String, transform: Transform, zIndex: Int): GameObject =
    GameObject(name, transform, zIndex, ListBuffer.empty[Component])

  implicit val gameObjectFormat: Format[GameObject] = Json.format[GameObject]

}
