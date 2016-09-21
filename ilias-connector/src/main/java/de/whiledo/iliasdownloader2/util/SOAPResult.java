package de.whiledo.iliasdownloader2.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SOAPResult{
	private String text;
	private String error;
	private boolean faultCode;
}