package com.nloyola.util

import java.io.File
import com.nloyola.renderers.Shader
import com.nloyola.renderers.Texture
import com.nloyola.components.Spritesheet
import scala.collection.mutable.Map

object AssetPool {

  private val shaders = Map.empty[String, Shader]
  private val textures = Map.empty[String, Texture]
  private val spritesheets = Map.empty[String, Spritesheet]

  def getShader(resourceName: String): Shader = {
    val file = new File(resourceName)

    shaders.get(file.getAbsolutePath()) match {
      case Some(shader) => shader
      case None =>
        val shader = new Shader(resourceName)
        shader.compile()
        shaders.put(file.getAbsolutePath(), shader)
        shader
    }
  }

  def getTexture(resourceName: String): Texture = {
    val file = new File(resourceName)

    textures.get(file.getAbsolutePath()) match {
      case Some(texture) => texture
      case None =>
        val texture = new Texture(resourceName)
        textures += (file.getAbsolutePath() -> texture)
        texture
    }
  }

  def addSpritesheet(resourceName: String, spritesheet: Spritesheet): Unit = {
    val file = new File(resourceName)

    spritesheets.get(file.getAbsolutePath()) match {
      case None => spritesheets += (file.getAbsolutePath() -> spritesheet)
      case Some(spritesheet) =>  // do nothing
    }
    ()
  }

  def getSpritesheet(resourceName: String): Spritesheet = {
    val file = new File(resourceName)
    spritesheets.get(file.getAbsolutePath()) match {
      case Some(spritesheet) =>  spritesheet
      case None => throw new Error(s"Error: spritesheet '$resourceName' not present in asset pool.")
    }
  }
}
