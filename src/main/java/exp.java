public class exp {

    public static void main(String[] args) {
        int d = 0;
        for (int i = -3; i <= 3; i++) {
            for (int j = -3; j <= 3; j++) {
                if(Math.abs(j)+Math.abs(i) > 3) continue;
                d++;
            }
        }
        System.out.println(d);
    }
}
