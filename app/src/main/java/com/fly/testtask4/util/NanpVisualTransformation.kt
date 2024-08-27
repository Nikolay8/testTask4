package com.fly.testtask4.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * A [VisualTransformation] that formats an input text as a Ukrainian phone number with country code.
 * The expected format is `+38 (XXX) XXX-XX-XX`.
 *
 * This class automatically inserts the appropriate symbols (parentheses, spaces, and hyphens)
 * while the user is typing. It also trims the input if it's longer than 10 digits.
 *
 * Example:
 * - Input: "1234567890"
 * - Output: "+38 (123) 456-78-90"
 *
 * @constructor Creates an instance of [NanpVisualTransformation].
 */
class NanpVisualTransformation : VisualTransformation {

    /**
     * Applies the transformation to the given [AnnotatedString].
     *
     * @param text The input [AnnotatedString] to be transformed.
     * @return A [TransformedText] containing the formatted phone number.
     */
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 10) text.text.substring(0..9) else text.text

        var out = if (trimmed.isNotEmpty()) "+38 (" else ""

        for (i in trimmed.indices) {
            if (i == 3) out += ") "
            if (i == 6) out += "-"
            if (i == 8) out += "-"
            out += trimmed[i]
        }
        return TransformedText(AnnotatedString(out), phoneNumberOffsetTranslator)
    }

    /**
     * A custom [OffsetMapping] implementation that adjusts the cursor position
     * according to the transformation applied.
     */
    private val phoneNumberOffsetTranslator = object : OffsetMapping {

        /**
         * Maps the original character offset to the transformed text offset.
         *
         * @param offset The original offset in the input text.
         * @return The offset in the transformed text.
         */
        override fun originalToTransformed(offset: Int): Int =
            when (offset) {
                0 -> offset
                // Add 1 for opening parenthesis.
                in 1..3 -> offset + 5
                // Add 3 for both parentheses and a space.
                in 4..6 -> offset + 7
                // Add 4 for both parentheses, space, and hyphen.
                else -> offset + 8
            }

        /**
         * Maps the transformed text offset back to the original character offset.
         *
         * @param offset The offset in the transformed text.
         * @return The original offset in the input text.
         */
        override fun transformedToOriginal(offset: Int): Int =
            when (offset) {
                0 -> offset
                // Subtract 1 for opening parenthesis.
                in 1..5 -> offset - 5
                // Subtract 3 for both parentheses and a space.
                in 6..10 -> offset - 7
                // Subtract 4 for both parentheses, space, and hyphen.
                else -> offset - 8
            }

    }
}