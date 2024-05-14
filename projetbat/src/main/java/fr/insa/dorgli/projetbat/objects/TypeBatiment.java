package fr.insa.dorgli.projetbat.objects;

// Notes pour plus tard :
//   - Est ce que l'utilisateur peut créer différents types de bâtiments lors de l'exécution ?
//	 Dans ce cas, il faudrait stocker le type sous une autre forme qu'un enum
//   - Est ce que le type de bâtiment est vraiment important ?
import fr.insa.dorgli.projetbat.utils.StructuredToString;

// TODO!!!!!! ajouter les types typeBatiment et typeAppart à deserialize et à Objects
public class TypeBatiment extends BObject {
	String nom;
	String description;

	public TypeBatiment(int id, String nom, String description) {
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
		return new StructuredToString.OfBObject(depth, this, indentFirst)
		    .textField("nom", nom)
		    .textField("description", description)
        	    .getValue();
	}
}
