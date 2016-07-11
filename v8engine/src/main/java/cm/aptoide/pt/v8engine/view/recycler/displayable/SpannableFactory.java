package cm.aptoide.pt.v8engine.view.recycler.displayable;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;

import cm.aptoide.pt.v8engine.R;

public class SpannableFactory {

	@NonNull
	public Spannable createStyleSpan(String text, int style, String... spanTexts) {
		final Spannable result = new SpannableString(text);
		for (String spanText : spanTexts) {
			int spanTextStart = text.indexOf(spanText);
			result.setSpan(new StyleSpan(style), spanTextStart, (spanTextStart + spanText.length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return result;
	}
}