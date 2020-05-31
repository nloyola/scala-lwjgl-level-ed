package mario.components

import mario.GameObject

trait Component {

  var gameObject: Option[GameObject]

  def start(): Unit

  def update(dt: Float): Unit
}
