public class exp {

    public static void main(String[] args) {
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if(Math.abs(j)+Math.abs(i) > 2) continue;
                System.out.println(j + " " + i);
            }
        }
    }
}
