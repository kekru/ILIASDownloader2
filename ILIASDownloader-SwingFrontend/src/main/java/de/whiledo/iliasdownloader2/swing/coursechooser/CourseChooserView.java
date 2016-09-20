package de.whiledo.iliasdownloader2.swing.coursechooser;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

import lombok.Getter;
import de.adesso.adzubix.jtablex.JTableX;

public class CourseChooserView extends JPanel {

	private static final long serialVersionUID = 1L;

	@Getter
	private JTableX<Course> table;
	
	@Getter
	private JCheckBox checkboxSyncAll;

	@Getter
	private JProgressBar progressbar;

	public CourseChooserView(final CourseChooserContr courseChooserContr){
		setLayout(new BorderLayout(10, 10));
		
		JPanel p = new JPanel(new BorderLayout());
		p.add(new JLabel("Welche Kurse sollen synchronisiert werden?"), BorderLayout.NORTH);
		p.add(progressbar = new JProgressBar(0, 100), BorderLayout.SOUTH);
		progressbar.setStringPainted(true);
		add(p, BorderLayout.NORTH);
		
		table = new JTableX<Course>();
		add(new JScrollPane(table), BorderLayout.CENTER);

		p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		{
			p.add(checkboxSyncAll = new JCheckBox("Alle Synchronisieren"));
		}
		{
			JButton b = new JButton("Weiteren Kurs hinzufügen");
			b.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					courseChooserContr.addCourse();
				}
			});
			p.add(b);
		}		
		{
			JButton b = new JButton("Passwörter zeigen");
			b.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					courseChooserContr.switchShowPasswords();
				}
			});
			p.add(b);
		}		
		add(p, BorderLayout.SOUTH);
	}
}
