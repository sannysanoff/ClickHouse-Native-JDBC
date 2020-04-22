package com.github.housepower.jdbc.data.type.complex;

import com.github.housepower.jdbc.ClickHouseArray;
import com.github.housepower.jdbc.connect.PhysicalInfo;
import com.github.housepower.jdbc.data.DataTypeFactory;
import com.github.housepower.jdbc.data.IDataType;
import com.github.housepower.jdbc.misc.SQLLexer;
import com.github.housepower.jdbc.misc.Validate;
import com.github.housepower.jdbc.serializer.BinaryDeserializer;
import com.github.housepower.jdbc.serializer.BinarySerializer;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Array;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataTypeArray implements IDataType {
    private final String name;
    private final Array defaultValue;
    private final IDataType elemDataType;
    private final IDataType offsetIDataType;

    public DataTypeArray(String name, IDataType elemDataType, IDataType offsetIDataType) throws SQLException {
        this.name = name;
        this.elemDataType = elemDataType;
        this.offsetIDataType = offsetIDataType;
        this.defaultValue = new ClickHouseArray(new Object[] {elemDataType.defaultValue()});
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int sqlTypeId() {
        return Types.ARRAY;
    }

    @Override
    public Object defaultValue() {
        return defaultValue;
    }

    @Override
    public Class javaTypeClass() {
        return Array.class;
    }

    @Override
    public boolean nullable() {
        return false;
    }

    @Override
    public Object deserializeTextQuoted(SQLLexer lexer) throws SQLException {
        Validate.isTrue(lexer.character() == '[');
        List<Object> arrayData = new ArrayList<Object>();
        for (; ; ) {
            if (lexer.isCharacter(']')) {
                lexer.character();
                break;
            }
            if (lexer.isCharacter(',')) {
                lexer.character();
            }
            arrayData.add(elemDataType.deserializeTextQuoted(lexer));
        }
        return new ClickHouseArray(arrayData.toArray());
    }

    public void serializeBinary(Object data, BinarySerializer dataBinarySerializer, List<List<Integer>> offsets, int level) throws SQLException, IOException {
        int dataOffset = ((Object[]) ((Array) data).getArray()).length;
        if (offsets.size() < level) {
            List<Integer> offset = new ArrayList<>();
            offset.add(dataOffset);
            offsets.add(offset);
        } else {
            int lastIdx =  offsets.get(level - 1).size() - 1;
            int lastOffset =  offsets.get(level - 1).get(lastIdx);
            offsets.get(level - 1).add(lastOffset + dataOffset);
        }

        for (Object v : ((Object[]) ((Array) data).getArray())) {
            if (elemDataType.sqlTypeId() == Types.ARRAY) {
                ((DataTypeArray)(elemDataType)).serializeBinary(v, dataBinarySerializer, offsets, level + 1);
            } else {
                elemDataType.serializeBinary(v, dataBinarySerializer);
            }
        }
    }

    @Override
    public void serializeBinary(Object data, BinarySerializer serializer) throws SQLException, IOException{
        throw new SQLException("DataTypeArray serializeBinary not supported");
    }

    @Override
    public void serializeBinaryBulk(Iterator<Object> data, BinarySerializer serializer)
        throws SQLException, IOException {
        throw new SQLException("DataTypeArray serializeBinaryBulk not supported");
    }

    @Override
    public void serializeBinaryBulk(Object[] data, BinarySerializer serializer)
        throws SQLException, IOException {
        throw new SQLException("DataTypeArray serializeBinaryBulk not supported");
    }

    @Override
    public Object deserializeBinary(BinaryDeserializer deserializer) throws SQLException, IOException {
        Long offset = (Long) offsetIDataType.deserializeBinary(deserializer);
        return elemDataType.deserializeBinaryBulk(offset.intValue(), deserializer);
    }


    @Override
    public Object deserializeBinaryBulk(int rows, BinaryDeserializer deserializer) throws IOException, SQLException {
        ClickHouseArray[] data = new ClickHouseArray[rows];
        if (rows == 0) {
            return data;
        }

        Object offsets = offsetIDataType.deserializeBinaryBulk(rows, deserializer);
        ClickHouseArray res =  new ClickHouseArray(
            elemDataType.deserializeBinaryBulk(((BigInteger) java.lang.reflect.Array.get(offsets, rows - 1)).intValue() , deserializer));

        for (int row = 0, lastOffset = 0; row < rows; row++) {
            BigInteger offset = (BigInteger) java.lang.reflect.Array.get(offsets, row);
            data[row] = res.slice(lastOffset, offset.intValue() - lastOffset);
            lastOffset = offset.intValue();
        }
        return data;
    }


    public static IDataType createArrayType(SQLLexer lexer, PhysicalInfo.ServerInfo serverInfo) throws SQLException {
        Validate.isTrue(lexer.character() == '(');
        IDataType arrayNestedType = DataTypeFactory.get(lexer, serverInfo);
        Validate.isTrue(lexer.character() == ')');
        return new DataTypeArray("Array(" + arrayNestedType.name() + ")",
                                 arrayNestedType, DataTypeFactory.get("UInt64", serverInfo));
    }
}