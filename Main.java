import java.util.Scanner;
import java.util.Random;

public class Main {
  public static void main(String[] args) {

    String[] messages = { "Please enter a starting row for your knight: ",
        "Please enter a starting column for your knight: ",
        "Now, please enter a desination row for your knight: ",
        "Now, please enter a destination column for your knight: " }; // prompts
    int[] inputList = new int[4];
    int[] xMoves = { 2, 2, 1, 1, -2, -2, -1, -1 }; // moves the knight can make for x
    int[] yMoves = { 1, -1, 2, -2, 1, -1, 2, -2 }; // moves the knight can make for y
    int[] oneMoveAwayX = new int[8];
    int[] oneMoveAwayY = new int[8];
    // movesMade is not an 8 x 8 matrix, startRow and startCol will act as the index
    // for the movesMade array, since startRow and startCol can be 8, that's an
    // error if size of movesMade was 8 x 8
    boolean[][] movesMade = new boolean[9][9];
    // variables to recieve inputs from inputList
    int startRow = 0;
    int startCol = 0;
    int endRow = 0;
    int endCol = 0;

    int numMoves = 0; //will count number of moves made
    //moved and randomNum set outside to prevent repeated declaration
    boolean moved; //value true or false, based on if the knight has moved
    int randomNum; //will hold a random number between 1-8
    
    Scanner sc = new Scanner(System.in);
    Random r = new Random();

    // Start of the Program :
    promptForInput(sc, messages, inputList);

    // Calls method to set inputs from inputList into the following variables
    startRow = setRowCol(0, inputList);
    startCol = setRowCol(1, inputList);
    endRow = setRowCol(2, inputList);
    endCol = setRowCol(3, inputList);

    // For loop initializes the two arrays of the positions where the knight
    // is one move away from the destination; done for more control
    //of getting the knight to the goal
    for (int i = 0; i < oneMoveAwayX.length; i++) {// i < 8
      oneMoveAwayX[i] = endRow + xMoves[i];
      oneMoveAwayY[i] = endCol + yMoves[i];
    }
    // Displays where we are starting and sets current position to true
    //so we can see our current spot and not return to it
    System.out.println("Start:");
    System.out.println("(" + startRow + ", " + startCol + ")");
    movesMade[startRow][startCol] = true;

    // While Loop: Reaching the goal
    while (startRow != endRow || startCol != endCol) {
      //moved always reset to false at the next iteration of loop
      //if moved is ever set to true, it means we don't have to do any other 
      //move until the next iteration
      moved = false; 
      // Will always test if we are one move away, if so, make the move to get to goal
      for (int i = 0; i < xMoves.length; i++) { // i < 8
        if (startRow + xMoves[i] == endRow && startCol + yMoves[i] == endCol) {
          startRow += xMoves[i]; //Updates the current row and column (moves the knight)
          startCol += yMoves[i];
          moved = true;  
        }
      }
      // Tests if we can get to a position where we would be one move away
      // (aka if we are in a position of being two moves away)
      if (!moved) {
        // for loop intended to go through array of positions where its one move away
        for (int i = 0; i < oneMoveAwayX.length; i++) { // i < 8
          // Nested for loop intended to go through array of
          // all possible moves
          for (int j = 0; j < xMoves.length; j++) { // j < 8
            // if statement is true if we can put ourselves to be one
            // move away; calls method to see if the move in xMoves/yMoves array at index j is valid
            if (startRow + xMoves[j] == oneMoveAwayX[i] &&
                startCol + yMoves[j] == oneMoveAwayY[i] &&
                validMove(startRow, startCol, xMoves, yMoves, j)) {
              startRow += xMoves[j];
              startCol += yMoves[j];
              moved = true;
              break;   //leaves the for loop
            }
          }
        }
      }
      // Nested while: Make a random move if none of the above was true
      while (!moved) {
        randomNum = r.nextInt(8);
        // Will test if the move at index randomNum in provided lists are valid
        if (validMove(startRow, startCol, xMoves, yMoves, randomNum)) {
          // Will test if the position we're moving to has been reached before;
          // will enter if statement if only the next position is false
          if (!(movesMade[startRow + xMoves[randomNum]][startCol + yMoves[randomNum]])) {
            startRow += xMoves[randomNum];
            startCol += yMoves[randomNum];
            moved = true;
            // movesMade is only needed for making random moves
            //since the other loops take us to the goal
            movesMade[startRow][startCol] = true;
          }
        }
      }
      numMoves++;   //counts the number of moves
      System.out.println("(" + startRow + ", " + startCol + ")"); //displays changes in position
    }
    System.out.println(numMoves + " move(s)");
  } // End of Program

  // Method: Displays messages/prompt, validates input through for loop.
  // valid inputs are placed in the inputs array and returned.
  public static void promptForInput(Scanner sc, String[] messages, int[] inputList) {
    String invalid; //set outside to prevent repeated declaration
    for (int i = 0; i < messages.length; i++) {
      System.out.println(messages[i]);
      if (!sc.hasNextInt()) { // If the input is a string, invalid input
        invalid = sc.nextLine();
        System.out.println(invalid + " is not a valid input. Please restart program ");
        System.exit(0);
      }
      inputList[i] = sc.nextInt();
      if (inputList[i] < 0 || inputList[i] > 8) { // If the input is negative or more than 8
        System.out.println(inputList[i] + " is not a valid input. Please restart program ");
        System.exit(0);
      }
    }
  }

  // Method: takes the array of inputs and returns one
  // of the following inputs based on the index i,
  //done for ease of use of values though variables in main
  public static int setRowCol(int i, int[] inputList) {
    int setRowCol = i;
    switch (i) { // due to multiple if conditions, used switch cases instead
      case 0:
        setRowCol = inputList[i]; // return for startRow
        break;
      case 1:
        setRowCol = inputList[i]; // return for startCol
        break;
      case 2:
        setRowCol = inputList[i]; // return for endRow
        break;
      case 3:
        setRowCol = inputList[i]; // return for endCol
        break;
    }
    return setRowCol;
  }

  // Method: checks if the change in position due to a move at index i of
  // the provided lists is valid; only true if the change in position is between 1
  // & 8; prevents invalid moves
  public static boolean validMove(int rowX, int columnY, int[] xMoves, int[] yMoves, int i) {
    boolean valid = false;
    if ((rowX + xMoves[i] > 0 && rowX + xMoves[i] <= 8) &&
        (columnY + yMoves[i] > 0 && columnY + yMoves[i] <= 8)) {
      valid = true;
    }
    return valid;
  }
}