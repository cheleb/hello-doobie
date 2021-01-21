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

import doobie.implicits._
import doobie.free.connection.ConnectionIO
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import cats.effect.concurrent.Ref

import cats.effect.IO

import cats.arrow.FunctionK
import cats.effect.LiftIO

import fs2._

class StreamingDoobieSpec extends AnyWordSpec with Matchers with DBConnection {

  val liftToConnIO: FunctionK[IO, ConnectionIO] = LiftIO.liftK[ConnectionIO]

  "Doobie" should {
    "stream result set" in {

      val pureStreal: Stream[fs2.Pure, Int] = Stream(1, 2, 3)

      val effectFullStream: Stream[IO, Int] = Stream.eval(IO(1))

      pureStreal.toList

      effectFullStream.compile.drain.unsafeRunSync()

      def doobie(counter: Ref[IO, Int]) =
        sql"select code, name from country"
          .query[(String, String)]
          .stream
          .evalTap {
            case ((_, name)) =>
              val el = if (name.startsWith("A")) counter.update(_ + 1) else IO.unit
              val o  = liftToConnIO(el)
              el.to[ConnectionIO]
          }
          .compile
          .drain
          .transact(pgsqlXa)

      val prog = for {
        counter <- Ref.of[IO, Int](0)
        _       <- doobie(counter)
        count   <- counter.get
        _       <- IO(println(s" whaou $count matches"))

      } yield ()

      prog.unsafeRunSync()
    }
  }
}
