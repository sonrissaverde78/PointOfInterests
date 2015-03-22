/*
    The Wikitude SDK allows you to combine geobased AR with image recognition to create a seamless experience for users.
*/
IrAndGeo = {};

IrAndGeo.markerNames = ['Union Circle', 'Eastminster', 'Small Ben', 'Silver Gate', 'Uptown', 'Downtown', 'Countryside', 'Outer Circle'];

IrAndGeo.stores = [];
IrAndGeo.markerAnimations = [];
IrAndGeo.error = false;
IrAndGeo.receivedLocation = false;
var imageDrawable = [];


IrAndGeo.res = {};
var dbPoi = [];

IrAndGeo.markerList = [];
IrAndGeo.currentMarker = null;

IrAndGeo.buttonsImagesPathAG;
IrAndGeo.ImagesToTrackPathAG;
IrAndGeo.ImagesToDrawPathAG;

IrAndGeo.TotalLoadedPois = 0;
IrAndGeo.bTracerAlert = false;
IrAndGeo.showPois = false;

IrAndGeo.GetExecuteOperationFromJSon = function(InputInfo)
{
    IrAndGeo.TracerAlert("GetExecuteOperationFromJSon InputInfo.length = " + InputInfo.length);
    /*
    IrAndGeo.TracerAlert("GetExecuteOperationFromJSon InputInfo.length = " + InputInfo.length);
    IrAndGeo.TracerAlert("InputInfo[0].TotalPois " + InputInfo[0].Operation);
    Operation = InputInfo[0].Operation;
    IrAndGeo.TracerAlert("InputInfo[1].envio " + InputInfo[1].envio);
    envio = InputInfo[1].envio;
    IrAndGeo.TracerAlert("envio " + envio);
    */
    i = 0;
    bEnd = false;
    do
    {
        Operation = parseInt(InputInfo[i].Operation);
        IrAndGeo.TracerAlert("Operation: " + Operation);
        switch(Operation) 
        {
            // Buttons Images path
            case 0:
                IrAndGeo.buttonsImagesPathAG    = InputInfo[i].buttonsImagesPathAG;
                IrAndGeo.ImagesToTrackPathAG    = InputInfo[i].ImagesToTrackPathAG;
                IrAndGeo.ImagesToDrawPathAG     = InputInfo[i].ImagesToDrawPathAG;
                IrAndGeo.TracerAlert("IrAndGeo.buttonsImagesPathAG: " + IrAndGeo.buttonsImagesPathAG);
                IrAndGeo.TracerAlert("IrAndGeo.ImagesToTrackPathAG: " + IrAndGeo.ImagesToTrackPathAG);
                IrAndGeo.TracerAlert("1");
                IrAndGeo.TracerAlert("IrAndGeo.ImagesToDrawPathAG: " + IrAndGeo.ImagesToDrawPathAG);
                break;
            // Buttons Images path
            case 1:
                IrAndGeo.buttonsImagesPathAG    = InputInfo[i].buttonsImagesPathAG;
                IrAndGeo.ImagesToTrackPathAG    = InputInfo[i].ImagesToTrackPathAG;
                IrAndGeo.ImagesToDrawPathAG     = InputInfo[i].ImagesToDrawPathAG;
                break;
           case 2:
                
                break;
            default:
                IrAndGeo.TracerAlert("Operation: Err" + Operation);
                break;
        }
        if (parseInt(InputInfo[i].End) == 0)
        {
            IrAndGeo.TracerAlert("End Operations:");
            bEnd = true;
        }
        i++;
    }while (bEnd == false);

}
IrAndGeo.loadPoisFromJSon = function(poiData)
{
	IrAndGeo.TotalLoadedPois = poiData.length;
    alert("loadPoisFromJSon poiData.length " + IrAndGeo.TotalLoadedPois);
    var singlePoi; 
    // loop through POI-information and create an AR.GeoObject (=Marker) per POI
    for (var currentPlaceNr = 0; currentPlaceNr < poiData.length; currentPlaceNr++) {
        singlePoi = {
            "id": poiData[currentPlaceNr].id,
            "name": poiData[currentPlaceNr].name,
            "latitude": parseFloat(poiData[currentPlaceNr].lat),
            "longitude": parseFloat(poiData[currentPlaceNr].long),
            "altitude": parseFloat(poiData[currentPlaceNr].alt),
            "City": poiData[currentPlaceNr].City,
            "ImagesToTrack": poiData[currentPlaceNr].ImagesToTrack,
            //"ImagesToDraw": poiData[currentPlaceNr].ImagesToDraw,
            "ImagesButtons": poiData[currentPlaceNr].ImagesButtons,
            "MainImage": poiData[currentPlaceNr].MainImage,
        };
        dbPoi[currentPlaceNr] = singlePoi;
		IrAndGeo.TracerAlert("poiData[currentPlaceNr].MainImage x" + "assets/ImagesToDraw/" + dbPoi[currentPlaceNr].MainImage);
		//dbPoi[currentPlaceNr].markerDrawable_MainImage = new AR.ImageResource("assets/ImagesToDraw/" + dbPoi[currentPlaceNr].MainImage, {
		//onError: IrAndGeo.errorLoading
		//});

        alert("dbPoi[currentPlaceNr].name          "    + " " + dbPoi[currentPlaceNr].name);
		//IrAndGeo.initImages(dbPoi[currentPlaceNr]);
        //World.markerList.push(new Marker(singlePoi));
    }
	
}

IrAndGeo.setupScene = function(lat, lon, alt) {
    // create 8 random markers with different marker names
    IrAndGeo.TracerAlert("setupScene()");
    var ppoi;

    for (var i = 0; i < IrAndGeo.TotalLoadedPois; i++) 
    {
        var objLat = lat + ((Math.random() - 0.5) / 1000);
        var objLon = lon + ((Math.random() - 0.5) / 1000);
        //IrAndGeo.TracerAlert("setupScene() -> Store Created");
        // IrAndGeo.createMarker(objLat, objLon, IrAndGeo.markerNames[i], i);
		
		IrAndGeo.TracerAlert("dbPoi[i].name " 		+ dbPoi[i].name);
		IrAndGeo.TracerAlert("dbPoi[i].latitude<" 	+ dbPoi[i].latitude + ">");   
		IrAndGeo.TracerAlert("dbPoi[i].longitude<" + dbPoi[i].longitude + ">");       


		IrAndGeo.TracerAlert("otra 2 vez dbPoi[i].name " + dbPoi[i].name);
		//dbPoi[i].latitude = objLat;// parseFloat(objLat);
        //dbPoi[i].longitude = objLon;// parseFloat(objLon);
        IrAndGeo.TracerAlert("dbPoi[i].latitude<" 	+ objLat + ">");   
		IrAndGeo.TracerAlert("dbPoi[i].longitude<" + objLon + ">");       
		

		IrAndGeo.markerList.push(new Marker(dbPoi[i], false));
		IrAndGeo.markerList.push(new Marker(dbPoi[i], true));
		
    }

    // create appearing animation
    IrAndGeo.showMarkersAnimation = new AR.AnimationGroup('parallel', IrAndGeo.markerAnimations);
	// IrAndGeo.TracerAlert("Before showStores()");
    IrAndGeo.showStores();
};
IrAndGeo.sendIdFromPoi = function (id)
{
	var architectSdkUrl = "architectsdk://markerselected?id=" + encodeURIComponent(id) + "&title=" + encodeURIComponent(name) + "&description=" + encodeURIComponent(name);
	document.location 	= architectSdkUrl;
}
IrAndGeo.stopReadPoi = function (id)
{
	var architectSdkUrl = "architectsdk://markerselected?stopAudio=" + encodeURIComponent(id) + "&title=" + encodeURIComponent(name) + "&description=" + encodeURIComponent(name);
	document.location 	= architectSdkUrl;
}
IrAndGeo.startReadPoi = function (id)
{
	var architectSdkUrl = "architectsdk://markerselected?startAudio=" + encodeURIComponent(id) + "&title=" + encodeURIComponent(name) + "&description=" + encodeURIComponent(name);
	document.location 	= architectSdkUrl;
}
IrAndGeo.FindInternet = function (id)
{
	var architectSdkUrl = "architectsdk://markerselected?FindInternet=" + encodeURIComponent(id) + "&title=" + encodeURIComponent(name) + "&description=" + encodeURIComponent(name);
	document.location 	= architectSdkUrl;
}
IrAndGeo.GoToWiKi = function (id)
{
	var architectSdkUrl = "architectsdk://markerselected?GoToWiki=" + encodeURIComponent(id) + "&title=" + encodeURIComponent(name) + "&description=" + encodeURIComponent(name);
	document.location 	= architectSdkUrl;
}
IrAndGeo.MakeQuestion = function (id)
{
	var architectSdkUrl = "architectsdk://markerselected?MakeQuestion=" + encodeURIComponent(id) + "&title=" + encodeURIComponent(name) + "&description=" + encodeURIComponent(name);
	document.location 	= architectSdkUrl;
}
IrAndGeo.createMarker = function(lat, lon, name, id)
{
//IrAndGeo.TracerAlert("createMarker()");
    // create an AR.GeoLocation from the latitude/longitude function parameters
    var loc = new AR.GeoLocation(lat, lon);

    // create an AR.ImageDrawable for the marker
    imageDrawable[id] = new AR.ImageDrawable(IrAndGeo.res.marker, 2, {
        scale: 0.0,
        onClick: function() {
            // IrAndGeo.TracerAlert(name);
		// var architectSdkUrl = "architectsdk://markerselected?id=" + encodeURIComponent(currentMarker.poiData.id) + "&title=" + encodeURIComponent(currentMarker.poiData.title) + "&description=" + encodeURIComponent(currentMarker.poiData.description);
		var architectSdkUrl = "architectsdk://markerselected?id=" + encodeURIComponent(id) + "&title=" + encodeURIComponent(name) + "&description=" + encodeURIComponent(name);
		/*
			The urlListener of the native project intercepts this call and parses the arguments. 
			This is the only way to pass information from JavaSCript to your native code. 
			Ensure to properly encode and decode arguments.
			Note: you must use 'document.location = "architectsdk://...' to pass information from JavaScript to native. 
			! This will cause an HTTP error if you didn't register a urlListener in native architectView !
		*/
		document.location = architectSdkUrl;
        }
    });

    // create marker animations and store it in the markerAnimations-array 
    IrAndGeo.markerAnimations.push(new AR.PropertyAnimation(imageDrawable[id], 'scale', 0.0, 1.0, 1000, {
        type: AR.CONST.EASING_CURVE_TYPE.EASE_OUT_BOUNCE
    }));
    // create a AR.GeoObject with the marker, disable it by setting the enabled-flag to 'false' and store it in the stores-array
    IrAndGeo.stores.push(new AR.GeoObject(loc, {
        drawables: {
            cam: imageDrawable[id]
        },
        enabled: false
    }));
};
IrAndGeo.onWebInternetSelected = function (marker)
{ 
	if (marker == IrAndGeo.currentMarker) 
	{
		IrAndGeo.GoToWiKi(IrAndGeo.currentMarker.poiData.id);	
	}
};
IrAndGeo.onFindInternetSelected = function (marker)
{ 
	if (marker == IrAndGeo.currentMarker) 
	{
		IrAndGeo.FindInternet(IrAndGeo.currentMarker.poiData.id);
	}
};
IrAndGeo.onMicrophneSelected = function (marker)
{ 
	if (marker == IrAndGeo.currentMarker) 
	{
		IrAndGeo.MakeQuestion(IrAndGeo.currentMarker.poiData.id);
	}
};
IrAndGeo.onSpeakerSelected = function (marker)
{
	if (marker == IrAndGeo.currentMarker) 
	{
		
		if (IrAndGeo.currentMarker.isSpeakerSelected == false)
		{
			// alert ("onSpeackerSelected activated");
			IrAndGeo.currentMarker.isSpeakerSelected = true;
			IrAndGeo.startReadPoi(marker.poiData.id);
			IrAndGeo.isSpeakerSelected = true;
		}
		else
		{
			// alert ("onSpeackerSelected deactivated");
			IrAndGeo.stopReadPoi(marker.poiData.id);
			IrAndGeo.currentMarker.isSpeakerSelected = false;
			IrAndGeo.isSpeakerSelected = false;
		}
	}

	else
	{
		alert ("onSpeackerSelected Fails");
	}
};
// fired when user pressed maker in cam
IrAndGeo.onMarkerSelected = function (marker) 
{
    if (IrAndGeo.currentMarker) 
	{
        if (IrAndGeo.currentMarker.poiData.id == marker.poiData.id) 
		{
			// deselect marker
			IrAndGeo.stopReadPoi(marker.poiData.id);
            IrAndGeo.currentMarker.setDeselected(IrAndGeo.currentMarker);
			IrAndGeo.currentMarker = null;
			return;
        }
		else
		{
			alert ("onMarkerSelected: Unselect before...");
			return;
		}
    }
	else
	{
		// highlight current one
		marker.setSelected(marker);
		IrAndGeo.currentMarker = marker;
		IrAndGeo.sendIdFromPoi(marker.poiData.id);
		return;
	}
};
    // screen was clicked but no geo-object was hit
AR.context.onScreenClick = function (marker) 
{
	// alert ("onScreenClick");
	if (IrAndGeo.currentMarker) 
	{
		// alert ("AR.context.onScreenClick() setDeselected");
		IrAndGeo.currentMarker.setDeselected(IrAndGeo.currentMarker);
		IrAndGeo.currentMarker.isSpeakerSelected = false;
		IrAndGeo.stopReadPoi(IrAndGeo.currentMarker.poiData.id);
		IrAndGeo.currentMarker=null;
	}
};
var StoresOn = 0;
IrAndGeo.showStores = function() 
{
    if (StoresOn == 0)
    {
        // display all GeoObjects by setting the enabled-flag to 'true'
        IrAndGeo.stores.forEach(function(x, idx) {
            x.enabled = true;
        });
        IrAndGeo.TracerAlert("IrAndGeo.showStores On");
        // nicely animate appearing of markers
        IrAndGeo.showMarkersAnimation.start();

        // set the info-text for the HTML message element and show it
        // document.getElementById("messageElement").innerHTML = "Look around for stores nearby!";
        // document.getElementById("messageElement").style.display = "block";
        StoresOn = 0;
    }
    else
    {
        IrAndGeo.TracerAlert ("Stores are already On");
    }
};

IrAndGeo.hideStores = function() {
    // disable all GeoObjects and reset to 0 size
    IrAndGeo.stores.forEach(function(obj, idx) {
        obj.enabled = false;
        obj.drawables.cam[0].scale = 0.0;
    });
    // hide the HTML message element
    //document.getElementById("messageElement").style.display = "none";
};

IrAndGeo.onClickImageReconizedSpeacker = function(id) {
	
	return function() 
	{
		// alert (id + "el showDeal");
		try 
		{
			if (IrAndGeo.isSpeakerSelected == false)
			{
				IrAndGeo.isSpeakerSelected = true;
				IrAndGeo.startReadPoi(id);
			}
			else
			{
				IrAndGeo.isSpeakerSelected = false;
				IrAndGeo.stopReadPoi(id);
			}
		} 
		catch (err) 
		{
			alert("Market.prototype.getOnClickTringer Error" + err);
		}
		return true;
    };
};

IrAndGeo.onClickImageReconizedGoToWiKi = function(id) {
	return function() 
	{
		// alert (id + "el GoToWiKi");
		IrAndGeo.GoToWiKi(id);
		return true;
    };
};

IrAndGeo.onClickImageReconizedFindInternet = function(id) {
	return function() 
	{
		// alert (id + "el FindInternet");
		IrAndGeo.FindInternet(id);
		return true;
    };
};
IrAndGeo.onClickImageReconizedMicrophone = function(id) {
	return function() 
	{
		// alert (id + "el FindInternet");
		IrAndGeo.MakeQuestion(id);
		return true;
    };
};
IrAndGeo.loadingStepDone = function() 
{
    IrAndGeo.TracerAlert("loadingStepDone()");
    /*
    if (!IrAndGeo.error && IrAndGeo.res.buttonStores.isLoaded() && IrAndGeo.res.buttonWeb.isLoaded() && IrAndGeo.res.buttonDeal.isLoaded() && IrAndGeo.res.marker.isLoaded() && IrAndGeo.receivedLocation && IrAndGeo.tracker && IrAndGeo.tracker.isLoaded()) 
    {
        IrAndGeo.TracerAlert("inside if tocho");
        //everything is loaded
        var cssDivLeft = " style='display: table-cell;vertical-align: middle; text-align: right; width: 50%; padding-right: 15px;'";
        var cssDivRight = " style='display: table-cell;vertical-align: middle; text-align: left;'";
        document.getElementById('messageElement').innerHTML =
            "<div" + cssDivLeft + ">Scan Shop Image Target:</div>" +
            "<div" + cssDivRight + "><img src='assets/ShopAdSmall.png'></img></div>";

        // Remove Scan target message after 10 sec.
        setTimeout(function() {
            document.getElementById("messageElement").style.display = "none";
        }, 10000);
    }
    */
};

// function for displaying a loading error in the HTML message element
IrAndGeo.errorLoading = function() {
    //document.getElementById("messageElement").innerHTML = "Unable to load image or tracker!";
    IrAndGeo.error = true;
};

IrAndGeo.initImages = function(poiData2) 
{
    //alert("initImages()" + poiData2.ImagesToTrack);
    // Create the tracker to recognize the shop ad
    // Create drawables to display on the recognized image
    //var buttonDeal = new AR.ImageDrawable(IrAndGeo.markerDrawable_speaker, 0.15, 
	//{
	//	onClick: IrAndGeo.onClickImageReconizedSpeacker (poiData2.id),
	//	offsetX: 0.0,
	//	offsetY: 0.0
	//});
	// 

    //var buttonWeb = new AR.ImageDrawable(IrAndGeo.markerDrawable_WebInternet, 0.15, 
	//{
	//	onClick: IrAndGeo.onClickImageReconizedGoToWiKi(poiData2.id),
	//	offsetX: 0.15,
	//	offsetY: 0.0
	//});
    //var buttonStores = new AR.ImageDrawable(IrAndGeo.markerDrawable_FindInternet, 0.15, 
	//{
	//	onClick: IrAndGeo.onClickImageReconizedFindInternet(poiData2.id),
	//	offsetX: 0.30,
	//	offsetY: 0.0
	//});
	
	//var buttonMicrophone = new AR.ImageDrawable(IrAndGeo.markerDrawable_microphone, 0.15, 
	//{
	//	onClick: IrAndGeo.onClickImageReconizedMicrophone(poiData2.id),
	//	offsetX: 0.45,
	//	offsetY: 0.0
	//});
	
    //IrAndGeo.menuDrawables = [buttonDeal, buttonWeb, buttonStores, buttonMicrophone];


    //// Create the object by defining the tracker, target name and its drawables
    //var trackable2DObject = new AR.Trackable2DObject(IrAndGeo.tracker, poiData2.ImagesToTrack, 
	//{
	//	drawables: 
	//	{
	//		cam: [buttonDeal, buttonWeb, buttonStores, buttonMicrophone]
	//	}
	//});
};
IrAndGeo.initIr = function() 
{
    IrAndGeo.TracerAlert("initIr()");
    // Create the tracker to recognize the shop ad
    //var trackerDataSetPath = "assets/ShopAd.wtc";
    var trackerDataSetPath = "assets/madrid-collection.wtc";
	IrAndGeo.isSpeakerSelected = false;
    //var trackerDataSetPath = "assets/leon-cibeles.wtc";
    IrAndGeo.tracker = new AR.Tracker(trackerDataSetPath, 
									{
										//onLoaded: IrAndGeo.loadingStepDone,
										onError: IrAndGeo.errorLoading
									});
};

// this function is called as soon as we receive GPS data 
AR.context.onLocationChanged = function(latitude, longitude, altitude, accuracy) 
{
    IrAndGeo.TracerAlert("onLocationChanged()" + "latitude: "+ latitude + " ,longitude: " + longitude + " altitude: " + longitude + " accuracy: "+ accuracy);
    // in order not to receive any further location updates the onLocationChanged trigger is set to null
    // AR.context.onLocationChanged = null;
    // flag to store that a location was received
    IrAndGeo.receivedLocation = true;
    // initialize the scene
    //latitude;
    //longitude;
    //altitude;
    var cssDivLeft = " style='display: table-cell;vertical-align: middle; text-align: right; width: 50%; padding-right: 15px;'";
    var cssDivRight = " style='display: table-cell;vertical-align: middle; text-align: left;'";
	var latitudeGMS = latitude;
	latitudeGMS = IrAndGeo.dec2gms (latitude, 1);
	var longitudeGMS = longitude;
	longitudeGMS = IrAndGeo.dec2gms (longitude, 2);
   // document.getElementById('messageElement').innerHTML = 
   // //"<div" + cssDivLeft + "> latitude: " + latitude + " longitude: " + longitude + " longitude: "" </div>";
   //"<div" + cssDivLeft + ">latitude \n" + latitudeGMS + " </div>" + 
   //"<div" + cssDivLeft + ">longitude \n" + longitudeGMS + " </div>" + 
   //"<div" + cssDivLeft + ">altitude \n" + altitude + " </div>";
 //   "<div" + cssDivLeft + ">longitude" + "</div>" +
 //   "<div" + cssDivLeft + ">altitude" + "</div>" +
 //   "<div" + cssDivLeft + ">accuracy" + "</div>" +
	document.getElementById('messageElement').innerHTML =  '<table align="center">' + 
														'<tr>' + 
														  '<td align="center"><font size="2"><strong>' + 'latitude' + '</strong></td>' + 
														  '<td align="center"><font size="2"><strong>' + 'longitude' + '</strong></td>' + 
														  '<td align="center"><font size="2"><strong>' + 'altitude' + '</strong></td>' + 
														'</tr>' + 
														'<tr>' + 
														  '<td align="center"><font size="2"><strong>' + latitudeGMS + '</strong></td>' + 
														  '<td align="center"><font size="2"><strong>' + longitudeGMS + '</strong></td>' + 
														  '<td align="center"><font size="2"><strong>' + altitude + '</strong></td>' + 
														'</tr>' + 
														'</table>'; 

    //"<div" + cssDivRight + "><img src='assets/ShopAdSmall.png'></img></div>";
	if (IrAndGeo.showPois == false)
	{
		IrAndGeo.setupScene(latitude, longitude, altitude);
		IrAndGeo.showPois = true;
	}
};

IrAndGeo.TracerAlert = function (szString)
{
	if (IrAndGeo.bTracerAlert == true)
	{
		alert (szString);
	}
}
IrAndGeo.markerDrawable_speaker = new AR.ImageResource("assets/buttons/speaker-48.png", {
    //onLoaded: IrAndGeo.loadingStepDone,
    onError: IrAndGeo.errorLoading
});
// Create the image resources that are used for the marker and the buttons
IrAndGeo.res.marker = new AR.ImageResource("assets/buttons/point-of-interest.png", {
    //onLoaded: IrAndGeo.loadingStepDone,
    onError: IrAndGeo.errorLoading
});
IrAndGeo.res.markerSelected = new AR.ImageResource("assets/buttons/point-of-interest_selected.png", {
    //onLoaded: IrAndGeo.loadingStepDone,
    onError: IrAndGeo.errorLoading
});
IrAndGeo.markerDrawable_WebInternet = new AR.ImageResource("assets/buttons/Wikipedia.png", {
    //onLoaded: IrAndGeo.loadingStepDone,
    onError: IrAndGeo.errorLoading
});
IrAndGeo.markerDrawable_FindInternet = new AR.ImageResource("assets/buttons/FindInternet.png", {
    //onLoaded: IrAndGeo.loadingStepDone,
    onError: IrAndGeo.errorLoading
});
IrAndGeo.markerDrawable_microphone = new AR.ImageResource("assets/buttons/microphone-3-128.png", {
    //onLoaded: IrAndGeo.loadingStepDone,
    onError: IrAndGeo.errorLoading
});
IrAndGeo.markerDrawable_ImageToShow = new AR.ImageResource("assets/marker_idle.png", {
	//onLoaded: IrAndGeo.loadingStepDone,
	onError: IrAndGeo.errorLoading
});
IrAndGeo.markerDrawable_idle = new AR.ImageResource("assets/marker_idle.png", {
    //onLoaded: IrAndGeo.loadingStepDone,
    onError: IrAndGeo.errorLoading
});

IrAndGeo.markerDrawable_selected = new AR.ImageResource("assets/marker_selected.png", {
    //onLoaded: IrAndGeo.loadingStepDone,
    onError: IrAndGeo.errorLoading
});
IrAndGeo.markerDrawable_directionIndicator = new AR.ImageResource("assets/indi.png", {
    //onLoaded: IrAndGeo.loadingStepDone,
    onError: IrAndGeo.errorLoading
});

IrAndGeo.res.buttonWeb = new AR.ImageResource("assets/YourShop_OpenWebsite.png", {
    //onLoaded: IrAndGeo.loadingStepDone,
    onError: IrAndGeo.errorLoading
});
IrAndGeo.res.buttonStores = new AR.ImageResource("assets/YourShop_FindShops.png", {
    //onLoaded: IrAndGeo.loadingStepDone,
    onError: IrAndGeo.errorLoading
});
IrAndGeo.res.buttonDeal = new AR.ImageResource("assets/YourShop_GetADeal.png", {
    //onLoaded: IrAndGeo.loadingStepDone,
    onError: IrAndGeo.errorLoading
});

IrAndGeo.res.deal = new AR.ImageResource("assets/YourShop_Deal.png", {
    //onLoaded: IrAndGeo.loadingStepDone,
    onError: IrAndGeo.errorLoading
});

IrAndGeo.dec2gms = function(valor, tipo)
{
	grados    = Math.abs(parseInt(valor));
	minutos   = (Math.abs(valor) - grados) * 60;
	segundos  = minutos;
	minutos   = Math.abs(parseInt(minutos));
	segundos  = Math.round((segundos - minutos) * 60 * 1000000) / 1000000;
	signo     = (valor < 0) ? -1 : 1;
	direccion = (tipo == 1) ? 
				((signo > 0) ? 'N' : 'S') : 
				((signo > 0) ? 'E' : 'W');
	
	if(isNaN(direccion))
		grados = grados * signo;
	var segundosTruncados = "" + segundos;
	return grados + "\u00b0 " + minutos + "' "+ segundosTruncados.substring(0, 3) + 
					 "\"" + ((isNaN(direccion)) ? (' ' + direccion) : '');
	//return {
	//	'grados'   : grados,
	//	'minutos'  : minutos,
	//	'segundos' : segundos,
	//	'direccion': direccion,
	//	'valor'    : grados + "\u00b0 " + minutos + "' "+ segundos + 
	//				 "\"" + ((isNaN(direccion)) ? (' ' + direccion) : '')
	//};
};

IrAndGeo.initIr();
