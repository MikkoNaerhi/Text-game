package o1.adventure


/** The class `Adventure` represents text adventure games. An adventure consists of a player and
  * a number of areas that make up the game world. It provides methods for playing the game one
  * turn at a time and for checking the state of the game.
  *
  * N.B. This version of the class has a lot of "hard-coded" information which pertain to a very
  * specific adventure game that involves a small trip through a twisted forest. All newly created
  * instances of class `Adventure` are identical to each other. To create other kinds of adventure
  * games, you will need to modify or replace the source code of this class. */
class Adventure {

  /** The title of the adventure game. */
  val title = "A pyramid adventure"

  private var start   = new Area("Hieroglyph Hall", "Beautiful hieroglyphs everywhere! You could spend days in here...")
  private val square2 = new Area("The Tomb of Tutankhamum", "The youngest pharao in Egyptian history.")
  private val square3 = new Area("Hallway of Horus", "A god that lost his left eye figthting with Set")
  private val square4 = new Area("Room of Ramses", "The long reign of Ramses was filled with different routes")
  private val square5 = new Area("Chamber of the Mummy", "Imhotep, the first engineer of Egypt was mummified here")
  private val square6 = new Area("Corridor of Anubis", "The original god of death, I must be close to the end")
  private val square7 = new Area("Origins of Osiris", "The god of underworld, the end is here")
  private val destination = square7

  start.setNeighbors(Vector("east" -> square2, "south" -> square3))
  square2.setNeighbors(Vector("west" -> start))
  square3.setNeighbors(Vector("north" -> start, "south" -> square4))
  square4.setNeighbors(Vector("north" -> square3, "south" -> square5, "east" -> square6))
  square5.setNeighbors(Vector("north" -> square4))
  square6.setNeighbors(Vector("west" -> square4))
  square7.setNeighbors(Vector("west" -> square6))


  square2.addItem(new Item("the eye of horus", "The eye of sacrifice, healing and protection."))

  val mymmy = new Mummy("Mummy of Imhotep", 10, 40)
  square5.addMummy(mymmy)

  /** The character that the player controls in the game. */
  val player = new Player(start)

  /** The number of turns that have passed since the start of the game. */
  var turnCount = 0
  /** The maximum number of turns that this adventure game allows before time runs out. */
  val timeLimit = 35


  /** Determines if the adventure is complete, that is, if the player has won. */
  def isComplete = this.player.location == this.destination && this.player.hasUsedItem

  /** Determines whether the player has won, lost, or quit, thereby ending the game. */
  def isOver = this.isComplete || this.player.hasQuit || this.turnCount == this.timeLimit || this.player.hitpoints <= 0

  /** Returns a message that is to be displayed to the player at the beginning of the game. */
  def welcomeMessage = "You are lost in the great pyramid of Giza. Walk through the history of Egypt, defeat Mummys and escape!\n\n" +
    "It is rumoured that the pyramid hides a great treasure, you don't want to leave it behind...\n"
    "There is no time to waste, Imhotep is getting stronger every day."

  /** Returns a message that is to be displayed to the player at the end of the game. The message
    * will be different depending on whether or not the player has completed their quest. */
  def goodbyeMessage = {
    if (this.isComplete)
      "The eye is glowing up, you step in to the dayligth, the eye is gone but somehting has changed for good\n" +
        "You're out of the Pyrmaid.\n"
    else if (this.turnCount == this.timeLimit)
      "Imhotep has gained his full strength. It's impossible to stop him now, Egypt will never recover.\nGame over!"
    else if(this.player.hitpoints <= 0)
      "You've gone to the afterlife, you're fate depends on Osiris' mercy...\n Game over!"
    else  // game over due to player quitting
      "Quitter!"
  }

  /** Plays a turn by executing the given in-game command, such as "go west". Returns a textual
    * report of what happened, or an error message if the command was unknown. In the latter
    * case, no turns elapse. */
  def playTurn(command: String) = {
    if(!square5.hasMummys){
      square6.setNeighbors(Vector("west" -> square4, "east" -> square7)) // Killing the mummy opens new area.
    }
    val action = new Action(command)
    val outcomeReport = action.execute(this.player)
    if (outcomeReport.isDefined) {
      this.turnCount += 1
    }
    outcomeReport.getOrElse("Unknown command: \"" + command + "\".")
  }
}

