package o1.adventure

import scala.collection.mutable.Map
import scala.util.Random


/** A `Player` object represents a player character controlled by the real-life user of the program.
  *
  * A player object's state is mutable: the player's location and possessions can change, for instance.
  *
  * @param startingArea  the initial location of the player */
class Player(startingArea: Area) {

  private var currentLocation = startingArea        // gatherer: changes in relation to the previous location
  private var quitCommandGiven = false
  private val bag = Map[String, Item]()
  private var itemUsed = false
  private var attackMove = 10
  private var hp = 40
  private var maxHp = 40

  /** Determines if the player has indicated a desire to quit the game. */
  def hasQuit = this.quitCommandGiven

  /** Returns the current location of the player. */
  def location = this.currentLocation

  /** Attempts to move the player in the given direction. This is successful if there
    * is an exit from the player's current location towards the direction name. Returns
    * a description of the result: "You go DIRECTION." or "You can't go DIRECTION." */
  def go(direction: String) = {
    val destination = this.location.neighbor(direction)
    this.currentLocation = destination.getOrElse(this.currentLocation)
    if (destination.isDefined) "You go " + direction + "." else "You can't go " + direction + "."
  }

  /** Causes the player to rest for a short while (this has no substantial effect in game terms).
    * Returns a description of what happened. */
  def rest() = {
    if(this.location.hasMummys){
      "No time to rest, the mummy is nearby"
    } else {
      hp = maxHp
      "You rest for a while and admire the Egyptian architecture, you gain your health back"
    }
  }


  /** Signals that the player wants to quit the game. Returns a description of what happened within
    * the game as a result (which is the empty string, in this case). */
  def quit() = {
    this.quitCommandGiven = true
    ""
  }

  def has(itemName: String): Boolean = {
    if(bag.contains(itemName))
      true
    else
      false
  }

  def drop(itemName: String): String = {
    if(has(itemName)){
      this.location.addItem(bag(itemName))
      bag -= itemName
      "You drop the " + itemName + "."
    }
    else
      "You don't have that!"
  }

  def examine(itemName: String): String = {
    if(has(itemName))
      "You look closely at the " + itemName + ".\n" + bag(itemName).description
    else
      "If you want to examine something, you need to pick it up first."
  }

  def get(itemName: String): String = {
    if(location.contains(itemName)){
      bag += itemName -> location.removeItem(itemName).get
      "You pick up the " + itemName + "."
    }
    else
      "There is no " + itemName + " here to pick up."
  }

  def inventory: String = {
    if(bag.nonEmpty)
      "You are carrying\n" + bag.keys.mkString("\n")
    else
      "You are empty handed."
  }

  def useItem(itemName: String) = {
    if(has(itemName)){
      itemName match {
        case "the eye of horus" => itemUsed = true; "You have used the item\n" +
          "If you have the courage try to use command xyzzy."
      }
    }else{
      "You don't have the item in question"
    }
  }

  def fight() = {
    if(this.location.hasMummys){
      val mummyInThisLocation = this.location.mummys.head
      var takenHit = Random.nextInt(this.location.mummys.head.attackDamage)
      var givenHit = Random.nextInt(this.attackMove)
      var playerHp = hp - takenHit
      var mummyHp = mummyInThisLocation.hp - givenHit
      if(playerHp <= 0){
        hp -= takenHit
        "The Mummy has consumed your body, your soul won't move to the afterlife."
      }
      else if(mummyHp <= 0){
        this.location.removeMummy()
        "You have defeated the mummy, Egypt is saved!\n" +
          "Something is happening in the pyramid, it must be the secret door I was reading about in the hieroglyphs..."
      }
      else{
        hp -= takenHit
        mummyInThisLocation.hp = mummyInThisLocation.hp - givenHit
        "You swing at the mummy, you dealt " + givenHit + " damage to the Mummy and the mummy has " + mummyHp + " hp left.\n" +
          "The mummy is consuming your body, it dealt " + takenHit + " damage to you and you have " + playerHp + " hp left"
      }
    }else{
      "There is nobody to fight here, try moving elsewhere."
    }
  }

  def hasUsedItem = this.itemUsed // Requirement for winning the game

  def hitpoints = hp

  def showHp = "You have " + hp + " hitpoints"

  def xyzzy = {
    if(this.hasUsedItem) {
      maxHp = 666
      hp = 666
      attackMove = 666
      "You carve out your left eye and place the eye of Horus where your eye used to be.\n" +
        "You gain the powers of Horus..."
    } else {
      "You must use the hidden treasure to activate this command."
    }
  }

  def help() = {
    "Commands: " +
    "\n- drop [itemname]:    Drops an item if player posesses the item in question" +
    "\n- examine[itemname]:  Player examines the item of the given name." +
    "\n- useitem [itemname]:     Uses item if the item is in your inventoryn" +
    "\n- get [itemname]:     Attempts to pick up an item with a given name." +
    "\n- go [direction]:     Attempts to move the player to the direction in question" +
    "\n- rest:               You heal for a while and gain back the lost health" +
    "\n- quit:               Ends the game" +
    "\n- fight:              Attacks Mummy if there is a mummy nearby" +
    "\n- inventory:          Shows a list of what the player is carrying." +
    "\n- hp:                 Shows your current health" +
    "\n- xyzzy:              If you posses the eye of the horus you gain more power otherwise does nothing" +
    "\n- hp:                 Shows How much hp you have left."
  }

  /** Returns a brief description of the player's state, for debugging purposes. */
  override def toString = "Now at: " + this.location.name

}


