package com.nloyola.scenes

import com.nloyola.Camera
import com.nloyola.GameObject
import com.nloyola.renderers.Renderer

import scala.collection.mutable.ListBuffer

trait Scene {

    protected val renderer = new Renderer()
    protected val camera: Camera
    private var isRunning = false
    protected val gameObjects = ListBuffer.empty[GameObject]

    def init(): Unit

    def start(): Unit = {
      gameObjects.foreach { go =>
        go.start()
        renderer.add(go)
      }
      isRunning = true
    }

    def addGameObjectToScene(go: GameObject): Unit = {
      gameObjects += go

      if (isRunning) {
        go.start
        renderer.add(go)
      }
    }

    def update(dt: Float): Unit

    def getCamera(): Camera = camera
}
