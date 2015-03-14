var kMarker_AnimationDuration_ChangeDrawable = 500;
var kMarker_AnimationDuration_Resize = 1000;
//IrAndGeo.Marker = function(poiData) {
function Marker(poiData) {

	alert ("Marker pp (): poiData.name " + poiData.name);

    this.poiData = poiData;
    this.isSelected = false;
	this.isSpeakerSelected = false;
    /*
        With AR.PropertyAnimations you are able to animate almost any property of ARchitect objects. This sample will animate the opacity of both background drawables so that one will fade out while the other one fades in. The scaling is animated too. The marker size changes over time so the labels need to be animated too in order to keep them relative to the background drawable. AR.AnimationGroups are used to synchronize all animations in parallel or sequentially.
    */

    this.animationGroup_idle = null;
    this.animationGroup_selected = null;

//alert("markerLocation = new AR.GeoLocation "); 
    // create the AR.GeoLocation from the poi data
    // var markerLocation = new AR.GeoLocation(poiData.latitude, poiData.longitude); //, poiData.altitude);
var markerLocation = new AR.GeoLocation(poiData.latitude, poiData.longitude); //,altitude);
// alert("this.markerDrawable_idle "); 
    // create an AR.ImageDrawable for the marker in idle state
    // this.markerDrawable_idle = new AR.ImageDrawable(IrAndGeo.markerDrawable_idle, 1.5, {
    this.markerDrawable_idle = new AR.ImageDrawable(IrAndGeo.res.marker, 1.5, {
        zOrder: 0,
        opacity: 1.0,
        /*
            To react on user interaction, an onClick property can be set for each AR.Drawable. The property is a function which will be called each time the user taps on the drawable. The function called on each tap is returned from the following helper function defined in marker.js. The function returns a function which checks the selected state with the help of the variable isSelected and executes the appropriate function. The clicked marker is passed as an argument.
        */
        onClick: Marker.prototype.getOnClickPoi(this)
    });
	

// alert("this.markerDrawable_selected "); 
    // create an AR.ImageDrawable for the marker in selected state
    //this.markerDrawable_selected = new AR.ImageDrawable(IrAndGeo.markerDrawable_selected, 1.5, {
    this.markerDrawable_selected = new AR.ImageDrawable(IrAndGeo.res.marker, 1.5, {
        zOrder: 0,
        opacity: 0.0,
        onClick: null
    });

	this.markerDrawable_speaker = new AR.ImageDrawable(IrAndGeo.markerDrawable_speaker, 1.5, {
        zOrder: 1,
        opacity: 0.0,
		offsetY: -1.0,
		offsetX: -1.0,
        onClick: null
    });
	this.markerDrawable_WebInternet = new AR.ImageDrawable(IrAndGeo.markerDrawable_WebInternet, 1.5, {
        zOrder: 2,
        opacity: 0.0,
		offsetY: 1.0,
		offsetX: 1.0,
        onClick: null// Marker.prototype.getOnClickWebInternet(this)
    });
    
	// create an AR.Label for the marker's Country 
    this.titleLabel = new AR.Label(poiData.name.trunc(10), 0.5, {
        zOrder: 0,
        offsetY: -1.0,
		//onClick: Marker.prototype.getOnClickPoi(this),
        style: {
            textColor: '#FFFFFF',
            fontStyle: AR.CONST.FONT_STYLE.BOLD
        }
		
    });

// alert("this.directionIndicatorDrawable = new AR.ImageDrawable "); 
    /*
        Create an AR.ImageDrawable using the AR.ImageResource for the direction indicator which was created in the World. Set options regarding the offset and anchor of the image so that it will be displayed correctly on the edge of the screen.
    */
    this.directionIndicatorDrawable = new AR.ImageDrawable(IrAndGeo.markerDrawable_directionIndicator, 0.1, {
        enabled: false,
        verticalAnchor: AR.CONST.VERTICAL_ANCHOR.TOP
    });

    /*
        Create the AR.GeoObject with the drawable objects and define the AR.ImageDrawable as an indicator target on the marker AR.GeoObject. The direction indicator is displayed automatically when necessary. AR.Drawable subclasses (e.g. AR.Circle) can be used as direction indicators.
    */
    this.markerObject = new AR.GeoObject( markerLocation, {
        drawables: {
            cam: [this.markerDrawable_idle, this.markerDrawable_selected, this.markerDrawable_WebInternet, this.markerDrawable_speaker, this.titleLabel],
            indicator: this.directionIndicatorDrawable
        }
    });
// alert("return this "); 
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
			alert("Market.prototype.getOnClickTringer Error" + err);
		}
		return true;
    };
};
Marker.prototype.getOnClickWebInternet = function(marker) {
	return function() 
	{
		alert ("WebInternet selected?");
        return true;
    };
};
Marker.prototype.getOnClickPoi = function(marker) {

    /*
        The setSelected and setDeselected functions are prototype Marker functions. 
        Both functions perform the same steps but inverted.
    */

    return function() {
		alert ("getOnClickPoi?");
        //if (!Marker.prototype.isAnyAnimationRunning(marker)) {
            if (marker.isSelected) {

                Marker.prototype.setDeselected(marker);
                try 
				{
                    IrAndGeo.onMarkerSelected(marker);
                } 
				catch (err) 
				{
                    alert("Market.prototype.getOnClickTringer Error" + err);
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
                    alert("Market.prototype.getOnClickTringer Error" + err);
                }

            }
        //} else {
        //    AR.logger.debug('a animation is already running');
        //}


        return true;
    };
};

/*
    Property Animations allow constant changes to a numeric value/property of an object, dependent on start-value, end-value and the duration of the animation. Animations can be seen as functions defining the progress of the change on the value. The Animation can be parametrized via easing curves.
*/

Marker.prototype.setSelected = function(marker) {

    marker.isSelected = true;

    // New: 
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

        /*
            There are two types of AR.AnimationGroups. Parallel animations are running at the same time, sequentials are played one after another. This example uses a parallel AR.AnimationGroup.
        */
        marker.animationGroup_selected = new AR.AnimationGroup(AR.CONST.ANIMATION_GROUP_TYPE.PARALLEL, [hideIdleDrawableAnimation, showSelectedDrawableAnimation, idleDrawableResizeAnimation, selectedDrawableResizeAnimation, titleLabelResizeAnimation]);
    }
	marker.markerDrawable_speaker.opacity 		= 1;
	marker.markerDrawable_WebInternet.opacity 	= 1;
	
	marker.markerDrawable_speaker.onClick 		= Marker.prototype.getOnClickSpeacker(marker);
	marker.markerDrawable_WebInternet.onClick 	= Marker.prototype.getOnClickWebInternet(marker);

    // removes function that is set on the onClick trigger of the idle-state marker
    marker.markerDrawable_idle.onClick = null;
    // sets the click trigger function for the selected state marker
    marker.markerDrawable_selected.onClick = Marker.prototype.getOnClickPoi(marker);

    // enables the direction indicator drawable for the current marker
    marker.directionIndicatorDrawable.enabled = true;
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
	marker.markerDrawable_WebInternet.opacity = 0;
	marker.markerDrawable_speaker.opacity = 0;
    // sets the click trigger function for the idle state marker
    marker.markerDrawable_idle.onClick = Marker.prototype.getOnClickPoi(marker);
    // removes function that is set on the onClick trigger of the selected-state marker
    marker.markerDrawable_selected.onClick = null;
	marker.markerDrawable_speaker.onClick = null;
	marker.markerDrawable_WebInternet.onCLick = null;

    // disables the direction indicator drawable for the current marker
    marker.directionIndicatorDrawable.enabled = false;
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
