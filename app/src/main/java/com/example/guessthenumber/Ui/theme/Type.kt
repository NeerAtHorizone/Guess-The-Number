package com.example.guessthenumber.Ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.Font
import com.example.guessthenumber.R
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

// initialize the font

val robotoFont = FontFamily(Font(R.font.robotocondensed_regular))
val montserratFont = FontFamily(Font(R.font.montserrat_regular))

/*
    The font role
    displayLarge - Roboto -Normal -36sp
    displayMedium - Montserrat -Bold - 20sp
    labelSmall - Montserrat -Bold -14sp
    bodyLarge - Montserrat -Normal -14sp
 */
val typography = Typography(
    displayLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        fontFamily = robotoFont
    ),
    displayMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        fontFamily = montserratFont
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        fontFamily = montserratFont
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        fontFamily = montserratFont
    ),
)


val AppTypography = typography

