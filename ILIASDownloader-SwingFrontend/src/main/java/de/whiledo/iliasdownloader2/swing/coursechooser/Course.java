package de.whiledo.iliasdownloader2.swing.coursechooser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {

	private boolean active;
	
	private String name;
	
	private long refId;
	
	private String password;
	
	public void switchActive(){
		active = !active;
	}
}
