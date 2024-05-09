package fr.insa.dorgli.projetbat.objects;

import fr.insa.dorgli.projetbat.StructuredToString;

// Note pour plus tard :
//   - Est ce que l'utilisateur peut créer différents types d'apparts lors de l'exécution ?
//	 Dans ce cas, il faudrait stocker le type sous une autre forme qu'un enum
//public enum  TypeAppart{
//	COMMUNS,
//	PRIVE,
//}
public class TypeAppart extends BObject {
	String nom;
	String description;

	public TypeAppart(int id, String nom, String description) {
		super(id);
		this.nom = nom;
		this.description = description;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString(int depth, boolean indentFirst) {
		return new StructuredToString.OfBObject(depth, getClass().getSimpleName(), indentFirst)
		    .field("nom", nom)
		    .field("description", description)
            .getValue();
	}
}
