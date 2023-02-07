public class Vector {
  public int x;
  public int y;
  public Vector (int xx, int yy) {
    x = xx;
    y = yy;
  }
  public void add (Vector other) {
    this.x += other.x;
    this.y += other.y;
  }
  public void scale (int num) {
    this.x *= num;
    this.y *= num;
  }
}