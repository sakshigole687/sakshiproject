
public class loop {
    public static void main(String[] args) {
        int n = 4;
        int m = 5;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (i == 1 || i == n || j == m) {
                    System.out.println("*");
                }
            }
        }
    }
}