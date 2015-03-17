
function MarkerTracker(poiData) {

	alert ("MarkerTracker " + poiData.name);

    this.poiData = poiData;
    this.isSelected = false;
	this.isSpeakerSelected = false;

    // Create the tracker to recognize the poi
    var trackerDataSetPath = "assets/cabeza-cibeles_2.wtc";

    this.tracker = new AR.Tracker(trackerDataSetPath, 
									{
										//onLoaded: IrAndGeo.loadingStepDone,
										//onError: IrAndGeo.errorLoading
									});

    // Create drawables to display on the recognized image
    this.buttonDeal = new AR.ImageDrawable(IrAndGeo.markerDrawable_speaker, 0.15, 
                                                                        {
                                                                            onClick: Marker.prototype.getOnClickPoiSpeaker(this),
                                                                            offsetX: 0.35,
                                                                            offsetY: -0.275
                                                                        });
    this.buttonWeb = new AR.ImageDrawable(IrAndGeo.markerDrawable_WebInternet, 0.15, 
                                                                        {
                                                                            onClick: Marker.prototype.getOnClickWebInternet(this),
                                                                            offsetX: 0.175,
                                                                            offsetY: -0.525
                                                                        });
    this.buttonStores = new AR.ImageDrawable(IrAndGeo.markerDrawable_FindInternet, 0.15, 
                                                                        {
                                                                            onClick: Marker.prototype.getOnClickFindInternet(this),
                                                                            offsetX: -0.1,
                                                                            offsetY: -0.275
                                                                        });

    var menuDrawables = [this.buttonDeal, this.buttonWeb, this.buttonStores];

    // Create the object by defining the tracker, target name and its drawables
    this.trackable2DObject = new AR.Trackable2DObject(this.tracker, "cabeza-cibeles_2", 
											{
												drawables: 
												{
													cam: [this.buttonDeal, this.buttonWeb, this.buttonStores]
												}
											});

    return this;
}
//MarkerTracker.prototype.getOnClickPoiSpeaker = function(markertracker) {
//	return function() 
//	{
//		alert("MarkerTracker getOnClickPoiSpeaker");
//		return true;
//    };
//};
//MarkerTracker.prototype.getOnClickWebInternet = function(markertracker) {
//	return function() 
//	{
//		alert("MarkerTracker getOnClickWebInternet");
//		return true;
//    };
//};
//MarkerTracker.prototype.getOnClickFindInternet = function(markertracker) {

//	return function() 
//	{
//		alert("MarkerTracker getOnClickFindInternet");
//		return true;
//    };
//};
