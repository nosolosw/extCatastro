package es.icarto.gvsig.catastro.actions;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.catastro.utils.CopyFeaturesUtils;
import es.icarto.gvsig.catastro.utils.Preferences;
import es.icarto.gvsig.catastro.utils.TOCLayerManager;
import es.icarto.gvsig.catastro.utils.ToggleEditing;

public class CreatePredioWhenAddNewManzana {

    private static FLyrVect sourceLayer;
    private TOCLayerManager tocLayerManager;

    public CreatePredioWhenAddNewManzana(FLyrVect layer) {
	tocLayerManager = new TOCLayerManager();
	this.sourceLayer = layer;
    }

    public void execute(){
	try {
	    CopyFeaturesUtils.copyFeatures(sourceLayer);
	    ToggleEditing te = new ToggleEditing();
	    te.stopEditing(sourceLayer, false);
	    getDestinationLayer().setActive(true);
	    te.startEditing(getDestinationLayer());
	    CopyFeaturesUtils.pasteFeatures((FLyrVect) getDestinationLayer());
	    te.stopEditing(getDestinationLayer(), false);
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private FLayer getDestinationLayer() {
	return tocLayerManager.getLayerByName(Preferences.PREDIOS_LAYER_NAME);
    }
}
