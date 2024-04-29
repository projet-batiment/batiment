package fr.insa.dorgli.projetbat;
package com.example;
private ArrayList<Piece> pieces;
private Appart appart;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.media.AudioClip;


public class App extends Application{
  public void start(Stage Stage) {
    Label message = new Label("Devis piece");
    message.setFont(new Font(20));

    
    
    Button refreshButton = new Button("actualiser");
    refreshButton.setOnAction(evt -> {System.out.println(Piece.prix););
    prix= getPiece.prix
    Scene scene = new Scene(root, 450, 200);
    stage.setScene(scene);
    stage.setTitle("Devis piece");
    stage.show(evt -> {"prix = " + Piece.prix;
                       Piece.ouvertures + Piece.ouvertures.prixOuverture;
                      });
    Button totalButton = new Button ("Total");
    totalButton.setonAction(evt -> {message.setText("TOTAL APPART:");
                                    System.out.println(pieces.prix + " € " );
                                   });
    Button detailButton = new Button ("DetailPieces");
    detailButton.setonAction(evt -> {message.setText("DETAIL PIECES");
                                     System.out.println(get.appart.TypeAppart + " n°: " + get.appart.nom);
                                     System.out.println("piece : " + get.pieces.nom + "| prix : " + get.pieces.prix );
                                    });
    Button totouvButton = new Button ("TotalOuverture");
    totouvButton.setonAction(evt -> {message.setText("TOTAL OUVERTURE");
                                     System.out.println("ouvertures du plafond : " + get.pieces.plafond.ouvertures.TypeOuvertureNiveau + "| prix ouverture : " + get.pieces.plafond.ouvertures.TypeOuvertureNiveau.prixOuverture);
                                     System.out.println("ouvertures du plafond : " + get.pieces.sol.ouvertures.TypeOuvertureNiveau + "| prix ouverture : " + get.pieces.sol.ouvertures.TypeOuvertureNiveau.prixOuverture);
                                     System.out.println("ouvertures des murs : " + get.pieces.murs.ouvertures.TypeOuvertureMur + "| prix ouverture : " + get.pieces.murs.ouvertures.TypeOuvertureMur.prixUnitaire);
                                    });

    Button totrevButton = new Button ("TotalRevetement");
    totrevButton.setonAction(evt -> {message.setText(" TOTAL REVETEMENT");
                                     System.out.println(" revetement mur: " + get.pieces.murs.revetements1.typeRevetement + "| prix revetement : " + get.pieces.murs.revetement1.typeRevetement.prixUnitaire);
                                     System.out.println(" revetement plafond: " + get.pieces.plafond.revetements.typeRevetement + "|prix revetement : " + get.pieces.plafond.revetements.typeRevetement.prixUnitaire);
                                     System.out.println(" revetement sol: " + get.pieces.sol.revetements.typeRevetement + "|prix revetement : " + get.pieces.sol.revetements.typeRevetement.prixUnitaire);
                                    });
    
    HBox buttonBar = new HBox(20, refreshButton, detailButton);
    buttonBar.setAlignment(Pos.Top);
    Hbox button2Bar= new HBox(20, totalButton, totrevButton, totouvButton)
    button2Bar.setAlignement(Pos.Bottom);
      
  }
}
    
                       
    
