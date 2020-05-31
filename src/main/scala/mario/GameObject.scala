package mario

import mario.components.Component
import scala.collection.mutable.ListBuffer
import scala.reflect.ClassTag

class GameObject(private val name: String, private var transform: Transform, zIndex: Int) {

  private val components = ListBuffer.empty[Component]

  def this(name: String) = {
    this(name, new Transform, 0)
  }

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

  def start(): Unit = components.foreach(_.start)

  def imgui(): Unit = components.foreach(_.imgui)

  def getTransform(): Transform = transform

  def setTransform(t: Transform): Unit = transform = t

  def getZIndex(): Int = zIndex

  override def toString: String = s"name: $name, components: ${components.length}, zIndex: $zIndex"

}
