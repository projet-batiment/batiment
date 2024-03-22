package main.java.fr.insa.dorgli.projetbat;

// Programme "principal" pour faire des tests avec les classes et compagnie
public class Tests {
    public static void main(String[] args) {
        Point p1 = new Point(2.1, 1.4, 0);
        Point p2 = new Point(4.2, 2.3, 0);
        Mur m1 = new Mur(p1, p2, 3.5);

        System.out.println(m1);
    }
}
