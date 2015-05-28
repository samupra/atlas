/*
 * Copyright 2015 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.atlas.chart.graphics

import java.awt.Graphics2D

/**
 * Draws horizontal grid lines based on a value axis.
 *
 * @param yaxis
 *     Axis to use for creating the scale and determining the the tick marks that correspond with
 *     the major grid lines.
 * @param major
 *     Style to use for drawing the major tick lines.
 * @param minor
 *     Style to use for drawing the minor tick lines.
 */
case class ValueGrid(
    yaxis: ValueAxis,
    major: Style = Constants.majorGridStyle,
    minor: Style = Constants.minorGridStyle) extends Element {

  def draw(g: Graphics2D, x1: Int, y1: Int, x2: Int, y2: Int): Unit = {
    val yscale = yaxis.scale(y1, y2)
    val ticks = yaxis.ticks(y1, y2)

    // Draw minor grid lines
    ticks match {
      case a :: b :: _ =>
        minor.configure(g)
        val minorGap = (b.v - a.v) / 5.0
        var y = a.v - minorGap * 5.0
        while (y < yaxis.max) {
          if (y > yaxis.min) {
            val py = yscale(y)
            g.drawLine(x1, py, x2, py)
          }
          y += minorGap
        }
      case _ =>
        // If there aren't at least two major tick marks, then don't bother with a
        // minor grid. Two major ticks are needed to work out the amount for each minor
        // interval in the grid.
    }

    // Draw major grid lines
    major.configure(g)
    ticks.foreach { tick =>
      val py = yscale(tick.v)
      g.drawLine(x1, py, x2, py)
    }
  }
}

