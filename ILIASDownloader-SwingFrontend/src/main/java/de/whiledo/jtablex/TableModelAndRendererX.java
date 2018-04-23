package de.whiledo.jtablex;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import lombok.Getter;
import lombok.Setter;
import lombok.val;

public abstract class TableModelAndRendererX <T> extends DefaultTableCellRenderer implements TableModel, ModelInterfaceForJTableX<T> {

	private static final long serialVersionUID = 6514323566464465374L;
	@Getter @Setter private List<T> rowObjects;
	private ArrayList<TableModelListener> listeners = new ArrayList<TableModelListener>();
	
	public TableModelAndRendererX(List<T> rowObjects){
		this.rowObjects = rowObjects;
	}
	
	public TableModelAndRendererX(){
		this(new ArrayList<T>());
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}

	@Override
	public int getRowCount() {
		return getRowObjects().size();
	}
	
	public String[] getTableHeadlines(){
		if(getClass().isAnnotationPresent(ModelInfo.class)){
			return getClass().getAnnotation(ModelInfo.class).tableHeadlines();
		}else{
			throw new RuntimeException(getClass().getName() + " muss mit @"+ModelInfo.class.getSimpleName()+" annotiert sein, oder getSpaltenUeberschriften() muss überschrieben werden.");
		}
	}

	@Override
	public int getColumnCount() {

		return getTableHeadlines().length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnIndex > -1 && columnIndex < getTableHeadlines().length ? getTableHeadlines()[columnIndex] : "";
	}

	@Override
	public Class<?> getColumnClass(int columnIndex){
		if(getClass().isAnnotationPresent(ModelInfo.class)){
			val classes = getClass().getAnnotation(ModelInfo.class).columnClasses();
			return classes[columnIndex <= classes.length -1 ? columnIndex : classes.length -1];
			
		}else{
			throw new RuntimeException(getClass().getName() + " muss mit @"+ModelInfo.class.getSimpleName()+" annotiert sein und dessen Attribut columnClasses muss gefüllt sein. Alternativ muss getColumnClass() überschrieben werden.");
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if(getClass().isAnnotationPresent(ModelInfo.class)){
			return getClass().getAnnotation(ModelInfo.class).cellEditable();
		}
		
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex){
		return getValueAt(rowObjects.get(rowIndex), columnIndex);
	}
	
	public Object getValueAt(T rowObject, int columnIndex){
		throw new UnsupportedOperationException("getValueAt(int rowIndex, int columnIndex) oder getValueAt(T rowObject, int columnIndex) muss von "+getClass().getName()+" überschrieben werden");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		getRowObjects().set(rowIndex, (T)aValue);
		updateTable();
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		listeners.add(l);
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		listeners.remove(l);
	}

	public synchronized void updateTable(){
		TableModelEvent e = new TableModelEvent(this);
		for(int i = 0, n = listeners.size(); i < n; i++){
			((TableModelListener) listeners.get(i)).tableChanged(e);
		}
	}
	
}
