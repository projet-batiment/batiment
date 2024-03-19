/*
 * TypeMur-Projet-Bat.java
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


public class Type_Mur {
	private int id;
	private double ep;
	private double prix_metre;
	private String nom;
	 
	 public Type_Mur(int id,double ep, double prix_metre,String nom){
		 this.id =id;
		 this.ep = ep;
		 this.prix_metre = prix_metre;
		 this.nom = nom;
		 
		  public int getId(){
		 return id;
	 }
	 
	 public void setId(int id){
		 this.id = id;
	 }
	 
	  public double getEp(){
		 return ep;
	 }
	 
	 public void setEp(double ep){
		 this.ep = ep;
	 }
	 
	  public double getPrix_metre(){
		 return prix_metre;
	 }
	 
	 public void setPrix_metre(double prix_metre){
		 this.prix_metre = prix_metre;
	 }
	 
	  public String getNom(){
		 return nom;
	 }
	 
	 public void setNom(String nom){
		 this.nom = nom;
	 }
}

