package de.whiledo.iliasdownloader2.swing.coursechooser;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;
import javax.swing.JTable;

import lombok.Getter;
import lombok.Setter;
import de.adesso.adzubix.jtablex.JTableX;
import de.adesso.adzubix.jtablex.ModelInfo;
import de.adesso.adzubix.jtablex.TableModelAndRendererX;

@ModelInfo(tableHeadlines={"", "Kurs", "Kurs Id", "Passwort"}, columnClasses={boolean.class, String.class, long.class, String.class})
public class CourseTableModel extends TableModelAndRendererX<Course> {

	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
	private boolean showPasswords;
	
	public CourseTableModel(final JTableX<Course> table){
		table.setModelAndRenderer(this);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e){
				tableClicked(table.convertRowIndexToModel(table.rowAtPoint(e.getPoint())), table.convertColumnIndexToModel(table.columnAtPoint(e.getPoint())));
			}
		});
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_SPACE){
					tableClicked(table.getSelectedModelRow(), 0);
				}
			}
		});
		
		table.getColumnModel().getColumn(0).setMaxWidth(40);
		
//		table.getColumnModel().getColumn(0).setMaxWidth(new JCheckBox().getWidth()+7);
		
//		table.enableRowSorter();
		updateTable();
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){

		if(value instanceof Boolean){
			JCheckBox cb = new JCheckBox();
			cb.setSelected((Boolean) value);
			return cb;
		}

		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}

	@Override
	public Object getValueAt(Course course, int columnIndex){
		switch(columnIndex){
		case 0: return course.isActive();
		case 1: return course.getName();
		case 2: return course.getRefId();
		case 3: return showPasswords ? course.getPassword() : "*****";
		default: throw new RuntimeException("Wrong columnIndex: "+columnIndex);
		}
	}

	public void tableClicked(int row, int column){
		if(column == 0 && row >= 0){
			getRowObjects().get(row).switchActive();
			updateTable();
		}
	}
}
