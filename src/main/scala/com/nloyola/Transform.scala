package com.nloyola

import org.joml.Vector2f;

class Transform(val position: Vector2f, val scale: Vector2f) {

  def this() = this(new Vector2f(), new Vector2f())

  def this(position: Vector2f) = this(position, new Vector2f())

  def copy(): Transform = new Transform(new Vector2f(position), new Vector2f(scale))

  def copy(to: Transform): Unit = {
    to.position.set(this.position)
    to.scale.set(this.scale)
    ()
  }

  override def equals(that: Any): Boolean = {
    that match {
      case t: Transform => position.equals(t.position) && scale.equals(t.scale)
      case _ => false
    }
  }

  override def hashCode: Int = 41 * position.hashCode + scale.hashCode
}
