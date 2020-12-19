package team.yi.rsql.ktorm.util

import org.ktorm.schema.*
import team.yi.rsql.ktorm.adapter.*
import team.yi.rsql.ktorm.converter.*

@Suppress("MemberVisibilityCanBePrivate")
object RsqlUtil {
    val defaultOperatorAdapters: List<OperatorAdapter>
        get() = listOf(
            IsNullOperatorAdapter(),
            IsNotNullOperatorAdapter(),
            IsEmptyOperatorAdapter(),
            IsNotEmptyOperatorAdapter(),
            IsNullOrEmptyOperatorAdapter(),
            NotIsNullOrEmptyOperatorAdapter(),

            EqualsOperatorAdapter(),
            NotEqualsOperatorAdapter(),
            EqualsIgnoreCaseOperatorAdapter(),
            NotEqualsIgnoreCaseOperatorAdapter(),

            InOperatorAdapter(),
            NotInOperatorAdapter(),

            LikeOperatorAdapter(),
            LikeIgnorecaseOperatorAdapter(),
            NotLikeOperatorAdapter(),
            NotLikeIgnorecaseOperatorAdapter(),
            StartsWithOperatorAdapter(),
            StartsWithIgnorecaseOperatorAdapter(),
            NotStartsWithOperatorAdapter(),
            NotStartsWithIgnorecaseOperatorAdapter(),
            EndsWithOperatorAdapter(),
            EndsWithIgnorecaseOperatorAdapter(),
            NotEndsWithOperatorAdapter(),
            NotEndsWithIgnorecaseOperatorAdapter(),
            ContainsOperatorAdapter(),
            ContainsIgnorecaseOperatorAdapter(),
            NotContainsOperatorAdapter(),
            NotContainsIgnorecaseOperatorAdapter(),

            BetweenOperatorAdapter(),
            NotBetweenOperatorAdapter(),

            GreaterOperatorAdapter(),
            GreaterOrEqualsOperatorAdapter(),
            NotGreaterOperatorAdapter(),
            NotGreaterOrEqualsOperatorAdapter(),
            LessOperatorAdapter(),
            LessOrEqualsOperatorAdapter(),
            NotLessOperatorAdapter(),
            NotLessOrEqualsOperatorAdapter(),
            BeforeOperatorAdapter(),
            NotBeforeOperatorAdapter(),
            AfterOperatorAdapter(),
            NotAfterOperatorAdapter(),
        )

    val defaultValueConverters: Map<SqlType<out Any>, FieldValueConverter>
        get() = mapOf(
            BooleanSqlType to BooleanValueConverter(),

            ShortSqlType to ShortValueConverter(),
            IntSqlType to IntValueConverter(),
            LongSqlType to LongValueConverter(),
            FloatSqlType to FloatValueConverter(),
            DoubleSqlType to DoubleValueConverter(),
            DecimalSqlType to DecimalValueConverter(),

            TextSqlType to TextValueConverter(),
            VarcharSqlType to VarcharValueConverter(),

            BytesSqlType to BytesValueConverter(),
            BlobSqlType to BlobValueConverter(),

            TimestampSqlType to TimestampValueConverter(),
            DateSqlType to DateValueConverter(),
            TimeSqlType to TimeValueConverter(),
            InstantSqlType to InstantValueConverter(),
            LocalDateTimeSqlType to LocalDateTimeValueConverter(),
            LocalDateSqlType to LocalDateValueConverter(),
            LocalTimeSqlType to LocalTimeValueConverter(),
            MonthDaySqlType to MonthDayValueConverter(),
            YearMonthSqlType to YearMonthValueConverter(),
            YearSqlType to YearValueConverter(),

            UuidSqlType to UuidValueConverter(),

            // EnumSqlType to EnumValueConverter(),
        )
}
