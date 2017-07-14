/*
* MIT License
*
* Copyright (c) 2017 Tim Fennell
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/

import java.io.File

import scala.io.Source
import scala.math.max

/**
  * Simple little program to replace "column -t" that has the following advantages:
  *   1) Treats multiple consecutive delimiters as multiple delimiters(!)
  *   2) Squeezes out empty lines and lines beginning with a comment character (#)
  */
object Tabulate extends App {
  val missing = args.filterNot(a => new File(a).canRead)

  if (args.contains("-h")) {
    val usage =
      """Tabulates tab-separated data into fixed-width columns for viewing. Defaults to
        |reading stdin unless arguments are provided in which case each argument is
        |interpreted as a file to be concatenated and tabulated.
        |
        |Lines in the input that are empty or start with '#' are ignored.
      """.stripMargin
    System.err.println(usage)
  }
  else if (missing.nonEmpty) {
    System.err.println("Cannot open file(s) for reading: " + missing.mkString(", "))
  }
  else {
    val sources = args match {
      case Array() => Seq(Source.fromInputStream(System.in))
      case fs      => fs.toSeq.map(f => Source.fromFile(f))
    }

    val lines     = sources.flatMap(s => s.getLines()).filter(l => l.nonEmpty && !l.startsWith("#"))
    val rows      = lines.map(_.split('\t'))
    val colCount  = rows.iterator.map(_.length).max
    val colWidths = new Array[Int](colCount)

    // Calculate the width of each column
    rows.foreach { row =>
      row.zipWithIndex.foreach { case (value, index) =>
        colWidths(index) = max(colWidths(index), value.length)
      }
    }

    // Output the rows
    rows.foreach { row =>
      row.zip(colWidths).foreach { case (value, width) =>
        System.out.print(pad(value, width))
      }
      System.out.println()
    }
  }

  /** Pad a string to a given width by adding spaces after the string. */
  private def pad(s: String, w: Int): String = {
    val builder = new StringBuilder(w)
    builder.append(s)
    while (builder.length < w + 2) builder.append(' ')
    builder.toString()
  }
}
