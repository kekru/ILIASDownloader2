package de.whiledo.iliasdownloader2.swing.service;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import de.whiledo.iliasdownloader2.swing.util.StatusCount;
import de.whiledo.iliasdownloader2.util.FileObject;
import de.whiledo.jtablex.JTableX;
import lombok.Getter;

public class MainFrame extends JFrame {

	public static final String SHOW_NEW_FILES = "Neue Dateien zeigen";

	private static final long serialVersionUID = 1L;

	private static final String TOOLTIP_CHOOSE_COURSES = "Wählen Sie die Kurse unter Synchronisation -> Kurse ändern";

	public static final String SYNCHRONISIERE = "Synchronisieren";

	@Getter
	private JTextField fieldLogin;

	@Getter
	private JPasswordField fieldPassword;

	@Getter
	private JProgressBar progressBar;

	@Getter
	private JTableX<FileObject> tableFiles;

	@Getter
	private JButton buttonSync;

	@Getter
	private JCheckBox checkboxNotDownload;

	private JLabel labelCourseCount;

	@Getter
	private JButton buttonShowNewFiles;

	@Getter
	private JLabel labelStatus;

	public MainFrame(final MainController mainController, JMenuBar menubar){
		
		setTitle(MainController.APP_NAME);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setIconImage(MainController.getImage("open_alt.png").getImage());

		setLayout(new BorderLayout());

		setJMenuBar(menubar);

		//Oben
		JPanel panel = new JPanel();
		{
			panel.setLayout(new FlowLayout(FlowLayout.LEFT));

			KeyListener keyListener = new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e){
					if(e.getKeyCode() == KeyEvent.VK_ENTER){
						mainController.buttonClickedSynchronize();
					}
				}
			};

			panel.add(new JLabel("Login:"));
			panel.add(fieldLogin = new JTextField(10));
			fieldLogin.addKeyListener(keyListener);

			panel.add(new JLabel("Passwort:"));
			panel.add(fieldPassword = new JPasswordField(10));
			fieldPassword.addKeyListener(keyListener);

			panel.add(buttonSync = new JButton(SYNCHRONISIERE, MainController.getImage("down_alt.png")));
			buttonSync.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					mainController.buttonClickedSynchronize();
				}
			});

			panel.add(checkboxNotDownload = new JCheckBox("Nur anzeigen"));
			checkboxNotDownload.setToolTipText("Dateien werden nur in der Liste angezeigt und nicht heruntergeladen.");

			panel.add(progressBar = new JProgressBar(0, 100));
			progressBar.setStringPainted(true);
			progressBar.setVisible(false);

			
			buttonShowNewFiles = new JButton(SHOW_NEW_FILES, MainController.getImage("library_bookmarked.png"));
			buttonShowNewFiles.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					mainController.showNewFiles();	
				}
			});
			panel.add(buttonShowNewFiles);
		}
		add(panel, BorderLayout.NORTH);

		panel = new JPanel();
		{
			panel.setLayout(new BorderLayout());
			tableFiles = mainController.generateFileObjectTable(mainController.getFileObjectTableModel());
			panel.add(new JScrollPane(tableFiles), BorderLayout.CENTER);
			


		}
		add(panel, BorderLayout.CENTER);
		
		JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel2.add(labelStatus = new JLabel());
		panel2.add(labelCourseCount = new JLabel());
		labelCourseCount.setToolTipText(TOOLTIP_CHOOSE_COURSES);
		panel.add(panel2, BorderLayout.SOUTH);

		setSize(1000, 700);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void setTitleCapture(String serverName){
		setTitle(MainController.APP_NAME + " - " + serverName);
	}

	public void updateCourseCount(int count, int max){
		if(count == 0){
			labelCourseCount.setText("<html>Keine Kurse gewählt<br>"+TOOLTIP_CHOOSE_COURSES+"</html>");
		}else{
			labelCourseCount.setText(count + " von " + max + " Kursen ausgewählt");
		}
	}

	public void updateLabelStatus(StatusCount statusCount) {
		labelStatus.setText("Aktualisiert: "+statusCount.updatedCount() + ", Fehler: "+statusCount.errorCount() + ", Ignoriert: "+statusCount.ignoredCount() + ", Bereits vorhanden: "+statusCount.oldCount() + "  ");
	}
}
