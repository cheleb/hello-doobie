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

package tv.teads.hello.doobie

import cats.implicits._

import doobie._
import doobie.implicits._
import cats.effect.Blocker
import cats.effect.IO
import java.util.concurrent.Executors
import cats.effect.ExitCode
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class StreamingDoobieSpec extends AnyWordSpec with Matchers with DBConnection {

  "Doobie" should {
    "stream result set" in {

      sql"select code, name from country"
        .query[(String, String)]
        .stream
        .map {
          case ((code, name)) =>
            println(s"$code: $name")
        }
        .compile
        .drain
        .transact(xa)
        .unsafeRunSync()
    }
  }
}
