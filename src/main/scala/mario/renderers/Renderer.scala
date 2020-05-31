package mario.renderers

import mario.components.SpriteRenderer
import mario.GameObject
import scala.collection.mutable.ListBuffer
import org.slf4j.LoggerFactory

class Renderer {
  private val MAX_BATCH_SIZE = 1000
  private var batches = ListBuffer.empty[RenderBatch]
  private val logger = LoggerFactory.getLogger(this.getClass)

  def add(obj: GameObject): Unit = {
    obj.getComponent[SpriteRenderer].foreach { spr =>
      logger.debug(s"add: spr: $spr")
      add(spr)
    }
  }

  private def add(sprite: SpriteRenderer): Unit = {
    val found = for {
        go <- sprite.gameObject
        tex <- sprite.getTexture
        batch <- batches.find { batch =>
          batch.hasRoom && (batch.getZIndex == go.getZIndex) && (batch.hasTextureRoom || batch.hasTexture(tex))
        }
      } yield batch

    found match {
      case Some(batch) => batch.addSprite(sprite)
      case None =>
        val newBatch = new RenderBatch(MAX_BATCH_SIZE, sprite.gameObject.map(_.getZIndex).getOrElse(0))
        newBatch.start()
        batches += newBatch
        newBatch.addSprite(sprite)
        batches = batches.sorted
    }
  }

  def render(): Unit = batches.foreach(_.render)
}
