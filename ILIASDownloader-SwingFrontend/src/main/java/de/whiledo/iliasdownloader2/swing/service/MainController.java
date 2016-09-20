package de.whiledo.iliasdownloader2.swing.service;

import static de.whiledo.iliasdownloader2.syncrunner.service.ServiceFunctions.ILIASDOWNLOADER_ANDROID_WEBSITE;
import static de.whiledo.iliasdownloader2.syncrunner.service.ServiceFunctions.ILIASDOWNLOADER_WEBSITE;
import static de.whiledo.iliasdownloader2.syncrunner.service.ServiceFunctions.readProperties;
import static de.whiledo.iliasdownloader2.syncrunner.service.ServiceFunctions.saveProperties;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import org.apache.commons.io.IOUtils;

import de.adesso.adzubix.jtablex.JTableX;
import de.adesso.adzubix.util.FunctionsX;
import de.adesso.adzubix.util.ObjectDoInterface;
import de.adesso.adzubix.util.TwoObjects;
import de.whiledo.iliasdownloader2.exception.IliasException;
import de.whiledo.iliasdownloader2.exception.IliasHTTPSException;
import de.whiledo.iliasdownloader2.service.FileSync;
import de.whiledo.iliasdownloader2.service.IliasUtil;
import de.whiledo.iliasdownloader2.swing.coursechooser.CourseChooserContr;
import de.whiledo.iliasdownloader2.swing.util.StatusCount;
import de.whiledo.iliasdownloader2.syncrunner.service.IliasProperties;
import de.whiledo.iliasdownloader2.syncrunner.service.IliasSyncListener;
import de.whiledo.iliasdownloader2.syncrunner.service.ServiceFunctions;
import de.whiledo.iliasdownloader2.syncrunner.service.SyncService;
import de.whiledo.iliasdownloader2.util.CircularStream;
import de.whiledo.iliasdownloader2.util.DownloadMethod;
import de.whiledo.iliasdownloader2.util.FileObject;
import de.whiledo.iliasdownloader2.util.LoginType;
import de.whiledo.iliasdownloader2.util.ObjectDoInterfaceX;
import de.whiledo.iliasdownloader2.util.SyncProgressListener;
import lombok.Getter;
import lombok.val;

public class MainController implements SyncProgressListener, IliasSyncListener {


	private MainFrame mainFrame;

	private IliasProperties iliasProperties;

	private static final String ERRORTEXT_CONNECTION_ERROR = "Die Verbindung konnte nicht hergestellt werden. Überprüfen Sie Ihre Logindaten und die Serveradresse.";
	private static final String EMAIL = "info@whiledo.de";

	public static final String APP_NAME = "ILIAS Downloader 2.1.3";

	private StatusCount statusCount = new StatusCount();
	
	@Getter
	private FileObjectTableModel fileObjectTableModel;

	private TrayIcon trayIcon;

	private SyncService syncService;

	public MainController(){
		iliasProperties = readProperties();

		if(iliasProperties.getLookAndFeel() == null || iliasProperties.getLookAndFeel().trim().isEmpty()){
			iliasProperties.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}

		try {
			UIManager.setLookAndFeel(iliasProperties.getLookAndFeel());
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		fileObjectTableModel = new FileObjectTableModel();
		mainFrame = new MainFrame(this, initMenuBar());

		//		initTableFiles();
		handleFirstStart();


		mainFrame.getFieldLogin().setText(iliasProperties.getUserName());
		updateTitleCaption();
		mainFrame.getCheckboxNotDownload().setSelected(!iliasProperties.isAllowDownload());

		if(!mainFrame.getFieldLogin().getText().trim().isEmpty()){
			mainFrame.getFieldPassword().requestFocus();
		}

		syncService = new SyncService(this, this,
				new ObjectDoInterfaceX<Void, IliasProperties>() {//IliasProperties Callback

			@Override
			public IliasProperties doSomething(Void object) {
				iliasProperties.setUserName(mainFrame.getFieldLogin().getText());
				iliasProperties.setAllowDownload(!mainFrame.getCheckboxNotDownload().isSelected());
				saveProperties(iliasProperties);
				return iliasProperties;
			}
		}, 
		new ObjectDoInterfaceX<Void, String>() {//Password Callback

			@Override
			public String doSomething(Void object) {
				return new String(mainFrame.getFieldPassword().getPassword());
			}
		},
		new ObjectDoInterfaceX<Throwable, Void>() {

			@Override
			public Void doSomething(Throwable e) {
				showError("Fehler bei der Dateisynchronisierung: " + e.getMessage(), e);
				return null;
			}
		});


		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowIconified(WindowEvent e) {
				minimizeToTray();
			}
			@Override
			public void windowClosing(WindowEvent e) {
				try{
					syncService.logoutIfLoggedIn();
				}catch(Exception ex){
					System.exit(0);
				}
			}
		});


		startOrStopAutoSync();
	}



	private void handleFirstStart() {

		if(!ServiceFunctions.getPropertiesFile().exists()){
			JOptionPane.showMessageDialog(mainFrame, "Willkommen bei " + APP_NAME, "Willkommen", JOptionPane.INFORMATION_MESSAGE);


			JPanel panel = new JPanel(new BorderLayout());
			panel.add(new JLabel("<html>Wenn Sie " + APP_NAME + " nutzen möchten, müssen Sie die folgende Punkte akzeptieren."
					+ "<ul>"
					+ "<li>This software is under the GNU GENERAL PUBLIC LICENSE Version 3.<br>"
					+ "View https://github.com/kekru/ILIASDownloader2/blob/master/LICENSE for detailed information."
					+ "</li>"
					+ "<li>Dieses Programm wird Ihr Passwort nicht speichern</li>"
					+ "<li>Dieses Programm wird Ihren Loginnamen und Ihr Passwort nur an den von Ihnen angegebenen Server senden</li>"
					+ "<li>Ihr Passwort wird im Arbeitsspeicher dieses Computers <b>nicht</b> verschlüsselt gespeichert.<br>Ein Schadprogramm könnte den Arbeitsspeicher auslesen und so an Ihre Logindaten gelangen.<br>Der Autor von " + APP_NAME + " übernimmt keine Verantwortung für die Sicherheit Ihrer Logindaten</li>"
					+ "<li>Im nächsten Schritt müssen Sie Ihren Ilias Server eingeben. Bitte achten Sie darauf, dass die Adresse mit 'https://' beginnt.<br>Das bewirkt eine gesicherte Verbindung zwischen " + APP_NAME + " und Ihrem Ilias Server</li>"
					+ "<li>Die Nutzung und die Weitergabe von " + APP_NAME + " ist kostenlos.</li>"					
					+ "<li>Beim Programmstart wird auf Updates überprüft. Dabei wird lediglich der Programmname an den Updateserver gesendet. Ihre Logindaten oder andere persönliche Daten werden nicht übertragen.<br>Wenn Sie die Updatefunktion ausschalten möchten, starten Sie das Programm mit dem Parameter '"+StartGui.NO_UPDATER+"'</li>"
					+ "</ul>"
					+ "</html>"), BorderLayout.NORTH);
			JButton licenseButton = new JButton("Open GNU GENERAL PUBLIC LICENSE Version 3");
			licenseButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					openWebsiteLicense();
					
				}
			});
			panel.add(licenseButton, BorderLayout.CENTER);
			
			JCheckBox checkboxAccept = new JCheckBox("Ich akzeptiere die hier aufgeführten Bedingungen/I accept these agreements.");
			checkboxAccept.setSelected(false);
			panel.add(checkboxAccept, BorderLayout.SOUTH);

			if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(mainFrame, panel, "Bedingungen", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE) && checkboxAccept.isSelected()){
				chooseServer();
			}else{
				System.exit(0);
			}

		}
	}


	public JTableX<FileObject> generateFileObjectTable(final FileObjectTableModel tableModel) {
		val table = new JTableX<FileObject>();
		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e){
				fileTableClicked(tableModel, table, e);
			}
		});
		table.setRowSelectionAllowed(true);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		table.setModelAndRenderer(tableModel);
		table.getColumnModel().getColumn(0).setMaxWidth(250);
		table.getColumnModel().getColumn(0).setPreferredWidth(150);
		table.getColumnModel().getColumn(3).setPreferredWidth(150);
		table.getColumnModel().getColumn(3).setMaxWidth(250);
		table.getColumnModel().getColumn(4).setMaxWidth(150);
		table.getColumnModel().getColumn(5).setMaxWidth(150);

		return table;
	}

	private void updateTitleCaption() {
		String s;
		try{
			s = IliasUtil.getIliasInstallationURL(iliasProperties.getIliasServerURL());
		}catch(Exception e){
			s = "Fehlerhafte Serveradresse";
		}
		if(mainFrame != null){
			mainFrame.setTitleCapture(s);
		}
	}


	public void buttonClickedSynchronize() {
		try{
			syncService.startOrStopSync();
		}catch(IliasException e){
			showError(ERRORTEXT_CONNECTION_ERROR, e);
		}
	}


	private boolean login() {
		try{
			syncService.login();
		}catch(IliasException e){
			showError(ERRORTEXT_CONNECTION_ERROR, e);
			return false;
		}
		return true;
	}


	public static Icon getFileIcon(File f){
		if(f.exists()){
			return FileSystemView.getFileSystemView().getSystemIcon(f);
		}

		try {
			return FunctionsX.getSystemIcon(f.getAbsolutePath());
		} catch (IOException e) {
			return null;
		}
	}


	public void progress(int percent) {
		mainFrame.getProgressBar().setValue(percent);		
	}

	public void fileLoadStart(FileObject fileObject) {
		addFileObjectToTable(fileObject);
	}

	public void fileLoadEnd(FileObject fileObject) {
		addFileObjectToTable(fileObject);

		switch (fileObject.getSyncState()) {
		case UPDATED:
			statusCount.addUpdated(fileObject);
			break;
		case ALREADY_UP_TO_DATE:
			statusCount.addOld(fileObject);
			break;
			
		case CORRUPT:
		case ERROR:
			statusCount.addError(fileObject);
			break;

		default:
			statusCount.addIgnored(fileObject);
			break;
		}
		updateNewFilesButton();
	}

	private void addFileObjectToTable(FileObject fileObject) {
		int index = fileObjectTableModel.getRowObjects().indexOf(fileObject);
		if(index != -1){
			fileObjectTableModel.getRowObjects().set(index, fileObject);
		}else{
			fileObjectTableModel.getRowObjects().add(fileObject);
		}
		fileObjectTableModel.updateTable();
	}

	public void fileTableClicked(FileObjectTableModel fileObjectTableModel, JTableX<FileObject> table, MouseEvent e) {
		int row = table.convertRowIndexToModel(table.rowAtPoint(e.getPoint()));
		//int column = table.convertColumnIndexToModel(table.columnAtPoint(e.getPoint()));
		final Collection<FileObject> selectedFileObjects = table.getSelectedObjects();

		final FileObject f = fileObjectTableModel.getRowObjects().get(row);

		if(SwingUtilities.isRightMouseButton(e)){
			JPopupMenu m = new JPopupMenu();
			{
				JMenuItem menuitem = new JMenuItem("Öffnen");
				menuitem.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						openFile(f);
					}
				});
				m.add(menuitem);
			}
			{
				JMenuItem menuitem = new JMenuItem("Ordner Öffnen");
				menuitem.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						openFolder(f);
					}
				});
				m.add(menuitem);
			}
			{
				JMenuItem menuitem = new JMenuItem("Herunterladen (über SOAP)");
				menuitem.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						downloadFiles(selectedFileObjects, DownloadMethod.WEBSERVICE);
					}


				});
				m.add(menuitem);
			}
			{
				JMenuItem menuitem = new JMenuItem("Herunterladen (über WEBDAV)");
				menuitem.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						if(!syncService.getIliasSoapService().isWebdavAuthenticationActive()){
							syncService.getIliasSoapService().enableWebdavAuthentication(mainFrame.getFieldLogin().getText(), mainFrame.getFieldPassword().getPassword());
						}
						downloadFiles(selectedFileObjects, DownloadMethod.WEBDAV);
					}


				});
				m.add(menuitem);
			}
			{
				JMenuItem menuitem = new JMenuItem("In Ilias öffnen");
				menuitem.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						openInIlias(f);
					}
				});
				m.add(menuitem);
			}
			{
				m.add(new JSeparator());
			}
			{
				JCheckBoxMenuItem menuitem = new JCheckBoxMenuItem("Ignorieren");
				menuitem.setSelected(iliasProperties.getBlockedFiles().contains(f.getRefId()));
				menuitem.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						switchIgnoreState(selectedFileObjects);
					}
				});
				m.add(menuitem);
			}
			{
				m.add(new JSeparator());
			}
			{
				JMenuItem menuitem = new JMenuItem("Fehler anzeigen");
				menuitem.setEnabled(f.getException() != null);
				menuitem.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						showError("Fehler bei " + f.getTargetFile().getAbsolutePath(), f.getException());
					}

				});
				m.add(menuitem);
			}
			m.show(table, e.getX(), e.getY());
		}

		if(SwingUtilities.isLeftMouseButton(e) && e.getClickCount() > 1){
			openFile(f);
		}
	}

	protected void switchIgnoreState(Collection<FileObject> selectedFileObjects) {
		for(FileObject f : selectedFileObjects){
			if(iliasProperties.getBlockedFiles().contains(f.getRefId())){
				iliasProperties.getBlockedFiles().remove(f.getRefId());
			}else{
				iliasProperties.getBlockedFiles().add(f.getRefId());
			}
		}
		saveProperties(iliasProperties);
	}

	private void downloadFiles(final Collection<FileObject> selectedFileObjects, final DownloadMethod downloadMethod) {
		new Thread(new Runnable() {

			@Override
			public void run() {

				if(login()){
					for(FileObject fileObject : selectedFileObjects){
						try{
							syncService.getFileSync().loadFileOrExerciseIgoreDownloadFileFlag(fileObject.getXmlObject(), downloadMethod);
						}catch(Exception e){
							showError("Die Datei "+fileObject.getTargetFile().getAbsolutePath() + " konnte nicht heruntegeladen werden", e);
						}
					}
					syncService.logoutIfNotRunning();
				}
			}
		}).start();
	}

	private void openFolder(FileObject f) {
		String folder = f.getTargetFile().getAbsolutePath();
		folder = folder.substring(0, folder.lastIndexOf(File.separator));
		try {
			Desktop.getDesktop().open(new File(folder));
		} catch (Exception e) {
			showError("Fehler beim Öffnen von " + folder, e);
		}
	}

	private void showError(final String description, final Throwable e) {

		if(trayIcon != null && iliasProperties.isShowNotifications()){
			trayIcon.displayMessage(APP_NAME, "Fehler: "+ description, MessageType.ERROR);
			trayIcon.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent ev) {
					if(trayIcon != null){
						trayIcon.removeActionListener(this);
					}
					showError(mainFrame, description, e);
				}
			});

		}else{
			showError(mainFrame, description, e);
		}
	}


	public static void showError(final Component parent, String description, final Throwable e) {
		String s = "";
		if(e instanceof IliasHTTPSException){
			s = "<br><br>Die SSL Verbindung konnte nicht aufgebaut werden."
					+ "<br>Sie benutzen Java Version "+System.getProperty("java.version")+"."
					+ "<br>Für einige SSL Verbindungen benötigen Sie <b>Java 1.8 oder höher.</b>"
					+ "<br>Sie können Java hier herunterladen: http://java.com"
					+ "<br><br>Alternativ können Sie in den Einstellungen beim Serverpfad https:// durch http:// ersetzen."
					+ "<br><b>Das wird aber NICHT empfohlen, da Ihr Loginname und Ihr Passwort ungeschützt übertragen werden.</b>";
		}

		description = description + s;

		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.add(new JLabel("<html>"+description+"</html>"));

		JButton b = new JButton("Details");
		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ev) {
				final JTextArea textarea = new JTextArea();
				val c = new CircularStream();

				Thread t = new Thread(new Runnable() {

					@Override
					public void run() {
						e.printStackTrace(new PrintStream(c.getOutputStream()));
						IOUtils.closeQuietly(c.getOutputStream());
					}
				});
				t.start();

				try {
					textarea.append(IOUtils.toString(c.getInputStream()));
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				try {
					t.join();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}


				JPanel p = new JPanel(new BorderLayout());
				p.add(new JLabel(e.getMessage()), BorderLayout.NORTH);

				JScrollPane scrollpane = new JScrollPane(textarea);
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				p.add(scrollpane, BorderLayout.CENTER);
				p.setMaximumSize(new Dimension(screenSize.width/2, screenSize.height/2));
				p.setPreferredSize(new Dimension(screenSize.width/2, screenSize.height/2));
				p.setSize(new Dimension(screenSize.width/2, screenSize.height/2));

				JOptionPane.showMessageDialog(parent, p, "Fehlerdetails", JOptionPane.ERROR_MESSAGE);

			}
		});
		panel.add(b);

		JOptionPane.showMessageDialog(parent, panel, "Fehler", JOptionPane.ERROR_MESSAGE);
	}


	private void openFile(final FileObject f){

		new Thread(new Runnable() {

			@Override
			public void run() {
				File file = f.getTargetFile();
				if(!file.exists() || file.length() != f.getFileSize()){

					try{
						if(login()){
							file = syncService.getFileSync().loadFileOrExerciseTemp(f);
							syncService.logoutIfNotRunning();
						}
					}catch(Exception e){
						showError("Die Datei "+file.getAbsolutePath() + " konnte nicht heruntegeladen werden", e);
					}
				}
				try {
					Desktop.getDesktop().open(file);
				} catch (Exception e) {
					showError("Fehler beim Öffnen von " + file.getAbsolutePath(), e);
				}
			}
		}).start();
	}


	private JMenuBar initMenuBar(){
		JMenuBar menubar = new JMenuBar();
		{
			JMenu menu = new JMenu("Synchronisation");
			{
				JMenuItem m = new JMenuItem("Kurse auswählen");
				m.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						chooseCourses();
					}
				});
				menu.add(m);
			}
			{
				JMenuItem m = new JMenuItem("Automatische Synchronisation");
				m.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						showAutoSyncSettings();
					}
				});
				menu.add(m);
			}
			{
				JMenuItem m = new JMenuItem("Neue Dateien zeigen");
				m.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						showNewFiles();
					}
				});
				menu.add(m);
			}
			menubar.add(menu);
		}
		{
			JMenu menu = new JMenu("Einstellungen");
			{
				JMenuItem m = new JMenuItem("Speicherpfad ändern");
				m.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						changeBaseDir();
					}
				});
				menu.add(m);
			}		
			{
				JMenuItem m = new JMenuItem("Speicherpfad öffnen");
				m.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						openBaseDir();
					}
				});
				menu.add(m);
			}		
			{
				JMenuItem m = new JMenuItem("Maximale Dateigröße ändern");
				m.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						changeMaxFileSize();
					}
				});
				menu.add(m);
			}
			{
				final JCheckBoxMenuItem menuitem = new JCheckBoxMenuItem("Benachrichtigungen im Systemtray anzeigen");
				menuitem.setSelected(iliasProperties.isShowNotifications());
				menuitem.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						iliasProperties.setShowNotifications(!iliasProperties.isShowNotifications());
						saveProperties(iliasProperties);
						menuitem.setSelected(iliasProperties.isShowNotifications());
					}
				});
				menu.add(menuitem);
			}
			{
				menu.add(new JSeparator());
			}			
			{
				JMenuItem m = new JMenuItem("Design ändern");
				m.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						changeLookAndFeel();
					}
				});
				menu.add(m);
			}		
			{
				menu.add(new JSeparator());
			}			
			{
				JMenuItem m = new JMenuItem("Server einstellen");
				m.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						chooseServer();
					}
				});
				menu.add(m);
			}		
			menubar.add(menu);
		}
		{
			JMenu menu = new JMenu("Hilfe");
			{
				JMenuItem m = new JMenuItem("Fehler melden, Feedback geben");
				m.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						openFeedbackPage();
					}
				});
				menu.add(m);
			}
			{
				JMenuItem m = new JMenuItem("Webseite");
				m.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						openWebsite();
					}
				});
				menu.add(m);
			}
			{
				JMenuItem m = new JMenuItem("Android App");
				m.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						openAndroidWebsite();
					}
				});
				menu.add(m);
			}
			{
				JMenuItem m = new JMenuItem("Info");
				m.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						showInfo();
					}
				});
				menu.add(m);
			}
			menubar.add(menu);
		}
		return menubar;
	}

	protected void showAutoSyncSettings() {
		JCheckBox checkboxAutoSyncActive;
		JTextField fieldSyncInterval;
		JPanel panel = new JPanel(new BorderLayout());
		{
			checkboxAutoSyncActive = new JCheckBox("Automatische Synchronisierung aktiv");
			checkboxAutoSyncActive.setSelected(iliasProperties.isAutoSyncActive());
			panel.add(checkboxAutoSyncActive, BorderLayout.NORTH);
		}
		{
			JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			panel2.add(new JLabel("Intervall in Minuten:"));
			fieldSyncInterval = new JTextField(String.valueOf(iliasProperties.getAutoSyncIntervalInSeconds() / 60), 20);
			panel2.add(fieldSyncInterval);
			panel.add(panel2, BorderLayout.CENTER);
		}
		{
			panel.add(new JLabel("<html>Sie können das Programm auch ohne GUI laufen lassen.<br>Weitere Informationen hierzu erhalten Sie, wenn Sie das Programm folgendermaßen starten<br>java -jar &lt;name&gt;.jar help</html>"), BorderLayout.SOUTH);
		}

		if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(mainFrame, panel, "Automatische Synchronisierung einstellen", JOptionPane.OK_CANCEL_OPTION)){
			iliasProperties.setAutoSyncActive(checkboxAutoSyncActive.isSelected());
			iliasProperties.setAutoSyncIntervalInSeconds(Integer.parseInt(fieldSyncInterval.getText()) * 60);
			saveProperties(iliasProperties);

			startOrStopAutoSync();
		}

	}



	private void startOrStopAutoSync() {
		try{
			if(iliasProperties.isAutoSyncActive()){
				syncService.startIntervalSync(iliasProperties.getAutoSyncIntervalInSeconds() * 1000);
			}else{
				syncService.stopIntervalSync();
			}
		}catch(Exception e){
			showError("Auto Sync Fehler", e);
		}
	}

	protected void openFeedbackPage() {
		String s = "https://whiledo.de/index.php?s=6";
		try {
			Desktop.getDesktop().browse(new URI(s));
		} catch (IOException | URISyntaxException e) {
			showError("Feedbackseite konnte nicht geöffnet werden. Besuchen Sie " + s, e);
		}

	}

	protected void openEMail() {
		try {
			Desktop.getDesktop().browse(new URI("mailto:" + EMAIL));
		} catch (IOException | URISyntaxException e) {
			showError("Das E-Mailprogramm konnte nicht geöffnet werden. Schreiben Sie an: " + EMAIL, e);
		}

	}

	protected void showInfo() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JLabel("<html>Kevin Krummenauer 2015<br><br>Icons by http://jonasraskdesign.com<br><br></html>"), BorderLayout.NORTH);
		JLabel link = new JLabel("Kontakt: " + EMAIL);
		link.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e){
				openEMail();
			}

		});
		link.setCursor(new Cursor(Cursor.HAND_CURSOR));
		link.setForeground(Color.BLUE);
		panel.add(link, BorderLayout.CENTER);

		JPanel panel2 = new JPanel(new GridLayout(0, 1, 4, 4));
		{
			panel2.add(new JLabel("<html><b>Libraries and its licenses</b> (click to open)</html>"), BorderLayout.NORTH);

			val list = Arrays.asList(
					new TwoObjects<String, String>("ILIAS Downloader 2", ServiceFunctions.ILIASDOWNLOADER_WEBSITE_LICENSE),
					new TwoObjects<String, String>("Java JDK 8", "http://www.oracle.com/technetwork/java/javase/terms/license/index.html"),
					new TwoObjects<String, String>("Apache Commons IO und Apache Commons Codec", "apache_license.txt"),
					new TwoObjects<String, String>("Project Lombok", "lombok_license.txt"),
					new TwoObjects<String, String>("KSOAP2", "ksoap2_android.txt"),
					new TwoObjects<String, String>("Simple XML Serialization", "apache_license.txt")					      
					);
			for(val lib : list){
				link = new JLabel(lib.getObjectA());

				if(lib.getObjectB() != null){
					link.addMouseListener(new MouseAdapter() {

						@Override
						public void mouseClicked(MouseEvent e){
							String s = lib.getObjectB();
							try {
								if(s.toLowerCase().startsWith("http")){
									Desktop.getDesktop().browse(new URI(s));
								}else{								
									showLicense(s);
								}
							} catch (Exception e1) {
								showError("Lizenz nicht gefunden: " + s, e1);
							}
						}

					});
					link.setCursor(new Cursor(Cursor.HAND_CURSOR));
					link.setForeground(Color.BLUE);
				}
				panel2.add(link);
			}
		}
		panel.add(panel2, BorderLayout.SOUTH);

		JOptionPane.showMessageDialog(mainFrame, panel, "Info " + APP_NAME, JOptionPane.INFORMATION_MESSAGE);

	}

	protected void showLicense(String licenseFile) throws IOException {
		final JTextArea textarea = new JTextArea(IOUtils.toString(FileSync.class.getResourceAsStream("/de/whiledo/iliasdownloader2/licenses/"+licenseFile)));

		JPanel p = new JPanel(new BorderLayout());

		JScrollPane scrollpane = new JScrollPane(textarea);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		p.add(scrollpane, BorderLayout.CENTER);
		p.setMaximumSize(new Dimension(screenSize.width/2, screenSize.height/2));
		p.setPreferredSize(new Dimension(screenSize.width/2, screenSize.height/2));
		p.setSize(new Dimension(screenSize.width/2, screenSize.height/2));

		JOptionPane.showMessageDialog(mainFrame, p, "Lizenz", JOptionPane.INFORMATION_MESSAGE);

	}



	private void openWebsite() {
		String s = ILIASDOWNLOADER_WEBSITE;
		try {
			Desktop.getDesktop().browse(new URI(s));
		} catch (IOException | URISyntaxException e) {
			showError("Fehler beim Öffnen von: " + s, e);
		}
	}
	
	private void openWebsiteLicense() {
		String s = ServiceFunctions.ILIASDOWNLOADER_WEBSITE_LICENSE;
		try {
			Desktop.getDesktop().browse(new URI(s));
		} catch (IOException | URISyntaxException e) {
			showError("Fehler beim Öffnen von: " + s, e);
		}
	}

	private void openAndroidWebsite() {
		String s = ILIASDOWNLOADER_ANDROID_WEBSITE;
		try {
			Desktop.getDesktop().browse(new URI(s));
		} catch (IOException | URISyntaxException e) {
			showError("Fehler beim Öffnen von: " + s, e);
		}
	}

	protected void chooseServer() {
		final TwoObjects<String, String> serverAndClientId = new TwoObjects<String, String>(iliasProperties.getIliasServerURL(), iliasProperties.getIliasClient());
		final boolean noConfigDoneYet = serverAndClientId.getObjectA() == null || serverAndClientId.getObjectA().trim().isEmpty();

		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		final JLabel labelServer = new JLabel("Server: " + serverAndClientId.getObjectA());
		final JLabel labelClientId = new JLabel("Client Id: " + serverAndClientId.getObjectB());

		JComboBox<LoginType> comboboxLoginType = null;
		JComboBox<DownloadMethod> comboboxDownloadMethod = null;

		final Runnable findOutClientId = new Runnable() {

			@Override
			public void run() {
				try{
					String s = IliasUtil.findClientByLoginPageOrWebserviceURL(serverAndClientId.getObjectA());
					serverAndClientId.setObjectB(s);
					labelClientId.setText("Client Id: " + s);
				}catch(Exception e1){
					showError("Client Id konnte nicht ermittelt werden", e1);
				}
			}
		};

		final Runnable promptInputServer = new Runnable() {

			@Override
			public void run() {

				String s = JOptionPane.showInputDialog(panel, "Geben Sie die Ilias Loginseitenadresse oder Webserviceadresse ein", serverAndClientId.getObjectA());
				if(s != null){
					try{
						s = IliasUtil.findSOAPWebserviceByLoginPage(s.trim());

						if(!s.toLowerCase().startsWith("https://")){
							JOptionPane.showMessageDialog(mainFrame, "Achtung! Die von Ihnen angegebene Adresse beginnt nicht mit 'https://'.\nDie Verbindung ist daher nicht ausreichend gesichert. Ein Angreifer könnte Ihre Ilias Daten und Ihr Passwort abgreifen", "Achtung, nicht geschützt", JOptionPane.WARNING_MESSAGE);
						}

						serverAndClientId.setObjectA(s);
						labelServer.setText("Server: " + serverAndClientId.getObjectA());

						if(noConfigDoneYet){
							findOutClientId.run();
						}
					}catch(IliasException e1){
						showError("Bitte geben Sie die Adresse der Ilias Loginseite oder die des Webservice an. Die Adresse der Loginseite muss 'login.php' enthalten", e1);
					}
				}
			}
		};

		{
			JPanel panel2 = new JPanel(new BorderLayout());

			panel2.add(labelServer, BorderLayout.NORTH);
			panel2.add(labelClientId, BorderLayout.SOUTH);

			panel.add(panel2, BorderLayout.NORTH);
		}
		{
			JPanel panel2 = new JPanel(new BorderLayout());

			JPanel panel3 = new JPanel(new GridLayout());
			{
				JButton b = new JButton("Server ändern");
				b.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						promptInputServer.run();
					}
				});
				panel3.add(b);

				b = new JButton("Client Id ändern");
				b.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						String s = JOptionPane.showInputDialog(panel, "Client Id eingeben", serverAndClientId.getObjectB());
						if(s != null){
							serverAndClientId.setObjectB(s);
							labelClientId.setText("Client Id: " + serverAndClientId.getObjectB());
						}

					}
				});
				panel3.add(b);

				b = new JButton("Client Id automatisch ermitteln");
				b.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						findOutClientId.run();
					}
				});
				panel3.add(b);
			}
			panel2.add(panel3, BorderLayout.NORTH);

			panel3 = new JPanel(new GridLayout(0, 2, 5, 2));
			{
				panel3.add(new JLabel("Loginmethode: "));
				comboboxLoginType = new JComboBox<LoginType>();
				FunctionsX.setComboBoxLayoutString(comboboxLoginType, new ObjectDoInterface<LoginType, String>() {

					@Override
					public String doSomething(LoginType loginType) {
						switch(loginType){
						case DEFAULT: return "Standard";
						case LDAP: return "LDAP";
						case CAS: return "CAS";
						default: return "<Fehler>";
						}
					}

				});
				val model = ((DefaultComboBoxModel<LoginType>) comboboxLoginType.getModel());
				for(LoginType loginType : LoginType.values()){
					model.addElement(loginType);
				}
				model.setSelectedItem(iliasProperties.getLoginType());
				panel3.add(comboboxLoginType);

				JLabel label = new JLabel("Dateien herunterladen über:");
				label.setToolTipText("Die restliche Kommunikation läuft immer über den SOAP Webservice");
				panel3.add(label);
				comboboxDownloadMethod = new JComboBox<DownloadMethod>();
				FunctionsX.setComboBoxLayoutString(comboboxDownloadMethod, new ObjectDoInterface<DownloadMethod, String>() {

					@Override
					public String doSomething(DownloadMethod downloadMethod) {
						switch(downloadMethod){
						case WEBSERVICE: return "SOAP Webservice (Standard)";
						case WEBDAV: return "WEBDAV";
						default: return "<Fehler>";
						}
					}

				});
				val model2 = ((DefaultComboBoxModel<DownloadMethod>) comboboxDownloadMethod.getModel());
				for(DownloadMethod downloadMethod : DownloadMethod.values()){
					model2.addElement(downloadMethod);
				}
				model2.setSelectedItem(iliasProperties.getDownloadMethod());
				panel3.add(comboboxDownloadMethod);
			}
			panel2.add(panel3, BorderLayout.WEST);

			panel.add(panel2, BorderLayout.SOUTH);
		}

		if(noConfigDoneYet){
			promptInputServer.run();
		}

		if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(mainFrame, panel, "Server konfigurieren", JOptionPane.OK_CANCEL_OPTION)){
			if(syncService != null){
				syncService.logoutIfLoggedIn();
			}
			iliasProperties.setIliasServerURL(serverAndClientId.getObjectA());
			iliasProperties.setIliasClient(serverAndClientId.getObjectB());
			iliasProperties.setLoginType((LoginType) comboboxLoginType.getSelectedItem());
			iliasProperties.setDownloadMethod((DownloadMethod) comboboxDownloadMethod.getSelectedItem());
			saveProperties(iliasProperties);
			updateTitleCaption();
		}

	}

	@SuppressWarnings("unchecked")
	protected void changeLookAndFeel() {
		final JDialog dialog = new JDialog(mainFrame, "Design ändern");
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);


		Collection<TwoObjects<String, String>> lookAndFeels = FunctionsX.asList(
				new TwoObjects<String, String>("Metal", UIManager.getCrossPlatformLookAndFeelClassName()),
				new TwoObjects<String, String>("Betriebssystem Standard", UIManager.getSystemLookAndFeelClassName()),
				new TwoObjects<String, String>("Nimbus", NimbusLookAndFeel.class.getName())
				);

		dialog.setLayout(new GridLayout(0, 1));
		ButtonGroup b = new ButtonGroup();
		for(final TwoObjects<String, String> lookAndFeel : lookAndFeels){
			JRadioButton rb = new JRadioButton(lookAndFeel.getObjectA());
			if(iliasProperties.getLookAndFeel().equals(lookAndFeel.getObjectB())){
				rb.setSelected(true);
			}
			rb.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						UIManager.setLookAndFeel(lookAndFeel.getObjectB());
						iliasProperties.setLookAndFeel(lookAndFeel.getObjectB());
						saveProperties(iliasProperties);
					} catch (ClassNotFoundException | InstantiationException
							| IllegalAccessException
							| UnsupportedLookAndFeelException e1) {
						showError("Fehler beim Ändern des Designs", e1);
					}
					SwingUtilities.updateComponentTreeUI(mainFrame);
					SwingUtilities.updateComponentTreeUI(dialog);

				}
			});
			b.add(rb);
			dialog.add(rb);
		}

		dialog.pack();
		dialog.setLocationRelativeTo(mainFrame);
		dialog.setVisible(true);		

	}

	protected void chooseCourses() {
		try{
			if(login()){
				CourseChooserContr courseChooserContr = new CourseChooserContr(iliasProperties.isSyncAll(), iliasProperties.getActiveCourses(), syncService.getIliasSoapService());
				if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(mainFrame, courseChooserContr.getView(), "Kurse auswählen", JOptionPane.OK_CANCEL_OPTION)){
					iliasProperties.setActiveCourses(courseChooserContr.getActiveCourses());
					iliasProperties.setSyncAll(courseChooserContr.isSyncAll());
					saveProperties(iliasProperties);
				}
			}
		}catch(Exception e){
			showError("Fehler beim Ändern der Kursinformationen", e);
		}
	}

	protected void changeMaxFileSize() {
		final JPanel panel = new JPanel();
		boolean keineLimitierung = iliasProperties.getMaxFileSize() == Long.MAX_VALUE;

		panel.setLayout(new GridLayout(1,0));

		panel.add(new JLabel("Maximale Dateigröße in MB"));

		final JCheckBox cb = new JCheckBox("Keine Limitierung");
		cb.setSelected(keineLimitierung);
		panel.add(cb);


		final JTextField fieldFileSize = new JTextField(String.valueOf(iliasProperties.getMaxFileSize() / (1024 * 1024)));
		panel.add(fieldFileSize);

		fieldFileSize.setVisible(!cb.isSelected());

		cb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fieldFileSize.setVisible(!cb.isSelected());

			}
		});

		if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(mainFrame, panel, "Maximale Dateigröße Ändern", JOptionPane.OK_CANCEL_OPTION)){

			if(cb.isSelected()){
				iliasProperties.setMaxFileSize(Long.MAX_VALUE);
			}else{
				iliasProperties.setMaxFileSize(Long.parseLong(fieldFileSize.getText()) * 1024 * 1024);
			}
			saveProperties(iliasProperties);
		}		

	}

	protected void openBaseDir() {
		try {
			Desktop.getDesktop().open(new File(iliasProperties.getBaseDirectory()));
		} catch (IOException e) {
			showError("Fehler beim öffnen von " + iliasProperties.getBaseDirectory(), e);
		}

	}

	protected void changeBaseDir() {
		File baseDir = new File(iliasProperties.getBaseDirectory());
		JFileChooser fileChooser = new JFileChooser(baseDir);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		if(JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(mainFrame)){
			String s = fileChooser.getSelectedFile().getAbsolutePath();
			JOptionPane.showMessageDialog(mainFrame, "Der neue Speicherort ist jetzt "+s, "Neuer Speicherort", JOptionPane.INFORMATION_MESSAGE);
			iliasProperties.setBaseDirectory(s);
			saveProperties(iliasProperties);
		}else{
			JOptionPane.showMessageDialog(mainFrame, "Der alte Speicherort wird beibehalten "+baseDir.getAbsolutePath(), "Speicherort", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void openInIlias(final FileObject f) {
		String s = syncService.getIliasSoapService().getURLInIlias(f.getXmlObject());
		try {
			Desktop.getDesktop().browse(new URI(s));
		} catch (IOException | URISyntaxException e1) {
			JOptionPane.showMessageDialog(mainFrame, "Fehler beim Öffnen von " + s);
		}
	}

	public static ImageIcon getImage(String name){
		return new ImageIcon(MainController.class.getResource("/de/whiledo/iliasdownloader2/swing/images/" + name));
	}





	private void minimizeToTray(){

		if(SystemTray.isSupported()){

			trayIcon = new TrayIcon(getImage("open_alt-small.png").getImage());

			trayIcon.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e){
					trayIconClicked();
				}
			});

			trayIcon.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					trayIconClicked();
				}
			});

			//		trayIcon.setPopupMenu(popup);



			trayIcon.setToolTip(APP_NAME);


			try {
				SystemTray.getSystemTray().add(trayIcon);
				mainFrame.setVisible(false);
			} catch (AWTException e1) {
				throw new RuntimeException(e1);	
			}

		}
	}

	protected void trayIconClicked() {
		mainFrame.setVisible(true);
		mainFrame.setExtendedState(JFrame.NORMAL);
		SystemTray.getSystemTray().remove(trayIcon);
		trayIcon = null;
	}



	@Override
	public void syncFinished() {
		mainFrame.getProgressBar().setVisible(false);
		mainFrame.getButtonSync().setText(MainFrame.SYNCHRONISIERE);

		if(iliasProperties.isShowNotifications() && trayIcon != null){
			int updatedFileCount = 0;
			int errorFileCount = 0;

			for(val f : fileObjectTableModel.getRowObjects()){
				switch(f.getSyncState()){
				case UPDATED: updatedFileCount++; break;
				case ERROR: errorFileCount++; break;
				case CORRUPT: errorFileCount++; break;
				default: break;
				}
			}

			if(updatedFileCount > 0 || errorFileCount > 0){
				trayIcon.displayMessage(APP_NAME, "Aktualisiert: " + updatedFileCount + (errorFileCount > 0 ? " Fehler: " + errorFileCount : ""), MessageType.INFO);
			}
		}
	}



	@Override
	public void syncStopped() {
		mainFrame.getButtonSync().setText(MainFrame.SYNCHRONISIERE);
		mainFrame.getProgressBar().setVisible(false);
	}



	@Override
	public void syncCoursesFound(Collection<Long> activeCourseIds, Collection<Long> allCourseIds) {
		mainFrame.updateCourseCount(activeCourseIds.size(), allCourseIds.size());
	}



	@Override
	public void syncStarted() {
		mainFrame.getButtonSync().setText("Stop");
		mainFrame.getProgressBar().setValue(0);
		mainFrame.getProgressBar().setVisible(true);		
		statusCount.clearTemp();
		fileObjectTableModel.getRowObjects().clear();
	}



	public void showNewFiles() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JLabel("Aktualisierte Dateien:"), BorderLayout.NORTH);

		final FileObjectTableModel tableModel = new FileObjectTableModel();
		tableModel.setRowObjects(statusCount.getUpdatedFilesAll());
		JTableX<FileObject> table = generateFileObjectTable(tableModel);

		JScrollPane scrollpane = new JScrollPane(table);
		scrollpane.setPreferredSize(new Dimension(mainFrame.getSize().width -100, mainFrame.getSize().height -200));
		panel.add(scrollpane, BorderLayout.CENTER);

		JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton buttonClear = new JButton("Liste leeren");
		buttonClear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				statusCount.clearAll();
				updateNewFilesButton();
				tableModel.updateTable();
			}
		});
		panel2.add(buttonClear);
		panel.add(panel2, BorderLayout.SOUTH);

		JOptionPane.showMessageDialog(mainFrame, panel, "Neue Dateien", JOptionPane.INFORMATION_MESSAGE);

	}


	protected void updateNewFilesButton() {
		mainFrame.getButtonShowNewFiles().setText(MainFrame.SHOW_NEW_FILES + (statusCount.getUpdatedFilesAll().isEmpty() ? "" : " ("+statusCount.getUpdatedFilesAll().size()+")"));
		mainFrame.updateLabelStatus(statusCount);
	}

}
