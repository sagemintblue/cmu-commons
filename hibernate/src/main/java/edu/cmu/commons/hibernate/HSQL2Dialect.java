package edu.cmu.commons.hibernate;

import java.sql.Types;
import java.util.UUID;

/**
 * Bug fix for more rigorous enforcement of column length, precision and scale
 * qualifiers in HSQLDB 2.0. This Dialect helps alleviate problems with
 * {@code BINARY} SQL column types and {@link UUID} Java properties.
 * 
 * If you are using JPA annotations along with Hibernate's schema generation
 * facilities, you <b>must</b> properly specify length restrictions such that
 * {@code BINARY} columns are created with appropriate lengths. For instance, a
 * {@code BINARY} column supporting a {@code UUID} property must have length 16:
 * 
 * <pre>
 * &#064;Entity
 * public class MyEntity {
 * 
 * 	&#064;Id
 * 	&#064;Column(length = 16)
 * 	private UUID id;
 * 
 * }
 * </pre>
 * 
 * @author hazen
 * @see <a
 * href="http://www.hsqldb.org/doc/2.0/guide/sqlgeneral-chapt.html#sqlgeneral_types_ops-sect">Basic
 * Types and Operations</a>
 * @see <a
 * href="http://www.hsqldb.org/doc/2.0/guide/sqlgeneral-chapt.html#N1056A">Type
 * Length, Precision and Scale</a>
 */
public class HSQL2Dialect extends org.hibernate.dialect.HSQLDialect {
	public HSQL2Dialect() {
		super();

		/**
		 * if you uncomment the next line, then all Boolean properties must be
		 * supported by columns of type {@code BIT(1)} explicitly, via use of
		 * {@code @Column(length = 1)}.
		 */
		// registerColumnType(Types.BIT, 255, "bit($l)");
		registerColumnType(Types.BIT, "bit");

		registerColumnType(Types.BINARY, "binary");
		registerColumnType(Types.BINARY, 255, "binary($l)");
		registerColumnType(Types.VARBINARY, 255, "varbinary($l)");

		registerColumnType(Types.LONGVARBINARY, "longvarbinary");
		registerColumnType(Types.LONGVARCHAR, "longvarchar");
		int megaBytes16 = 1024 * 1024 * 16;
		registerColumnType(Types.LONGVARBINARY, megaBytes16, "longvarbinary($l)");
		registerColumnType(Types.LONGVARCHAR, megaBytes16, "longvarchar($l)");

		registerColumnType(Types.BLOB, "blob");
		registerColumnType(Types.CLOB, "clob");
		int teraBytes64 = 1024 * 1024 * 1024 * 1024 * 64;
		registerColumnType(Types.BLOB, teraBytes64, "blob($l)");
		registerColumnType(Types.CLOB, teraBytes64, "clob($l)");
	}
}
