var kMarker_AnimationDuration_ChangeDrawable = 500;
var kMarker_AnimationDuration_Resize = 1000;
//IrAndGeo.Marker = function(poiData) {
function Marker(poiData, ImageRecognition) {

	// alert ("Marker (): poiData.name " + poiData.name);
    this.poiData = poiData;
    this.isSelected = false;
	this.isSpeakerSelected = false;
    /*
        With AR.PropertyAnimations you are able to animate almost any property of ARchitect objects. This sample will animate the opacity of both background drawables so that one will fade out while the other one fades in. The scaling is animated too. The marker size changes over time so the labels need to be animated too in order to keep them relative to the background drawable. AR.AnimationGroups are used to synchronize all animations in parallel or sequentially.
    */

    this.animationGroup_idle = null;
    this.animationGroup_selected = null;
    // create an AR.ImageDrawable for the marker in idle state
    // this.markerDrawable_idle = new AR.ImageDrawable(IrAndGeo.markerDrawable_idle, 1.5, {
    this.markerDrawable_idle = new AR.ImageDrawable(IrAndGeo.res.marker, 2.0, {
        zOrder: 0,
		enabled: true,
        opacity: 1.0,
		offsetX: 0.0,
		offsetY: 0.0,	
        // To react on user interaction, an onClick property can be set for each AR.Drawable. The property is a function which will be called each time the user taps on the drawable. The function called on each tap is returned from the following helper function defined in marker.js. The function returns a function which checks the selected state with the help of the variable isSelected and executes the appropriate function. The clicked marker is passed as an argument.
        onClick: Marker.prototype.getOnClickPoi(this)
    });
	
    // create an AR.ImageDrawable for the marker in selected state
    this.markerDrawable_selected = new AR.ImageDrawable(IrAndGeo.res.marker, 2.8, {
        zOrder: 0,
		enabled: true,
        opacity: 0.0,
		offsetX: 0.0,
		offsetY: 0.0,		
        onClick: null
    });
	
	this.markerDrawable_speaker = new AR.ImageDrawable(IrAndGeo.markerDrawable_speaker, 0.15, {
        zOrder: 1,
		enabled: false,
        opacity: 1.0,
		offsetX: 0.15,
		offsetY: 0.0,
        onClick: null
    });
	this.markerDrawable_WebInternet = new AR.ImageDrawable(IrAndGeo.markerDrawable_WebInternet, 0.15, {
        zOrder: 1,
		enabled: false,
        opacity: 1.0,
		offsetX: 0.30,
		offsetY: 0.0,
        onClick: null// Marker.prototype.getOnClickWebInternet(this)
    });
 	this.markerDrawable_FindInternet = new AR.ImageDrawable(IrAndGeo.markerDrawable_FindInternet, 0.15, {
        zOrder: 1,
		enabled: false,
        opacity: 1.0,
		offsetX: 0.45,
		offsetY: 0,
        onClick: null// Marker.prototype.getOnClickWebInternet(this)
    });
	
	this.markerDrawable_microphone = new AR.ImageDrawable(IrAndGeo.markerDrawable_microphone, 0.15, 
	{
		zOrder: 1,
		enabled: false,
		opacity: 1.0,
		offsetX: 0.60,
		offsetY: 0.0,
		onClick: null	// Marker.prototype.getOnClickWebInternet(this)
	});
	
	// create an AR.Label for the marker's Country 
    this.titleLabel = new AR.Label(poiData.name.trunc(20), 0.5, {
        zOrder: 0,
        offsetY: -1.0,
		opacity: 1.0,
        style: {
            textColor: '#FFFFFF',
            fontStyle: AR.CONST.FONT_STYLE.BOLD
        }
		
    });
    this.titleLabelSelected = new AR.Label(poiData.name.trunc(20), 0.7, {
        zOrder: 0,
        offsetY: -1.0,
		opacity: 0.0,
        style: {
            textColor: '#FFFFFF',
            fontStyle: AR.CONST.FONT_STYLE.BOLD
        }
    });    
    //Create an AR.ImageDrawable using the AR.ImageResource for the direction indicator which was created in the World. Set options regarding the offset and anchor of the image so that it will be displayed correctly on the edge of the screen.
	if (ImageRecognition == false)
	{
		var trackable2DObject = new AR.Trackable2DObject(IrAndGeo.tracker, poiData.ImagesToTrack, 
		{
			drawables: 
			{
				cam: [this.markerDrawable_idle, this.markerDrawable_selected, this.markerDrawable_WebInternet, this.markerDrawable_FindInternet, this.markerDrawable_speaker, this.titleLabel, this.titleLabelSelected, this.markerDrawable_microphone]
			}
		});
		this.directionIndicatorDrawable = null;
		this.markerObject = null;
		this.DistanceLabel = null;
		this.GeoMarker = false; 
	}
	else
	{
		this.GeoMarker = true;
	    this.DistanceLabel = new AR.Label("", 0.3, {
        zOrder: 0,
        offsetY: -1.4,
		opacity: 1.0,
        style: {
            textColor: '#FFFFFF',
            fontStyle: AR.CONST.FONT_STYLE.BOLD
			}
		});
		this.directionIndicatorDrawable = new AR.ImageDrawable(IrAndGeo.markerDrawable_directionIndicator, 0.1, {
			enabled: false,
			verticalAnchor: AR.CONST.VERTICAL_ANCHOR.TOP
		});
		// create the AR.GeoLocation from the poi data  
		var markerLocation = new AR.GeoLocation(poiData.latitude, poiData.longitude); //,altitude);
		/*
			Create the AR.GeoObject with the drawable objects and define the AR.ImageDrawable as an indicator target on the marker AR.GeoObject. The direction indicator is displayed automatically when necessary. AR.Drawable subclasses (e.g. AR.Circle) can be used as direction indicators.
		*/
		this.markerObject = new AR.GeoObject( markerLocation, {
			drawables: {
				cam: [this.markerDrawable_idle, this.markerDrawable_selected, this.markerDrawable_WebInternet, this.markerDrawable_FindInternet, this.markerDrawable_speaker, this.titleLabel, this.titleLabelSelected, this.markerDrawable_microphone, this.DistanceLabel],
				indicator: this.directionIndicatorDrawable
			}
		});	
	
	}	
    return this;
}
Marker.prototype.getOnClickSpeacker = function(marker) {
	return function() 
	{
		try 
		{
			IrAndGeo.onSpeakerSelected(marker);
		} 
		catch (err) 
		{
			alert("Market.prototype.getOnClickSpeacker Error" + err);
		}
		return true;
    };
};
Marker.prototype.getOnClickFindInternet = function(marker) {
	return function() 
	{	
		try 
		{
			IrAndGeo.onFindInternetSelected(marker);
		} 
		catch (err) 
		{
			alert("Market.prototype.getOnClickFindInternet Error" + err);
		}
	
        return true;
    };
};
Marker.prototype.getOnClickMicrophone = function(marker) {
	return function() 
	{	
		try 
		{
			IrAndGeo.onMicrophneSelected(marker);
		} 
		catch (err) 
		{
			alert("Market.prototype.onMicrophneSelected Error" + err);
		}
	
        return true;
    };
};
Marker.prototype.getOnClickWebInternet = function(marker) {
	return function() 
	{		
		try 
		{
			IrAndGeo.onWebInternetSelected(marker);
		} 
		catch (err) 
		{
			alert("Market.prototype.getOnClickWebInternet Error" + err);
		}
	
        return true;
    };
};
Marker.prototype.getOnClickPoi = function(marker) {

    /*
        The setSelected and setDeselected functions are prototype Marker functions. 
        Both functions perform the same steps but inverted.
    */

    return function() {
        //if (!Marker.prototype.isAnyAnimationRunning(marker)) {
            if (marker.isSelected) {

                Marker.prototype.setDeselected(marker);
                try 
				{
                    IrAndGeo.onMarkerSelected(marker);
                } 
				catch (err) 
				{
                    alert("Market.prototype.getOnClickPoi Error" + err);
                }
            } 
			else 
			{
                Marker.prototype.setSelected(marker);
                try 
				{
                    IrAndGeo.onMarkerSelected(marker);
                } 
				catch (err) 
				{
                    alert("Market.prototype.getOnClickPoi Error" + err);
                }

            }

        return true;
    };
};

/*
    Property Animations allow constant changes to a numeric value/property of an object, dependent on start-value, end-value and the duration of the animation. Animations can be seen as functions defining the progress of the change on the value. The Animation can be parametrized via easing curves.
*/
Marker.prototype.setSelected = function(marker) 
{
    marker.isSelected = true;
	
    if (marker.animationGroup_selected === null) {

        // create AR.PropertyAnimation that animates the opacity to 0.0 in order to hide the idle-state-drawable
        var hideIdleDrawableAnimation = new AR.PropertyAnimation(marker.markerDrawable_idle, "opacity", null, 0.0, kMarker_AnimationDuration_ChangeDrawable);
        // create AR.PropertyAnimation that animates the opacity to 1.0 in order to show the selected-state-drawable
        var showSelectedDrawableAnimation = new AR.PropertyAnimation(marker.markerDrawable_selected, "opacity", null, 1.0, kMarker_AnimationDuration_ChangeDrawable);

        // create AR.PropertyAnimation that animates the scaling of the idle-state-drawable to 1.2
        var idleDrawableResizeAnimation = new AR.PropertyAnimation(marker.markerDrawable_idle, 'scaling', null, 1.2, kMarker_AnimationDuration_Resize, new AR.EasingCurve(AR.CONST.EASING_CURVE_TYPE.EASE_OUT_ELASTIC, {
            amplitude: 2.0
        }));
        // create AR.PropertyAnimation that animates the scaling of the selected-state-drawable to 1.2
        var selectedDrawableResizeAnimation = new AR.PropertyAnimation(marker.markerDrawable_selected, 'scaling', null, 1.2, kMarker_AnimationDuration_Resize, new AR.EasingCurve(AR.CONST.EASING_CURVE_TYPE.EASE_OUT_ELASTIC, {
            amplitude: 2.0
        }));
        // create AR.PropertyAnimation that animates the scaling of the title label to 1.2
        var titleLabelResizeAnimation = new AR.PropertyAnimation(marker.titleLabel, 'scaling', null, 1.2, kMarker_AnimationDuration_Resize, new AR.EasingCurve(AR.CONST.EASING_CURVE_TYPE.EASE_OUT_ELASTIC, {
            amplitude: 2.0
        }));
        // create AR.PropertyAnimation that animates the scaling of the description label to 1.2
        //var descriptionLabelResizeAnimation = new AR.PropertyAnimation(marker.descriptionLabel, 'scaling', null, 1.2, kMarker_AnimationDuration_Resize, new AR.EasingCurve(AR.CONST.EASING_CURVE_TYPE.EASE_OUT_ELASTIC, {
        //    amplitude: 2.0
        //}));
        // There are two types of AR.AnimationGroups. Parallel animations are running at the same time, sequentials are played one after another. This example uses a parallel AR.AnimationGroup.

        marker.animationGroup_selected = new AR.AnimationGroup(AR.CONST.ANIMATION_GROUP_TYPE.PARALLEL, [hideIdleDrawableAnimation, showSelectedDrawableAnimation, idleDrawableResizeAnimation, selectedDrawableResizeAnimation, titleLabelResizeAnimation]);
    }
    // enables the direction indicator drawable for the current marker
	if (marker.GeoMarker == true)
	{
		// distance and altitude are measured in meters by the SDK. You may convert them to miles / feet if required.
		if (marker.directionIndicatorDrawable != null)
			marker.directionIndicatorDrawable.enabled = true;

		if (marker.markerObject != null)	
		{
			var distanceToUserValue = marker.markerObject.locations[0].distanceToUser();
			
			if (distanceToUserValue > 999)
			{
				distanceToUserString = ((distanceToUserValue / 1000).toFixed(2) + " km");
			}
			else
			{
				distanceToUserString = (Math.round(distanceToUserValue) + " m");
			}
			marker.markerDrawable_speaker.scale = 2.0;
			marker.DistanceLabel.text = "" + distanceToUserString;
		}
	}
	
	
	marker.markerDrawable_speaker.enabled 		= true;
	marker.markerDrawable_WebInternet.enabled 	= true;
	marker.markerDrawable_FindInternet.enabled 	= true;
	marker.markerDrawable_microphone.enabled 	= true;
	
	marker.markerDrawable_selected.opacity  = 1;
	marker.markerDrawable_idle.opacity = 0;
	marker.titleLabelSelected.opacity  	= 1;
	marker.titleLabel.opacity	= 0;
	

	
	marker.markerDrawable_speaker.onClick 		= Marker.prototype.getOnClickSpeacker(marker);
	marker.markerDrawable_WebInternet.onClick 	= Marker.prototype.getOnClickWebInternet(marker);
	marker.markerDrawable_FindInternet.onClick 	= Marker.prototype.getOnClickFindInternet(marker);
	marker.markerDrawable_microphone.onClick 	= Marker.prototype.getOnClickMicrophone(marker);

    // removes function that is set on the onClick trigger of the idle-state marker
    marker.markerDrawable_idle.onClick = null;
    // sets the click trigger function for the selected state marker
    marker.markerDrawable_selected.onClick = Marker.prototype.getOnClickPoi(marker);


    // starts the selected-state animation
    marker.animationGroup_selected.start();
};

Marker.prototype.setDeselected = function(marker) {

    marker.isSelected = false;

    if (marker.animationGroup_idle === null) {

        // create AR.PropertyAnimation that animates the opacity to 1.0 in order to show the idle-state-drawable
        var showIdleDrawableAnimation = new AR.PropertyAnimation(marker.markerDrawable_idle, "opacity", null, 1.0, kMarker_AnimationDuration_ChangeDrawable);
        // create AR.PropertyAnimation that animates the opacity to 0.0 in order to hide the selected-state-drawable
        var hideSelectedDrawableAnimation = new AR.PropertyAnimation(marker.markerDrawable_selected, "opacity", null, 0, kMarker_AnimationDuration_ChangeDrawable);
        // create AR.PropertyAnimation that animates the scaling of the idle-state-drawable to 1.0
        var idleDrawableResizeAnimation = new AR.PropertyAnimation(marker.markerDrawable_idle, 'scaling', null, 1.0, kMarker_AnimationDuration_Resize, new AR.EasingCurve(AR.CONST.EASING_CURVE_TYPE.EASE_OUT_ELASTIC, {
            amplitude: 2.0
        }));
        // create AR.PropertyAnimation that animates the scaling of the selected-state-drawable to 1.0
        var selectedDrawableResizeAnimation = new AR.PropertyAnimation(marker.markerDrawable_selected, 'scaling', null, 1.0, kMarker_AnimationDuration_Resize, new AR.EasingCurve(AR.CONST.EASING_CURVE_TYPE.EASE_OUT_ELASTIC, {
            amplitude: 2.0
        }));
        // create AR.PropertyAnimation that animates the scaling of the title label to 1.0
        var titleLabelResizeAnimation = new AR.PropertyAnimation(marker.titleLabel, 'scaling', null, 1.0, kMarker_AnimationDuration_Resize, new AR.EasingCurve(AR.CONST.EASING_CURVE_TYPE.EASE_OUT_ELASTIC, {
            amplitude: 2.0
        }));
        // create AR.PropertyAnimation that animates the scaling of the description label to 1.0
        // var descriptionLabelResizeAnimation = new AR.PropertyAnimation(marker.descriptionLabel, 'scaling', null, 1.0, kMarker_AnimationDuration_Resize, new AR.EasingCurve(AR.CONST.EASING_CURVE_TYPE.EASE_OUT_ELASTIC, {
        //    amplitude: 2.0
        //}));

        /*
            There are two types of AR.AnimationGroups. Parallel animations are running at the same time, sequentials are played one after another. This example uses a parallel AR.AnimationGroup.
        */
        marker.animationGroup_idle = new AR.AnimationGroup(AR.CONST.ANIMATION_GROUP_TYPE.PARALLEL, [showIdleDrawableAnimation, hideSelectedDrawableAnimation, idleDrawableResizeAnimation, selectedDrawableResizeAnimation, titleLabelResizeAnimation]);
    }
	if (marker.GeoMarker == true)
	{
		marker.DistanceLabel.text = "";
		// disables the direction indicator drawable for the current marker
		if (marker.directionIndicatorDrawable != null)
			marker.directionIndicatorDrawable.enabled = false;
	}	
	
	marker.markerDrawable_WebInternet.enabled = false;
	marker.markerDrawable_FindInternet.enabled = false;
	marker.markerDrawable_microphone.enabled = false;
	marker.markerDrawable_speaker.enabled = false;
	marker.titleLabelSelected.opacity  	= 0;
	marker.titleLabel.opacity	= 1;
	
	
	
	marker.markerDrawable_selected.opacity  = 0;
	marker.markerDrawable_idle.opacity = 1;
    // sets the click trigger function for the idle state marker
    marker.markerDrawable_idle.onClick = Marker.prototype.getOnClickPoi(marker);
    // removes function that is set on the onClick trigger of the selected-state marker.
    marker.markerDrawable_selected.onClick = null;
	marker.markerDrawable_speaker.onClick = null;
	marker.markerDrawable_WebInternet.onCLick = null;
	marker.markerDrawable_FindInternet.onCLick = null;
	marker.markerDrawable_microphone.onCLick = null;

    // starts the idle-state animation
    marker.animationGroup_idle.start();
};

Marker.prototype.isAnyAnimationRunning = function(marker) {

    if (marker.animationGroup_idle === null || marker.animationGroup_selected === null) {
        return false;
    } else {
        if ((marker.animationGroup_idle.isRunning() === true) || (marker.animationGroup_selected.isRunning() === true)) {
            return true;
        } else {
            return false;
        }
    }
};

// will truncate all strings longer than given max-length "n". e.g. "foobar".trunc(3) -> "foo..."
String.prototype.trunc = function(n) {
    return this.substr(0, n - 1) + (this.length > n ? '...' : '');
};
