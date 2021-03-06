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

import doobie._
import doobie.implicits._

import doobie.postgres.implicits._

import tv.teads.hello.doobie.model.Person
import java.{ util => ju }
import cats.effect.IO

import java.time.LocalDateTime

class TransactionSpec extends AnyWordSpec with DBConnection {

  "Doobie" should {

    def insertPerson(p: Person): ConnectionIO[Int] =
      sql"INSERT INTO person (id, name, birthdate) VALUES(${p.id}, ${p.name}, ${p.birthDate})".update.run

    "Combine statement" in {

      def countPerson(): ConnectionIO[Int] =
        sql"SELECT count(*) FROM person".query[Int].unique

      val insertAndCount =
        insertPerson(Person(ju.UUID.randomUUID(), "Olivier", LocalDateTime.now()))
          .flatMap(_ => countPerson)
          .transact(pgsqlXa)
          .unsafeRunSync()

      println(s"$insertAndCount person in db.")
    }

    "Combine with IO" in {

      val sendEmail = IO(println("Email sent."))

      val sendEmailWithConnection = sendEmail.to[ConnectionIO]

      val insertLucas = insertPerson(Person(ju.UUID.randomUUID(), "Lucas", LocalDateTime.now()))

      val insertLucasInTx = insertLucas
        .transact(pgsqlXa)
      // sendEmail; Begin; Insert; Commit
      sendEmail.flatMap(_ => insertLucasInTx).unsafeRunSync()

      // Begin; sendEmail; Insert; Commit
      sendEmail.flatMap(_ => insertLucasInTx).unsafeRunSync()

      // Begin; Insert; sendEmail; Commit
      insertLucas.flatMap(_ => sendEmailWithConnection).transact(pgsqlXa).unsafeRunSync

      // Begin; Insert; Commit; sendEmail;
      insertLucasInTx.flatMap(_ => sendEmail).unsafeRunSync()

    }

  }

}
