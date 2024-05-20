package fr.insa.dorgli.projetbat.objects;

public abstract class BObject extends SelectableId {
	// est ce que l'objet en construction est prêt?
	public abstract boolean ready();

	public BObject(int id) {
		super(id);
	}
	public BObject() { }
}