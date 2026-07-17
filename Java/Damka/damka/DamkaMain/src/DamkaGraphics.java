import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import acm.graphics.GCanvas;
import acm.graphics.GObject;
import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;

public class DamkaGraphics extends GCanvas implements MouseListener {
	public GOval currentKenchi;
	public GOval killer;
	public Ujra ujra;
	public Kenchi kenchi;
	ArrayList<GRect> ujrebi;
	double xpart;
	double ypart;
	Color currentColor;
	public boolean mainPlayersTurn;
	public boolean currentKillingPosition;
	public boolean DcurrentKillingPosition;

	
	public DamkaGraphics() {
		addMouseListener(this);
		ujra = new Ujra();
		kenchi = new Kenchi();
		ujrebi = new ArrayList<>();
		mainPlayersTurn = true;
	}
	

	public void initDisplay() {
		setLocation(0, 0);
		setBackground(Color.CYAN);
		GRect board = new GRect(1, 1);
		board.setSize(720, 720);
		xpart = (double) board.getWidth() / 8;
		ypart = (double) board.getHeight() / 8;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Color color = squareColor(i, j);
				GRect square = new GRect(1, 1);
				square.setSize(xpart, ypart);
				square.setLocation(i * xpart, j * xpart);
				square.setFillColor(color);
				square.setFilled(true);
				add(square);
				ujrebi.add(square);
				ujra.add(square, i, j);
				if ((i + j) % 2 != 0 && j < 3) {
					GOval oval = addKenchi(square, Color.black, xpart, ypart, i, j);
					kenchi.add(oval, i, j);
				}
				if ((i + j) % 2 != 0 && j > 4) {
					GOval oval1 = addKenchi(square, Color.red, xpart, ypart, i, j);
					kenchi.add(oval1, i, j);
				}
			}
		}
	}

	public GOval addKenchi(GRect rect, Color color, double i, double j, int m, int n) {
		GOval oval1 = new GOval(i, j);
		oval1.setLocation(rect.getX(), rect.getY());
		oval1.setFillColor(color);
		oval1.setFilled(true);
		add(oval1);
		return oval1;
	}

	private Color squareColor(int i, int j) {
		if ((i + j) % 2 != 0) {
			return Color.darkGray;
		}
		return Color.white;
	}
	public boolean isBlack(GRect rect) {
		if (rect.getFillColor().equals(Color.white))
			return false;
		return true;
	}

	public boolean isValid(GRect rect, ArrayList<Integer> ovalLoc, Color color) {
		ArrayList<Integer> rectLoc = ujra.getLoc(rect);
		if (Math.abs(rectLoc.get(0) - ovalLoc.get(0)) == 1) {
			if (color.equals(Color.red)) {
				return (rectLoc.get(1) == ovalLoc.get(1) - 1) && isBlack(rect);
			} else {
				return (rectLoc.get(1) == ovalLoc.get(1) + 1) && isBlack(rect);
			}
		}
		return false;
	}

	public boolean isKillable(GRect rect, ArrayList<Integer> ovalLoc, Color color) {
		if (rect == null)
			return false;
		ArrayList<Integer> rectLoc = ujra.getLoc(rect);
		GOval oval1 = kenchi.getKenchi(rectLoc.get(0), rectLoc.get(1));
		if (oval1 != null)
			return false;
		if (isPossibleCandidate(rectLoc, ovalLoc)) {
			GOval oval = kenchi.getKenchi((ovalLoc.get(0) + rectLoc.get(0)) / 2, (ovalLoc.get(1) + rectLoc.get(1)) / 2);
			if (oval == null)
				return false;
			if (oval.getFillColor().equals(color))
				return false;
			return true;
		} else {
			return false;
		}
	}

	public boolean isPossibleCandidate(ArrayList<Integer> rectLoc, ArrayList<Integer> ovalLoc) {
		if (Math.abs(rectLoc.get(0) - ovalLoc.get(0)) == 2 && Math.abs(rectLoc.get(1) - ovalLoc.get(1)) == 2)
			return true;
		return false;
	}

	public boolean killIsPossible(ArrayList<Integer> ovalLoc, Color color) {
		GRect rect1 = new GRect(1, 1);
		GRect rect2 = new GRect(1, 1);
		GRect rect3 = new GRect(1, 1);
		GRect rect4 = new GRect(1, 1);
		rect1 = ujra.getUjra(ovalLoc.get(0) + 2, ovalLoc.get(1) + 2);
		rect2 = ujra.getUjra(ovalLoc.get(0) - 2, ovalLoc.get(1) + 2);
		rect3 = ujra.getUjra(ovalLoc.get(0) + 2, ovalLoc.get(1) - 2);
		rect4 = ujra.getUjra(ovalLoc.get(0) - 2, ovalLoc.get(1) - 2);
		if (isKillable(rect1, ovalLoc, color))
			return true;
		if (isKillable(rect2, ovalLoc, color))
			return true;
		if (isKillable(rect3, ovalLoc, color))
			return true;
		if (isKillable(rect4, ovalLoc, color))
			return true;
		return false;
	}

	public boolean killIsPossibleForPlayer(boolean mainPlayersTurn) {
		ArrayList<GOval> list = new ArrayList<>(kenchi.kenchiMapOval.values());
		if (mainPlayersTurn) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getFillColor().equals(Color.black)) {
					if (killIsPossible(kenchi.getLoc(list.get(i)), list.get(i).getFillColor())) {
						return true;
					}
				}
			}
			return false;
		} else {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getFillColor().equals(Color.red)) {
					if (killIsPossible(kenchi.getLoc(list.get(i)), list.get(i).getFillColor())) {
						return true;
					}
				}
			}
			return false;
		}
	}

	public boolean currentIsKiller() {
		return kenchi.getLoc(currentKenchi).equals(kenchi.getLoc(killer));
	}

	public void play(GRect rect, ArrayList<Integer> rectLoc) {
		kenchi.empty(kenchi.getLoc(currentKenchi));
		currentKenchi.setLocation(rect.getX(), rect.getY());
		currentKenchi.setFillColor(currentColor);
		add(currentKenchi);
		kenchi.add(currentKenchi, rectLoc.get(0), rectLoc.get(1));
		if (rectLoc.get(1) == 0 || rectLoc.get(1) == 7) {
			kenchi.setDamka(currentKenchi);
		}
		currentKillingPosition = killIsPossibleForPlayer(mainPlayersTurn);
		currentKenchi = null;
		mainPlayersTurn = !mainPlayersTurn;
	}

	
	public void kill(GRect rect, ArrayList<Integer> ovalLoc, ArrayList<Integer> rectLoc) {
		kenchi.empty(kenchi.getLoc(currentKenchi));
		currentKenchi.setLocation(rect.getX(), rect.getY());
		GOval oval = kenchi.getKenchi((ovalLoc.get(0) + rectLoc.get(0)) / 2, (ovalLoc.get(1) + rectLoc.get(1)) / 2);
		kenchi.empty(kenchi.getLoc(oval));
		remove(oval);
		currentKenchi.setFillColor(currentColor);
		add(currentKenchi);
		if (killer == null) {
			killer = currentKenchi;
		} else {
			killer.setLocation(rect.getX(), rect.getY());
		}
		kenchi.add(currentKenchi, rectLoc.get(0), rectLoc.get(1));
		// Here is the problem!!!
		currentKillingPosition = killIsPossibleForPlayer(mainPlayersTurn);
		if (!currentKenchi.getColor().equals(Color.yellow)) {
			if ((rectLoc.get(1) == 0 && currentColor.equals(Color.red))
					|| (rectLoc.get(1) == 7 && currentColor.equals(Color.black))) {
				kenchi.setDamka(currentKenchi);
			}
		}
		currentKenchi = null;
	}

	private void GOvalInteractions(GObject obj) {
		if (currentKenchi != null) {
			GOval oval = (GOval) obj;
			if (currentKenchi.equals(oval)) {
				oval.setFillColor(currentColor);
				currentKenchi = null;
			} else {
				currentKenchi.setFillColor(currentColor);
				currentColor = oval.getFillColor();
				currentKenchi = oval;
				currentKenchi.setFillColor(Color.green);
			}
		} else {
			GOval kenchi1 = (GOval) obj;
			currentKenchi = kenchi1;
			currentColor = kenchi1.getFillColor();
			kenchi1.setFillColor(Color.green);
		}
	}

	private void mainPlayersInteractions(GRect rect, ArrayList<Integer> rectLoc, ArrayList<Integer> ovalLoc) {
		if (currentColor.equals(Color.red)) {
			if (killer != null) {
				if (currentIsKiller() && isKillable(rect, kenchi.getLoc(killer), killer.getFillColor())) {
					kill(rect, ovalLoc, rectLoc);
					if (!killIsPossible(kenchi.getLoc(killer), killer.getFillColor())) {
						killer = null;
						mainPlayersTurn = !mainPlayersTurn;
					}
				}
			} else {
				if (currentKillingPosition) {
					if (isKillable(rect, kenchi.getLoc(currentKenchi), currentColor)) {
						kill(rect, ovalLoc, rectLoc);
						if (!killIsPossible(kenchi.getLoc(killer), killer.getFillColor())) {
							killer = null;
							mainPlayersTurn = !mainPlayersTurn;
						}
					}
				} else {
					if (isValid(rect, ovalLoc, currentColor)) {
						play(rect, rectLoc);
					}
				}
			}
		}
	}

	private void secondPlayersInteractions(GRect rect, ArrayList<Integer> rectLoc, ArrayList<Integer> ovalLoc) {
		if (currentColor.equals(Color.black)) {
			if (killer != null) {
				if (currentIsKiller() && isKillable(rect, kenchi.getLoc(killer), killer.getFillColor())) {
					kill(rect, ovalLoc, rectLoc);
					if (!killIsPossible(kenchi.getLoc(killer), killer.getFillColor())) {
						killer = null;
						mainPlayersTurn = !mainPlayersTurn;
					}
				}
			} else {
				if (currentKillingPosition) {
					if (isKillable(rect, kenchi.getLoc(currentKenchi), currentColor)) {
						kill(rect, ovalLoc, rectLoc);
						if (!killIsPossible(kenchi.getLoc(killer), killer.getFillColor())) {
							killer = null;
							mainPlayersTurn = !mainPlayersTurn;
						}
					}
				} else {
					if (isValid(rect, ovalLoc, currentColor)) {
						play(rect, rectLoc);
					}
				}
			}
		}
	}

	private boolean DisValid(GRect rect, ArrayList<Integer> ovalLoc, Color color) {
		ArrayList<Integer> rectLoc = ujra.getLoc(rect);
		int x = Math.abs(rectLoc.get(0) - ovalLoc.get(0));
		int y = Math.abs(rectLoc.get(1) - ovalLoc.get(1));
		if (!isEmptyBetween(rect, ovalLoc)) {
			return false;
		}
		return x == y;
	}

	private boolean isEmptyBetween(GRect rect, ArrayList<Integer> ovalLoc) {
		ArrayList<Integer> rectLoc = ujra.getLoc(rect);
		int x1 = ovalLoc.get(0);
		int x2 = rectLoc.get(0);
		int y1 = ovalLoc.get(1);
		int y2 = rectLoc.get(1);
		int x = x2 - x1;
		int y = y2 - y1;
		if (x < 0) {
			int x3 = x1;
			x1 = x2;
			x2 = x3;
		}
		if (y < 0) {
			int y3 = y1;
			y1 = y2;
			y2 = y3;
		}
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if ((i > x1 && i < x2) && (j > y1 && j < y2)) {
					GOval oval = kenchi.getKenchi(i, j);
					if (oval != null) {
						return false;
					}
				}
			}
		}
		return true;
	}
	/*
	 * Since killing methods were made a little permanent, I will leave this
	 * program as it is and safely declare that Damka as a playable game is
	 * finished Extra features could be added later. And the original damka will
	 * be written is C++, In summer. Yes, this is the mission. I made the java
	 * version and The java version is the Damka which is widely accesible in
	 * the world. But in C++, Damka which will be made is one, which is normally
	 * played in my country. All this will be done in summer. For now, game is
	 * finished! This was a great journey!.. 
	 */

	private void DmainPlayersInteractions(GRect rect, ArrayList<Integer> rectLoc, ArrayList<Integer> ovalLoc) {
		if (currentColor.equals(Color.red)) {
			if (killer != null) {
				if (currentIsKiller() && isKillable(rect, kenchi.getLoc(killer), killer.getFillColor())) {
					kill(rect, ovalLoc, rectLoc);
					if (!killIsPossible(kenchi.getLoc(killer), killer.getFillColor())) {
						killer = null;
						mainPlayersTurn = !mainPlayersTurn;
					}
				}
			} else {
				if (currentKillingPosition) {
					if (isKillable(rect, kenchi.getLoc(currentKenchi), currentColor)) {
						kill(rect, ovalLoc, rectLoc);
						if (!killIsPossible(kenchi.getLoc(killer), killer.getFillColor())) {
							killer = null;
							mainPlayersTurn = !mainPlayersTurn;
						}
					}
				} else {
					if (DisValid(rect, ovalLoc, currentColor)) {
						play(rect, rectLoc);
					}
				}
			}
		}
	}

	private void DsecondPlayersInteractions(GRect rect, ArrayList<Integer> rectLoc, ArrayList<Integer> ovalLoc) {
		if (currentColor.equals(Color.black)) {
			if (killer != null) {
				if (currentIsKiller() && isKillable(rect, kenchi.getLoc(killer), killer.getFillColor())) {
					kill(rect, ovalLoc, rectLoc);
					if (!killIsPossible(kenchi.getLoc(killer), killer.getFillColor())) {
						killer = null;
						mainPlayersTurn = !mainPlayersTurn;
					}
				}
			} else {
				if (currentKillingPosition) {
					if (isKillable(rect, kenchi.getLoc(currentKenchi), currentColor)) {
						kill(rect, ovalLoc, rectLoc);
						if (!killIsPossible(kenchi.getLoc(killer), killer.getFillColor())) {
							killer = null;
							mainPlayersTurn = !mainPlayersTurn;
						}
					}
				} else {
					if (DisValid(rect, ovalLoc, currentColor)) {
						play(rect, rectLoc);
					}
				}
			}
		}
	}

	// No new class or methods need to be added
	// I will add additional methods here
	@Override
	public void mouseClicked(MouseEvent e) {
		GObject obj = getElementAt(e.getX(), e.getY());
		if (obj != null) {
			if (obj instanceof GOval) {
				GOvalInteractions(obj);
			} else if (obj instanceof GRect) {
				if (currentKenchi != null) {
					GRect rect = (GRect) obj;
					ArrayList<Integer> ovalLoc = kenchi.getLoc(currentKenchi);
					ArrayList<Integer> rectLoc = ujra.getLoc(rect);
					if (mainPlayersTurn) {
						if (!currentKenchi.getColor().equals(Color.yellow)) {
							mainPlayersInteractions(rect, rectLoc, ovalLoc);
						} else {
							DmainPlayersInteractions(rect, rectLoc, ovalLoc);
						}
					} else if (!mainPlayersTurn) {
						if (!currentKenchi.getColor().equals(Color.yellow)) {
							secondPlayersInteractions(rect, rectLoc, ovalLoc);
						} else {
							DsecondPlayersInteractions(rect, rectLoc, ovalLoc);
						}
					}
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
