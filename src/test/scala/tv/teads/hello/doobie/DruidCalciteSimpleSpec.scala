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

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.must.Matchers
import doobie.util.transactor.Transactor

class DruidCalciteSimpleSpec extends AnyWordSpec with Matchers with DBConnection {

  def runQuery(tx: Transactor[cats.effect.IO]) =
    sql"""SELECT "countryName", cast(count(*) as integer) as c from "wiki" group by "countryName" order by c desc limit 5"""
      .query[(Option[String], Long)]
      .stream
      .map {
        case ((country, count)) =>
          (s"$country: $count")
      }
      .compile
      .toList
      .transact(tx)
      .unsafeRunSync()

  "Apache calcite" must {
    "Aggregate data through calcite" in {

      println(runQuery(calciteDruidXa))
      println(runQuery(calciteDruid2Xa))

    }
  }
}
