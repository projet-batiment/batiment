/*
 * TypeRevetement-Projet-Bat.java
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


		public class TypeRevetement {
			private String nomrev;
			private double prixuni;
	
		public TypeRevetement(String name){
			int ligne;
			for(int i=0;i<19;i++){
				TAB[i][2]=int n // nom de tableau à modifier selon le nom du tableau des différents objets
				if (n.equals (name)) {
					ligne=i;
				}
			}
		}
		this.nomrev=TAB[n][2];
		this.prixuni=TAB[n][7];

		public String getNomrev(){
			return nomrev;
			}
	 
		public void setNomrev(String nomrev){
			this.nomrev = nomrev;
			}
		public double getPrixuni(){
			return prixuni;
	 }	
	 
		public void setPrixuni(double prixuni){
			this.prixuni = prixuni;
	 }
}

