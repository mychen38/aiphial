/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package me.uits.aiphial.imaging.runner


import java.awt.image.BufferedImage
import me.uits.aiphial.imaging.SegmentatorAdapter

import ru.nickl.meanShift.direct.filter.SimpleMSFilter

import scala.collection.JavaConversions.asIterable
import com.beust.jcommander.Parameter;
import scalarunner.Tools._

class NaiveMSCli() {

  @Parameter(names = Array("-cr"), description = "colorRange")
  var coloRange = 7

  @Parameter(names = Array("-sr"), description = "spatialRange")
  var spatialRange = 2

  @Parameter(names = Array("-mr"), description = "minreg")
  var minreg = 0


  def process(image:BufferedImage):BufferedImage={

    val ifilter = new SimpleMSFilter{
      setColorRange(coloRange)
      setSquareRange(spatialRange.toShort)
    }

    val is  = new ru.nickl.meanShift.direct.segmentator.SimpleSegmentator(ifilter){
      setMinRegionSize(minreg)

    }

    is.setSourceImage(image)
    
    val segmentator = new SegmentatorAdapter(is)
    segmentator.doClustering()
    

    val r = paintClusters(image.getWidth,image.getHeight, segmentator.getClusters())

    return r;
  }



}
