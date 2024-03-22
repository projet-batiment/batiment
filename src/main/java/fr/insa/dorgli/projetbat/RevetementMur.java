package main.java.fr.insa.dorgli.projetbat;

public class RevetementMur {
    TypeRevetement typeRevetement;
    int pos1L;
    int pos1H;
    int pos2L;
    int pos2H;

    public RevetementMur(TypeRevetement typeRevetement, int pos1L, int pos1H, int pos2L, int pos2H) {
        this.pos1L = pos1L;
        this.pos1H = pos1H;
        this.pos2L = pos2L;
        this.pos2H = pos2H;
    }
}
