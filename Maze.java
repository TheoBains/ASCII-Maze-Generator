import java.util.Random;
import java.util.Scanner;
import java.util.Stack;
import java.util.ArrayList;

class Maze {

    public boolean gameover = false;
    public long time = 500;
    public int cols;
    public int rows;
    public int moves;
    public int highscore;
    public char[][] map;
    public boolean[][] visited;
    public boolean generated;
    public int px; // player x
    public int py; // player y
    public int fx; // finish x
    public int fy; // finish y
    public int gx; // generator x
    public int gy; // generator y
    public char[] icons = { ' ', '|', '-', '+', '0', 'X', '#' };
    Scanner scanner;

    public static void main(String[] args) {
        Maze maze = new Maze();
        maze.play();
    }

    public void init() {
        scanner = new Scanner(System.in);

        System.out.println("How wide would you like the maze to be? (Any odd number greater than 3 please)");
        cols = scanner.nextInt();

        System.out.println("How tall would you like the maze to be? (Any odd number greater than 3 please)");
        rows = scanner.nextInt();

        gameover = false;

        moves = 0;

        Stack<Integer> XHistory = new Stack<>();
        Stack<Integer> YHistory = new Stack<>();

        Random randompx = new Random();
        Random randompy = new Random();

        px = (((randompx.nextInt(cols - 2)) / 2) * 2) + 1; // sets random player location
        py = (((randompy.nextInt(rows - 2)) / 2) * 2) + 1;

        Random randomfx = new Random();
        Random randomfy = new Random();

        fx = (((randomfx.nextInt(cols - 2)) / 2) * 2) + 1; // sets random finish location
        fy = (((randomfy.nextInt(rows - 2)) / 2) * 2) + 1;

        while (fx == px && fy == py) { // stops finish being the start
            randomfx = new Random();
            randomfy = new Random();

            fx = (((randomfx.nextInt(cols - 2)) / 2) * 2) + 1;
            fy = (((randomfy.nextInt(rows - 2)) / 2) * 2) + 1;
        }

        Random randomx = new Random();
        Random randomy = new Random();

        gx = (((randomx.nextInt(cols - 2)) / 2) * 2) + 1; // sets random point to generate from
        gy = (((randomy.nextInt(rows - 2)) / 2) * 2) + 1;

        map = new char[cols][rows]; // sets map array
        visited = new boolean[cols + 2][rows + 2]; // sets array to store visited

        // grid setup
        for (int x = 1; x < cols - 1; x++) { // fills in blank space
            for (int y = 1; y < rows - 1; y++) {
                map[x][y] = icons[0];
            }
        }
        for (int x = 0; x < cols; x += 2) { // vertical lines
            for (int y = 0; y < rows; y++) {
                map[x][y] = icons[1];
            }
        }
        for (int y = 0; y < rows; y += 2) { // horizontal lines
            for (int x = 0; x < cols; x++) {
                map[x][y] = icons[2];
            }
        }
        for (int x = 0; x < cols; x += 2) { // corners
            for (int y = 0; y < rows; y += 2) {
                map[x][y] = icons[3];
            }
        }

        generated = false;
        int counter = 1;

        while (generated == false) {
            visited[gx + 1][gy + 1] = true;

            char direction = GetDirection(gx, gy);

            if (direction == 'X') {
                XHistory.pop();
                YHistory.pop();

                gx = XHistory.peek();
                gy = YHistory.peek();
            } else if (direction == 'N') {
                map[gx][gy - 1] = icons[0];
                gy = gy - 2;
                XHistory.push(gx);
                YHistory.push(gy);
            } else if (direction == 'E') {
                map[gx + 1][gy] = icons[0];
                gx = gx + 2;
                XHistory.push(gx);
                YHistory.push(gy);
            } else if (direction == 'S') {
                map[gx][gy + 1] = icons[0];
                gy = gy + 2;
                XHistory.push(gx);
                YHistory.push(gy);
            } else if (direction == 'W') {
                map[gx - 1][gy] = icons[0];
                gx = gx - 2;
                XHistory.push(gx);
                YHistory.push(gy);
            }

            counter = counter + 1;

            if (XHistory.size() == 1 && counter > 2) {
                generated = true;
            }
        }

        map[px][py] = icons[4]; // sets player start
        map[fx][fy] = icons[5]; // sets finish
    }

    public Character GetDirection(int gx, int gy) {
        ArrayList<Character> directions = new ArrayList<Character>();

        if (visited[gx + 1][gy - 1] == false && !(gy == 1)) {
            directions.add('N');
        }
        if (visited[gx + 3][gy + 1] == false && !(gx == (cols - 2))) {
            directions.add('E');
        }
        if (visited[gx + 1][gy + 3] == false && !(gy == (rows - 2))) {
            directions.add('S');
        }
        if (visited[gx - 1][gy + 1] == false && !(gx == 1)) {
            directions.add('W');
        }

        if (directions.size() > 0) {
            Random random = new Random();
            char d = directions.get(random.nextInt(directions.size()));
            directions.clear();
            return (d);
        } else {
            directions.clear();
            return ('X');
        }
    }

    public void render() {
        System.out.println(
                "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nUse the W,A,S,D keys to move your character (0) to reach the target (X)\n\n");
        for (int i = 0; i < (cols / 2) - 9; i++) {
            System.out.print("~");
        }
        System.out.print("Welcome To the Maze!");
        for (int i = 0; i < (cols / 2) - 10; i++) {
            System.out.print("~");
        }
        System.out.println();
        System.out.print("\nMove: " + moves);
        for (int i = 0; i < cols - 21; i++) {
            System.out.print(" ");
        }
        System.out.println("Highscore: " + highscore + "\n\n");
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                System.out.print(map[x][y]);
            }
            System.out.println();
        }
    }

    public void input() {
        char input = scanner.next().charAt(0);

        switch (input) {
            case 'a':
                if (map[px - 1][py] == icons[0] || map[px - 1][py] == icons[5]) {
                    map[px][py] = icons[0];
                    px = px - 2;
                    map[px][py] = icons[4];
                    moves = moves + 1;
                }
                break;
            case 'd':
                if (map[px + 1][py] == icons[0] || map[px + 1][py] == icons[5]) {
                    map[px][py] = icons[0];
                    px = px + 2;
                    map[px][py] = icons[4];
                    moves = moves + 1;
                }
                break;
            case 's':
                if (map[px][py + 1] == icons[0] || map[px][py + 1] == icons[5]) {
                    map[px][py] = icons[0];
                    py = py + 2;
                    map[px][py] = icons[4];
                    moves = moves + 1;
                }
                break;
            case 'w':
                if (map[px][py - 1] == icons[0] || map[px][py - 1] == icons[5]) {
                    map[px][py] = icons[0];
                    py = py - 2;
                    map[px][py] = icons[4];
                    moves = moves + 1;
                }
                break;
            default:
                break;
        }
    }

    public void update() {
        if (px == fx && py == fy) {
            gameover = true;
        }
    }

    public void endgame() {
        boolean newhs = false;
        if (moves < highscore || highscore == 0) {
            highscore = moves;
            newhs = true;
        }
        render();
        if (newhs == true) {
            System.out.println("Well done! That's a new highscore of " + highscore + " moves!");
        } else {
            System.out.println("Congratulations, you completed the maze in " + moves + " moves!");
        }
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
        }
    }

    public final void play() {
        init();
        gameloop();
        endgame();
    }

    public final void gameloop() {
        while (!gameover) {
            render();
            input();
            update();
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
            }
        }
    }
}