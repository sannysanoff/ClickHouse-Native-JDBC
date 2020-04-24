package com.github.housepower.jdbc.data;

import com.github.housepower.jdbc.data.type.complex.DataTypeArray;
import com.github.housepower.jdbc.serializer.BinarySerializer;

import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class Column {

    private final String name;
    private final IDataType type;

    Object values;
    long[] valuesLong;
    double[] valuesDouble;  // see also: getArray
    int[] valuesInt;  // see also: getArray
    private ColumnWriterBuffer buffer;
    private boolean isArray;
    private List<List<Integer>> offsets;

    public Column(String name, IDataType type) {
        this.name = name;
        this.type = type;
    }

    public Object getArray() {
        if (valuesDouble != null) return valuesDouble;
        if (valuesLong != null) return valuesLong;
        if (valuesInt != null) return valuesInt;
        Class comp = values.getClass().getComponentType();
        if (comp == Double.class) {
            double[] retval = new double[Array.getLength(values)];
            Double[] arr = (Double[])values;
            for(int i=0; i<retval.length; i++) {
                retval[i] = arr[i] != null ? arr[i] : Double.NaN;
            }
            return retval;
        }
        if (comp == Long.class) {
            double[] retval = new double[Array.getLength(values)];
            Long[] arr = (Long[])values;
            for(int i=0; i<retval.length; i++) {
                retval[i] = arr[i] != null ? arr[i] : Long.MIN_VALUE;
            }
            return retval;
        }
        if (comp == Integer.class) {
            double[] retval = new double[Array.getLength(values)];
            Integer[] arr = (Integer[]) values;
            for (int i = 0; i < retval.length; i++) {
                retval[i] = arr[i]!= null ? arr[i] : Integer.MIN_VALUE;
            }
            return retval;
        }
        System.out.println("WARNING: class of values: "+values.getClass().getName());
        return values;
    }

    public Column(String name, IDataType type, Object values) {
        this.values = values;
        if (values instanceof long[]) {
            valuesLong = (long[])values;
        } else if (values instanceof double[]) {
            valuesDouble = (double[])values;
        } else if (values instanceof int[]) {
            valuesInt = (int[])values;
        }
        this.name = name;
        this.type = type;
        if (this.type.sqlTypeId() == Types.ARRAY) {
            this.isArray = true;
            this.offsets = new ArrayList<>();
        }
    }

    public void initWriteBuffer() {
        this.buffer = new ColumnWriterBuffer();
    }

    public String name() {
        return this.name;
    }

    public IDataType type() {
        return this.type;
    }

    public Object values(int rowIdx) {
        return Array.get(this.values, rowIdx);
    }

    public long longs(int rowIdx) {
        return valuesLong[rowIdx];
    }

    public double doubles(int rowIdx) {
        return valuesDouble[rowIdx];
    }

    public int ints(int rowIdx) {
        return valuesInt[rowIdx];
    }

    public void write(Object object) throws IOException, SQLException {
        if (isArray) {
            DataTypeArray typ = (DataTypeArray)(type());
            typ.serializeBinary(object, buffer.column, offsets, 1);
        } else {
            type().serializeBinary(object, buffer.column);
        }
    }


    public void serializeBinaryBulk(BinarySerializer serializer) throws SQLException, IOException {
        serializer.writeStringBinary(name);
        serializer.writeStringBinary(type.name());

        //writer offsets
        if (offsets != null) {
            for (List<Integer> offsetList : offsets) {
                for (int offset : offsetList) {
                    serializer.writeLong(offset);
                }
            }
        }

        buffer.writeTo(serializer);
    }

    public ColumnWriterBuffer getBuffer() {
        return buffer;
    }
}
