public class Board {
  private static String pieceIDS = "abcdefghijkl";
  public boolean turn;
  private Piece[] playerPieces;
  private Piece[] botPieces;
  private String[][] board;
  public Board () {
    this.board = new String[8][8];
    for (String[] arr : this.board) {
      for (int i = 0; i < 8; i++) {
        arr[i] = " "; 
      }
    }
    int counter = 0;
    botPieces = new Piece[12];
    boolean reAdd = false;
    for (int i = 1; i < 24; i += 2) {
      if (i >= 9 && i <= 15) {
        reAdd = true;
        i--;
      }
      else {
        reAdd = false;
      }
      Piece piece = new Piece(i / 8, i % 8, "" + counter, 0); 
      botPieces[counter] = piece;
      board[i / 8][i % 8] = "" + counter;
      counter++;
      if (reAdd) {
        i++;
      }
    }
    counter = 0;
    playerPieces = new Piece[12];
    for (int i = 40; i < 64; i += 2) {
      if (i >= 48 && i <= 55) {
        reAdd = true;
        i++;
      }
      else {
        reAdd = false;
      }
      char id = (char) ('a' + counter);
      Piece piece = new Piece(i / 8, i % 8, id + "", 1);
      playerPieces[counter] = piece;
      board[i / 8][i % 8] = id + "";
      counter++;
      if (reAdd) {
        i--;
      }
    }
    this.turn = false;
  }
  public String toString () {
    String out = new String("");
    for (String[] arr : this.board) {
      out += "|---|---|---|---|---|---|---|---|\n";
      for (String str : arr) {
        if (isNumeric(str)) {
          if (botPieces[Integer.parseInt(str)].isKing()) {
            str = "0";
          }
          else {
            str = "O";
          }
        }
        else if (isAlpha(str) && playerPieces[pieceIDS.indexOf(str)].isKing()) {
          str = str.toUpperCase();
        }
        out += "| " + str + " ";
      }
      out += "|\n";
    }
    return "\n" + out + "|---|---|---|---|---|---|---|---|\n";
    
  }
  private static boolean isNumeric (String s) {
    try {
      Integer.parseInt(s);
      return true;
    }
    catch (Exception e) {
      return false;
    }
  }
  public void playerMove (String s) {
    String move;
    String piece;
    try {
      piece = s.substring(0, 1);
      move = s.substring(1, 2);
    }
    catch (Exception e) {
      System.out.println("Please enter an input in the form of [piece][move], such as d4!");
      return;
    }
    Vector v;
    if (move.equals("1")) {
      v = new Vector(-1, -1);
    }
    else if (move.equals("2")) {
      v = new Vector(1, -1);
    }
    else if (move.equals("3")) {
      v = new Vector(-1, 1);
    }
    else if (move.equals("4")) {
      v = new Vector(1, 1);
    }
    else {
      System.out.println("You can only make a move from 1 to 4!");
      return;
    }
    // get location of object
    Piece pce;
    try {
      pce = playerPieces[pieceIDS.indexOf(piece)];
    }
    catch (Exception e) {
      System.out.println("You can only move a piece that you have on the board!");
      return;
    }
    if (pce == null) {
      System.out.println("You cannot move a taken piece!");
      return;
    }
    if (!pce.isKing() && (move.equals("3") || move.equals("4"))) {
      System.out.println("You cannot move backwards when your piece is not a king!");
      return;
    }
    Vector loco = pce.getLocation();
    Vector result = new Vector(loco.x, loco.y);
    result.add(v);
    try {
      if (isNumeric(this.board[result.y][result.x]) && checkCapture(result, v)) {
        this.board[loco.y][loco.x] = " ";
        v.scale(2);
        pce.move(v);
        loco = pce.getLocation();
        this.board[loco.y][loco.x] = piece;
        
        
        String temp = this.board[result.y][result.x];
        this.board[result.y][result.x] = " ";
        botPieces[Integer.parseInt(temp)] = null;
        // delete the other piece that has been captured
      }
      else if (!this.board[result.y][result.x].equals(" ")) {
        System.out.println("Sorry, this move leads to another piece!");
        return;
      }
      else {
        this.board[loco.y][loco.x] = " ";
        pce.move(v);
        loco = pce.getLocation();
        this.board[loco.y][loco.x] = piece;
      }
    }
    catch (Exception e) {
      System.out.println("Sorry, this move does not work!");
      return;
    }
    turn = !turn;
  }
  private boolean checkCapture (Vector result, Vector move) {
    Vector cap = new Vector(result.x, result.y);
    cap.add(move);
    try {
      if (this.board[cap.y][cap.x].equals(" ")) {
        return true;
      }
      else {
        return false;
      }
    }
    catch (Exception e) {
      return false;
    }
  }
  public void botMove () {
    if (botCapture() == 1) {
      turn = !turn;
      return;
    }
    else if (moveWithoutCapture() == 1) {
      turn = !turn;
      return;
    }
    else {
      moveAnywhere();
    }
    turn = !turn;
    
    // check if there are captures available
    // check if there are normal moves that are not resulting in captures
    // check if there are normal moves that result in captures
  }
  private void capture (Vector location, Vector move, Piece piece) {
    // clears where the capturing piece was
    this.board[location.y][location.x] = " ";
    
    // clears the piece that was previously on the spot
    String temp = this.board[location.y + move.y][location.x + move.x];
    this.board[location.y + move.y][location.x + move.x] = " ";
    playerPieces[pieceIDS.indexOf(temp)] = null;
    
    // moves the capturing piece
    move.scale(2);
    location.add(move);
    this.board[location.y][location.x] = piece.getID();
  }
  private int botCapture () {
    for (Piece piece : botPieces) {
      if (piece == null) {
        continue;
      }
      Vector location = piece.getLocation();
      Vector move1 = new Vector(1, 1);
      Vector move2 = new Vector(-1, 1);
      Vector result1 = new Vector(location.x, location.y);
      Vector result2 = new Vector(location.x, location.y);
      result1.add(move1);
      result2.add(move2);
      if (checkCapture(result1, move1) && isAlpha(board[location.y + move1.y][location.x + move1.x])) {
        capture(location, move1, piece);
        return 1;
      }
      else if (checkCapture(result2, move2) && isAlpha(board[location.y + move2.y][location.x + move2.x])) {
        capture (location, move2, piece);
        return 1;
      }
      if (piece.isKing()) {
        Vector move3 = new Vector(1, -1);
        Vector move4 = new Vector(-1, -1);
        Vector result3 = new Vector(location.x, location.y);
        Vector result4 = new Vector(location.x, location.y);
        result3.add(move3);
        result4.add(move4);
        if (checkCapture(result3, move3) && isAlpha(board[location.y + move3.y][location.x + move3.x])) {
          capture(location, move3, piece);
          return 1;
        }
        else if (checkCapture(result4, move4) && isAlpha(board[location.y + move4.y][location.x + move4.x])) {
          capture (location, move4, piece);
          return 1;
        }
      }
    }
    return -1;
  }
  private int moveWithoutCapture() {
    for (Piece piece : botPieces) {
      if (piece == null) {
        continue;
      }
      Vector location = piece.getLocation();
      if (piece.isKing()) {
        Vector move3 = new Vector(1, -1);
        Vector move4 = new Vector(-1, -1);
        Vector result3 = new Vector(location.x, location.y);
        Vector result4 = new Vector(location.x, location.y);
        result3.add(move3);
        result4.add(move4);
        if (checkMove(result3, move3, piece)) {
          move(location, move3, piece);
          return 1;
        }
        else if (checkMove(result4, move4, piece)) {
          move(location, move4, piece);
          return 1;
        }
      }
      Vector move1 = new Vector(1, 1);
      Vector move2 = new Vector(-1, 1);
      Vector result1 = new Vector(location.x, location.y);
      Vector result2 = new Vector(location.x, location.y);
      result1.add(move1);
      result2.add(move2);
      if (checkMove(result1, move1, piece)) {
        move(location, move1, piece);
        return 1;
      }
      else if (checkMove(result2, move2, piece)) {
        move(location, move2, piece);
        return 1;
      }
    }
    return -1;
  }
  private void move (Vector location, Vector move, Piece piece) {
    this.board[location.y][location.x] = " ";
    location.add(move);
    this.board[location.y][location.x] = piece.getID();
  }
  private boolean isAlpha (String s) {
    if (s.equals(" ")) {
      return false;
    }
    try {
      Integer.parseInt(s);
      return false;
    }
    catch (Exception e) {
      return true;
    }
  }
  public void promote() {
    for (Piece piece : playerPieces) {
      if (piece == null) {
        continue;
      }
      if (piece.getLocation().y == 0) {
        piece.makeKing();
      }
    }
    for (Piece piece : botPieces) {
      if (piece == null) {
        continue;
      }
      if (piece.getLocation().y == 7) {
        piece.makeKing();
      }
    }
    return;
  }
  private boolean checkMove(Vector result, Vector move, Piece piece) {
    Vector test = new Vector(result.x, result.y);
    try {
      if (board[test.y][test.x].equals(" ")) {
        if (findEnemies(test, piece)) {
          // if the opponents sqaure in front of you when you try to move, dont.
          return false;
        }
        else {
          // otherwise, if its empty or the square is on the bot team, we can move
          return true;
        }
      }
      else {
        // if the square where you want to move is not a space, dont move there
        return false;
      }
    }
    catch (Exception e) {
      // if there is no square in the result location, return false and continue on
      return false;
    }
  }
  private boolean findEnemies (Vector v, Piece piece) {
    try { // modify case to include when there are captures or not
      if (isAlpha(board[v.y + 1][v.x + 1]) && (board[v.y - 1][v.x - 1].equals(" ") || board[v.y - 1][v.x - 1].equals(piece.getID()))) {
        return true;
      }
    }
    catch (Exception e) {}
    try {
      if (isAlpha(board[v.y + 1][v.x - 1]) && (board[v.y - 1][v.x + 1].equals(" ") || board[v.y - 1][v.x + 1].equals(piece.getID()))) {
        return true;
      }
    }
    catch (Exception e) {}
    try {
      if (isAlpha(board[v.y - 1][v.x + 1]) && (board[v.y + 1][v.x - 1].equals(" ") || board[v.y + 1][v.x - 1].equals(piece.getID())) && playerPieces[pieceIDS.indexOf(board[v.y - 1][v.x + 1])].isKing()) {
        return true;
      }
    }
    catch (Exception e) {}
    try {
      if (isAlpha(board[v.y - 1][v.x - 1]) && (board[v.y + 1][v.x + 1].equals(" ") || board[v.y + 1][v.x + 1].equals(piece.getID())) && playerPieces[pieceIDS.indexOf(board[v.y - 1][v.x - 1])].isKing()) {
        return true;
      }
    }
    catch (Exception e) {}
    return false;
  }
  private void moveAnywhere() {
    for (Piece piece : botPieces) {
      if (piece == null) {
        continue;
      }
      Vector location = piece.getLocation();
      Vector move1 = new Vector(1, 1);
      Vector move2 = new Vector(-1, 1);
      Vector result1 = new Vector(location.x, location.y);
      Vector result2 = new Vector(location.x, location.y);
      result1.add(move1);
      result2.add(move2);
      try {
        if (board[result1.y][result1.x].equals(" ")) {
          move(location, move1, piece);
          return;
        }
      }
      catch (Exception e) {}
      try {
        if (board[result2.y][result2.x].equals(" ")) {
          move(location, move2, piece);
          return;
        }
      }
      catch (Exception e) {}
      if (piece.isKing()) {
        Vector move3 = new Vector(1, -1);
        Vector move4 = new Vector(-1, -1);
        Vector result3 = new Vector(location.x, location.y);
        Vector result4 = new Vector(location.x, location.y);
        result3.add(move3);
        result4.add(move4);
        try {
          if (board[result3.y][result3.x].equals(" ")) {
            move(location, move3, piece);
            return;
          }
        }
        catch (Exception e) {}
        try {
          if (board[result4.y][result4.x].equals(" ")) {
            move(location, move4, piece);
            return;
          }
        }
        catch (Exception e) {}
      }
    }
    return;
  }
  public boolean gameIsOver() {
    boolean playerOver = true;
    for (Piece pc : playerPieces) {
      if (pc != null) {
        playerOver = false;
      }
    }
    boolean botOver = true;
    for (Piece pc : botPieces) {
      if (pc != null) {
        botOver = false;
      }
    }
    return playerOver || botOver;
  }
}
