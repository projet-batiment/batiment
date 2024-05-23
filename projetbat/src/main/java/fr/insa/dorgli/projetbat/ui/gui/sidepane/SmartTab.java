package fr.insa.dorgli.projetbat.ui.gui.sidepane;

import fr.insa.dorgli.projetbat.core.Config;
import fr.insa.dorgli.projetbat.utils.VoidConsumer;
import java.util.ArrayList;
import java.util.function.Consumer;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public abstract class SmartTab extends Tab {
	private final Config config;
	private final VBox ourVBox;

	// update handler arraylists
	private final ArrayList<EventHandler<ActionEvent>> saveFunctions;
	private final ArrayList<VoidConsumer> resetFunctions;
	private final ArrayList<Consumer<Pane>> initFunctions;
	private boolean initialized = false;

	// update handler callers
	public final void save(ActionEvent event) {
		saveFunctions.stream().forEach((EventHandler<ActionEvent> each) -> each.handle(event));

		// always trigger redraw after object has been saved
		config.getMainWindow().getCanvasContainer().redraw();
	};
	public final void reset() {
		resetFunctions.stream().forEach((VoidConsumer each) -> each.function());
	};
	public final void initialize() {
		if (initialized)
			return;

		initialized = true;
		initFunctions.stream().forEach((Consumer<Pane> each) -> each.accept((Pane) ourVBox));
		super.setContent(ourVBox);

		reset();
	};

	// update handler adders
	public final void prependSaveFunction(EventHandler<ActionEvent> saveFunction) {
		this.saveFunctions.add(0, saveFunction);
	};
	public final void prependResetFunction(VoidConsumer resetFunction) {
		this.resetFunctions.add(0, resetFunction);
	};
	public final void prependInitFunction(Consumer<Pane> initFunction) {
		this.initFunctions.add(0, initFunction);
	};

	public final void resize(double width) {
		ourVBox.setPrefWidth(width);
	}

	public SmartTab(Config config, String tabName) {
		super(tabName);
		this.config = config;

		saveFunctions = new ArrayList<>();
		resetFunctions = new ArrayList<>();
		initFunctions = new ArrayList<>();

		ourVBox = new VBox();
	}
}