package interfaceMain;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class MainInterface {

	private JFrame frame;
	private ArrayList<Rule> rulesList = new ArrayList<Rule>();
	private ArrayList<Email> hamList = new ArrayList<Email>();
	private ArrayList<Email> spamList = new ArrayList<Email>();
	private JTable tableAut;
	private JTable tableMan;
	private DefaultTableModel modelo = new DefaultTableModel();
	private int fn = 0;
	private int fp = 0;
	private int totalMessages = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainInterface window = new MainInterface();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainInterface() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 650);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.getContentPane().setLayout(null);

		// Manual

		JProgressBar progressBarFNM = new JProgressBar();
		progressBarFNM.setBounds(514, 539, 146, 20);
		frame.getContentPane().add(progressBarFNM);
		// progressBarFNM.

		JProgressBar progressBarFPM = new JProgressBar();
		progressBarFPM.setBounds(514, 559, 146, 20);
		frame.getContentPane().add(progressBarFPM);

		JLabel labelFnm = new JLabel("FN");
		labelFnm.setBounds(490, 539, 61, 20);
		frame.getContentPane().add(labelFnm);

		JLabel labelFpm = new JLabel("FP");
		labelFpm.setBounds(490, 559, 61, 20);
		frame.getContentPane().add(labelFpm);

		JButton btnGravar = new JButton("Gravar");
		btnGravar.setBounds(413, 591, 117, 29);
		frame.getContentPane().add(btnGravar);
		btnGravar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				// gravarFicheiro(file);
			}
		});

		JButton btnAvaliar = new JButton("Avaliar");
		btnAvaliar.setBounds(524, 591, 117, 29);
		frame.getContentPane().add(btnAvaliar);
		btnAvaliar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				lerFicheiro("./ham.log");
				lerFicheiro("./spam.log");
				for(Email email : hamList) {
					double peso = email.getPesoMail();
					if(peso < 5) {
						fn++;
						progressBarFPM.setMinimum(0);
						progressBarFPM.setMaximum(hamList.size());
						progressBarFNM.setStringPainted(true);
					}
				}
				for(Email email : spamList) {
					double peso = email.getPesoMail();
					if(peso > 5) {
						fp++;
						progressBarFPM.setMinimum(0);
						progressBarFPM.setMaximum(spamList.size());
						
						progressBarFPM.setStringPainted(true);
						
					}
				}
			}
		});

		JButton btnEditar = new JButton("Editar");
		btnEditar.setBounds(636, 591, 117, 29);
		frame.getContentPane().add(btnEditar);
		btnEditar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				tableMan.setEditingColumn(1);
			}
		});

		JScrollPane scrollPaneMan = new JScrollPane();
		scrollPaneMan.setBounds(413, 82, 345, 445);
		frame.getContentPane().add(scrollPaneMan);
		tableMan = new JTable();

		scrollPaneMan.setViewportView(tableMan);

		// Automático

		JProgressBar progressBarFNA = new JProgressBar();
		progressBarFNA.setBounds(142, 539, 146, 20);
		frame.getContentPane().add(progressBarFNA);

		JProgressBar progressBarFPA = new JProgressBar();
		progressBarFPA.setBounds(142, 559, 146, 20);
		frame.getContentPane().add(progressBarFPA);

		JLabel labelFna = new JLabel("FN");
		labelFna.setBounds(114, 539, 61, 20);
		frame.getContentPane().add(labelFna);

		JLabel labelFpa = new JLabel("FP");
		labelFpa.setBounds(114, 559, 61, 20);
		frame.getContentPane().add(labelFpa);

		JScrollPane scrollPaneAut = new JScrollPane();
		scrollPaneAut.setBounds(32, 82, 345, 445);
		frame.getContentPane().add(scrollPaneAut);

		tableAut = new JTable();
		scrollPaneAut.setViewportView(tableAut);

		// Check Box

		JCheckBox checkBoxRules = new JCheckBox("Rules.cf");
		checkBoxRules.setBounds(32, 30, 82, 23);
		frame.getContentPane().add(checkBoxRules);

		JCheckBox checkBoxHam = new JCheckBox("Ham.log");
		checkBoxHam.setBounds(114, 30, 85, 23);
		frame.getContentPane().add(checkBoxHam);

		JCheckBox checkBoxSpam = new JCheckBox("Spam.log");
		checkBoxSpam.setBounds(200, 30, 99, 23);
		frame.getContentPane().add(checkBoxSpam);

		JButton btnStart = new JButton("Start");
		btnStart.setBounds(303, 29, 117, 29);
		frame.getContentPane().add(btnStart);
		btnStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (checkBoxRules.isSelected()) {
					lerFicheiro("./rules.cf");
					modelo.addColumn("Rules");
					modelo.addColumn("Pesos");
					if (rulesList.isEmpty()) {
						modelo.addRow(new String[] { "Sem informações", "Sem informações" });
					} else {
						for (int i = 0; i < rulesList.size(); i++) {
							modelo.addRow(new String[] { rulesList.get(i).getName(), String.valueOf(0.0) });

						}
					}
					tableMan.setModel(modelo);
				}
				if (checkBoxHam.isSelected()) {
					lerFicheiro("./ham.log");
					modelo.addColumn("Messages");
					modelo.addColumn("Pesos");
					if (rulesList.isEmpty()) {
						modelo.addRow(new String[] { "Sem informações", "Sem informações" });
					} else {
						for (int i = 0; i < rulesList.size(); i++) {
							modelo.addRow(new String[] { rulesList.get(i).getName(), String.valueOf(0.0) });
						}
					}
					tableMan.setModel(modelo);
				}
				if (checkBoxSpam.isSelected()) {
					lerFicheiro("./spam.log");
					modelo.addColumn("Messages");
					modelo.addColumn("Pesos");
					if (rulesList.isEmpty()) {
						modelo.addRow(new String[] { "Sem informações", "Sem informações" });
					} else {
						for (int i = 0; i < rulesList.size(); i++) {
							modelo.addRow(new String[] { rulesList.get(i).getName(), String.valueOf(0.0) });
						}
					}
					tableMan.setModel(modelo);
				}
			}
		});
	}


	public void avaliarEmail(Email email) {
		if (email.getPesoMail() < 5) {
			fn++;
		} else {
			fp++;
		}
	}

	public void gravarFicheiro(String file) {

	}

	public void lerFicheiro(String file) {
		try {
			Scanner scan = new Scanner(new File(file));
			if (file.equals("./rules.cf")) {
				while (scan.hasNextLine()) {
					Rule rule = new Rule(scan.nextLine(), 0.0);
					rulesList.add(rule);
					// rulesList.add(scan.nextLine());
				}
			} else if (file.equals("./ham.log")) {
				while (scan.hasNextLine()) {
					String name = scan.next();
					ArrayList<Rule> emailRules = new ArrayList<Rule>();
					while(scan.hasNext()) {
						Rule rule = new Rule(scan.next(), 0.0);
						emailRules.add(rule);
					}
					Email email = new Email(name, emailRules);
					hamList.add(email);
					// hamList.add(scan.nextLine());
				}
			} else if (file.equals("./spam.log")) {
				while (scan.hasNextLine()) {
					String name = scan.next();
					ArrayList<Rule> emailRules = new ArrayList<Rule>();
					while(scan.hasNext()) {
						Rule rule = new Rule(scan.next(), 0.0);
						emailRules.add(rule);
					}
					Email email = new Email(scan.next(), emailRules);
					hamList.add(email);
					// spamList.add(scan.nextLine());
				}
			}
			scan.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getFn() {
		return fn;
	}

	public int getFp() {
		return fp;
	}

	public int getTotalMessages() {
		return totalMessages;
	}

}
