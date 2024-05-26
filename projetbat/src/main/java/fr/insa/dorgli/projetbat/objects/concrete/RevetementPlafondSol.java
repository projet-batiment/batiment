package fr.insa.dorgli.projetbat.objects.concrete;

import fr.insa.dorgli.projetbat.objects.types.TypeRevetement;

public class RevetementPlafondSol extends Revetement {
	public RevetementPlafondSol() { }
	public RevetementPlafondSol(int id, TypeRevetement typeRevetement, double pos1L, double pos1H, double pos2L, double pos2H) {
		super(id, typeRevetement, pos1L, pos1H, pos2L, pos2H);
	}

	@Override
	public boolean isReactive() {
		return (pos1L == 0 && pos2L == 0 && pos1H == 0 && pos2H == 0);
	}

	@Override
	public double aire() {
		if (super.getParents().getFirst() instanceof Piece piece) {
			// comme les pièces ne sont pas rectangulaires, soit le revêtement occupe tout, soit seulement un carré
			// il aurait fallu que le revetement de sol aie ses propres Points pour pouvoir tout calculer comme il faut
			// mais bon...
			if (isReactive())
				return piece.aire();
			else
				return (this.pos1L - this.pos2L) * (this.pos1H - this.pos2H);
		} else
			throw new IllegalCallerException("Should have a piece parent, but parents are : " + String.valueOf(super.getParents()));
	}

	@Override
	public double calculerPrix() {
		if (typeRevetement == null)
			return 0;

		return typeRevetement.getPrixUnitaire() * aire();
	}
}