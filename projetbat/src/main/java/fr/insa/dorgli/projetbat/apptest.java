package fr.insa.dorgli.projetbat;
package com.example;
private ArrayList<Piece> pieces;
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
    
                       
    
