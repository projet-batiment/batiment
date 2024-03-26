/*
 * Batiment.java
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

package main.java.fr.insa.dorgli.projetbat;

import java.util.*

		
public class Batiment {
	private TypeBatiment typeBatiment;
	private ArrayList niveau;
	private ArrayList appart;
	
	public Batiment( TypeBatiment typeBatiment, Arraylist niveau, Arraylist appart){
		this.typeBatiment = typeBatiment;
		this.niveau = niveau;
		this.appart = appart;
		
		public TypeBatiment getTypeBatiment(){
		 return typeBatiment;
	 }
	 
	 public void setTypeBatiment(TypeBatiment typeBatiment){
		 this.typeBatiment = typeBatiment;
	 }
	 
	  public Arraylist getNiveau(){
		 return niveau;
	 }
	 
	 public void setNiveau(Arraylist niveau){
		 this.niveau = niveau;
	 }
	 
	  public Arraylist getAppart(){
		 return appart;
	 }
	 
	 public void setAppart(Arraylist appart){
		 this.appart = appart;
	 }
	
}

