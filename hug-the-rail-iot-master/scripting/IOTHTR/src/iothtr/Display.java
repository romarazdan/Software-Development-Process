package iothtr;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.JTextComponent;

public class Display {

	public static Boolean RUNNING;
	
	//Image Directory for GUI Images
	private static String IMAGE_DIR = IoTEngine.CWD + "/IOTHTR_FILES/IMAGES/";
	//Frame for the current working window 
	private static JFrame FRAME;
	//Label for the speedometer 
	private static JLabel TRAIN_SPEED;
	
	//Wheel slip Panel and Icon
	private static JPanel WHEEL_SLIP_PANEL;
	private static JLabel WHEEL_SLIP_ICON; 
	
	//Object Detection information
	private static JPanel OBJ_DETECTION;
	private static JProgressBar DIST_BAR;
	private static JLabel OBJECT_TYPE;
	private static JLabel OBJECT_SPEED;
	private static JLabel OBJECT_DISTANCE;
	
	//static integers for the Object types
	public static int OBJECT_NONE = 0; 
	public static int OBJECT_STAND = 1; 
	public static int OBJECT_MOVING = 2; 
	
	//Weather Detection Panel
	private static JPanel WEATHER_PANEL;
	private static JLabel WEATHER_ICON;
	
	//static integers for weather types
	public static int WEATHER_SUNNY = 0;
	public static int WEATHER_RAINING = 1;
	public static int WEATHER_SNOWING = 2;
	public static int WEATHER_HIGHWINDS = 3;
	
	//Horn Panel and Label
	private static JPanel HORN_PANEL;
	private static JLabel HORN_ICON;
	private static JLabel HORN_COUNTDOWN;
	private static Boolean HORN_ON = false;
	
	//Gate Panel and Icon
	private static JPanel GATE_PANEL;
	private static JLabel GATE_ICON;
	
	
	//alerts control
	private static ConsoleBox ALERTS_CONSOLE;
	
	
	//Static function that loads the login window
	public static void loginInit() {
		RUNNING = false;
		int frameWidth = 400;
		int frameHeight = 225;
		
		//Create the window for the Login 
		JPanel panel = new JPanel();
		FRAME = new JFrame("IOTHTR user authentication.");
		FRAME.setSize(frameWidth, frameHeight);
		FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FRAME.setLocationRelativeTo(null);
		FRAME.add(panel);
		panel.setLayout(null);
		
		
		//User name label and text box
		JLabel userLabel = new JLabel("Username ");
		userLabel.setBounds(10,20,80,25);
		panel.add(userLabel);
		
		JTextField userText = new JTextField(20);
		userText.setBounds(100, 20, 165, 25);
		panel.add(userText);
		
		
		//Password label and pass box
		JLabel passwordLabel = new JLabel("Password ");
		passwordLabel.setBounds(10,50,80,25);
		panel.add(passwordLabel);		
		
		JPasswordField passwordText = new JPasswordField();
		passwordText.setBounds(100, 50, 165, 25);
		panel.add(passwordText);
		
		
		JLabel failure = new JLabel(" ");
		failure.setBounds(10,110,300,25);
		
		//Button and failure label
		JButton button = new JButton("Login");
		button.setBounds(10,80,80,25);
		button.addActionListener(new ActionListener() {
			//When button is clicked
			public void actionPerformed(ActionEvent e) {
				Display.loginClicked(userText, passwordText, failure);
			}
		});
		panel.add(button);
		panel.add(failure);
		
		FRAME.setVisible(true);
	}

	//function run when the login button is clicked
	private static void loginClicked(JTextField userText, JPasswordField passwordText, JLabel failure) {
		String username = userText.getText();
		String password = passwordText.getText();
		if (IoTEngine.authenticateUser(username, password)) {
			//destroy window and launch IoTEngine
			FRAME.dispatchEvent(new WindowEvent(FRAME, WindowEvent.WINDOW_DEACTIVATED));
			FRAME.setVisible(false);
			Display.welcomeMessageInit();
		}
		else {
			//display error message
			failure.setText("Error: Incorrect username or password.");
			failure.setForeground(Color.RED);
		}
	}
	
	
	//displays the welcome message to the user
	private static void welcomeMessageInit() {
		
		int frameWidth = 400;
		int frameHeight = 225;
		
		FRAME = new JFrame("Welcome " + IoTEngine.getCurrentUser());
		FRAME.setSize(frameWidth, frameHeight);
		FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FRAME.setLocationRelativeTo(null);
		FRAME.setLayout(new GridBagLayout());
	
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		FRAME.add(panel);
		
		JLabel welcomeMessage = new JLabel("Welcome " + IoTEngine.getCurrentUser() + ", your privilige level is " + IoTEngine.PRIV_STR[IoTEngine.getCurrentPrivilage()] + ".", SwingConstants.CENTER);
		welcomeMessage.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(welcomeMessage);
		
		JLabel spacing = new JLabel(" ");
		spacing.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(spacing);
		
		JButton proceed = new JButton("Proceed");
		proceed.setAlignmentX(Component.CENTER_ALIGNMENT);
		proceed.addActionListener(new ActionListener() {
			//When button is clicked
			public void actionPerformed(ActionEvent e) {
				Display.proceedClicked();
			}
		});
		
		panel.add(proceed);
		
		
		FRAME.setVisible(true);
	}
	
	
	//function run when the proceed button is clicked
	private static void proceedClicked() {
		FRAME.dispatchEvent(new WindowEvent(FRAME, WindowEvent.WINDOW_DEACTIVATED));
		FRAME.setVisible(false);
		Display.engineInit();
	}
	
	
	//Runs the main IoTEngine feature
	private static void engineInit() {
		
		//FRAME setup
		FRAME = new JFrame("IOTHTR Engine");
		FRAME.setSize(1920, 1080);
		FRAME.setExtendedState(JFrame.MAXIMIZED_BOTH);
		FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//mainLayout setup
		JPanel mainLayout = new JPanel();
		mainLayout.setLayout(new BorderLayout());
		
		//Admin Protal
		if (IoTEngine.getCurrentPrivilage() == 1) {
			JPanel adminPortal = new JPanel();
			JButton runSimulation = new JButton("Run Simulation");
			runSimulation.addActionListener(new ActionListener() {
				//When button is clicked
				public void actionPerformed(ActionEvent e) {
					initSimWindow();
				}
			});
			JButton accountConfigurations = new JButton("Configure Accounts");
			accountConfigurations.addActionListener(new ActionListener() {
				//When button is clicked
				public void actionPerformed(ActionEvent e) {
					initAccountConfig();
				}
			});
			JButton viewLogs = new JButton("View Logs");
			viewLogs.addActionListener(new ActionListener() {
				//When button is clicked
				public void actionPerformed(ActionEvent e) {
					initLogViewer();
				}
			});
			adminPortal.add(runSimulation);
			adminPortal.add(accountConfigurations);
			adminPortal.add(viewLogs);
			mainLayout.add(adminPortal, BorderLayout.NORTH);
		}
		
		
		//IoTcentral setup
		JPanel iotCentral = new JPanel();
		iotCentral.setLayout(new BorderLayout());
		mainLayout.add(iotCentral, BorderLayout.CENTER);
			
			//hud setup
			JPanel hud = new JPanel();
			hud.setLayout(new GridLayout(2, 3));
			hud.setBorder(BorderFactory.createLineBorder(Color.black));
			iotCentral.add(hud, BorderLayout.CENTER);
			
				//Weather Description
				WEATHER_PANEL = new JPanel();
				WEATHER_PANEL.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
				WEATHER_PANEL.setLayout(new GridBagLayout());
				WEATHER_ICON = new JLabel();
				WEATHER_PANEL.add(WEATHER_ICON);
				hud.add(WEATHER_PANEL);
			
				//Speedometer
				JPanel speedometer = new JPanel();
				speedometer.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
				speedometer.setLayout(new GridLayout());
				TRAIN_SPEED = new JLabel(String.valueOf(IoTEngine.getCurrentTrainSpeed()) + " KM/H", JLabel.CENTER);
				TRAIN_SPEED.setFont(new Font(TRAIN_SPEED.getName(), Font.PLAIN, 40));
				speedometer.add(TRAIN_SPEED);
				hud.add(speedometer);
				
				//Slippage Detection
				WHEEL_SLIP_PANEL = new JPanel();
				WHEEL_SLIP_PANEL.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
				WHEEL_SLIP_PANEL.setLayout(new GridBagLayout());
				WHEEL_SLIP_ICON = new JLabel();
				WHEEL_SLIP_PANEL.add(WHEEL_SLIP_ICON);
				hud.add(WHEEL_SLIP_PANEL);
				
				
				//Gate Detection
				GATE_PANEL = new JPanel();
				GATE_PANEL.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
				GATE_PANEL.setLayout(new GridBagLayout());
				GATE_ICON = new JLabel();
				GATE_PANEL.add(GATE_ICON);
				hud.add(GATE_PANEL);
				
				//Object Detection
				OBJ_DETECTION = new JPanel();
				OBJ_DETECTION.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
				OBJ_DETECTION.setLayout(new GridBagLayout());
				GridBagConstraints gbc = new GridBagConstraints();
				OBJECT_TYPE = new JLabel("No Object Detected.");
				OBJECT_TYPE.setFont(new Font(OBJECT_TYPE.getName(), Font.PLAIN, 20));
				OBJECT_SPEED = new JLabel("Object Speed: 0 KM/H");
				OBJECT_SPEED.setFont(new Font(OBJECT_SPEED.getName(), Font.PLAIN, 20));
				OBJECT_DISTANCE = new JLabel("0m");
				DIST_BAR = new JProgressBar(JProgressBar.VERTICAL, 0, 1000);
				gbc.gridx = 0;
				gbc.gridy = 0;
				gbc.gridwidth = 1;
				gbc.gridheight = 1;
				OBJ_DETECTION.add(OBJECT_TYPE, gbc);
				gbc.gridx = 0;
				gbc.gridy = 3;
				gbc.gridwidth = 1;
				gbc.gridheight = 1;
				OBJ_DETECTION.add(OBJECT_SPEED, gbc);
				gbc.insets = new Insets(140,200,0,0);
				gbc.gridx = 0;
				gbc.gridy = 1;
				gbc.gridwidth = 1;
				gbc.gridheight = 2;
				OBJ_DETECTION.add(OBJECT_DISTANCE, gbc);
				gbc.insets = new Insets(5, 10, 5, 5);
				gbc.gridx = 1;
				gbc.gridy = 1;
				gbc.gridwidth = 1;
				gbc.gridheight = 2;
				OBJ_DETECTION.add(DIST_BAR, gbc);
				hud.add(OBJ_DETECTION);
				
				//Horn Functionality
				HORN_PANEL = new JPanel();
				HORN_PANEL.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
				HORN_PANEL.setLayout(new GridBagLayout());
				HORN_ICON = new JLabel();
				HORN_COUNTDOWN = new JLabel(" ");
				HORN_COUNTDOWN.setFont(new Font(HORN_COUNTDOWN.getName(), Font.PLAIN, 18));
				gbc.gridx = 0;
				gbc.gridy = 0;
				gbc.gridwidth = 1;
				gbc.gridheight = 1;
				HORN_PANEL.add(HORN_ICON, gbc);
				gbc.gridx = 0;
				gbc.gridy = 1;
				gbc.gridwidth = 1;
				gbc.gridheight = 1;
				HORN_PANEL.add(HORN_COUNTDOWN, gbc);
				hud.add(HORN_PANEL);
			
			//alerts setup
			JPanel alerts = new JPanel();
			alerts.setLayout(new BorderLayout());
			alerts.setBorder(BorderFactory.createLineBorder(Color.black));
			iotCentral.add(alerts, BorderLayout.EAST);
			
				//Alerts panel
				JPanel alertsPanel = new JPanel();
				alertsPanel.setLayout(new BorderLayout());
				alerts.add(alertsPanel, BorderLayout.CENTER);
				
					//Alerts Pad
					JPanel alertsPad = new JPanel();
					alertsPad.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
					alertsPad.setLayout(new GridLayout());
					alertsPanel.add(alertsPad, BorderLayout.CENTER);
					
						//Alerts Box
						ALERTS_CONSOLE = new ConsoleBox(alertsPad);
						ALERTS_CONSOLE.sendMessage("Welcome " + IoTEngine.getCurrentUser() + ". This is your alert console. You will recieve alerts from the IoTEngine here.", ConsoleBox.STANDARD);
						ALERTS_CONSOLE.sendMessage("You may click the clear button down below to clear the console if it gets cluttered.", ConsoleBox.STANDARD);
						
				//clear button
				JButton clearButton = new JButton("Clear");
				clearButton.addActionListener(new ActionListener() {
					//When button is clicked
					public void actionPerformed(ActionEvent e) {
						ALERTS_CONSOLE.clearConsole();
					}
				});
				alerts.add(clearButton, BorderLayout.SOUTH);
		
		//StatusPanel setup
		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new BorderLayout());
		statusPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		mainLayout.add(statusPanel, BorderLayout.SOUTH);
			
			//Status name
			JLabel name = new JLabel("Name: " + IoTEngine.getCurrentUser());
			name.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			statusPanel.add(name, BorderLayout.WEST);
			
			//Status privilege
			JLabel priv = new JLabel("Privilege: " + IoTEngine.PRIV_STR[IoTEngine.getCurrentPrivilage()]);
			priv.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			statusPanel.add(priv, BorderLayout.EAST);
			
		
		FRAME.add(mainLayout);
		FRAME.setVisible(true);
		
		//Initialize Icons
		Display.setIconImage(WHEEL_SLIP_ICON, WHEEL_SLIP_PANEL, "wheel_slip_off.jpg");
		Display.setIconImage(WEATHER_ICON, WEATHER_PANEL, "weather_sunny.jpeg");
		Display.setIconImage(HORN_ICON, HORN_PANEL, "horn_off.jpg");
		Display.setIconImage(GATE_ICON, GATE_PANEL, "gate_closed.jpg");
		
		RUNNING = true;
		
	}
	
	
	//Starts and handles configurations to the simulation window
	private static void initSimWindow() {
		int frameWidth = 720;
		int frameHeight = 1280;
		
		//Create the window for the Login 
		JPanel panel = new JPanel();
		JFrame frame = new JFrame("Simulator");
		frame.setSize(frameWidth, frameHeight);
		frame.setLocationRelativeTo(null);
		frame.add(panel);
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		Hashtable<String, Sensor> allSensors = IoTEngine.SENSORS;
		Set<Entry<String, Sensor>> entrySet = allSensors.entrySet();
		gbc.insets = new Insets(10, 10, 10, 10);
		
		JLabel TrainSpeed = new JLabel("Train Speed: ");
		TrainSpeed.setFont(new Font(TrainSpeed.getName(), Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		panel.add(TrainSpeed, gbc);
		JTextField speed = new JTextField(20);
		gbc.gridx = 1;
		panel.add(speed, gbc);
		JButton applySpeed = new JButton("Apply");
		applySpeed.setPreferredSize(new Dimension(100, 20));
		applySpeed.addActionListener(new ActionListener() {
			//When button is clicked
			public void actionPerformed(ActionEvent e) {
				IoTEngine.setCurrentTrainSpeed(Integer.valueOf(speed.getText()));
			}
		});
		gbc.gridx = 2;
		panel.add(applySpeed,gbc);
		
		JLabel Gate_Pos = new JLabel("Gate Coordinates: ");
		Gate_Pos.setFont(new Font(TrainSpeed.getName(), Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		panel.add(Gate_Pos, gbc);
		JTextField longitude = new JTextField(20);
		gbc.gridx = 1;
		panel.add(longitude, gbc);
		JTextField latitude = new JTextField(20);
		gbc.gridx = 1;
		gbc.gridy = 2;
		panel.add(latitude, gbc);
		JButton applyCoord = new JButton("Apply");
		applyCoord.setPreferredSize(new Dimension(100, 20));
		applyCoord.addActionListener(new ActionListener() {
			//When button is clicked
			public void actionPerformed(ActionEvent e) {
				IoTEngine.GATE_COORD[0]= Double.valueOf(longitude.getText());
				IoTEngine.GATE_COORD[1]= Double.valueOf(latitude.getText());
			}
		});
		gbc.gridy = 1;
		gbc.gridx = 2;
		gbc.gridheight = 2;
		panel.add(applyCoord,gbc);
		int i = 3;
		for(Entry<String, Sensor> entry1 : entrySet) {
			
			JLabel sensorName = new JLabel(entry1.getKey() + ": ");
			sensorName.setFont(new Font(sensorName.getName(), Font.PLAIN, 20));
			gbc.gridx = 0;
			gbc.gridy = i;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			panel.add(sensorName, gbc);
			JTextField input = new JTextField(20);
			JTextField input2 = new JTextField(20);
			gbc.gridx = 1;
			panel.add(input, gbc);
			if (entry1.getValue().getClass().equals(GPS.class)) {
				i++;
				gbc.gridy = i;
				panel.add(input2, gbc);
				gbc.gridheight = 2;
				gbc.gridy = i - 1;
			}
			JButton apply = new JButton("Apply");
			apply.setPreferredSize(new Dimension(100, 20));
			apply.addActionListener(new ActionListener() {
				//When button is clicked
				public void actionPerformed(ActionEvent e) {
					if (entry1.getValue().getClass().equals(WaterSensor.class)) {
						WaterSensor temp = (WaterSensor) entry1.getValue();
						if (Integer.valueOf(input.getText()) == 0) {
							temp.receiveData(false);
						}
						else {
							temp.receiveData(true);
						}
					}
					else if (entry1.getValue().getClass().equals(Thermostat.class)) {
						Thermostat temp = (Thermostat) entry1.getValue();
						temp.receiveData(Integer.valueOf(input.getText()));
					}
					else if (entry1.getValue().getClass().equals(Tachometer.class)) {
						Tachometer temp = (Tachometer) entry1.getValue();
						temp.receiveData(Integer.valueOf(input.getText()));
					}
					else if (entry1.getValue().getClass().equals(ProximitySensor.class)) {
						ProximitySensor temp = (ProximitySensor) entry1.getValue();
						temp.receiveData(Double.valueOf(input.getText()));
					}
					else if (entry1.getValue().getClass().equals(GPS.class)) {
						GPS temp = (GPS) entry1.getValue();
						temp.receiveData(Double.valueOf(input.getText()), Double.valueOf(input2.getText()));
					}
					else if (entry1.getValue().getClass().equals(Anemometer.class)) {
						Anemometer temp = (Anemometer) entry1.getValue();
						temp.receiveData(Integer.valueOf(input.getText()));
					}

					
					
				}
			});
			gbc.gridx = 2;
			panel.add(apply, gbc);
			i++;
			
		}
		
		
		frame.setVisible(true);
	}
	
	
	//starts the window and handles the features of the admin account configurations feature
	private static void initAccountConfig() {
		int frameWidth = 720;
		int frameHeight = 1280;
		
		JPanel panel = new JPanel();
		JFrame frame = new JFrame("Account Configurer");
		frame.setSize(frameWidth, frameHeight);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new BorderLayout());
		frame.add(panel, BorderLayout.CENTER);
		
		JButton createUser = new JButton("Create User");
		createUser.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		createUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initCreateUser();
			}
		});
		frame.add(createUser, BorderLayout.NORTH);
		
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints(); 
		
		
		ArrayList<String[]> allUsers = IoTEngine.getAllUsers();
		
		for (int i = 0; i < allUsers.size(); i++) {
			JPanel userContent = new JPanel();
			userContent.setLayout(new GridBagLayout());
			
			gbc.insets = new Insets(3, 3, 3, 3);
			JLabel userName = new JLabel(allUsers.get(i)[0]);
			userName.setFont(new Font(userName.getName(), Font.PLAIN, 20));
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.gridwidth = 2;
			gbc.gridheight = 1;
			userContent.add(userName, gbc);
			gbc.gridwidth = 1;
			
			
			JLabel newUserPassName = new JLabel("New Password: ");
			gbc.gridx = 0;
			gbc.gridy = 1;
			userContent.add(newUserPassName, gbc);
			JPasswordField newUserPass = new JPasswordField(20);
			gbc.gridx = 1;
			gbc.gridy = 1;
			userContent.add(newUserPass, gbc);
			
			JLabel confUserPassName = new JLabel("Confirm Password: ");
			gbc.gridx = 0;
			gbc.gridy = 2;
			userContent.add(confUserPassName, gbc);
			JPasswordField confUserPass = new JPasswordField(20);
			gbc.gridx = 1;
			gbc.gridy = 2;
			userContent.add(confUserPass, gbc);
			
			JLabel successOrFail = new JLabel(" ");
			
			String name = allUsers.get(i)[0];
			
			JButton setPass = new JButton("Set Password");
			setPass.addActionListener(new ActionListener() {
				//When button is clicked
				public void actionPerformed(ActionEvent e) {
					Boolean sOrF = IoTEngine.changePassword(name, newUserPass.getText(), confUserPass.getText());
					if (sOrF) {
						successOrFail.setText("Success");
						successOrFail.setForeground(Color.GREEN);
					}
					else {
						successOrFail.setText("Failure");
						successOrFail.setForeground(Color.RED);
					}
				}
			});
			gbc.gridx = 1;
			gbc.gridy = 4;
			gbc.gridwidth = 1;
			
			userContent.add(setPass, gbc);
			
			int index = i;
			
			JButton delUser = new JButton("Delete User");
			delUser.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					IoTEngine.deleteUser(allUsers.get(index)[0]);
					userContent.setVisible(false);
				}
			});
			
			gbc.gridx = 2;
			gbc.gridy = 0;
			gbc.gridwidth = 1;
			
			userContent.add(delUser, gbc);
			
			gbc.gridx = 0;
			gbc.gridy = 5;
			gbc.gridwidth = 2;
			userContent.add(successOrFail, gbc);
			
			gbc.gridx = 0;
			gbc.gridy = i;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.insets = new Insets(10, 10, 10, 10);
			panel.add(userContent, gbc);
		}
		
		frame.setVisible(true);
	}
	
	
	//Starts and handles configurations for the create user window
	private static void initCreateUser() {
		int frameWidth = 510;
		int frameHeight = 275;
		
		//Create the window for the Login 
		JPanel panel = new JPanel();
		JFrame frame = new JFrame("User Creation");
		frame.setSize(frameWidth, frameHeight);
		frame.setLocationRelativeTo(null);
		frame.add(panel);
		panel.setLayout(null);
		
		
		//User name label and text box
		JLabel userLabel = new JLabel("Username ");
		userLabel.setBounds(10,20,165,25);
		panel.add(userLabel);
		
		JTextField userText = new JTextField(20);
		userText.setBounds(150, 20, 165, 25);
		panel.add(userText);
		
		
		//Password label and pass box
		JLabel passwordLabel = new JLabel("Password ");
		passwordLabel.setBounds(10,50,165,25);
		panel.add(passwordLabel);		
		
		JPasswordField passwordText = new JPasswordField();
		passwordText.setBounds(150, 50, 165, 25);
		panel.add(passwordText);
		
		JLabel confPasswordLabel = new JLabel("Confirm Password ");
		confPasswordLabel.setBounds(10,80,165,25);
		panel.add(confPasswordLabel);		
		
		JPasswordField confPasswordText = new JPasswordField();
		confPasswordText.setBounds(150, 80, 165, 25);
		panel.add(confPasswordText);
		
		
		JLabel failure = new JLabel(" ");
		failure.setBounds(10,140,500,25);
		
		//Button and failure label
		JButton button = new JButton("Create");
		button.setBounds(10,110,80,25);
		button.addActionListener(new ActionListener() {
			//When button is clicked
			public void actionPerformed(ActionEvent e) {
				if (IoTEngine.createUser(userText.getText(), passwordText.getText(), confPasswordText.getText())) {
					failure.setText("User Succefully Created! Close and reopen the account config window to see result.");
					failure.setForeground(Color.BLUE);
				}
				else {
					failure.setText("Error: passwords do not match.");
					failure.setForeground(Color.RED);
				}
			}
		});
		panel.add(button);
		panel.add(failure);
		
		frame.setVisible(true);
	}
	
	
	//opens the log viewer window
	private static void initLogViewer() {
		int frameWidth = 720;
		int frameHeight = 1280;
		
		JPanel panel = new JPanel();
		JFrame frame = new JFrame("Log Viewer");
		frame.setSize(frameWidth, frameHeight);
		frame.setLocationRelativeTo(null);
		frame.add(panel);
		panel.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		Hashtable<String, Log> allLogs = LogHandler.LOG_LIST;
		Set<Entry<String, Log>> entrySet = allLogs.entrySet();
		gbc.insets = new Insets(10, 10, 10, 10);
		
		int i = 0;
		for(Entry<String, Log> entry1 : entrySet) {
			JButton newButton = new JButton(entry1.getKey());
			newButton.addActionListener(new ActionListener() {
				//When button is clicked
				public void actionPerformed(ActionEvent e) {
					initConsoleLogViewer(entry1.getValue());
				}
			});
			newButton.setPreferredSize(new Dimension(200, 60));
			
			gbc.gridx = 0;
			gbc.gridy = i;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			i++;
			panel.add(newButton, gbc);
		}
		frame.setVisible(true);
	}
	
	//handles the features for displaying the log selected
	private static void initConsoleLogViewer(Log log) {
		int frameWidth = 800;
		int frameHeight = 1000;
		
		JPanel panel = new JPanel();
		JFrame frame = new JFrame(log.logName);
		frame.setSize(frameWidth, frameHeight);
		frame.setLocationRelativeTo(null);

		
		frame.setLayout(new BorderLayout());
		frame.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout());
		JPanel innerpanel = new JPanel();
		innerpanel.setLayout(new GridLayout());
		panel.add(innerpanel, BorderLayout.CENTER);
		ConsoleBox viewer = new ConsoleBox(innerpanel);
		
		ArrayList<String> fileContents = log.logAsArrLst();

		for (int i = 0; i < fileContents.size(); i++) {
			viewer.sendMessage(fileContents.get(i), ConsoleBox.STANDARD);
		}
		
		JButton clearLog = new JButton("Clear Log");
		clearLog.addActionListener(new ActionListener() {
			//When button is clicked
			public void actionPerformed(ActionEvent e) {
				log.clearLog();
				viewer.clearConsole();
			}
		});
		clearLog.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		panel.add(clearLog, BorderLayout.SOUTH);
		
		
		frame.setVisible(true);

		
		
	}
	
	//updates the speedometer
	public static void updateSpeed() {
		TRAIN_SPEED.setText(String.valueOf(IoTEngine.getCurrentTrainSpeed()) + "  KM/H");
	}
	
	//Updates the slip icon
	public static void updateSlipIcon(Boolean isActive) {
		if(isActive) {
			Display.setIconImage(WHEEL_SLIP_ICON, WHEEL_SLIP_PANEL, "wheel_slip_on.jpg");
		}
		else {
			Display.setIconImage(WHEEL_SLIP_ICON, WHEEL_SLIP_PANEL, "wheel_slip_off.jpg");
		}
	}
	
	//Updates the horn icon
	public static void updateHornIcon(Boolean isActive) {
		if(isActive) {
			Display.setIconImage(HORN_ICON, HORN_PANEL, "horn_on.png");
		}
		else {
			Display.setIconImage(HORN_ICON, HORN_PANEL, "horn_off.jpg");
		}
	}
	
	//updates the Gate Icon
	public static void updateGateIcon(Boolean isOpen) {
		if(isOpen) {
			Display.setIconImage(GATE_ICON, GATE_PANEL, "gate_open.jpg");
		}
		else {
			Display.setIconImage(GATE_ICON, GATE_PANEL, "gate_closed.jpg");
		}
	}
	
	//updates the weather icon
	public static void updateWeather(int weatherType) {
		if (weatherType == 0) {
			Display.setIconImage(WEATHER_ICON, WEATHER_PANEL, "weather_sunny.jpeg");
		}
		else if (weatherType == 1) {
			Display.setIconImage(WEATHER_ICON, WEATHER_PANEL, "weather_raining.jpg");
		}
		else if (weatherType == 2) {
			Display.setIconImage(WEATHER_ICON, WEATHER_PANEL, "weather_snowing.jpg");
		}
		else if (weatherType == 3) {
			Display.setIconImage(WEATHER_ICON, WEATHER_PANEL, "weather_highwinds.jpg");
		}
	}

	
	//handles the display features of the object type
	public static void setObjectType(int objType) {
		if (objType == 0) {
			OBJECT_TYPE.setText("No Object Detected.");
		}
		else if (objType == 1) {
			OBJECT_TYPE.setText("Object Type: Stationary");
		}
		else {
			OBJECT_TYPE.setText("Object Type: Moving");
		}
	}
	
	//handles the display of the object speed feature
	public static void setObjectSpeed(int speed) {
		OBJECT_SPEED.setText("Object Speed: " + String.valueOf(speed) + "  KM/H");
	}
	
	//handles the display features of the distance bar
	public static void setDistanceBar(int distance) {
		DIST_BAR.setValue(distance);
		OBJECT_DISTANCE.setText(String.valueOf(distance) + "m");
		GridBagConstraints gbc = new GridBagConstraints();
		float yOffset = 140 - (((float)distance/DIST_BAR.getMaximum()) * 280);
		if (distance > 1000) {
			yOffset = -140;
			OBJECT_DISTANCE.setText("1000m");
		}
		gbc.insets = new Insets((int)yOffset,200,0,0);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 2;
		OBJ_DETECTION.add(OBJECT_DISTANCE, gbc);
	}
	
	//sends an alert to the console
	public static void sendAlert(String message, int severity) {
		ALERTS_CONSOLE.sendMessage(message, severity);
	}
	
	//initiates the countdown for how long the conductor should honk the horn
	public static void startHornCountdown(int time) {
		if (!HORN_ON) {
			HORN_ON = true;
			Timer timer = new Timer(1000, new ActionListener() {
				int timeLeft = time;
			    public void actionPerformed(ActionEvent e)
			    {
			        HORN_COUNTDOWN.setText(String.valueOf(timeLeft / 1000) + "s");
			        timeLeft -= 1000;
			        if(timeLeft<0)
			        {
			        	HORN_COUNTDOWN.setText(" ");
			        	HORN_ON = false;
			        	updateHornIcon(false);
			        	((Timer) (e.getSource())).stop();
			        }
			    }
			});
			timer.start();
			
		}
	}
	
	//general function to set an icon image to be responsive to it's panel
	private static void setIconImage(JLabel icon, JPanel parent, String image_str) {
		BufferedImage image = null; 
		try {
		    image = ImageIO.read(new File(IMAGE_DIR + image_str));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		int imageSize = Math.min(parent.getWidth(), parent.getHeight());
		
		Image scaledImage = image.getScaledInstance(imageSize/2, imageSize/2, Image.SCALE_SMOOTH);
		
		ImageIcon result = new ImageIcon(scaledImage);
		icon.setIcon(result);
	}
	
	
	
}
