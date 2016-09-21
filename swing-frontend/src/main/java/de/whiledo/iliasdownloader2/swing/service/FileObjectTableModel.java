package de.whiledo.iliasdownloader2.swing.service;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;

import de.adesso.adzubix.jtablex.ModelInfo;
import de.adesso.adzubix.jtablex.TableModelAndRendererX;
import de.whiledo.iliasdownloader2.util.FileObject;
import de.whiledo.iliasdownloader2.util.Functions;

@ModelInfo(tableHeadlines={"Status", "Name", "Pfad", "Änderungsdatum", "Größe (MB)", "Ref Id"}, columnClasses={String.class, String.class, String.class, long.class, double.class, long.class})
public class FileObjectTableModel extends TableModelAndRendererX<FileObject> {

	//	public FileObjectTableModel(final JTableX<Course> table){
	//		table.setModelAndRenderer(this);
	//		table.addMouseListener(new MouseAdapter() {
	//			@Override
	//			public void mouseClicked(MouseEvent e){
	//				tableClicked(table.convertRowIndexToModel(table.rowAtPoint(e.getPoint())), table.convertColumnIndexToModel(table.columnAtPoint(e.getPoint())));
	//			}
	//		});
	//		
	////		table.getColumnModel().getColumn(0).setMaxWidth(new JCheckBox().getWidth()+7);
	//		
	////		table.enableRowSorter();
	//		updateTable();
	//	}

	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
		JLabel tableCellRendererComponent = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		column = table.convertColumnIndexToModel(column);
		row = table.convertRowIndexToModel(row);

		tableCellRendererComponent.setIcon(null);

		if(column == 1){
			try{
				tableCellRendererComponent.setIcon(MainController.getFileIcon(getRowObjects().get(row).getTargetFile()));
			}catch(Exception e){

			}
			
		}else if(column == 2){
				try{
					tableCellRendererComponent.setIcon(MainController.getFileIcon(getRowObjects().get(row).getTargetFile().getParentFile()));
				}catch(Exception e){
					
				}
				
		}else if(column == 3){
			tableCellRendererComponent.setText(Functions.formatDate((long) value));//hier im Renderer gemacht, damit der Row Sorter funktioniert
			
		}

		return tableCellRendererComponent;
	}

	

	@Override
	public Object getValueAt(FileObject f, int columnIndex){
		if(f == null){
			return "";
		}

		switch(columnIndex){
		case 0: return String.valueOf(f.getSyncState().getReadableName());
		case 1: return f.getXmlObject().getTitle();
		case 2: return f.getXmlObject().getPath();
		case 3: return f.getLastUpdated();
		case 4: return bytesToMegaBytes(f.getFileSize());
		case 5: return f.getRefId();
		default: throw new RuntimeException("Wrong column index: " + columnIndex);
		}
	}

	private double bytesToMegaBytes(long bytes){
		double megabytes = bytes / 1_000_000d;
		int m = (int) (megabytes * 1000);
		return m / 1000d;
	}

	//	public void tableClicked(int row, int column){
	//		if(column == 0){
	//			getRowObjects().get(row).switchActive();
	//			updateTable();
	//		}
	//	}
}
