import java.util.Scanner;
import java.util.concurrent.TimeUnit;

class Main {
  public static void main(String[] args) {
    Board board = new Board();
    Scanner scan = new Scanner(System.in);
    System.out.println("\nWelcome to Checkers! Move your piece according to the following reference: \n");
    System.out.println("  -|---|---|---|-\n   | 1 |   | 2 |\n  -|---|---|---|-\n   |   | K |   |\n  -|---|---|---|-\n   | 3 |   | 4 |\n  -|---|---|---|-\n");
    System.out.println("As you can see above, the moves 1-4 represent the possible moves you can make with your piece. Your pieces are represented by letters, and are captial if they are a king. The bot's pieces are represeted by O's, and 0's if they are kings. Make a move by typing the piece letter followed by the move number, using the reference. An example move is \"d2\".\n");
    System.out.println("Note: Double captures are not supported in this game. Also, the only way to end the game is to take all the opponent's pieces, or lose all of yours.\n");
    System.out.println("Read the instructions and press enter when you are ready to play.");
    scan.nextLine();
    while (!board.gameIsOver()) {
      if (!board.turn) {
        System.out.println(board);
        board.playerMove(scan.nextLine());
        board.promote();
      }
      else {
        try {
          System.out.println(board);
          TimeUnit.SECONDS.sleep(1);
          System.out.print("My turn.");
          TimeUnit.SECONDS.sleep(1);
          board.botMove();
          board.promote();
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    System.out.println(board + "The game is over!");
    scan.close();
  }
}