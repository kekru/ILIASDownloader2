package de.whiledo.iliasdownloader2.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum SyncState {
	LOADING("Lädt"),
	UPDATED("Aktualisiert"),
	ALREADY_UP_TO_DATE("Bereits aktuell"),	
	ERROR("Fehler"),
	CORRUPT("Unvollständig"),
	IGNORED_IGNORE_LIST("Ignoriert: Ignorieren-Liste"),
	IGNORED_NO_DOWNLOAD_ALLOWED("Ignoriert: Nur anzeigen"),
	IGNORED_GREATER_MAX_SIZE("Ignoriert: Zu Groß");
	
	@Getter
	private final String readableName;
}
