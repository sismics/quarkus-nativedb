package com.sismics.nativedb.dialect;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.ValueExtractor;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.java.UUIDTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PostgresUUIDType extends AbstractSingleColumnStandardBasicType<UUID> {
    public static final PostgresUUIDType INSTANCE = new PostgresUUIDType();

    public PostgresUUIDType() {
        super(PostgresUUIDSqlTypeDescriptor.INSTANCE, UUIDTypeDescriptor.INSTANCE);
    }

    public String getName() {
        return "helpers.db.dialect.PostgresUUIDType";
    }

    public static class PostgresUUIDSqlTypeDescriptor implements SqlTypeDescriptor {
        public static final PostgresUUIDSqlTypeDescriptor INSTANCE = new PostgresUUIDSqlTypeDescriptor();

        public PostgresUUIDSqlTypeDescriptor() {
        }

        public int getSqlType() {
            return 1111;
        }

        public boolean canBeRemapped() {
            return true;
        }

        public <X> ValueBinder<X> getBinder(final JavaTypeDescriptor<X> javaTypeDescriptor) {
            return new ValueBinder<X>() {
                @Override
                public void bind(PreparedStatement preparedStatement, X x, int i, WrapperOptions wrapperOptions)
                        throws SQLException {

                }

                @Override
                public void bind(CallableStatement st, X value, String s, WrapperOptions options) throws SQLException {
                    st.setObject(PostgresUUIDSqlTypeDescriptor.this.getSqlType(),
                            javaTypeDescriptor.unwrap(value, UUID.class, options));
                }
            };
        }

        public <X> ValueExtractor<X> getExtractor(final JavaTypeDescriptor<X> javaTypeDescriptor) {
            return new ValueExtractor<X>() {
                @Override
                public X extract(ResultSet rs, String name, WrapperOptions options) throws SQLException {
                    return javaTypeDescriptor.wrap(rs.getObject(name), options);
                }

                @Override
                public X extract(CallableStatement callableStatement, int i, WrapperOptions wrapperOptions)
                        throws SQLException {
                    return null;
                }

                @Override
                public X extract(CallableStatement callableStatement, String[] strings, WrapperOptions wrapperOptions)
                        throws SQLException {
                    return null;
                }
            };
        }
    }
}
