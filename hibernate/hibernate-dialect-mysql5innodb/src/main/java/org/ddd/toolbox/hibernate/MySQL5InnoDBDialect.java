package org.ddd.toolbox.hibernate;

import java.sql.Types;

import org.hibernate.dialect.MySQLInnoDBDialect;

/**
 * MySQL5InnoDBDialect
 *
 * <br>
 * Patterns:
 * 
 * <br>
 * Revisions:
 * jnorris: Nov 18, 2007: Initial revision.
 *
 * @author jnorris
 */
public class MySQL5InnoDBDialect extends MySQLInnoDBDialect
{
    /**
     * 
     */
    public MySQL5InnoDBDialect()
    {
        super();

        // Over-ride settings for certain column types to take advantage of new
        // MySQL 5 column types
        registerColumnType(Types.BINARY, 65535, "binary($l)");
        registerColumnType(Types.VARBINARY, 65535, "varbinary($l)");
        registerColumnType(Types.VARBINARY, 256, "varbinary($l)");
        registerColumnType(Types.VARCHAR, 65535, "varchar($l)");
    }

}
