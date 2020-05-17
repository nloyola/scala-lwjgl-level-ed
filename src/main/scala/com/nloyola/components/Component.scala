package com.nloyola.components

import com.nloyola.GameObject

trait Component {

  var gameObject: Option[GameObject]

  def start(): Unit

  def update(dt: Float): Unit
}
