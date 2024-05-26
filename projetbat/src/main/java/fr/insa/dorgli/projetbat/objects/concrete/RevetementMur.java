package fr.insa.dorgli.projetbat.objects.concrete;

import fr.insa.dorgli.projetbat.objects.types.TypeRevetement;

public class RevetementMur extends Revetement {
	public RevetementMur() { }
	public RevetementMur(int id, TypeRevetement typeRevetement, double pos1L, double pos1H, double pos2L, double pos2H) {
		super(id, typeRevetement, pos1L, pos1H, pos2L, pos2H);
	}

	@Override
	public boolean isReactive() {
		return (pos1L == 0 && pos2L == 0 && pos1H == 0 && pos2H == 0);
	}

	@Override
	public double aire() {
		if (super.getParents().getFirst() instanceof Mur mur) {
			if (isReactive())
				return mur.getPointDebut().getPoint().distance(mur.getPointFin().getPoint()) * mur.getHauteur();
			else
				return (this.pos1L - this.pos2L) * (this.pos1H - this.pos2H);

//			double largeur;
//			double hauteur;
//
//			if ((pos1L == 0 ) && (pos2L == 0 )){
//				largeur = mur.getPointDebut().getPoint().distance(mur.getPointFin().getPoint());
//			} else {
//				largeur = this.pos1L - this.pos2L;
//			}
//			if ((pos1H == 0) && (pos2H == 0)) {
//				hauteur = mur.getHauteur();
//			} else {
//				hauteur = this.pos1H - this.pos2H;
//			}
		} else
			throw new IllegalCallerException("Should have a mur parent, but parents are : " + String.valueOf(super.getParents()));
	}

	@Override
	public double calculerPrix() {
		if (typeRevetement == null)
			return 0;

		return super.typeRevetement.getPrixUnitaire() * aire();
	}
}