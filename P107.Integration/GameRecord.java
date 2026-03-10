public class GameRecord implements Comparable<GameRecord> {

  private String name;
  private GameRecord.Continent location;
  private int score;  //calculated by 18*damageGiven - 44 * damageTaken + 49969
  private int damageTaken;
  private int damageGiven;
  private String completionTime; //formatted in hhh:mm:ss

  //constructor
  public GameRecord(String name, GameRecord.Continent location, int score,
                    int damageTaken, int damageGiven, String completionTime) {
    this.name = name;
    this.location = location;
    this.score = score;
    this.damageTaken = damageTaken;
    this.damageGiven = damageGiven;
    this.completionTime = completionTime;
  }

  //accessors
  public String getName() {return this.name;}
  public GameRecord.Continent getContinent(){return this.location;}
  public int getScore() {return this.score;}
  public int getDamageTaken() {return this.damageTaken;}
  public int getDamageGiven() {return this.damageGiven;}
  public String getCompletionTime() {return this.completionTime;}

  // comparisons are made using scores, larger numbers are bigger
  @Override
  public int compareTo(GameRecord other) {
    return this.score-other.score;
  }

  protected static enum Continent {
    AFRICA, ASIA, ANTARCTICA, AUSTRALIA, EUROPE, NORTH_AMERICA, SOUTH_AMERICA
  }
}
