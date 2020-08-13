package controller;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.text.Font;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.sun.prism.paint.Color;

import model.cards.Card;
import model.cards.minions.Minion;
import model.cards.spells.AOESpell;
import model.cards.spells.FieldSpell;
import model.cards.spells.HeroTargetSpell;
import model.cards.spells.LeechingSpell;
import model.cards.spells.MinionTargetSpell;
import model.cards.spells.Spell;
import model.heroes.Hero;
import model.heroes.Hunter;
import model.heroes.Mage;
import model.heroes.Paladin;
import model.heroes.Priest;
import model.heroes.Warlock;
import engine.Game;
import engine.GameListener;
import exceptions.CannotAttackException;
import exceptions.FullFieldException;
import exceptions.FullHandException;
import exceptions.HeroPowerAlreadyUsedException;
import exceptions.InvalidTargetException;
import exceptions.NotEnoughManaException;
import exceptions.NotSummonedException;
import exceptions.NotYourTurnException;
import exceptions.TauntBypassException;
import view.ImagePanel;
import view.View;

public class Controller implements ActionListener, GameListener {
	private View view;
	private Game model;
	private Hero firstHero;
	private Hero secondHero;
	boolean fourthView;
	JButton opponentDetails;
	JButton opponentIcon;
	JButton opponentDeck;
	JButton currentDeck;
	JButton currentIcon;
	JButton currentDetails;
	boolean heroPowerInitiated;
	Spell spellHolder = null;
	Minion minionHolder = null;

	ArrayList<JButton> currentFieldArray = new ArrayList<JButton>();
	ArrayList<JButton> opponentFieldArray = new ArrayList<JButton>();
	ArrayList<JButton> currentHandArray = new ArrayList<JButton>();
	ArrayList<JButton> opponentHandArray = new ArrayList<JButton>();

	public Controller() {

		view = new View();
		view.getWelcome().addActionListener(this);

		view.revalidate();
		view.repaint();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		view.playSound("sounds/Clicks.wav").start();
		if (e.getActionCommand().equals("PLAY NOW!")) {
			view.secondView();
			for (int i = 0; i < view.getHerosButtons().size(); i++) {
				view.getHerosButtons().get(i).addActionListener(this);
			}
		} else if (view.getHerosButtons().contains(e.getSource())) {
			try {

				setHeros(e.getActionCommand());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (CloneNotSupportedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FullHandException e1) {
				String outString = stringCardDetails((Card) (e1.getBurned()),
						false);
				JOptionPane.showMessageDialog(
						(Component) e.getSource(),
						"Your hand is already full!\n"
								+ ("<html><font color ='black'" + outString
										.substring(25, outString.length())),
						"FULL HAND!", JOptionPane.INFORMATION_MESSAGE);
			}
		} else {
			if (e.getActionCommand().equals("endTurn")) {
				try {
					model.endTurn();
				} catch (FullHandException e1) {
					String outString = stringCardDetails(
							(Card) (e1.getBurned()), false);
					JOptionPane
							.showMessageDialog(
									(Component) e.getSource(),
									"Your hand is already full!\n"
											+ ("<html><font color ='black'" + outString
													.substring(25,
															outString.length())),
									"FULL HAND!",
									JOptionPane.INFORMATION_MESSAGE);

				} catch (CloneNotSupportedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else if (e.getActionCommand().equals("useHeroPower")) {
				try {
					Hero h1 = model.getCurrentHero();
					if (h1 instanceof Hunter) {
						((Hunter) h1).useHeroPower();
					} else if (h1 instanceof Paladin) {
						((Paladin) h1).useHeroPower();
					} else if (h1 instanceof Warlock) {
						((Warlock) h1).useHeroPower();
					} else {
						heroPowerInitiated = true;
					}

				} catch (NotEnoughManaException e1) {
					JOptionPane.showMessageDialog((Component) e.getSource(),
							"You do not have enough Mana Crystals!",
							"NOT ENOUGH MANA CRYSTALS!",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (HeroPowerAlreadyUsedException e1) {
					JOptionPane.showMessageDialog((Component) e.getSource(),
							"You have already used your Hero Power!",
							"HERO POWER ALREADY USED",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (NotYourTurnException e1) {
					JOptionPane.showMessageDialog((Component) e.getSource(),
							"Please wait for your turn!", "NOT YOUR TURN!",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (FullHandException e1) {
					String outString = stringCardDetails(
							(Card) (e1.getBurned()), false);
					JOptionPane
							.showMessageDialog(
									(Component) e.getSource(),
									"Your hand is already full!\n"
											+ ("<html><font color ='black'" + outString
													.substring(25,
															outString.length())),
									"FULL HAND!",
									JOptionPane.INFORMATION_MESSAGE);
				} catch (FullFieldException e1) {
					JOptionPane.showMessageDialog((Component) e.getSource(),
							"Your field is already full!", "FULL FIELD!",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (CloneNotSupportedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else if (heroPowerInitiated) {
				Hero h2 = null;
				Minion m2 = null;
				boolean correctMinion = false;
				boolean correctHero = false;
				if (e.getActionCommand().equals("currentHero")) {
					h2 = model.getCurrentHero();
					correctHero = true;
				} else if (e.getActionCommand().equals("opponentHero")) {
					h2 = model.getOpponent();
					correctHero = true;
				} else {
					int i = 0;
					if (currentHandArray.indexOf(e.getSource()) != -1) {
						i = currentHandArray.indexOf(e.getSource());
						if (model.getCurrentHero().getHand().get(i) instanceof Minion) {
							m2 = (Minion) model.getCurrentHero().getHand()
									.get(i);
							correctMinion = true;
						}
					} else if (opponentHandArray.indexOf(e.getSource()) != -1) {
						i = opponentHandArray.indexOf(e.getSource());
						if (model.getOpponent().getHand().get(i) instanceof Minion) {
							m2 = (Minion) model.getOpponent().getHand().get(i);
							correctMinion = true;
						}
					} else if (currentFieldArray.indexOf(e.getSource()) != -1) {
						i = currentFieldArray.indexOf(e.getSource());
						if (model.getCurrentHero().getField().get(i) instanceof Minion) {
							correctMinion = true;
							m2 = (Minion) model.getCurrentHero().getField()
									.get(i);
						}
					} else if (opponentFieldArray.indexOf(e.getSource()) != -1) {
						i = opponentFieldArray.indexOf(e.getSource());
						if (model.getOpponent().getField().get(i) instanceof Minion) {
							m2 = (Minion) model.getOpponent().getField().get(i);
							correctMinion = true;
						}
					}
				}
				if (correctMinion) {
					heroPowerInitiated = false;
					if (model.getCurrentHero() instanceof Mage) {
						try {
							((Mage) model.getCurrentHero()).useHeroPower(m2);
						} catch (NotEnoughManaException e1) {
							JOptionPane.showMessageDialog(
									(Component) e.getSource(),
									"You do not have enough Mana Crystals!",
									"NOT ENOUGH MANA CRYSTALS!",
									JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						} catch (HeroPowerAlreadyUsedException e1) {
							JOptionPane.showMessageDialog(
									(Component) e.getSource(),
									"You have already used your Hero Power!",
									"HERO POWER ALREADY USED",
									JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						} catch (NotYourTurnException e1) {
							JOptionPane.showMessageDialog(
									(Component) e.getSource(),
									"Please wait for your turn!",
									"NOT YOUR TURN!",
									JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						} catch (FullHandException e1) {
							String outString = stringCardDetails(
									(Card) (e1.getBurned()), false);
							JOptionPane
									.showMessageDialog(
											(Component) e.getSource(),
											"Your hand is already full!\n"
													+ ("<html><font color ='black'" + outString
															.substring(
																	25,
																	outString
																			.length())),
											"FULL HAND!",
											JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						} catch (FullFieldException e1) {
							JOptionPane.showMessageDialog(
									(Component) e.getSource(),
									"Your field is already full!",
									"FULL FIELD!",
									JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						} catch (CloneNotSupportedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else if (model.getCurrentHero() instanceof Priest) {
						try {
							((Priest) model.getCurrentHero()).useHeroPower(m2);
						} catch (NotEnoughManaException e1) {
							JOptionPane.showMessageDialog(
									(Component) e.getSource(),
									"You do not have enough Mana Crystals!",
									"NOT ENOUGH MANA CRYSTALS!",
									JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						} catch (HeroPowerAlreadyUsedException e1) {
							JOptionPane.showMessageDialog(
									(Component) e.getSource(),
									"You have already used your Hero Power!",
									"HERO POWER ALREADY USED",
									JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						} catch (NotYourTurnException e1) {
							JOptionPane.showMessageDialog(
									(Component) e.getSource(),
									"Please wait for your turn!",
									"NOT YOUR TURN!",
									JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						} catch (FullHandException e1) {
							String outString = stringCardDetails(
									(Card) (e1.getBurned()), false);
							JOptionPane
									.showMessageDialog(
											(Component) e.getSource(),
											"Your hand is already full!\n"
													+ ("<html><font color ='black'" + outString
															.substring(
																	25,
																	outString
																			.length())),
											"FULL HAND!",
											JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						} catch (FullFieldException e1) {
							JOptionPane.showMessageDialog(
									(Component) e.getSource(),
									"Your field is already full!",
									"FULL FIELD!",
									JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						} catch (CloneNotSupportedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				} else if (correctHero) {
					heroPowerInitiated = false;
					if (model.getCurrentHero() instanceof Mage) {
						try {
							((Mage) model.getCurrentHero()).useHeroPower(h2);
						} catch (NotEnoughManaException e1) {
							JOptionPane.showMessageDialog(
									(Component) e.getSource(),
									"You do not have enough Mana Crystals!",
									"NOT ENOUGH MANA CRYSTALS!",
									JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						} catch (HeroPowerAlreadyUsedException e1) {
							JOptionPane.showMessageDialog(
									(Component) e.getSource(),
									"You have already used your Hero Power!",
									"HERO POWER ALREADY USED",
									JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						} catch (NotYourTurnException e1) {
							JOptionPane.showMessageDialog(
									(Component) e.getSource(),
									"Please wait for your turn!",
									"NOT YOUR TURN!",
									JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						} catch (FullHandException e1) {
							String outString = stringCardDetails(
									(Card) (e1.getBurned()), false);
							JOptionPane
									.showMessageDialog(
											(Component) e.getSource(),
											"Your hand is already full!\n"
													+ ("<html><font color ='black'" + outString
															.substring(
																	25,
																	outString
																			.length())),
											"FULL HAND!",
											JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						} catch (FullFieldException e1) {
							JOptionPane.showMessageDialog(
									(Component) e.getSource(),
									"Your field is already full!",
									"FULL FIELD!",
									JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						} catch (CloneNotSupportedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else if (model.getCurrentHero() instanceof Priest) {
						try {
							((Priest) model.getCurrentHero()).useHeroPower(h2);
						} catch (NotEnoughManaException e1) {
							JOptionPane.showMessageDialog(
									(Component) e.getSource(),
									"You do not have enough Mana Crystals!",
									"NOT ENOUGH MANA CRYSTALS!",
									JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						} catch (HeroPowerAlreadyUsedException e1) {
							JOptionPane.showMessageDialog(
									(Component) e.getSource(),
									"You have already used your Hero Power!",
									"HERO POWER ALREADY USED",
									JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						} catch (NotYourTurnException e1) {
							JOptionPane.showMessageDialog(
									(Component) e.getSource(),
									"Please wait for your turn!",
									"NOT YOUR TURN!",
									JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						} catch (FullHandException e1) {
							String outString = stringCardDetails(
									(Card) (e1.getBurned()), false);
							JOptionPane
									.showMessageDialog(
											(Component) e.getSource(),
											"Your hand is already full!\n"
													+ ("<html><font color ='black'" + outString
															.substring(
																	25,
																	outString
																			.length())),
											"FULL HAND!",
											JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						} catch (FullFieldException e1) {
							JOptionPane.showMessageDialog(
									(Component) e.getSource(),
									"Your field is already full!",
									"FULL FIELD!",
									JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						} catch (CloneNotSupportedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				} else {
					JOptionPane.showMessageDialog((Component) e.getSource(),
							"Incorrect Card Chosen",
							"Please choose either a Hero or a Minion!",
							JOptionPane.INFORMATION_MESSAGE);
				}

			} else if (minionHolder != null) {
				int i = 0;
				Minion m2 = null;
				Hero h2 = null;
				if (e.getActionCommand().equals("currentHero")) {
					h2 = model.getCurrentHero();

				} else if (e.getActionCommand().equals("opponentHero")) {
					h2 = model.getOpponent();

				} else if (currentHandArray.indexOf(e.getSource()) != -1) {
					i = currentHandArray.indexOf(e.getSource());
					if (model.getCurrentHero().getHand().get(i) instanceof Minion) {
						m2 = (Minion) model.getCurrentHero().getHand().get(i);
					}
				} else if (opponentHandArray.indexOf(e.getSource()) != -1) {
					i = opponentHandArray.indexOf(e.getSource());
					if (model.getOpponent().getHand().get(i) instanceof Minion) {
						m2 = (Minion) model.getOpponent().getHand().get(i);

					}
				} else if (currentFieldArray.indexOf(e.getSource()) != -1) {
					i = currentFieldArray.indexOf(e.getSource());
					if (model.getCurrentHero().getField().get(i) instanceof Minion) {

						m2 = (Minion) model.getCurrentHero().getField().get(i);
					}
				} else if (opponentFieldArray.indexOf(e.getSource()) != -1) {
					i = opponentFieldArray.indexOf(e.getSource());
					if (model.getOpponent().getField().get(i) instanceof Minion) {
						m2 = (Minion) model.getOpponent().getField().get(i);
					}
				}
				if (m2 != null) {

						try {
							model.getCurrentHero().attackWithMinion(
									minionHolder, m2);
						} catch (CannotAttackException e1) {
							JOptionPane.showMessageDialog((Component) e.getSource(),
									"Your minion can not attack!",
									" CAN NOT ATTACK!",
									JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						} catch (NotYourTurnException e1) {
							JOptionPane.showMessageDialog((Component) e.getSource(),
									"Please wait for your turn!",
									" NOT YOUR TURN!",
									JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						} catch (TauntBypassException e1) {
							JOptionPane.showMessageDialog((Component) e.getSource(),
									"You can not attack your target while your opponent has tanunt minion(s) in their field! ",
									" YOUR OPPONENT HAS TAUNT!",
									JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						} catch (InvalidTargetException e1) {
							JOptionPane.showMessageDialog((Component) e.getSource(),
									"Please choose a valid target to attack!",
									" INVALID TARGET!",
									JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						} catch (NotSummonedException e1) {
							JOptionPane.showMessageDialog((Component) e.getSource(),
									"This minion is not yet summoned to the field",
									" MINION NOT SUMMONED!",
									JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						}

					} else if (h2 != null) {
						try {
							model.getCurrentHero().attackWithMinion(
									minionHolder, h2);
						} catch (CannotAttackException e1) {
							JOptionPane.showMessageDialog((Component) e.getSource(),
									"Your minion can not attack!",
									" CAN NOT ATTACK!",
									JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						} catch (NotYourTurnException e1) {
							JOptionPane.showMessageDialog((Component) e.getSource(),
									"Please wait for your turn!",
									" NOT YOUR TURN!",
									JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						} catch (TauntBypassException e1) {
							JOptionPane.showMessageDialog((Component) e.getSource(),
									"You can not attack your target while your opponent has tanunt minion(s) in their field! ",
									" YOUR OPPONENT HAS TAUNT!",
									JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						} catch (NotSummonedException e1) {
							JOptionPane.showMessageDialog((Component) e.getSource(),
									"This minion is not yet summoned to the field",
									" MINION NOT SUMMONED!",
									JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						} catch (InvalidTargetException e1) {
							JOptionPane.showMessageDialog((Component) e.getSource(),
									"Please choose a valid target to attack!",
									" INVALID TARGET!",
									JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						}
					} else {
						JOptionPane
								.showMessageDialog(
										(Component) e.getSource(),
										"DO NOT CHOOSE A SPELL!",
										"Please choose a minion or a hero to be attacked.",
										JOptionPane.INFORMATION_MESSAGE);

					}
					minionHolder = null;
				} else if (spellHolder != null) {
				int i = 0;
				Minion m2 = null;
				Hero h2 = null;
				if (e.getActionCommand().equals("currentHero")) {
					h2 = model.getCurrentHero();

				} else if (e.getActionCommand().equals("opponentHero")) {
					h2 = model.getOpponent();

				} else if (currentHandArray.indexOf(e.getSource()) != -1) {
					i = currentHandArray.indexOf(e.getSource());
					if (model.getCurrentHero().getHand().get(i) instanceof Minion) {
						m2 = (Minion) model.getCurrentHero().getHand().get(i);
					}
				} else if (opponentHandArray.indexOf(e.getSource()) != -1) {
					i = opponentHandArray.indexOf(e.getSource());
					if (model.getOpponent().getHand().get(i) instanceof Minion) {
						m2 = (Minion) model.getOpponent().getHand().get(i);

					}
				} else if (currentFieldArray.indexOf(e.getSource()) != -1) {
					i = currentFieldArray.indexOf(e.getSource());
					if (model.getCurrentHero().getField().get(i) instanceof Minion) {

						m2 = (Minion) model.getCurrentHero().getField().get(i);
					}
				} else if (opponentFieldArray.indexOf(e.getSource()) != -1) {
					i = opponentFieldArray.indexOf(e.getSource());
					if (model.getOpponent().getField().get(i) instanceof Minion) {
						m2 = (Minion) model.getOpponent().getField().get(i);

					}
				}
				if ((spellHolder instanceof MinionTargetSpell) && m2 != null) {
					try {
						model.getCurrentHero().castSpell(
								(MinionTargetSpell) spellHolder, m2);
					} catch (NotYourTurnException e1) {
						JOptionPane.showMessageDialog((Component) e.getSource(),
								"Please wait for your turn!", "NOT YOUR TURN!",
								JOptionPane.INFORMATION_MESSAGE);
						e1.printStackTrace();
					} catch (NotEnoughManaException e1) {
						JOptionPane.showMessageDialog((Component) e.getSource(),
								"You do not have enough Mana Crystals!",
								"NOT ENOUGH MANA CRYSTALS!",
								JOptionPane.INFORMATION_MESSAGE);
						e1.printStackTrace();
					} catch (InvalidTargetException e1) {
						JOptionPane.showMessageDialog((Component) e.getSource(),
								"Please choose a valid target to attack!",
								" INVALID TARGET!",
								JOptionPane.INFORMATION_MESSAGE);
						e1.printStackTrace();
					}
				} else if ((spellHolder instanceof HeroTargetSpell)
						&& h2 != null) {
					try {
						model.getCurrentHero().castSpell(
								(HeroTargetSpell) spellHolder, h2);
					} catch (NotYourTurnException e1) {
						JOptionPane.showMessageDialog((Component) e.getSource(),
								"Please wait for your turn!", "NOT YOUR TURN!",
								JOptionPane.INFORMATION_MESSAGE);
						e1.printStackTrace();
					} catch (NotEnoughManaException e1) {
						JOptionPane.showMessageDialog((Component) e.getSource(),
								"You do not have enough Mana Crystals!",
								"NOT ENOUGH MANA CRYSTALS!",
								JOptionPane.INFORMATION_MESSAGE);
						e1.printStackTrace();
					}
				} else if ((spellHolder instanceof LeechingSpell) && m2 != null) {
					try {
						model.getCurrentHero().castSpell(
								(LeechingSpell) spellHolder, m2);
					} catch (NotYourTurnException e1) {
						JOptionPane.showMessageDialog((Component) e.getSource(),
								"Please wait for your turn!", "NOT YOUR TURN!",
								JOptionPane.INFORMATION_MESSAGE);
						e1.printStackTrace();
					} catch (NotEnoughManaException e1) {
						JOptionPane.showMessageDialog((Component) e.getSource(),
								"You do not have enough Mana Crystals!",
								"NOT ENOUGH MANA CRYSTALS!",
								JOptionPane.INFORMATION_MESSAGE);
						e1.printStackTrace();
					}
				} else {
					JOptionPane
							.showMessageDialog(
									(Component) e.getSource(),
									"INVALID COMBINATION!",
									"Please choose a minion when casting a Leeching Spell of a Minion Target Spell, or a hero when casting a Hero Specific Spell!",
									JOptionPane.INFORMATION_MESSAGE);
				}

				spellHolder = null;

			} else if (currentHandArray.indexOf(e.getSource()) != -1) {
				Card c = model.getCurrentHero().getHand()
						.get(currentHandArray.indexOf(e.getSource()));
				if (c instanceof Minion) {
					try {
						model.getCurrentHero().playMinion((Minion) c);
					} catch (NotYourTurnException e1) {
						JOptionPane.showMessageDialog((Component) e.getSource(),
								"Please wait for your turn!", "NOT YOUR TURN!",
								JOptionPane.INFORMATION_MESSAGE);
						e1.printStackTrace();
					} catch (NotEnoughManaException e1) {
						JOptionPane.showMessageDialog(
								(Component) e.getSource(),
								"You do not have enough Mana Crystals!",
								"NOT ENOUGH MANA CRYSTALS!",
								JOptionPane.INFORMATION_MESSAGE);
						e1.printStackTrace();
					} catch (FullFieldException e1) {
						JOptionPane.showMessageDialog(
								(Component) e.getSource(),
								"Your field is already full!", "FULL FIELD!",
								JOptionPane.INFORMATION_MESSAGE);
						e1.printStackTrace();
					}
				}

				if (c instanceof Spell) { // cast spell
					if (c instanceof FieldSpell) {
						try {
							model.getCurrentHero().castSpell((FieldSpell) c);
						} catch (NotYourTurnException e1) {
							JOptionPane.showMessageDialog((Component) e.getSource(),
									"Please wait for your turn!", "NOT YOUR TURN!",
									JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						} catch (NotEnoughManaException e1) {
							JOptionPane.showMessageDialog((Component) e.getSource(),
									"You do not have enough Mana Crystals!",
									"NOT ENOUGH MANA CRYSTALS!",
									JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						}
					} else if (c instanceof AOESpell) {
						try {
							model.getCurrentHero().castSpell((AOESpell) c,
									model.getOpponent().getField());
						} catch (NotYourTurnException e1) {
							JOptionPane.showMessageDialog((Component) e.getSource(),
									"Please wait for your turn!", "NOT YOUR TURN!",
									JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						} catch (NotEnoughManaException e1) {
							JOptionPane.showMessageDialog((Component) e.getSource(),
									"You do not have enough Mana Crystals!",
									"NOT ENOUGH MANA CRYSTALS!",
									JOptionPane.INFORMATION_MESSAGE);
							e1.printStackTrace();
						}
					} else {
						spellHolder = (Spell) c;
					}
				}
			} else if (currentFieldArray.indexOf(e.getSource()) != -1) {
				minionHolder = (Minion) model.getCurrentHero().getField()
						.get(currentFieldArray.indexOf(e.getSource()));
			}
			
			if (fourthView){
				this.updatePlayerDetails();
			}
		}
				

		}


	

	public void setHeros(String s) throws IOException,
			CloneNotSupportedException, FullHandException {
		Hero h = null;
		switch (s.charAt(0)) {
		case '1':
			h = new Hunter();
			break;
		case '2':
			h = new Mage();
			break;
		case '3':
			h = new Paladin();
			break;
		case '4':
			h = new Priest();
			break;
		case '5':
			h = new Warlock();
			break;
		}
		if (this.firstHero == null) {
			this.firstHero = h;
			view.thirdView();
			for (int i = 0; i < view.getHerosButtons().size(); i++) {
				view.getHerosButtons().get(i).addActionListener(this);
			}

		} else {
			this.secondHero = h;
			model = new Game(firstHero, secondHero);
			view.fourthView();
			fourthView = true;
			view.getEndTurn().addActionListener(this);
			view.getUseHeroPower().addActionListener(this);
			IntializePlayerDetails();
		}

	}

	public void IntializePlayerDetails() {
		opponentDetails = new JButton(model.getOpponent().toString());
		opponentDetails.setHorizontalTextPosition(JButton.CENTER);
		opponentDetails.setVerticalTextPosition(JButton.CENTER);
		opponentDetails.setOpaque(false);
		opponentDetails.setContentAreaFilled(false);
		opponentDetails.setBorderPainted(false);

		view.getPlayerDetails().add(opponentDetails);
		view.getPlayerDetails().add(Box.createVerticalGlue());

		opponentIcon = new JButton();
		if (model.getOpponent() instanceof Hunter)
			opponentIcon.setIcon(new ImageIcon("img/rexxar icon.png"));
		else if (model.getOpponent() instanceof Mage)
			opponentIcon.setIcon(new ImageIcon("img/Mage icon.png"));
		else if (model.getOpponent() instanceof Paladin)
			opponentIcon.setIcon(new ImageIcon("img/Paladin icon.png"));
		else if (model.getOpponent() instanceof Priest)
			opponentIcon.setIcon(new ImageIcon("img/Priest icon.png"));
		else if (model.getOpponent() instanceof Warlock)
			opponentIcon.setIcon(new ImageIcon("img/warlock icon.png"));
		opponentIcon.setOpaque(false);
		opponentIcon.setContentAreaFilled(false);
		opponentIcon.setBorderPainted(false);
		opponentIcon.setActionCommand("opponentHero");
		opponentIcon.addActionListener(this);

		view.getPlayerDetails().add(opponentIcon);
		view.getPlayerDetails().add(Box.createVerticalGlue());

		String opponentDeckNumber = "<html><font color='white' size = 16>"
				+ model.getOpponent().getDeck().size() + "</font></html>";
		opponentDeck = new JButton();
		opponentDeck.setIcon(new ImageIcon("img/card-1.jpg"));
		opponentDeck.setText(opponentDeckNumber);
		opponentDeck.setHorizontalTextPosition(JButton.CENTER);
		opponentDeck.setVerticalTextPosition(JButton.TOP);

		opponentDeck.setOpaque(false);
		opponentDeck.setContentAreaFilled(false);
		opponentDeck.setBorderPainted(false);
		view.getPlayerDetails().add(opponentDeck);
		view.getPlayerDetails().add(Box.createVerticalGlue());

		String currentDeckNumber = "<html><font color='white' size = 16>"
				+ model.getCurrentHero().getDeck().size() + "</font></html>";
		currentDeck = new JButton();

		currentDeck.setActionCommand("current Deck");
		currentDeck.addActionListener(this);
		currentDeck.setIcon(new ImageIcon("img/card-1.jpg"));
		currentDeck.setText(currentDeckNumber);
		currentDeck.setHorizontalTextPosition(JButton.CENTER);
		currentDeck.setVerticalTextPosition(JButton.BOTTOM);

		currentDeck.setOpaque(false);
		currentDeck.setContentAreaFilled(false);
		currentDeck.setBorderPainted(false);
		view.getPlayerDetails().add(currentDeck);
		view.getPlayerDetails().add(Box.createVerticalGlue());

		currentIcon = new JButton();
		if (model.getCurrentHero() instanceof Hunter)
			currentIcon.setIcon(new ImageIcon("img/rexxar icon.png"));
		else if (model.getCurrentHero() instanceof Mage)
			currentIcon.setIcon(new ImageIcon("img/Mage icon.png"));
		else if (model.getCurrentHero() instanceof Paladin)
			currentIcon.setIcon(new ImageIcon("img/Paladin icon.png"));
		else if (model.getCurrentHero() instanceof Priest)
			currentIcon.setIcon(new ImageIcon("img/Priest icon.png"));
		else if (model.getCurrentHero() instanceof Warlock)
			currentIcon.setIcon(new ImageIcon("img/warlock icon.png"));
		currentIcon.setOpaque(false);
		currentIcon.setContentAreaFilled(false);
		currentIcon.setBorderPainted(false);
		currentIcon.setActionCommand("currentHero");
		currentIcon.addActionListener(this);

		view.getPlayerDetails().add(currentIcon);
		view.getPlayerDetails().add(Box.createVerticalGlue());

		currentDetails = new JButton(model.getCurrentHero().toString());
		currentDetails.setHorizontalTextPosition(JButton.CENTER);
		currentDetails.setVerticalTextPosition(JButton.CENTER);
		currentDetails.setOpaque(false);
		currentDetails.setContentAreaFilled(false);
		currentDetails.setBorderPainted(false);

		view.getPlayerDetails().add(currentDetails);
		view.getPlayerDetails().add(Box.createVerticalGlue());

		this.updateFieldAndHand();

		view.revalidate();
		view.repaint();
	}

	public void updatePlayerDetails() {
		this.opponentDetails.setText(model.getOpponent().toString());
		opponentDetails.setHorizontalTextPosition(JButton.CENTER);
		opponentDetails.setVerticalTextPosition(JButton.CENTER);

		if (model.getOpponent() instanceof Hunter)
			opponentIcon.setIcon(new ImageIcon("img/rexxar icon.png"));
		else if (model.getOpponent() instanceof Mage)
			opponentIcon.setIcon(new ImageIcon("img/Mage icon.png"));
		else if (model.getOpponent() instanceof Paladin)
			opponentIcon.setIcon(new ImageIcon("img/Paladin icon.png"));
		else if (model.getOpponent() instanceof Priest)
			opponentIcon.setIcon(new ImageIcon("img/Priest icon.png"));
		else if (model.getOpponent() instanceof Warlock)
			opponentIcon.setIcon(new ImageIcon("img/warlock icon.png"));

		if (model.getCurrentHero() instanceof Hunter)
			currentIcon.setIcon(new ImageIcon("img/rexxar icon.png"));
		else if (model.getCurrentHero() instanceof Mage)
			currentIcon.setIcon(new ImageIcon("img/Mage icon.png"));
		else if (model.getCurrentHero() instanceof Paladin)
			currentIcon.setIcon(new ImageIcon("img/Paladin icon.png"));
		else if (model.getCurrentHero() instanceof Priest)
			currentIcon.setIcon(new ImageIcon("img/Priest icon.png"));
		else if (model.getCurrentHero() instanceof Warlock)
			currentIcon.setIcon(new ImageIcon("img/warlock icon.png"));

		currentDetails.setText(model.getCurrentHero().toString());
		currentDetails.setHorizontalTextPosition(JButton.CENTER);
		currentDetails.setVerticalTextPosition(JButton.CENTER);

		String currentDeckNumber = "<html><font color='white' size = 16>"
				+ model.getCurrentHero().getDeck().size() + "</font></html>";
		currentDeck.setText(currentDeckNumber);

		String opponentDeckNumber = "<html><font color='white' size = 16>"
				+ model.getOpponent().getDeck().size() + "</font></html>";
		opponentDeck.setText(opponentDeckNumber);

		this.updateFieldAndHand();

		view.revalidate();
		view.repaint();

	}

	public void updateFieldAndHand() {
		Hero currentH = model.getCurrentHero();
		Hero oppH = model.getOpponent();

		opponentHandArray.clear();
		view.getCardsInHandOpponent().removeAll();
		for (int i = 0; i < oppH.getHand().size(); i++) {
			JButton card = new JButton(new ImageIcon("img/card-1.jpg"));
			card.setOpaque(false);
			card.setContentAreaFilled(false);
			opponentHandArray.add(card);
			view.getCardsInHandOpponent().add(card);
			view.getCardsInHandOpponent().add(Box.createHorizontalGlue());
		}

		opponentFieldArray.clear();
		view.getOpponentField().removeAll();
		for (int i = 0; i < oppH.getField().size(); i++) {
			JButton card = new JButton(stringCardDetails(
					oppH.getField().get(i), false), new ImageIcon(
					"img/card-1.jpg"));
			card.setHorizontalTextPosition(JButton.CENTER);
			card.setVerticalTextPosition(JButton.CENTER);
			card.setOpaque(false);
			card.setContentAreaFilled(false);
			card.addActionListener(this);
			opponentFieldArray.add(card);
			view.getOpponentField().add(card);
			view.getOpponentField().add(Box.createHorizontalGlue());
		}

		currentFieldArray.clear();
		view.getCurrentField().removeAll();
		for (int i = 0; i < currentH.getField().size(); i++) {
			JButton card = new JButton(stringCardDetails(currentH.getField()
					.get(i), true), new ImageIcon("img/card-1.jpg"));
			card.setHorizontalTextPosition(JButton.CENTER);
			card.setVerticalTextPosition(JButton.CENTER);
			card.setOpaque(false);
			card.setContentAreaFilled(false);
			card.addActionListener(this);
			currentFieldArray.add(card);
			view.getCurrentField().add(card);
			view.getCurrentField().add(Box.createHorizontalGlue());
		}

		currentHandArray.clear();
		view.getCardsInHandCurrent().removeAll();
		for (int i = 0; i < currentH.getHand().size(); i++) {
			JButton card = new JButton(stringCardDetails(currentH.getHand()
					.get(i), false), new ImageIcon("img/playingCards-1.jpg"));
			card.setSize(120, 200);
			card.setHorizontalTextPosition(JButton.CENTER);
			card.setVerticalTextPosition(JButton.CENTER);
			card.setOpaque(false);
			card.setContentAreaFilled(false);
			card.addActionListener(this);
			currentHandArray.add(card);
			view.getCardsInHandCurrent().add(card);
			view.getCardsInHandCurrent().add(Box.createHorizontalGlue());

		}
	}

	public String stringCardDetails(Card card, boolean current) { // check
																	// charge
																	// and font
		String s = "<html><font color ='white'>";
		if (card instanceof Minion) {
			Minion m = (Minion) card;
			s += m.getName() + "<br>Mana Cost: " + m.getManaCost() + "<br>"
					+ m.getRarity() + "<br>Attack: " + m.getAttack()
					+ "<br>HP: " + m.getCurrentHP() + "/" + m.getMaxHP()
					+ "<br>Taunt: " + ((m.isTaunt()) ? "Yes" : "No")
					+ "<br>Divine: " + ((m.isDivine()) ? "Yes" : "No");
			if (current) {
				s += "<br>Can Attack: " + ((!m.isSleeping()) ? "Yes" : "No");
			} else {
				s += "<br>Charge: " + ((!m.isSleeping()) ? "Yes" : "No");
			}
		} else if (card instanceof Spell) {
			Spell sp = (Spell) card;
			s += sp.getName() + "<br>" + sp.getRarity() + "<br>Mana Cost: "
					+ sp.getManaCost();
		}
		s += "</font> </html>";
		return s;
	}

	@Override
	public void onGameOver() {
		String heroWon = "";
		if(this.firstHero.getCurrentHP() <= 0){
			heroWon = "Player 2 ";
		} else{
			heroWon = "Player 1 ";
		}
		
		JOptionPane.showMessageDialog(
				null,
				"GAME OVER",
				heroWon + "WINS!",
				JOptionPane.INFORMATION_MESSAGE);
		this.view.dispose();
	}

	public static void main(String[] args) {
		new Controller();
	}
}
