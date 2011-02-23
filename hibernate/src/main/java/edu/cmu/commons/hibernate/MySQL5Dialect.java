package edu.cmu.commons.hibernate;

import java.sql.Types;

/**
 * Custom MySQL5 Hibernate Dialect which resolves issues with "binary" column
 * types.
 * 
 * @author hazen
 */
public class MySQL5Dialect extends org.hibernate.dialect.MySQL5Dialect {
	public MySQL5Dialect() {
		super();
		registerColumnType(Types.BINARY, "binary");
		registerColumnType(Types.BINARY, 255, "binary($l)");
		registerColumnType(Types.VARBINARY, 255, "varbinary($l)");
		registerColumnType(Types.LONGVARBINARY, "longvarbinary");
		registerColumnType(Types.LONGVARCHAR, "longvarchar");
		int megaBytes16 = 1024 * 1024 * 16;
		registerColumnType(Types.LONGVARBINARY, megaBytes16, "longvarbinary($l)");
		registerColumnType(Types.LONGVARCHAR, megaBytes16, "longvarchar($l)");
	}
}
