/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.fr.insa.dorgli.projetbat;
import java.util.ArrayList;
//import main.java.fr.insa.dorgli.projetbat.Piece;
    
/**
 *
 * @author rdorgli01
 */
public class Appart {
    private String nom;
    private ArrayList<Piece> pieces;
    private TypeAppart typeAppart;
    
public Appart(ArrayList<Piece> pieces,TypeAppart typeAppart, String nom){
        this.pieces=pieces;
        this.typeAppart=typeAppart;
        this.nom=nom;
        
}

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    public void setPieces(ArrayList<Piece> pieces) {
        this.pieces = pieces;
    }

    public TypeAppart getTypeAppart() {
        return typeAppart;
    }

    public void setTypeAppart(TypeAppart typeAppart) {
        this.typeAppart = typeAppart;
    }
    
    
    
   
}
