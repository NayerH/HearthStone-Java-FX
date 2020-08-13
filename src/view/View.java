package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.layout.Border;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import model.heroes.Hero;

public class View extends JFrame {
	private Clip mainSound;

	private JPanel playButton;
	private JButton welcome;
	private JButton endTurn;
	private JButton useHeroPower;

	private ArrayList<JButton> herosButtons;
	
	private JPanel centerPanel;
	private JPanel opponentPanel;
	private JPanel currentPanel;
	
	private JPanel cardsInHandCurrent;
	private JPanel currentField;
	private JPanel opponentField;
	private JPanel buttonsControlGame;
	private JPanel playerDetails;
	private JPanel cardsInHandOpponent;

	public JPanel getCardsInHandCurrent() {
		return cardsInHandCurrent;
	}

	public JPanel getCenterPanel() {
		return centerPanel;
	}

	public JPanel getCurrentField() {
		return currentField;
	}

	public JPanel getOpponentField() {
		return opponentField;
	}

	public JPanel getButtonsControlGame() {
		return buttonsControlGame;
	}

	public JPanel getPlayerDetails() {
		return playerDetails;
	}

	public JPanel getCardsInHandOpponent() {
		return cardsInHandOpponent;
	}

	public Clip getMainSound() {
		return mainSound;
	}

	public void setMainSound(Clip mainSound) {
		this.mainSound = mainSound;
	}

	public JButton getWelcome() {
		return welcome;
	}

	public void setWelcome(JButton welcome) {
		this.welcome = welcome;
	}

	public View() {
		this.setSize(1600, 900);
		this.setBounds(160, 90, 1600, 900);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("HearthStone");

		try {
			BufferedImage myImage = ImageIO.read(new File("img/Img1-1.jpg"));
			this.setContentPane(new ImagePanel(myImage));
			this.setLayout(new BorderLayout());
			playButton = new JPanel();
			this.add(playButton, BorderLayout.SOUTH);

			welcome = new JButton("PLAY NOW!");

			welcome.setBackground(Color.ORANGE);
			welcome.setFont(new Font("Monospaced", Font.BOLD, 40));
			welcome.setPreferredSize(new Dimension(250, 90));
			welcome.setForeground(Color.WHITE);
			welcome.setFocusPainted(false);
			playButton.add(welcome);
			playButton.setOpaque(false);

		} catch (IOException e) {
			e.printStackTrace();
		}
		mainSound = playSound("sounds/Main.wav");
		mainSound.start();

		this.setVisible(true);
		this.setResizable(false);
		this.revalidate();
		this.repaint();
	}

	public static void main(String[] args) {

	}

	public void secondView() {
		this.getContentPane().removeAll();
		BufferedImage myImage;
		try {
			myImage = ImageIO.read(new File("img/SecondView.jpg"));
			this.setContentPane(new ImagePanel(myImage));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setLayout(new BorderLayout());
		herosButtons = new ArrayList<JButton>();
		JButton hunterButton = new JButton();
		hunterButton.setIcon(new ImageIcon("img/Rexxar.png"));
		hunterButton.setActionCommand("1Hunter");
		herosButtons.add(hunterButton);
		hunterButton.setOpaque(false);
		hunterButton.setContentAreaFilled(false);

		JButton mageButton = new JButton();
		mageButton.setIcon(new ImageIcon("img/Mage.png"));
		mageButton.setActionCommand("2Mage");
		herosButtons.add(mageButton);
		mageButton.setOpaque(false);
		mageButton.setContentAreaFilled(false);

		JButton paladinButton = new JButton();
		paladinButton.setIcon(new ImageIcon("img/Paladin.png"));
		paladinButton.setActionCommand("3Paladin");
		herosButtons.add(paladinButton);
		paladinButton.setOpaque(false);
		paladinButton.setContentAreaFilled(false);

		JButton priestButton = new JButton();
		priestButton.setIcon(new ImageIcon("img/Priest.png"));
		priestButton.setActionCommand("4Priest");
		herosButtons.add(priestButton);
		priestButton.setOpaque(false);
		priestButton.setContentAreaFilled(false);

		JButton warlockButton = new JButton();
		warlockButton.setIcon(new ImageIcon("img/warlock.png"));
		warlockButton.setActionCommand("5Warlock");
		herosButtons.add(warlockButton);
		warlockButton.setOpaque(false);
		warlockButton.setContentAreaFilled(false);
		warlockButton.setBounds(300, 300, 300, 300);

		JPanel herosPanel = new JPanel();
		herosPanel.setSize(new Dimension(0, 0));
		BoxLayout boxlayout = new BoxLayout(herosPanel, BoxLayout.X_AXIS);
		herosPanel.setLayout(boxlayout);
		herosPanel.setBorder(new EmptyBorder(300, 50, 300, 50));
		herosPanel.add(hunterButton);
		herosPanel.add(Box.createHorizontalGlue());
		herosPanel.add(mageButton);
		herosPanel.add(Box.createHorizontalGlue());
		herosPanel.add(paladinButton);
		herosPanel.add(Box.createHorizontalGlue());
		herosPanel.add(priestButton);
		herosPanel.add(Box.createHorizontalGlue());
		herosPanel.add(warlockButton);

		herosPanel.setOpaque(false);
		this.add(herosPanel, BorderLayout.CENTER);

		this.revalidate();
		this.repaint();
	}

	public void thirdView() {
		this.getContentPane().removeAll();
		BufferedImage myImage;
		try {
			myImage = ImageIO.read(new File("img/ThirdView.jpg"));
			this.setContentPane(new ImagePanel(myImage));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setLayout(new BorderLayout());
		
		herosButtons = new ArrayList<JButton>();
		JButton hunterButton = new JButton();
		hunterButton.setIcon(new ImageIcon("img/Rexxar.png"));
		hunterButton.setActionCommand("1Hunter");
		herosButtons.add(hunterButton);
		hunterButton.setOpaque(false);
		hunterButton.setContentAreaFilled(false);

		JButton mageButton = new JButton();
		mageButton.setIcon(new ImageIcon("img/Mage.png"));
		mageButton.setActionCommand("2Mage");
		herosButtons.add(mageButton);
		mageButton.setOpaque(false);
		mageButton.setContentAreaFilled(false);

		JButton paladinButton = new JButton();
		paladinButton.setIcon(new ImageIcon("img/Paladin.png"));
		paladinButton.setActionCommand("3Paladin");
		herosButtons.add(paladinButton);
		paladinButton.setOpaque(false);
		paladinButton.setContentAreaFilled(false);

		JButton priestButton = new JButton();
		priestButton.setIcon(new ImageIcon("img/Priest.png"));
		priestButton.setActionCommand("4Priest");
		herosButtons.add(priestButton);
		priestButton.setOpaque(false);
		priestButton.setContentAreaFilled(false);

		JButton warlockButton = new JButton();
		warlockButton.setIcon(new ImageIcon("img/warlock.png"));
		warlockButton.setActionCommand("5Warlock");
		herosButtons.add(warlockButton);
		warlockButton.setOpaque(false);
		warlockButton.setContentAreaFilled(false);
		warlockButton.setBounds(300, 300, 300, 300);

		JPanel herosPanel = new JPanel();
		herosPanel.setSize(new Dimension(0, 0));
		BoxLayout boxlayout = new BoxLayout(herosPanel, BoxLayout.X_AXIS);
		herosPanel.setLayout(boxlayout);
		herosPanel.setBorder(new EmptyBorder(300, 50, 300, 50));
		herosPanel.add(hunterButton);
		herosPanel.add(Box.createHorizontalGlue());
		herosPanel.add(mageButton);
		herosPanel.add(Box.createHorizontalGlue());
		herosPanel.add(paladinButton);
		herosPanel.add(Box.createHorizontalGlue());
		herosPanel.add(priestButton);
		herosPanel.add(Box.createHorizontalGlue());
		herosPanel.add(warlockButton);

		herosPanel.setOpaque(false);
		this.add(herosPanel, BorderLayout.CENTER);
		this.revalidate();
		this.repaint();
	}

	public void fourthView() {
		this.getContentPane().removeAll();
		mainSound.stop();
		BufferedImage myImage;
		try {
			myImage = ImageIO.read(new File("img/Background-2.jpg"));
			this.setContentPane(new ImagePanel(myImage));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.getContentPane().setLayout(new BorderLayout());
		
		centerPanel = new JPanel(new BorderLayout());
		centerPanel.setOpaque(false);
		this.add(centerPanel, BorderLayout.CENTER);
		
		opponentPanel = new JPanel(new BorderLayout());
		opponentPanel.setOpaque(false);
		centerPanel.add(opponentPanel, BorderLayout.NORTH);
		
		currentPanel = new JPanel(new BorderLayout());
		currentPanel.setOpaque(false);
		centerPanel.add(currentPanel, BorderLayout.SOUTH);
		
		cardsInHandCurrent = new JPanel();
		BoxLayout boxlayout1 = new BoxLayout(cardsInHandCurrent, BoxLayout.X_AXIS);
		cardsInHandCurrent.setOpaque(false);
		cardsInHandCurrent.setLayout(boxlayout1);
		currentPanel.add(cardsInHandCurrent, BorderLayout.SOUTH);

		currentField = new JPanel();
		BoxLayout boxlayout2 = new BoxLayout(currentField, BoxLayout.X_AXIS);
		currentField.setLayout(boxlayout2);
		currentField.setOpaque(false);
		currentPanel.add(currentField, BorderLayout.NORTH);
		

		opponentField = new JPanel();
		BoxLayout boxlayout3 = new BoxLayout(opponentField, BoxLayout.X_AXIS);
		opponentField.setLayout(boxlayout3);
		opponentField.setOpaque(false);
		opponentPanel.add(opponentField, BorderLayout.SOUTH);
		
		cardsInHandOpponent = new JPanel();
		BoxLayout boxlayout4 = new BoxLayout(cardsInHandOpponent, BoxLayout.X_AXIS);
		cardsInHandOpponent.setLayout(boxlayout4);
		cardsInHandOpponent.setOpaque(false);
		opponentPanel.add(cardsInHandOpponent, BorderLayout.NORTH);

		buttonsControlGame = new JPanel(new BorderLayout());
		buttonsControlGame.setSize(100,0);
		this.add(buttonsControlGame, BorderLayout.EAST);
		JPanel buttonsControlGameSub = new JPanel(new BorderLayout());
		

		endTurn = new JButton(new ImageIcon("img/EndTurnButton.png"));
		endTurn.setActionCommand("endTurn");
		endTurn.setOpaque(false);
		endTurn.setContentAreaFilled(false);
		buttonsControlGameSub.add(endTurn, BorderLayout.SOUTH);

		useHeroPower = new JButton(new ImageIcon("img/UseHeroPowerButton.png"));
		useHeroPower.setActionCommand("useHeroPower");
		useHeroPower.setOpaque(false);
		useHeroPower.setContentAreaFilled(false);
		buttonsControlGameSub.add(useHeroPower, BorderLayout.NORTH);
		buttonsControlGameSub.setOpaque(false);
		buttonsControlGame.add(buttonsControlGameSub, BorderLayout.NORTH);
		buttonsControlGame.setOpaque(false);

		playerDetails = new JPanel();
		this.add(playerDetails, BorderLayout.WEST);
		playerDetails.setSize(150,0);
		BoxLayout boxlayout = new BoxLayout(playerDetails, BoxLayout.Y_AXIS);
		playerDetails.setLayout(boxlayout);
		playerDetails.setOpaque(false);

		

		this.revalidate();
		this.repaint();
	}

	public ArrayList<JButton> getHerosButtons() {
		return herosButtons;
	}

	public void setHerosButtons(ArrayList<JButton> herosButtons) {
		this.herosButtons = herosButtons;
	}

	public static Clip playSound(String filepath) {
		try {
			AudioInputStream audioTnputStream = AudioSystem
					.getAudioInputStream(new File(filepath).getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioTnputStream);
			return clip;
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public JPanel getPlayButton() {
		return playButton;
	}

	public void setPlayButton(JPanel playButton) {
		this.playButton = playButton;
	}

	public JButton getEndTurn() {
		return endTurn;
	}

	public JButton getUseHeroPower() {
		return useHeroPower;
	}

}
