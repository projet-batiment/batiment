package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.core.Project;
import fr.insa.dorgli.projetbat.objects.Devis;
import fr.insa.dorgli.projetbat.objects.SelectableId;
import fr.insa.dorgli.projetbat.objects.concrete.*;
import fr.insa.dorgli.projetbat.objects.types.*;
import java.util.HashMap;
import java.util.HashSet;
import javafx.scene.control.TabPane;

public class SidePane extends TabPane {
	Config config;

	private SmartTab selectionTab;
	private final HashMap<Devis, DevisEditor> devisEditors;

	private void addTab(SmartTab tab) {
		super.getTabs().add(0, tab);
	}

	private void setSelectionTab(SmartTab tab) {
		super.getTabs().remove(selectionTab);
		selectionTab = tab;
		addTab(tab);
	}

	public final void addDevis(Devis devis) {
		DevisEditor linkedEditor = new DevisEditor(config, devis);
		devisEditors.put(devis, linkedEditor);
		addTab(linkedEditor);
	}
	public final void removeDevis(Devis devis) {
		DevisEditor linkedEditor = devisEditors.get(devis);
		devisEditors.remove(devis);
		super.getTabs().remove(linkedEditor);
	}

	public final void updateSelection() {
		HashSet<SelectableId> selectedObjects = config.controller.state.getSelectedElements();

		if (config.project.objects.getAll().isEmpty())
			setSelectionTab(new EmptyProject(config));

		else if (selectedObjects.isEmpty()) {
			setSelectionTab(new Empty(config));

		} else if (selectedObjects.size() > 1) {
			setSelectionTab(new MultiSelectTab(config, selectedObjects));

		} else {
			switch (selectedObjects.iterator().next()) {
				case Batiment batiment -> {
					setSelectionTab(new BatimentEditor(config, batiment));
				}
				case Niveau niveau -> {
					setSelectionTab(new NiveauEditor(config, niveau));
				}
				case Appart appart -> {
					setSelectionTab(new AppartEditor(config, appart));
				}
				case Piece piece -> {
					setSelectionTab(new PieceEditor(config, piece));
				}
				case Mur mur -> {
					setSelectionTab(new MurEditor(config, mur));
				}
				case Point point -> {
					setSelectionTab(new PointEditor(config, point));
				}

				case TypeAppart typeAppart -> {
					setSelectionTab(new TypeAppartEditor(config, typeAppart));
				}
				case TypeBatiment typeBatiment -> {
					setSelectionTab(new TypeBatimentEditor(config, typeBatiment));
				}
				case TypeMur typeMur -> {
					setSelectionTab(new TypeMurEditor(config, typeMur));
				}
				case TypeOuvertureMur typeOuvertureMur -> {
					setSelectionTab(new TypeOuvertureMurEditor(config, typeOuvertureMur));
				}
				case TypeOuvertureNiveaux typeOuvertureNiveau -> {
					setSelectionTab(new TypeOuvertureNiveauEditor(config, typeOuvertureNiveau));
				}
				case TypeRevetement typeOuvertureNiveau -> {
					setSelectionTab(new TypeRevetementEditor(config, typeOuvertureNiveau));
				}

				case Project project -> {
					setSelectionTab(new ProjectEditor(config, project));
				}

				case Devis devis -> {
					addDevis(devis);
				}

				case null -> {
					config.tui.warn("sidePane: singleObject: (null)...");
					setSelectionTab(new Empty(config));
				}

				default -> {
					config.tui.warn("sidePane: singleObject: something else...");
					setSelectionTab(new Empty(config));
				}
			}
		}
	}

	public SidePane(Config config) {
		this.config = config;

		this.devisEditors = new HashMap<>();
		super.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		final double width = 250;
		super.setMinWidth(width);

		updateSelection();
	}
}