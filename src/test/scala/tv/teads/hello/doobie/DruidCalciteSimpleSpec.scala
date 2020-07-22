package tv.teads.hello.doobie

import doobie.implicits._

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.must.Matchers
import doobie.util.transactor.Transactor

class DruidCalciteSimpleSpec extends AnyWordSpec with Matchers with DBConnection{

    def runQuery(tx: Transactor[cats.effect.IO]): Unit =
     sql"""SELECT "countryName", cast(count(*) as integer) as c from "wiki" group by "countryName" order by c desc limit 5"""
        .query[(Option[String], Long)]
        .stream
        .map {
          case ((country, count)) =>
            println(s"$country: $count")
        }
        .compile
        .drain
        .transact(tx)
        .unsafeRunSync()

    "Apache calcite" must {
        "Aggregate data through calcite" in {

             runQuery(calciteDruidXa)
             runQuery(calciteDruid2Xa)
        
        
        }
    }
}