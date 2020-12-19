<div style="text-align: center;">

[![Build Status][10]][11]
[![GitHub release (latest by date)][20]][21]
[![Maven Central][30]][31]
[![Semantic Versioning 2.0.0][40]][41]
[![Conventional Commits][50]][51]
[![GitHub][60]][61]

</div>

> # Warning
> **Project is unstable, break changes maybe occur.**

# rsql-ktorm

Integration RSQL query language and [ktorm][100] framework.

About RSQL please see: [rsql-parser](110), [KOSapi](120)(RSQL was originally created for KOSapi).

# Quick Start

```kotlin
val rsql = "name!=bob and (name==alice or id=in=(2,3,4,5)) or manager_id>0 and hire_date=before=2099-12-31 or department.location=notnull=1"

val ktormRsql = KtormRsql.builder()
    .from(
        database.from(Employees)
            .leftJoin(Employees.department, on = Employees.department.id.eq(Employees.departmentId))
    )
    .select(
        Employees.id,
        Employees.name,
        Employees.departmentId,
        Employees.department.name,
    )
    .build()

// build ktorm predicate
val predicate = ktormRsql.buildPredicate(rsql)

// ktorm query
val q = ktormRsql.query
    .whereWithConditions {
        // inject rsql predicate
        it += predicate

        // more predicates
        it += Employees.job.isNotNull()
        it += Employees.department.id.greater(0)
    }
    .orderBy(
        Employees.departmentId.desc(),
        Employees.name.asc(),
        Employees.department.location.desc(),
    )

// print sql or fetch results
log.info { q.sql }
```

It's will print sql like:

```sql
SELECT "t_employee"."id" AS "t_employee_id", "t_employee"."name" AS "t_employee_name", "t_employee"."department_id" AS "t_employee_department_id", "_ref0"."name" AS "_ref0_name"
FROM "t_employee"
         LEFT JOIN "t_department" "_ref0" ON "_ref0"."id" = "t_employee"."department_id"
WHERE (("t_employee"."job" IS NOT NULL) AND ("_ref0"."id" > ?))
  AND (((("t_employee"."name" <> ?) AND (("t_employee"."name" = ?) OR ("t_employee"."id" IN (?, ?, ?, ?)))) OR
        (("t_employee"."manager_id" > ?) AND ("t_employee"."hire_date" < ?))) OR ("_ref0"."location" IS NOT NULL))
ORDER BY "t_employee"."department_id" DESC, "t_employee"."name", "_ref0"."location" DESC

-- Parameters: [0(int), bob(varchar), alice(varchar), 2(int), 3(int), 4(int), 5(int), 0(int), 2099-12-31(date)]
```

# Modules

- `rsql-ktorm`: the general syntax implementation of rsql.
- `rsql-ktorm-mysql`: **NOT READY!** Will include features in mysql dialect.
- `rsql-ktorm-oracle`: **NOT READY!** Will include features in oracle dialect.
- `rsql-ktorm-postgresql`: **NOT READY!** Will include features in postgresql dialect.
- `rsql-ktorm-sqlite`: **NOT READY!** Will include features in sqlite dialect.
- `rsql-ktorm-sqlserver`: **NOT READY!** Will include features in sqlserver dialect.

# Custom

# Usage

## Operators

| Operator | Syntax |
| -------- | ------ |
| IS_NULL | `=null=` `=isnull=` `=isNull=` |
| IS_NOT_NULL | `=notnull=` `=notNull=` `=notisnull=` `=notIsNull=` `=isNotNull=` |
| IS_EMPTY | `=empty=` `=isempty=` `=isEmpty=` |
| IS_NOT_EMPTY | `=notempty=` `=notEmpty=` `=notisempty=` `=notIsEmpty=` |
| IS_NULL_OR_EMPTY | `=nullorempty=` `=isnullorempty=` `=isNullOrEmpty=` |
| NOT_IS_NULL_OR_EMPTY | `=notnullorempty=` `=notisnullorempty=` `=notIsNullOrEmpty=` |
| EQUALS | `=eq=` `==` |
| NOT_EQUALS | `=ne=` `=noteq=` `=notEq=` `!=` |
| EQUALS_IGNORECASE | `=ieq=` `=equalsignorecase=` `=equalsIgnoreCase=` `=equalsIgnorecase=` |
| NOT_EQUALS_IGNORECASE | `=ine=` `=notequalsignorecase=` `=notEqualsIgnoreCase=` `=notEqualsIgnorecase=` |
| IN | `=in=` |
| NOT_IN | `=notin=` `=notIn=` `=out=` |
| LIKE | `=like=` |
| LIKE_IGNORECASE | `=ilike=` `=likeignorecase=` `=likeIgnoreCase=` `=likeIgnorecase=` |
| NOT_LIKE | `=notlike=` `=notLike=` |
| NOT_LIKE_IGNORECASE | `=notilike=` `=notlikeignorecase=` `=notLikeIgnoreCase=` `=notLikeIgnorecase=` |
| STARTS_WITH | `=sw=` `=startswith=` `=startsWith=` |
| STARTS_WITH_IGNORECASE | `=isw=` `=startswithignorecase=` `=startsWithIgnoreCase=` `=startsWithIgnorecase=` |
| NOT_STARTS_WITH | `=notsw=` `=notstartswith=` `=notStartsWith=` |
| NOT_STARTS_WITH_IGNORECASE | `=inotsw=` `=notstartswithignorecase=` `=notStartsWithIgnoreCase=` `=notStartsWithIgnorecase=` |
| ENDS_WITH | `=ew=` `=endswith=` `=endsWith=` |
| ENDS_WITH_IGNORECASE | `=iew=` `=endswithignorecase=` `=endsWithIgnoreCase=` `=endsWithIgnorecase=` |
| NOT_ENDS_WITH | `=notew=` `=notendswith=` `=notEndsWith=` |
| NOT_ENDS_WITH_IGNORECASE | `=inotew=` `=notendswithignorecase=` `=notEndsWithIgnoreCase=` `=notEndsWithIgnorecase=` |
| CONTAINS | `=con=` `=contains=` |
| CONTAINS_IGNORECASE | `=icon=` `=containsignorecase=` `=containsIgnoreCase=` `=containsIgnorecase=` |
| NOT_CONTAINS | `=notcon=` `=notcontains=` `=notContains=` |
| NOT_CONTAINS_IGNORECASE | `=inotcon=` `=notContainsignorecase=` `=notContainsIgnoreCase=` `=notContainsIgnorecase=` |
| BETWEEN | `=between=` |
| NOT_BETWEEN | `=notbetween=` `=notBetween=` |
| GREATER | `=gt=` `>` `=greater=` |
| GREATER_OR_EQUALS | `=gte=` `=ge=` `>=` `=greaterorequals=` `=greaterOrEquals=` |
| NOT_GREATER | `=notgt=` `=notgreater=` `=notGreater=` |
| NOT_GREATER_OR_EQUALS | `=notgte=` `=notge=` `=notgreaterorequals=` `=notGreaterOrEquals=` |
| LESS | `=lt=` `<` `=less=` |
| LESS_OR_EQUALS | `=lte=` `=le=` `<=` `=lessorequals=` `=lessOrEquals=` |
| NOT_LESS | `=notlt=` `=notless=` `=notLess=` |
| NOT_LESS_OR_EQUALS | `=notlte=` `=notle=` `=notlessorequals=` `=notLessOrEquals=` |
| BEFORE | `=before=` |
| NOT_BEFORE | `=notbefore=` `=notBefore=` |
| AFTER | `=after=` |
| NOT_AFTER | `=notafter=` `=notAfter=` |

## Custom operators

```kotlin
class CustomLikeOperatorAdapter : AbstractOperatorAdapter("xlike", "~=") {
    override fun getExpression(
        rsqlOperator: RsqlOperator,
        left: ColumnDeclaring<Any>,
        right: List<Any?>,
        node: ComparisonNode
    ): ScalarExpression<Boolean>? {
        if (right.size > 1) throw RuntimeException(name)

        return left.like(right.firstOrNull() ?: return null)
    }
}
```

## Custom value converters

```kotlin
class CustomBooleanValueConverter : FieldValueConverter {
    override fun convert(arguments: Collection<String>, sourceTable: BaseTable<*>): List<Any?> {
        return arguments.map {
            it == "yes"
        }
    }
}
```

## Date format

By default, the library supports the following date formats:

| pattern | regex |
| -------- | ------ |
| dd MMMM yyyy HH:mm:ss | `^\d{1,2}\s[a-z]{4,}\s\d{4}\s\d{1,2}:\d{1,2}:\d{1,2}$` |
| dd MMM yyyy HH:mm:ss | `^\d{1,2}\s[a-z]{3}\s\d{4}\s\d{1,2}:\d{1,2}:\d{1,2}$` |
| yyyy/MM/dd HH:mm:ss | `^\d{4}/\d{1,2}/\d{1,2}\s\d{1,2}:\d{1,2}:\d{1,2}$` |
| MM/dd/yyyy HH:mm:ss | `^\d{1,2}/\d{1,2}/\d{4}\s\d{1,2}:\d{1,2}:\d{1,2}$` |
| yyyy-MM-dd'T'HH:mm:ss | `^\d{4}-\d{1,2}-\d{1,2}'?T'?\d{1,2}:\d{1,2}:\d{1,2}$` |
| yyyy-MM-dd'T'HH:mm:ss'Z' | `^\d{4}-\d{1,2}-\d{1,2}'?T'?\d{1,2}:\d{1,2}:\d{1,2}'?Z'?$` |
| yyyy-MM-dd'T'HH:mm:ss.SSS'Z' | `^\d{4}-\d{1,2}-\d{1,2}'?T'?\d{1,2}:\d{1,2}:\d{1,2}\.\d{3}'?Z'?$` |
| yyyy-MM-dd HH:mm:ss | `^\d{4}-\d{1,2}-\d{1,2}\s\d{1,2}:\d{1,2}:\d{1,2}$` |
| dd-MM-yyyy HH:mm:ss | `^\d{1,2}-\d{1,2}-\d{4}\s\d{1,2}:\d{1,2}:\d{1,2}$` |
| yyyyMMdd HHmmss | `^\d{8}\s\d{6}$` |
| dd MMMM yyyy HH:mm | `^\d{1,2}\s[a-z]{4,}\s\d{4}\s\d{1,2}:\d{1,2}$` |
| dd MMM yyyy HH:mm | `^\d{1,2}\s[a-z]{3}\s\d{4}\s\d{1,2}:\d{1,2}$` |
| yyyy/MM/dd HH:mm | `^\d{4}/\d{1,2}/\d{1,2}\s\d{1,2}:\d{1,2}$` |
| MM/dd/yyyy HH:mm | `^\d{1,2}/\d{1,2}/\d{4}\s\d{1,2}:\d{1,2}$` |
| yyyy-MM-dd HH:mm | `^\d{4}-\d{1,2}-\d{1,2}\s\d{1,2}:\d{1,2}$` |
| yyyy.MM.dd HH:mm | `^\d{4}.\d{1,2}.\d{1,2}\s\d{1,2}:\d{1,2}$` |
| dd-MM-yyyy HH:mm | `^\d{1,2}-\d{1,2}-\d{4}\s\d{1,2}:\d{1,2}$` |
| yyyyMMdd HHmm | `^\d{8}\s\d{4}$` |
| dd MMMM yyyy | `^\d{1,2}\s[a-z]{4,}\s\d{4}$` |
| dd MMM yyyy | `^\d{1,2}\s[a-z]{3}\s\d{4}$` |
| yyyy/MM/dd | `^\d{4}/\d{1,2}/\d{1,2}$` |
| MM/dd/yyyy | `^\d{1,2}/\d{1,2}/\d{4}$` |
| yyyy.MM.dd | `^\d{4}\.\d{1,2}\.\d{1,2}$` |
| yyyy-MM-dd | `^\d{4}-\d{1,2}-\d{1,2}$` |
| dd.MM.yyyy | `^\d{1,2}\.\d{1,2}\.\d{4}$` |
| dd-MM-yyyy | `^\d{1,2}-\d{1,2}-\d{4}$` |
| yyyy | `^\d{4}$` |
| yy-MM-dd HH:mm | `^\d{2}-\d{1,2}-\d{1,2}\s\d{1,2}:\d{1,2}$` |
| yy.MM.dd | `^\d{2}\.\d{1,2}\.\d{1,2}$` |
| yy-MM-dd | `^\d{2}-\d{1,2}-\d{1,2}$` |
| yy-MM | `^\d{2}-\d{1,2}$` |
| yyyy.MM | `^\d{4}\.\d{1,2}$` |
| yyyy-MM | `^\d{4}-\d{1,2}$` |
| MM.yyyy | `^\d{1,2}\.\d{4}$` |
| MM-yyyy | `^\d{1,2}-\d{4}$` |
| HH:mm:ss | `^\d{1,2}:\d{1,2}:\d{1,2}$` |
| HH:mm:ss'Z' | `^\d{1,2}:\d{1,2}:\d{1,2}'?Z'?$` |
| HH:mm:ss.SSS'Z' | `^\d{1,2}:\d{1,2}:\d{1,2}\.\d{3}'?Z'?$` |
| HH:mm | `\d{1,2}:\d{1,2}$` |

> TODO: add custom date format support.

# Author

[@ymind][6], full stack engineer.

# License

This is open-sourced software licensed under the [MIT license][9].

[6]: https://github.com/ymind
[9]: https://opensource.org/licenses/MIT
[10]: https://github.com/ymind/rsql-ktorm/workflows/rsql-ktorm/badge.svg?branch=main
[11]: https://github.com/ymind/rsql-ktorm/actions
[20]: https://img.shields.io/github/v/release/ymind/rsql-ktorm
[21]: https://github.com/ymind/rsql-ktorm/releases
[30]: https://img.shields.io/maven-central/v/team.yi.rsql/rsql-ktorm
[31]: https://search.maven.org/artifact/team.yi.rsql/rsql-ktorm
[40]: https://img.shields.io/badge/Semantic%20Versioning-2.0.0-brightgreen
[41]: https://semver.org/
[50]: https://img.shields.io/badge/Conventional%20Commits-1.0.0-yellow.svg
[51]: https://conventionalcommits.org
[60]: https://img.shields.io/github/license/ymind/rsql-ktorm
[61]: https://github.com/ymind/rsql-ktorm/blob/main/LICENSE
[100]: https://github.com/kotlin-orm/ktorm
[110]: https://github.com/jirutka/rsql-parser
[120]: https://kosapi.fit.cvut.cz/projects/kosapi/wiki
