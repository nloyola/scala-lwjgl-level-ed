package com.nloyola

import com.nloyola.renderers.Renderer
import scala.collection.mutable.ListBuffer

trait Scene {

  protected val renderer = new Renderer
  protected val _camera: Camera
  protected var isRunning = false
  protected val gameObjects = ListBuffer.empty[GameObject]

  def init(): Unit

  def update(deltaTime: Float): Unit

  def start(): Unit = {
    gameObjects.foreach { obj =>
      obj.start
      renderer.add(obj)
    }
    isRunning = true
  }

  def addGameObject(obj: GameObject): Unit = {
    gameObjects += obj
    if (isRunning) {
      obj.start
      renderer.add(obj)
    }
  }

  def camera(): Camera = _camera

}
