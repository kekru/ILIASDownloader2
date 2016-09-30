package de.whiledo.iliasdownloader2.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import lombok.Data;

@Data
public class CircularStream {

	private InputStream inputStream;
	private OutputStream outputStream;
	
	public CircularStream(){
		try {
			PipedInputStream p = new PipedInputStream();
			inputStream = p;
			outputStream = new PipedOutputStream(p);			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
