package es.icarto.gvsig.catastro.evaluator.rules;

import java.util.ArrayList;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.drivers.IFeatureIterator;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.vividsolutions.jts.geom.Geometry;

import es.icarto.gvsig.catastro.constants.ConstantManager;
import es.icarto.gvsig.catastro.utils.Preferences;
import es.icarto.gvsig.catastro.utils.TOCLayerManager;

public class CheckPredioIsWithinOneManzana implements IRule {

    ArrayList<IGeometry> geoms;

    public CheckPredioIsWithinOneManzana(ArrayList<IGeometry> geoms) {
	this.geoms = geoms;
    }

    @Override
    public boolean isObey() {
	Geometry manzanaJTSGeom = getManzanaGeom();
	for (IGeometry geom : geoms) {
	    Geometry predioJTSGeom = geom.toJTSGeometry();
	    Geometry manzanaJTSGeomWithBuffer = manzanaJTSGeom.buffer(0.5);
	    if (!predioJTSGeom.coveredBy(manzanaJTSGeomWithBuffer)) {
		return false;
	    }
	}
	return true;
    }

    private Geometry getManzanaGeom() {
	TOCLayerManager tocLayerManager = new TOCLayerManager();
	FLyrVect manzana = tocLayerManager
		.getLayerByName(Preferences.MANZANAS_LAYER_NAME);
	IGeometry manzanaGeom = getGeomFromFLyrVect(manzana);
	Geometry manzanaJTSGeom = manzanaGeom.toJTSGeometry();
	return manzanaJTSGeom;
    }

    private IGeometry getGeomFromFLyrVect(FLyrVect layer) {
	ConstantManager constantManager = new ConstantManager();
	try {
	    String sqlQuery = "select * from '" + layer.getRecordset().getName() + "'" +
		    " where " + Preferences.PAIS_NAME_IN_DB + " = " + constantManager.getConstants().getPais() + " "+
		    " and " + Preferences.ESTADO_NAME_IN_DB + " = " + constantManager.getConstants().getEstado() + " "+
		    " and " + Preferences.MUNICIPIO_NAME_IN_DB + " = " + constantManager.getConstants().getMunicipio() + " "+
		    " and " + Preferences.LIMITE_NAME_IN_DB + " = " + constantManager.getConstants().getLimiteMunicipal() + " "+
		    " and " + Preferences.MANZANA_NAME_IN_DB + " = " + constantManager.getConstants().getManzana() + " "+
		    " and " + Preferences.REGION_NAME_IN_DB + " = " + constantManager.getConstants().getRegion() +";";
	    IFeatureIterator featureIterator = layer.getSource().getFeatureIterator(sqlQuery, null);
	    return featureIterator.next().getGeometry();
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    @Override
    public String getMessage() {
	if (geoms.size() == 1) {
	    return PluginServices.getText(this,
		    "rule_merged_predio_is_not_within_selected_manzana");
	} else {
	    return PluginServices.getText(this,
		    "rule_predio_is_not_within_manzana");
	}
    }

}
