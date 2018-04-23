package de.whiledo.jtablex;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * Die Tabelle ist generisch. Die spezifizierende Klasse ist die Klasse von den Objekten, die in der
 * zugehörigen Model-Klasse verwaltet werden. Z.B.: Im Tablemodel sollen "FAQ"-Objekte verwaltet werden,
 * dann <code>JTableX&lt;FAQ&gt; table = new JTableX&lt;FAQ&gt;();</code>
 */
public class JTableX<T> extends JTable {

	private static final long serialVersionUID = 2742371464176771327L;
	private final Map<Integer, RowFilter<Object, Object>> rowFilterMap;


	public JTableX(){
		rowFilterMap = new HashMap<Integer, RowFilter<Object, Object>>();
		//getTableHeader().setReorderingAllowed(false);
	}

	public JTableX(TableModelAndRendererX<T> tableModelAndRendererX){
		this();
		setModelAndRenderer(tableModelAndRendererX);
	}


	/**
	 * Gibt das Objekt des TableModels zurück, das zur ausgewählten Tabellenzeile gehört.
	 * @param listWithTableModelObjects Liste mit den Objekten des TableModels. Der Typ der
	 *        Objekte ist, ist der mit dem diese JTableX erzeugt wurde (-&gt; Generics)
	 * @return Objekt des TableModels, das zur ausgewählten Tabellenzeile gehört
	 */
	public T getSelectedObject(List<T> listWithTableModelObjects){

		if(tableRowIsSelected()) {
			return listWithTableModelObjects.size() > 0 ? listWithTableModelObjects.get(getSelectedModelRow()) : null;
		}else{
			return null;
		}
	}


	/**
	 * Gibt das Objekt des TableModels zurück, das zur ausgewählten Tabellenzeile gehört.
	 * <br><b>Wichtig:</b> Das TableModel dieser JTableX muss ModelInterfaceForJTableX&lt;T&gt; implementieren.
	 * Statt T, muss hier die Klasse der vom TableModel verwalteten Zeilen-Objekte eingetragen werden.
	 * @return Objekt des TableModels, das zur ausgewählten Tabellenzeile gehört
	 * @throws RuntimeException Fliegt, falls das TableModel dieser JTableX nicht ModelInterfaceForJTableX&lt;T&gt; implementiert.
	 */
	@SuppressWarnings("unchecked")
	public T getSelectedObject() throws RuntimeException{
		if(getModel() instanceof ModelInterfaceForJTableX){

			return getSelectedObject(((ModelInterfaceForJTableX<T>)getModel()).getRowObjects());
		}else{
			throw new RuntimeException("Wenn getSelectedObject() ohne Parameter aufgerufen wird, muss das TableModel das Interface ModelInterfaceForJTableX<T> implementieren.");
		}

	}



	/**
	 * Gibt die Objekte zurück, die zu den ausgewählten Zeilen gehören.
	 * @param listWithTableModelObjects Liste mit den Objekten des TableModels. Der Typ der
	 *        Objekte ist, ist der mit dem diese JTableX erzeugt wurde (-&gt; Generics)
	 * @return List mit den entsprechenden Objekten
	 */
	public List<T> getSelectedObjects(List<T> listWithTableModelObjects){
		int[] selectedColumns = getSelectedRows();
		List<T> list = new ArrayList<T>();

		for(int i=0; i < selectedColumns.length; i++){
			list.add(listWithTableModelObjects.get(convertTableRowToModelRow(selectedColumns[i])));
		}

		return list;
	}



	/**
	 * Gibt die Objekte zurück, die zu den ausgewählten Zeilen gehören.
	 * @return List mit den entsprechenden Objekten
	 */
	@SuppressWarnings("unchecked")
	public List<T> getSelectedObjects() throws RuntimeException{
		if(getModel() instanceof ModelInterfaceForJTableX){

			return getSelectedObjects(((ModelInterfaceForJTableX<T>)getModel()).getRowObjects());
		}else{
			throw new RuntimeException("Wenn getSelectedObjects() ohne Parameter aufgerufen wird, muss das TableModel das Interface ModelInterfaceForJTableX<T> implementieren.");
		}
	}


	/**
	 * Gibt die Zeilen-Objekte des TableModels zurück
	 * @return Liste mit Zeilen-Objekten
	 * @throws RuntimeException
	 */
	@SuppressWarnings("unchecked")
	public List<T> getRowObjects() throws RuntimeException{
		if(getModel() instanceof ModelInterfaceForJTableX){

			return ((ModelInterfaceForJTableX<T>)getModel()).getRowObjects();
		}else{
			throw new RuntimeException("Das TableModel muss für diese Methode das Interface ModelInterfaceForJTableX<T> implementieren.");
		}
	}



	/**
	 * Selektiert die Zeile, die zum übergebenen Objekt gehört
	 * @param rowObject Das Objekt dessen zugehörige Zeile markiert werden soll
	 * @param listWithTableModelObjects Liste mit den Objekten des TableModels. Der Typ der
	 *        Objekte ist, ist der mit dem diese JTableX erzeugt wurde (-&gt; Generics)
	 */
	public void tableSelect(T rowObject, List<T> listWithTableModelObjects){
		if(listWithTableModelObjects.contains(rowObject)){
			tableSelect(convertModelRowToTableRow(listWithTableModelObjects.indexOf(rowObject)));
		}else{
			clearSelection();
		}
	}


	/**
	 * Selektiert die Zeile, die zum übergebenen Objekt gehört
	 * @param rowObject Das Objekt dessen zugehörige Zeile markiert werden soll
	 */
	@SuppressWarnings("unchecked")
	public void tableSelect(T rowObject){
		if(getModel() instanceof ModelInterfaceForJTableX){

			tableSelect(rowObject, ((ModelInterfaceForJTableX<T>)getModel()).getRowObjects());
		}else{
			throw new IllegalArgumentException("Wenn tableSelect(T rowObject) ohne Parameter aufgerufen wird, muss das TableModel das Interface ModelInterfaceForJTableX<T> implementieren.");
		}
	}


	/**
	 * Scrollt die Tabelle so, dass die ausgewählte Zeile sichtbar wird
	 */
	public void scrollToSelectedRowVisible(){
		scrollRectToVisible(new Rectangle(getCellRect(getSelectedRow(), 0, true)));
	}


	/**
	 * Gibt true zurück, wenn eine Zeile ausgewählt ist
	 * @return true - eine Zeile ist ausgewählt, false - keine Zeile gewählt
	 */
	public boolean tableRowIsSelected(){
		return getSelectedRow() != -1;
	}


	/**
	 * Konvertiert einen Zeilenindex der Tabelle zum passenden Modelindex (diese unterscheiden sich, wenn ein RowSorter die Tabelle filtert,
	 * oder sortiert)
	 * @param column Tabellenzeilenindex
	 * @return Modelindex
	 */
	public int convertTableColumnToModelColumn(int column){
		return convertColumnIndexToModel(column);
	}


	/**
	 * Wählt einen Eintrag in der als Parameter übergebene Tabelle in der gewünschten Zeile aus
	 * @param index Index der Zeile, die markiert werden soll
	 */
	public void tableSelect(int index){
		final int count = getRowCount();
		if(count > 0 && index >= 0 && index < count){
			setRowSelectionInterval(index, index);
		} else {
			clearSelection();
		}
	}


	/**
	 * Gibt die in Zeile im Model zurück, die zur vom User markierten Zeile gehört. Die Zeilen sind unterschiedlich
	 * @return Zeile des Model
	 */
	public int getSelectedModelRow(){
		final int selectedTableRow = getSelectedRow();
		return selectedTableRow != -1 ? convertTableRowToModelRow(selectedTableRow) : -1;
	}

	public int convertTableRowToModelRow(int row){
		return getRowSorter().convertRowIndexToModel(row);
	}


	public int convertModelRowToTableRow(int row){
		return getRowSorter().convertRowIndexToView(row);
	}


	public int getSelectedModelColumn(){
		return convertColumnIndexToModel(getSelectedColumn());
	}


	public boolean isSelected(){
		return getSelectedRow() != -1;
	}


	/**
	 * Aktiviert einen Row Sorter für die übergebene JTable<br>
	 * <b>Wichtig:</b> Diese Methode sollte aufgerufen werden <b>nachdem</b> der JTable ein Model
	 * gegeben wurde.
	 */
	public void enableRowSorter(){
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>();
		setRowSorter(sorter);
		sorter.setModel(getModel());
	}

	/**
	 * Setzt einen Row Filter auf diese Tabelle.
	 * @param regexFilter Regulärer Ausdruck. Nur Zeilen, dessen Inhalt auf diesen Regulären Ausdruck passen werden angezeigt.
	 * @param columnIndex Der Index der Spalte, auf der gefiltert werden soll.
	 */
	public synchronized void addRowFilter(String regexFilter, int columnIndex){

		rowFilterMap.put(columnIndex, RowFilter.regexFilter(regexFilter, columnIndex));
		aktualisiereRowFilter();
	}

	/**
	 * Entfernt den RowFilter, der auf der spezifizierten Spalte ist.
	 * @param columnIndex Spalte, dessen RowFilter entfernt werden soll.
	 */
	public synchronized void removeRowFilter(int columnIndex){

		rowFilterMap.remove(columnIndex);
		aktualisiereRowFilter();
	}

	@SuppressWarnings("unchecked")
	private synchronized void aktualisiereRowFilter(){
		//if(getRowSorter() instanceof TableRowSorter){
		((TableRowSorter<TableModel>)getRowSorter()).setRowFilter(RowFilter.andFilter(rowFilterMap.values()));

		//}
	}


	public void setModelAndRenderer(TableModelAndRendererX<T> tableModelAndRenderer){
		setModel(tableModelAndRenderer);
		setRenderer(tableModelAndRenderer);
	}


	public void setRenderer(TableCellRenderer renderer){
		setDefaultRenderer(Object.class, renderer);
	}

	@Override
	public void setModel(TableModel dataModel) {
		super.setModel(dataModel);

		enableRowSorter();
	}


	//	@Deprecated
	//	public String getColumnOrderString(){//FIXME geht noch nicht
	//		String s = "";
	//
	//		for(int i=0; i < getColumnModel().getColumnCount(); i++){
	//			if(i != convertColumnIndexToView(i)){
	//				s = s.concat(i + "," + convertColumnIndexToView(i));
	//				if(i < getColumnModel().getColumnCount() - 1)
	//					s = s.concat(";");
	//			}
	//		}
	//
	//		return s;
	//	}


	//	@Deprecated
	//	public void setColumnOrderByString(String columnOrdner){//FIXME geht noch
	//		if(!columnOrdner.trim().isEmpty()){
	//			for(String s : columnOrdner.split(";")){
	//				String[] indexes = s.split(",");
	//				moveColumn(Integer.parseInt(indexes[0]), Integer.parseInt(indexes[1]));
	//			}
	//		}
	//	}

}