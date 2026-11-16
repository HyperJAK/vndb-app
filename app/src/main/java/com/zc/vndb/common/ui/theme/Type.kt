package com.zc.vndb.common.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

private val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = com.zc.vndb.R.array.com_google_android_gms_fonts_certs,
)

private fun googleFontFamily(name: String, vararg weights: FontWeight): FontFamily {
    val font = GoogleFont(name)
    return FontFamily(weights.map { Font(googleFont = font, fontProvider = provider, weight = it) })
}

/** Display & section titles. */
val SpaceGrotesk = googleFontFamily("Space Grotesk", FontWeight.SemiBold, FontWeight.Bold)

/** Narrative & metadata. */
val HankenGrotesk = googleFontFamily(
    "Hanken Grotesk",
    FontWeight.Normal,
    FontWeight.SemiBold,
    FontWeight.Bold,
)

/** Data, IDs, tags, scores. */
val JetBrainsMono = googleFontFamily("JetBrains Mono", FontWeight.Medium, FontWeight.SemiBold)

// DESIGN.md token -> Material 3 slot:
// headline-lg        -> headlineLarge
// headline-lg-mobile -> headlineMedium
// headline-md        -> headlineSmall, titleLarge
// headline-sm        -> titleMedium
// body-lg/md/sm      -> bodyLarge/Medium/Small
// label-lg           -> labelLarge, titleSmall
// label-md/sm (mono) -> labelMedium/Small
private val headlineLg = TextStyle(
    fontFamily = SpaceGrotesk,
    fontWeight = FontWeight.Bold,
    fontSize = 30.sp,
    lineHeight = 36.sp,
    letterSpacing = (-0.02).em,
)
private val headlineLgMobile = TextStyle(
    fontFamily = SpaceGrotesk,
    fontWeight = FontWeight.Bold,
    fontSize = 24.sp,
    lineHeight = 30.sp,
    letterSpacing = (-0.01).em,
)
private val headlineMd = TextStyle(
    fontFamily = SpaceGrotesk,
    fontWeight = FontWeight.SemiBold,
    fontSize = 20.sp,
    lineHeight = 26.sp,
)
private val headlineSm = TextStyle(
    fontFamily = SpaceGrotesk,
    fontWeight = FontWeight.SemiBold,
    fontSize = 17.sp,
    lineHeight = 22.sp,
)
private val bodyLg = TextStyle(
    fontFamily = HankenGrotesk,
    fontWeight = FontWeight.Normal,
    fontSize = 15.sp,
    lineHeight = 22.sp,
)
private val bodyMd = TextStyle(
    fontFamily = HankenGrotesk,
    fontWeight = FontWeight.Normal,
    fontSize = 13.sp,
    lineHeight = 18.sp,
)
private val bodySm = TextStyle(
    fontFamily = HankenGrotesk,
    fontWeight = FontWeight.Normal,
    fontSize = 11.sp,
    lineHeight = 15.sp,
)
private val labelLg = TextStyle(
    fontFamily = HankenGrotesk,
    fontWeight = FontWeight.SemiBold,
    fontSize = 12.sp,
    lineHeight = 16.sp,
    letterSpacing = 0.01.em,
)
private val labelMd = TextStyle(
    fontFamily = JetBrainsMono,
    fontWeight = FontWeight.Medium,
    fontSize = 11.sp,
    lineHeight = 14.sp,
)
private val labelSm = TextStyle(
    fontFamily = JetBrainsMono,
    fontWeight = FontWeight.SemiBold,
    fontSize = 9.sp,
    lineHeight = 12.sp,
    letterSpacing = 0.04.em,
)

private val defaults = Typography()

val VndbTypography = Typography(
    displayLarge = defaults.displayLarge.copy(
        fontFamily = SpaceGrotesk,
        fontWeight = FontWeight.Bold,
    ),
    displayMedium = defaults.displayMedium.copy(
        fontFamily = SpaceGrotesk,
        fontWeight = FontWeight.Bold,
    ),
    displaySmall = defaults.displaySmall.copy(
        fontFamily = SpaceGrotesk,
        fontWeight = FontWeight.Bold,
    ),
    headlineLarge = headlineLg,
    headlineMedium = headlineLgMobile,
    headlineSmall = headlineMd,
    titleLarge = headlineMd,
    titleMedium = headlineSm,
    titleSmall = labelLg,
    bodyLarge = bodyLg,
    bodyMedium = bodyMd,
    bodySmall = bodySm,
    labelLarge = labelLg,
    labelMedium = labelMd,
    labelSmall = labelSm,
)
