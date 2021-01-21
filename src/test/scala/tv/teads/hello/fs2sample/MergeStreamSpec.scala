/*
 * Copyright (c) 2018 Olivier NOUGUIER
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package tv.teads.hello.fs2sample

import fs2._
import cats.effect.{ ContextShift, IO }

import org.scalatest.wordspec.AnyWordSpec

class MergeStreamSpec extends AnyWordSpec {
  "FS2" should {

    "merge stream " in {

      implicit val ioContextShift: ContextShift[IO] =
        IO.contextShift(scala.concurrent.ExecutionContext.Implicits.global)

      Stream(1, 2, 3)
        .merge(Stream.eval(IO { Thread.sleep(2000); 5 }))
        .merge(Stream.eval(IO { Thread.sleep(300); 4 }))
        .map(i => i * 10)
        .evalTap(i => IO(println(i)))
        .compile
        .drain
        .unsafeRunSync()

//        println(res)
    }
  }
}
