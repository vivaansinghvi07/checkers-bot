public class Piece {
  private int ID;
  private Vector location;
  private String id;
  private int team;
  private boolean king;
  public Piece (int y, int x, String id, int t) {
    location = new Vector(x, y);
    this.id = id; 
    this.team = t;
    this.king = false;
  }
  public Vector getLocation() {
    return this.location;
  }
  public void move(Vector v) {
    this.location.add(v);
  }
  public void makeKing() {
    this.king = true;
  }
  public boolean isKing() {
    return this.king;
  }
  public String getID() {
    return this.id;
  }
}