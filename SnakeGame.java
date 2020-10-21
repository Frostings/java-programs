import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Scanner;

// Point class represents x, y tuple
class Point {
  int x;
  int y;
  Point(int x, int y) {
    this.x = x;
    this.y = y;
  }
}

// Creates a Frame to validate input from.
class Input extends Frame implements KeyListener {
  Point direction;
  private TextArea area;

  Input() {
    direction = new Point(0, -1);
    area = new TextArea();
    area.addKeyListener(this);
    add(area);
    setLayout(null);
    setVisible(true);  
  }

  public void keyPressed(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_UP) {
      if (direction.x != 0 || direction.y != 1) {
        direction.x = 0;
        direction.y = -1;
      }
    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
      if (direction.x != 0 || direction.y != -1) {
        direction.x = 0;
        direction.y = 1;
      }
    } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
      if (direction.y != 0 || direction.x != 1) {
        direction.x = -1;
        direction.y = 0;
      }
    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
      if (direction.y != 0 || direction.x != -1) {
        direction.x = 1;
        direction.y = 0;
      }
    }
  }

  public void keyReleased(KeyEvent e) {}
  public void keyTyped(KeyEvent e) {}  
}

// Linked list
class Node {
  Point pos;
  Node next;

  Node(Point pos, Node next) {
    this.pos = pos;
    this.next = next;
  }
}

// Snake implemented as a reversed linked list, with access to the end.
class Snake {
  Node head;
  Node tail;

  private int growAmount = 3;
  private Point bound;

  Snake(Point bound) {
    this.bound = bound;
    Node head = new Node(new Point(bound.x/2, bound.y-1), null);
    this.head = head;
    this.tail = head;
  }

  // Move the snake. Returns -1 if I hit a wall or myself, 1 if I'm growing, or 0 otherwise.
  int move(Point dir) {
    Point newPos = new Point(head.pos.x + dir.x, head.pos.y + dir.y);
    if (newPos.x < 0 || newPos.x >= bound.x) return -1;
    if (newPos.y < 0 || newPos.y >= bound.y) return -1;
    Node newHead = new Node(newPos, null);
    head.next = newHead;
    head = newHead;
    if (growAmount > 0) {
      --growAmount;
      return 1;
    } else {
      tail = tail.next;
      return 0;
    }
  }

  void onConsume() {
    growAmount += 3;
  }
}

// Our game
class Game {
  private int score = 0;
  private Point bound;
  private Point food = new Point(0,0);
  private char[][] cells;
  private Input input;
  private Snake snake;
  private Random random = new Random();
  private BufferedOutputStream buffer = new BufferedOutputStream(System.out);

  // constructor
  Game(Point bound) {
    this.bound = bound;
    cells = new char[bound.x][bound.y];

    for (int i = 0; i < bound.y; ++i) {
      for (int j = 0; j < bound.x; ++j) {
        cells[i][j] = ' ';
      }
    }

    cells[bound.y-1][bound.x/2] = 'O';
    snake = new Snake(bound);

    input = new Input();
    createFood();
  }

  // update the view into console
  void printGame() throws IOException {
    buffer.write(("SCORE: "+score+"\n").getBytes());
    buffer.write('*');
    for (int i = 0; i < bound.x; ++i) {
      buffer.write('-');
    }
    buffer.write('*');
    buffer.write('\n');
    for (int i = 0; i < bound.y; ++i) {
      buffer.write('|');
      for (int j = 0; j < bound.x; ++j) {
        buffer.write(cells[i][j]);
      }
      buffer.write('|');
      buffer.write('\n');
    }
    buffer.write('*');
    for (int i = 0; i < bound.x; ++i) {
      buffer.write('-');
    }
    buffer.write('*');
    buffer.write('\n');
    buffer.flush();
  }

  // returns true if I made a valid move, false otherwise
  boolean updateState() {
    int lastTailX = snake.tail.pos.x;
    int lastTailY = snake.tail.pos.y;
    int moveCode = snake.move(input.direction);
    // I hit a wall!
    if (moveCode == -1) {
      return false;
    }
    // I hit myself!
    if (cells[snake.head.pos.y][snake.head.pos.x] == 'O') {
      return false;
    }
    // Update my head
    cells[snake.head.pos.y][snake.head.pos.x] = 'O';
    // Check if there's a food
    if (snake.head.pos.x == food.x && snake.head.pos.y == food.y) {
      snake.onConsume();
      ++score;
      createFood();
    }
    // Update my tail
    if (moveCode == 0) {
      cells[lastTailY][lastTailX] = ' ';
    }
    return true;
  }

  // create a food in a random location, not on top of my body
  void createFood() {
    // Create a random food location
    int posX = random.nextInt(bound.x);
    int posY = random.nextInt(bound.y);
    
    // Find the first cell that's open if I'm trying to add onto my body
    while(cells[posY][posX] == 'O') {
      ++posX;
      if (posX == bound.x) {
        posX = 0;
        ++posY;
      }
      if (posY == bound.y) {
        posY = 0;
      }
    }
    cells[posY][posX] = '.';
    
    food.x = posX;
    food.y = posY;
  }

  // the game entry point
  void start() throws InterruptedException, IOException {
    System.out.println("Press ENTER to start");
    Scanner sc = new Scanner(System.in);
    sc.nextLine();
    while(true) {
      if (!updateState()) {
        System.out.println("GAME OVER.");
        return;
      }
      printGame();
      Thread.sleep(200);
    }
  }
}

public class Main {
  public static void main(String[] args) throws InterruptedException, IOException {
    boolean playAgain = true;
    do {
      Scanner sc = new Scanner(System.in);
      Game game = new Game(new Point(30, 30));
      game.start();
      System.out.println("PLAY AGAIN? [Y/N]");
      String input = sc.nextLine();
      if (input.equals("N") || input.equals("n")) {
        playAgain = false;
      }
    } while (playAgain);
  }
}