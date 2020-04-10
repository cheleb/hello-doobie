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

import org.scalatest.wordspec.AnyWordSpec
import cats.effect.IO

import doobie._
import doobie.implicits._
import doobie.postgres.implicits._

import tv.teads.hello.doobie.model.Person

class NestingTypeSpec extends AnyWordSpec with DBConnection {
  "Doobie" must {
    "Nesting type" in {
      val httpCall = IO("Olivier")

      val sqlQueryFromHttpCall: IO[ConnectionIO[List[Person]]] = httpCall.map { name =>
        sql"SELECT p.id, p.name, p.age FROM person p WHERE p.name = $name".query[Person].to[List]
      }

      val persons = sqlQueryFromHttpCall.flatMap(_.transact(xa)).unsafeRunSync()
      println(persons)

    }
  }
}
