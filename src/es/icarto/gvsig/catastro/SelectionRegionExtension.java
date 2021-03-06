package es.icarto.gvsig.catastro;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.tools.Behavior.PointBehavior;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.icarto.gvsig.catastro.constants.ConstantManager;
import es.icarto.gvsig.catastro.constants.ConstantsSelectionListener;
import es.icarto.gvsig.catastro.utils.Preferences;
import es.icarto.gvsig.catastro.utils.TOCLayerManager;

public class SelectionRegionExtension extends Extension {

    TOCLayerManager tocLayerManager;

    @Override
    public void initialize() {
	// do nothing
    }

    @Override
    public void execute(String actionCommand) {
	tocLayerManager = new TOCLayerManager();
	tocLayerManager
		.setActiveAndVisibleLayer(Preferences.REGIONES_LAYER_NAME);
	View view = (View) PluginServices.getMDIManager().getActiveWindow();
	MapControl mc = view.getMapControl();
	if (!mc.getNamesMapTools().containsKey("constantsSelectionRegion")) {
	    ConstantsSelectionListener csl = new ConstantsSelectionListener(mc);
	    mc.addMapTool("constantsSelectionRegion", new PointBehavior(csl));
	}
	mc.setTool("constantsSelectionRegion");
    }

    @Override
    public boolean isEnabled() {
	return true;
    }

    @Override
    public boolean isVisible() {
	ConstantManager constantManager = new ConstantManager();
	if (constantManager.getConstants() == null) {
	    PluginServices.getMainFrame().getStatusBar()
		    .setMessage("constants", "R:- M:- P:-");
	}
	return true;
    }

}
