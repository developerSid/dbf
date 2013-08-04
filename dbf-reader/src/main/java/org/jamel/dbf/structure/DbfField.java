package org.jamel.dbf.structure;

import java.io.DataInput;
import java.io.IOException;

import org.jamel.dbf.exception.DbfException;
import org.jamel.dbf.utils.DbfUtils;


/**
 * Field descriptor in dbf header (fix 32 bytes for each field)
 * @see <a href="http://www.fship.com/dbfspecs.txt">DBF specification (2a, 2b)</a>
 *
 * @author Sergey Polovko
 */
public class DbfField {

    private String fieldName;                   /* 0-10  */
    private byte dataType;                      /* 11    */
    private int reserv1;                        /* 12-15 */
    private int fieldLength;                    /* 16    */
    private byte decimalCount;                  /* 17    */
    private short reserv2;                      /* 18-19 */
    private byte workAreaId;                    /* 20    */
    private short reserv3;                      /* 21-22 */
    private byte setFieldsFlag;                 /* 23    */
    private byte[] reserv4 = new byte[7];       /* 24-30 */
    private byte indexFieldFlag;                /* 31    */


    private DbfField() {
    }

    /**
     * <p>Creates a DBFField object from the data read from the given DataInputStream.</p>
     * <p/>
     * <p>The data in the DataInputStream object is supposed to be organised correctly
     * and the stream "pointer" is supposed to be positioned properly.</p>
     *
     * @param in DataInputStream
     * @return created DBFField object.
     * @throws java.io.IOException if any stream reading problems occurs.
     */
    public static DbfField read(DataInput in) throws DbfException {
        try {
            DbfField field = new DbfField();

            byte[] nameBuf = new byte[11];                      /* 0-10  */
            in.readFully(nameBuf);

            int nonZeroIndex = nameBuf.length - 1;
            while (nonZeroIndex >= 0 && nameBuf[nonZeroIndex] == 0) nonZeroIndex--;
            field.fieldName = new String(nameBuf, 0, nonZeroIndex + 1);

            field.dataType = in.readByte();                     /* 11    */
            field.reserv1 = DbfUtils.readLittleEndianInt(in);   /* 12-15 */
            field.fieldLength = in.readUnsignedByte();          /* 16    */
            field.decimalCount = in.readByte();                 /* 17    */
            field.reserv2 = DbfUtils.readLittleEndianShort(in); /* 18-19 */
            field.workAreaId = in.readByte();                   /* 20    */
            field.reserv2 = DbfUtils.readLittleEndianShort(in); /* 21-22 */
            field.setFieldsFlag = in.readByte();                /* 23    */
            in.readFully(field.reserv4);                        /* 24-30 */
            field.indexFieldFlag = in.readByte();               /* 31    */

            return field;
        } catch (IOException e) {
            throw new DbfException("Cannot read Dbf field", e);
        }
    }

    public String getName() {
        return fieldName;
    }

    public byte getDataType() {
        return dataType;
    }

    public int getFieldLength() {
        return fieldLength;
    }

    public int getDecimalCount() {
        return decimalCount;
    }
}

