package com.example.test2048;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

public class GameView extends GridLayout {

	private Card[][] cards = new Card[4][4];
	private List<Point> points = new ArrayList<Point>();
	private boolean addFlag = false;

	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initGameView();
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initGameView();
	}

	public GameView(Context context) {
		super(context);
		initGameView();
	}

	@Override
	// 第一次创建时执行，只执行一次。
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		int card_width = (Math.min(w, h) - 10) / 4;
		addCards(card_width, card_width);
		startGame();

	}

	private void startGame() {
		MainActivity.getMainActivity().clearScore();
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				cards[x][y].setNum(0);
			}
		}
		addRandomNum();
		addRandomNum();
	}

	private void addRandomNum() {
		points.clear();
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				if (cards[x][y].getNum() <= 0) {
					points.add(new Point(x, y));
				}
			}
		}

		Point p = points.remove((int) (Math.random() * points.size()));
		cards[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);
	}

	private void addCards(int card_width, int card_heigth) {
		Card c;
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				c = new Card(getContext());
				c.setNum(0);
				addView(c, card_heigth, card_heigth);

				cards[x][y] = c;
			}
		}
	}

	private void checkGameOver() {
		boolean completeFlag = true;
		ALL: for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				if (cards[x][y].getNum() == 0
						|| (x > 0 && cards[x][y].getNum() == cards[x - 1][y]
								.getNum())
						|| (x < 3 && cards[x][y].getNum() == cards[x + 1][y]
								.getNum())
						|| (y > 0 && cards[x][y].getNum() == cards[x][y - 1]
								.getNum())
						|| (y < 3 && cards[x][y].getNum() == cards[x][y + 1]
								.getNum())) {
					completeFlag = false;
					break ALL;
				}
			}
		}

		if (completeFlag)
			new AlertDialog.Builder(getContext())
					.setTitle("...")
					.setMessage("游戏结束")
					.setCancelable(false)
					.setPositiveButton("重来",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									startGame();
								}
							}).show();
	}

	public void initGameView() {
		setColumnCount(4);
		setBackgroundColor(Color.BLACK);
		setOnTouchListener(new View.OnTouchListener() {

			private float startX, startY, offsetX, offsetY;

			@Override
			// 返回true，touchmove，touchup 才可用
			public boolean onTouch(View arg0, MotionEvent arg1) {
				addFlag = false;
				switch (arg1.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = arg1.getX();
					startY = arg1.getY();
					break;
				case MotionEvent.ACTION_UP:
					offsetX = arg1.getX() - startX;
					offsetY = arg1.getY() - startY;
					if (Math.abs(offsetX) > Math.abs(offsetY)) {
						if (offsetX < -5) {
							swipeLeft();
						} else if (offsetX > 5) {
							swipeRight();
						}
					} else {
						if (offsetY < -5) {
							swipeUp();
						} else if (offsetY > 5) {
							swipeDown();
						}
					}
					break;
				default:
					break;
				}
				return true;
			}
		});
	}

	private void swipeLeft() {
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				for (int x1 = x + 1; x1 < 4; x1++) {
					if (cards[x1][y].getNum() > 0) {
						if (cards[x][y].getNum() <= 0) {
							cards[x][y].setNum(cards[x1][y].getNum());
							cards[x1][y].setNum(0);
							x--;
							addFlag = true;
						} else if (cards[x][y].equals(cards[x1][y])) {
							cards[x][y].setNum(cards[x][y].getNum() * 2);
							cards[x1][y].setNum(0);
							MainActivity.getMainActivity().addScore(
									cards[x][y].getNum());
							addFlag = true;
						}

						break;
					}

				}
			}
		}
		if (addFlag == true) {
			addRandomNum();
			checkGameOver();
		}
	}

	private void swipeRight() {
		for (int y = 0; y < 4; y++) {
			for (int x = 3; x >= 0; x--) {
				for (int x1 = x - 1; x1 >= 0; x1--) {
					if (cards[x1][y].getNum() > 0) {
						if (cards[x][y].getNum() <= 0) {
							cards[x][y].setNum(cards[x1][y].getNum());
							cards[x1][y].setNum(0);
							x++;
							addFlag = true;
						} else if (cards[x][y].equals(cards[x1][y])) {
							cards[x][y].setNum(cards[x][y].getNum() * 2);
							cards[x1][y].setNum(0);
							MainActivity.getMainActivity().addScore(
									cards[x][y].getNum());
							addFlag = true;
						}

						break;
					}

				}
			}
		}
		if (addFlag == true) {
			addRandomNum();
			checkGameOver();
		}
	}

	private void swipeUp() {
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				for (int y1 = y + 1; y1 < 4; y1++) {
					if (cards[x][y1].getNum() > 0) {
						if (cards[x][y].getNum() <= 0) {
							cards[x][y].setNum(cards[x][y1].getNum());
							cards[x][y1].setNum(0);
							y1++;
							addFlag = true;
						} else if (cards[x][y].getNum() == cards[x][y1]
								.getNum()) {
							cards[x][y].setNum(cards[x][y].getNum() * 2);
							cards[x][y1].setNum(0);
							MainActivity.getMainActivity().addScore(
									cards[x][y].getNum());
							addFlag = true;
						}

						break;
					}
				}
			}
		}
		if (addFlag == true) {
			addRandomNum();
			checkGameOver();
		}
	}

	private void swipeDown() {
		for (int x = 0; x < 4; x++) {
			for (int y = 3; y >= 0; y--) {
				for (int y1 = y - 1; y1 >= 0; y1--) {
					if (cards[x][y1].getNum() > 0) {
						if (cards[x][y].getNum() <= 0) {
							cards[x][y].setNum(cards[x][y1].getNum());
							cards[x][y1].setNum(0);
							y++;
							addFlag = true;
						} else if (cards[x][y].getNum() == cards[x][y1]
								.getNum()) {
							cards[x][y].setNum(cards[x][y].getNum() * 2);
							cards[x][y1].setNum(0);
							MainActivity.getMainActivity().addScore(
									cards[x][y].getNum());
							addFlag = true;
						}

						break;
					}
				}
			}
		}
		if (addFlag == true) {
			addRandomNum();
			checkGameOver();
		}
	}

}
