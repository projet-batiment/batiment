package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.objects.BObject;
import fr.insa.dorgli.projetbat.objects.Mur;
import fr.insa.dorgli.projetbat.objects.Piece;
import javafx.scene.layout.Pane;

public class SidePaneContainer extends Pane {
	private final Config config;

	public SidePaneContainer(Config config) {
		this.config = config;
		this.setMaxWidth(200);

		useObject(null);
	}

	public final void useObject(BObject object) {
		SidePane newPane;

		switch (object) {
			case Piece piece -> {
				config.tui.debug("useObject: is a Piece");
				newPane = new PiecePane(config, piece);
			}
			case Mur mur -> {
				config.tui.debug("useObject: is a mur");
				newPane = new MurPane(config, mur);
			}
			case null, default -> {
				config.tui.debug("useObject: sth else...");
				newPane = new Empty();
			}
		}

		newPane.setMaxWidth(this.getMaxWidth());
		this.getChildren().setAll(newPane);
	}
}
