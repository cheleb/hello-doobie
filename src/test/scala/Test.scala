package tv.teads.hello

object Test extends App {

  println(Seq(1, 2, 3).collect {
    case i if i % 2 == 0 => i
  })

}
