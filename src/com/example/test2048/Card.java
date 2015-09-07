package com.example.test2048;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Card extends FrameLayout {

	private TextView textView;
	int num = 0;

	public Card(Context context) {
		super(context);

		textView = new TextView(getContext());
		textView.setTextSize(32);
		textView.setGravity(Gravity.CENTER);
		textView.setBackgroundColor(Color.GREEN);
		LayoutParams layoutParams = new LayoutParams(-1, -1);
		layoutParams.setMargins(10, 10, 0, 0);
		addView(textView, layoutParams);

		setNum(0);
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
		if (num <= 0)
			textView.setText("");
		else
			textView.setText(num + "");
	}

	public boolean equals(Card o) {
		return getNum() == o.getNum();
	}

}
