/*
 * TypeOuvertureMur-Projet-Bat.java
 * 
 * Copyright 2024 Elève <Elève@RGER-VRBSAEQD8G>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 * 
 * 
 */


public class TypeOuvertureMur {
	private double prix_unitaire;
	private double h;
	private double l;
	private String nom;
	
	 public TypeOuvertureMur(double prix_unitaire,double h, double l,String nom){
		 this.prix_unitaire =prix_unitaire;
		 this.h = h;
		 this.l = l;
		 this.nom = nom;
		 
		 	  public double getPrix_unitaire(){
		 return prix_unitaire;
	 }
	 
	 public void setPrix_unitaire(double prix_unitaire){
		 this.prix_unitaire = prix_unitaire;
		
	 }
	 	  public double getH(){
		 return h;
	 }
	 
	 public void setH(double h){
		 this.h = h;
	 }
	 
	 	  public double getL(){
		 return l;
	 }
	 
	 public void setL(double l){
		 this.l = l;
	 }
	 
	 	  public String getNom(){
		 return nom;
	 }
	 
	 public void setNom(String nom){
		 this.nom = nom;
	 }
}

