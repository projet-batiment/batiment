package fr.insa.dorgli.projetbat.objects;

// Note pour plus tard :

import fr.insa.dorgli.projetbat.Deserialize;

//   - Est ce que l'utilisateur peut créer différents types d'apparts lors de l'exécution ?
//	 Dans ce cas, il faudrait stocker le type sous une autre forme qu'un enum
//public enum  TypeAppart{
//	COMMUNS,
//	PRIVE,
//}
public class TypeAppart {
	String name;
	String description;

	public TypeAppart(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "TypeAppart { name: '" + name + "', description: '" + description + "' }";
	}
	public String toStringShort() {
		return "( #TODO )";
	}

	public String serialize(Objects objects) {
		int id = objects.getIdOfTypeAppart(this);
		return String.join(",",
		    String.valueOf(id),
		    Deserialize.escapeString(name),
		    Deserialize.escapeString(description)
		);
	}
}
