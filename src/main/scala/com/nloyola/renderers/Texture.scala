package com.nloyola.renderers

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11._
import org.lwjgl.stb.STBImage._
import org.slf4j.LoggerFactory

class Texture(private val filepath: String) {
  private val logger = LoggerFactory.getLogger(this.getClass)

  // Generate texture on GPU

  private var width  = 0
  private var height = 0
  private val texID: Int = init

  private def init(): Int = {
    val id = glGenTextures
    glBindTexture(GL_TEXTURE_2D, id)

    // Set texture parameters
    // Repeat image in both directions
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)
    // When stretching the image, pixelate
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
    // When shrinking an image, pixelate
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)

    val widthBuffer  = BufferUtils.createIntBuffer(1)
    val heightBuffer = BufferUtils.createIntBuffer(1)
    val channels     = BufferUtils.createIntBuffer(1)

    stbi_set_flip_vertically_on_load(true)
    val image = stbi_load(filepath, widthBuffer, heightBuffer, channels, 0)

    if (image != null) {
      width  = widthBuffer.get(0)
      height = heightBuffer.get(0)

      logger.debug(s"loaded image: $image ($width, $height)");

      val format = channels.get(0) match {
        case 3 => GL_RGB
        case 4 => GL_RGBA
        case _ =>
          throw new IllegalStateException("Error: (Texture) Unknown number of channels: " + channels.get(0))
      }

      glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, image)
    } else {
      throw new IllegalStateException("Error: (Texture) Could not load image: " + filepath)
    }

    stbi_image_free(image)
    logger.debug("init: id: {}", id)
    id
  }

  def bind(): Unit = {
    glBindTexture(GL_TEXTURE_2D, texID)
  }

  def unbind(): Unit = glBindTexture(GL_TEXTURE_2D, 0)

  def getWidth(): Int = width

  def getHeight(): Int = height

  /**
   *  Used only for debugging
   */
  def debugTexture(x: Float, y: Float, width: Float, height: Float): Unit = {
    //usually glOrtho would not be included in our game loop
    //however, since it's deprecated, let's keep it inside of this debug function which we will remove later
    glMatrixMode(GL_PROJECTION)
    glLoadIdentity()
    glOrtho(0, 1920, 1080, 0, 1, -1)
    glMatrixMode(GL_MODELVIEW)
    glLoadIdentity()
    glEnable(GL_TEXTURE_2D) //likely redundant will be removed upon migration to "modern GL"

    //bind the texture before rendering it
    bind()

    //setup our texture coordinates
    //(u,v) is another common way of writing (s,t)
    val u  = 0f
    val v  = 0f
    val u2 = 1f
    val v2 = 1f

    //immediate mode is deprecated -- we are only using it for quick debugging
    glColor4f(1f, 1f, 1f, 1f)
    glBegin(GL_QUADS)
    glTexCoord2f(u, v)
    glVertex2f(x, y)
    glTexCoord2f(u, v2)
    glVertex2f(x, y + height)
    glTexCoord2f(u2, v2)
    glVertex2f(x + width, y + height)
    glTexCoord2f(u2, v)
    glVertex2f(x + width, y)
    glEnd()
  }

  override def toString: String = s"filepath: $filepath, texId: $texID, ($width, $height)"

}
