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

import doobie._
import cats.effect._

import scala.concurrent.ExecutionContext
import doobie.util.transactor.Strategy
import doobie.free.connection.unit
trait DBConnection {

  implicit val contextShift = IO.contextShift(ExecutionContext.global)

  val pgsqlXa = Transactor.fromDriverManager[IO]("org.postgresql.Driver", "jdbc:postgresql:world")

  val calciteDruidXa = Transactor.strategy.set(
    Transactor.fromDriverManager[IO](
      "org.apache.calcite.jdbc.Driver",
      "jdbc:calcite:model=src/test/resources/druid-wiki-model.json"
    ),
    Strategy.default.copy(after = unit, oops = unit)
  )
  val calciteDruid2Xa = Transactor.strategy.set(
    Transactor.fromDriverManager[IO](
      "org.apache.calcite.jdbc.Driver",
      "jdbc:calcite:model=src/test/resources/druid-wiki-model2.json"
    ),
    Strategy.default.copy(after = unit, oops = unit)
  )

}
