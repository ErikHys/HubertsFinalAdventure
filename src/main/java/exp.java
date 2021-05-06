public class exp {

    public static void main(String[] args) {
        int d = 0;
        for (int i = -5; i <= 5; i++) {
            for (int j = -5; j <= 5; j++) {
                if(Math.abs(j)+Math.abs(i) > 5) continue;
                d++;
            }
        }
        System.out.println(d);
    }
}
