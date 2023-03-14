package com.modatta.plugins.facetec

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import com.facetec.sdk.FaceTecCancelButtonCustomization
import com.facetec.sdk.FaceTecCustomization

fun createFaceTecCustomization(): FaceTecCustomization {
    val customization = FaceTecCustomization();

    val backgroundColor = Color.parseColor("#f8f8f8")
    val primaryColor = Color.parseColor("#ff5757")
    val secondaryColor = Color.parseColor("#475569")
    val disabledColor = Color.parseColor("#cbd1d7")
    val disabledTextColor = Color.parseColor("#9ea9b5")
    val white = Color.parseColor("#ffffff")

    val highlightRegularFont = Typeface.create("sans-serif", Typeface.BOLD)
    val highlightBoldFont = Typeface.create("sans-serif", Typeface.BOLD)
    val baseRegularFont = Typeface.create("sans-serif", Typeface.NORMAL)

    customization.initialLoadingAnimationCustomization.messageFont = highlightBoldFont
    customization.initialLoadingAnimationCustomization.foregroundColor = backgroundColor
    customization.initialLoadingAnimationCustomization.backgroundColors = primaryColor

    customization.guidanceCustomization.readyScreenHeaderFont = baseRegularFont
    customization.guidanceCustomization.backgroundColors = backgroundColor
    customization.guidanceCustomization.foregroundColor = secondaryColor
    customization.guidanceCustomization.readyScreenHeaderFont = highlightBoldFont
    customization.guidanceCustomization.readyScreenHeaderTextColor = secondaryColor
    customization.guidanceCustomization.readyScreenSubtextFont = baseRegularFont
    customization.guidanceCustomization.retryScreenHeaderFont = highlightRegularFont
    customization.guidanceCustomization.retryScreenHeaderTextColor = secondaryColor
    customization.guidanceCustomization.retryScreenSubtextTextColor = secondaryColor
    customization.guidanceCustomization.retryScreenSubtextFont = baseRegularFont
    customization.guidanceCustomization.buttonBackgroundNormalColor = primaryColor
    customization.guidanceCustomization.buttonTextNormalColor = white
    customization.guidanceCustomization.buttonBackgroundDisabledColor = disabledColor
    customization.guidanceCustomization.buttonTextDisabledColor = disabledTextColor
    customization.guidanceCustomization.buttonBackgroundHighlightColor = primaryColor
    customization.guidanceCustomization.buttonTextHighlightColor = white
    customization.guidanceCustomization.buttonCornerRadius = 8
    customization.guidanceCustomization.headerFont = highlightBoldFont
    customization.guidanceCustomization.subtextFont = baseRegularFont
    customization.guidanceCustomization.buttonFont = highlightBoldFont
    customization.guidanceCustomization.retryScreenIdealImage = R.drawable.ideal_image_3
    customization.guidanceCustomization.retryScreenSlideshowImages = arrayOf(
            R.drawable.ideal_image_1,
            R.drawable.ideal_image_2,
            R.drawable.ideal_image_3,
            R.drawable.ideal_image_4,
            R.drawable.ideal_image_5
    ).toIntArray()

    customization.overlayCustomization.backgroundColor = backgroundColor
    customization.overlayCustomization.brandingImage = R.drawable.logo

    customization.feedbackCustomization.textFont = baseRegularFont
    customization.feedbackCustomization.backgroundColors = backgroundColor
    customization.feedbackCustomization.textColor = secondaryColor
    customization.feedbackCustomization.cornerRadius = 8

    customization.frameCustomization.backgroundColor = backgroundColor
    customization.frameCustomization.borderColor = backgroundColor
    customization.frameCustomization.borderWidth = 0
    customization.frameCustomization.elevation = 0
    customization.frameCustomization.cornerRadius = 0

    customization.ovalCustomization.strokeColor = backgroundColor
    customization.ovalCustomization.progressColor1 = backgroundColor
    customization.ovalCustomization.progressColor2 = backgroundColor
    customization.ovalCustomization.strokeWidth = 0

    customization.resultScreenCustomization.backgroundColors = backgroundColor
    customization.resultScreenCustomization.messageFont = highlightBoldFont
    customization.resultScreenCustomization.foregroundColor = secondaryColor
    customization.resultScreenCustomization.activityIndicatorColor = backgroundColor
    customization.resultScreenCustomization.uploadProgressFillColor = primaryColor
    customization.resultScreenCustomization.uploadProgressTrackColor = secondaryColor
    customization.resultScreenCustomization.customActivityIndicatorImage = -1
    customization.resultScreenCustomization.resultAnimationForegroundColor = backgroundColor
    customization.resultScreenCustomization.resultAnimationSuccessBackgroundImage = -1
    customization.resultScreenCustomization.resultAnimationUnsuccessBackgroundImage = -1

    customization.cancelButtonCustomization.location =
            FaceTecCancelButtonCustomization.ButtonLocation.TOP_RIGHT
    customization.cancelButtonCustomization.customImage = R.drawable.close_icon

    return customization;
}
