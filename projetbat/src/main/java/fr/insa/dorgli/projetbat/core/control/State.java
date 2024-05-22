package fr.insa.dorgli.projetbat.core.control;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.BObject;
import fr.insa.dorgli.projetbat.objects.Devis;
import fr.insa.dorgli.projetbat.objects.SelectableId;
import fr.insa.dorgli.projetbat.objects.concrete.Batiment;
import fr.insa.dorgli.projetbat.objects.concrete.DrawableRoot;
import fr.insa.dorgli.projetbat.objects.concrete.Niveau;

import java.util.HashSet;

public class State {
	public enum ActionState {
		DEFAULT, // none
		MULTI_SELECT,

		CREATE,
	}

	private final Config config;

	private Batiment currentBatiment;
	public Niveau currentNiveau;
	private DrawableRoot viewRootElement;

	private final HashSet<SelectableId> selectedElements;
	private final HashSet<Devis> devisList;
	private ActionState actionState;

	private Creator creator;

	public State(Config config) {
		this.config = config;

		this.currentBatiment = null;
		this.currentNiveau = null;

		this.viewRootElement = null;
		this.selectedElements = new HashSet<>();
		this.devisList = new HashSet<>();
		this.actionState = ActionState.DEFAULT;

		this.creator = null;
	}

	public ActionState getActionState() {
		return actionState;
	}

	public void setActionState(ActionState newState) {
		config.tui.diveWhere("state/setActionState");
		config.tui.debug("new state " + newState);

		switch (newState) {
			case CREATE -> {
				config.tui.error("setAction CREATE shouldn't be called directly");
//				actionState = ActionState.CREATE;
//				config.tui.debug("create: initialised " + newWhich + " creation");
//				switch (newWhich) {
//					case BATIMENT
//						-> creator = new Creator(new Batiment());
//					case NIVEAU
//						-> creator = new Creator(new Niveau());
//					case APPART
//						-> creator = new Creator(new Appart());
//					case PIECE
//						-> creator = new Creator(new Piece());
//					case MUR
//						-> creator = new Creator(new Mur());
//					case PLAFONDSOL
//						-> creator = new Creator(new PlafondSol());
//
//					case REVETEMENT_MUR 
//						-> creator = new Creator(new RevetementMur());
//					case REVETEMENT_PLAFONDSOL 
//						-> creator = new Creator(new RevetementPlafondSol());
//					case OUVERTURE_MUR 
//						-> creator = new Creator(new OuvertureMur());
//					case OUVERTURE_NIVEAUX 
//						-> creator = new Creator(new OuvertureNiveaux());
//
//					case TYPE_BATIMENT 
//						-> creator = new Creator(new TypeBatiment());
//					case TYPE_APPART 
//						-> creator = new Creator(new TypeAppart());
//					case TYPE_MUR 
//						-> creator = new Creator(new TypeMur());
//					case TYPE_OUVERTURE_MUR 
//						-> creator = new Creator(new TypeOuvertureMur());
//					case TYPE_OUVERTURE_NIVEAUX 
//						-> creator = new Creator(new TypeOuvertureNiveau());
//					case TYPE_REVETEMENT 
//						-> creator = new Creator(new TypeRevetement());
//
//					default -> {
//						config.tui.error("create: not implemented: " + newWhich);
//						actionState = ActionState.DEFAULT;
//					}
//				}
			}

			case MULTI_SELECT, DEFAULT
				-> actionState = newState;

			default -> {
				config.tui.error("unknown state: " + newState + ": fallback to DEFAULT");
				actionState = ActionState.DEFAULT;
			}
		}

		config.tui.popWhere();
	}

	public HashSet<SelectableId> getSelectedElements() {
		return selectedElements;
	}
	public boolean isSelectedElement(SelectableId elm) {
		return selectedElements.contains(elm);
	}
	public void toggleSelectedElement(SelectableId elm) {
		if (selectedElements.contains(elm))
			selectedElements.remove(elm);
		else
			selectedElements.add(elm);

		config.getMainWindow().getSidePaneContainer().updateSelection();
	}
	public void removeSelectedElement(SelectableId elm) {
		selectedElements.remove(elm);
		config.getMainWindow().getSidePaneContainer().updateSelection();
	}
	public void setSelectedElement(SelectableId elm) {
		clearSelectedElement();
		selectedElements.add(elm);
		config.getMainWindow().getSidePaneContainer().updateSelection();
	}
	public void clearSelectedElement() {
		selectedElements.clear();
		config.getMainWindow().getSidePaneContainer().updateSelection();
	}

	public void addDevis(Devis devis) {
		devisList.add(devis);
		config.getMainWindow().getSidePaneContainer().addDevis(devis);
	}
	public void removeDevis(Devis devis) {
		devisList.remove(devis);
		config.getMainWindow().getSidePaneContainer().removeDevis(devis);
	}

	public DrawableRoot getViewRootElement() {
		return viewRootElement;
	}

	public void setViewRootElement(DrawableRoot viewRootElement) {
		this.viewRootElement = viewRootElement;
		config.getMainWindow().getCanvasContainer().redraw();
	}

	public Batiment getCurrentBatiment() {
		return currentBatiment;
	}

	public void setCurrentBatiment(Batiment currentBatiment) {
		this.currentBatiment = currentBatiment;
	}


	public <T extends BObject> Creator create(T object) {
		actionState = ActionState.CREATE;
		creator = new Creator(object);
		return creator;
	}
	public Creator getCreator() {
		return creator;
	}

	public void endCreation() {
		this.creator = null;
		setActionState(ActionState.DEFAULT);
		config.tui.debug("state/endCreation: cleared creator, set actionState to DEFAULT");
	}
}
