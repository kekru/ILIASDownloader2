package de.whiledo.iliasdownloader2.swing.coursechooser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import de.whiledo.iliasdownloader2.exception.IliasException;
import de.whiledo.iliasdownloader2.service.ILIASSoapService;
import de.whiledo.iliasdownloader2.swing.service.MainController;
import de.whiledo.iliasdownloader2.xmlentities.course.XmlCourse;

public class CourseChooserContr {


	private CourseChooserView courseChooserView;

	private List<Course> activeCourses;

	private ILIASSoapService iliasSoapServiceLoggedIn;

	private CourseTableModel tableModelAndRenderer;

	public CourseChooserContr(boolean syncAll, Collection<Long> activeCourseIds, ILIASSoapService iliasSoapServiceLoggedIn){
		this.iliasSoapServiceLoggedIn = iliasSoapServiceLoggedIn;		

		if(!iliasSoapServiceLoggedIn.isLoggedIn()){
			throw new IliasException("Geben Sie bitte gültige Logindaten ein.");
		}

		requestCourseDataAsync(activeCourseIds, iliasSoapServiceLoggedIn);
		courseChooserView = new CourseChooserView(this);
		courseChooserView.getCheckboxSyncAll().setSelected(syncAll);

		tableModelAndRenderer = new CourseTableModel(courseChooserView.getTable());
		courseChooserView.getProgressbar().setValue(0);
	}

	private void requestCourseDataAsync(final Collection<Long> activeCourseIds, final ILIASSoapService iliasSoapServiceLoggedIn) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try{

					Set<Long> setOfCourses = new HashSet<Long>(activeCourseIds);
					setOfCourses.addAll(iliasSoapServiceLoggedIn.getCourseIds());

					activeCourses = new ArrayList<Course>();

					int i=0;
					for(long refId : setOfCourses){
						try{
							activeCourses.add(getCourse(activeCourseIds.contains(refId), refId));
						}catch(IliasException e){
							MainController.showError(courseChooserView, "Fehler beim Abfragen der Kursinformationen bei Kurs " + refId, e);
						}
						i++;
						courseChooserView.getProgressbar().setValue((int) ((Double.valueOf(i) / setOfCourses.size()) * 100));
					}
					tableModelAndRenderer.setRowObjects(activeCourses);
					tableModelAndRenderer.updateTable();

				}catch(Exception e){
					MainController.showError(courseChooserView, "Fehler beim Abfragen der Kursinformationen", e);
				}
			}
		}).start();
	}

	private Course getCourse(boolean active, long refId) {
		XmlCourse c = iliasSoapServiceLoggedIn.getCourse(refId);
		return new Course(active, c.getMetaData().getGeneral().getTitle(), refId, c.getSettings().getRegistration().getPassword());
	}

	public JComponent getView(){
		return courseChooserView;
	}

	public void addCourse() {
		String s = JOptionPane.showInputDialog(courseChooserView, "Geben Sie die Kurs Id ein (refId).\nDie Kurs Id ist eine Zahl, die Sie in der Webseitenadresse des Kurses finden");
		if(s != null){
			try{
				long refId = Long.parseLong(s);
				tableModelAndRenderer.getRowObjects().add(getCourse(true, refId));
				tableModelAndRenderer.updateTable();
			}catch(NumberFormatException e){
				MainController.showError(courseChooserView, "Bitte geben Sie eine Zahl ein", e);
			}catch(Exception e){
				MainController.showError(courseChooserView, "Die Kursinformationen konnten nicht abgerufen werden", e);
			}
		}
	}

	public Set<Long> getActiveCourses(){
		Set<Long> result = new HashSet<Long>();
		for(Course c : tableModelAndRenderer.getRowObjects()){
			if(c.isActive()){
				result.add(c.getRefId());
			}
		}
		return result;
	}

	public void switchShowPasswords() {
		tableModelAndRenderer.setShowPasswords(!tableModelAndRenderer.isShowPasswords());
		tableModelAndRenderer.updateTable();		
	}

	public boolean isSyncAll(){
		return courseChooserView.getCheckboxSyncAll().isSelected();
	}
}
