/*
 * Point-projet-bat.java
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


public class Point {
	private int id;
	private double x;
	private double y;
	private int niveauId;
	 
	 public Point(int id,double x, double y,int nivid){
		 this.id =id;
		 this.x = x;
		 this.y = y;
		 this.niveauId = nivid;
	 }
	 
	 public int getId(){
		 return id;
	 }
	 
	 public void setId(int id){
		 this.id = id;
	 }
	 
	 public double getX(){
		 return x;
	 }
	 
	 public void setX(double x){
		 this.x = x;
	 }
	 
	 public double getY(){
		 return y;
	 }
	 
	 public void setY(double y){
		 this.y = y;
	 }
	 public int getNiveauId(){
		 return niveauId;
	 }
	 
	 public void setNiveauId(int niveauId){
		 this.niveauId = nivid;
	 }
}

