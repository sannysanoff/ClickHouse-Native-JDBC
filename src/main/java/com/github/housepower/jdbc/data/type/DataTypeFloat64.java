package com.github.housepower.jdbc.data.type;

import com.github.housepower.jdbc.data.IDataType;
import com.github.housepower.jdbc.misc.SQLLexer;
import com.github.housepower.jdbc.serializer.BinaryDeserializer;
import com.github.housepower.jdbc.serializer.BinarySerializer;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Types;

public class DataTypeFloat64 implements IDataType {

    private static final Double DEFAULT_VALUE = 0.0D;

    @Override
    public String name() {
        return "Float64";
    }

    @Override
    public int sqlTypeId() {
        return Types.DOUBLE;
    }

    @Override
    public Object defaultValue() {
        return DEFAULT_VALUE;
    }

    @Override
    public Class javaTypeClass() {
        return Double.class;
    }

    @Override
    public boolean nullable() {
        return false;
    }

    @Override
    public void serializeBinary(Object data, BinarySerializer serializer) throws SQLException, IOException {
        serializer.writeDouble(((Number) data).doubleValue());
    }

    @Override
    public Double deserializeBinary(BinaryDeserializer deserializer) throws SQLException, IOException {
        return deserializer.readDouble();
    }

    @Override
    public Object deserializeBinaryBulk(int rows, BinaryDeserializer deserializer) throws IOException {
        double[] data = new double[rows];
        for (int row = 0; row < rows; row++) {
            data[row] = deserializer.readDouble();
        }
        return data;
    }

    @Override
    public Object deserializeTextQuoted(SQLLexer lexer) throws SQLException {
        return lexer.numberLiteral().doubleValue();
    }

}
