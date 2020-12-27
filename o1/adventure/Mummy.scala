package o1.adventure

class Mummy(val name: String, var attackDamage: Int, var hp: Int) {
  def alive = hp > 0
}
