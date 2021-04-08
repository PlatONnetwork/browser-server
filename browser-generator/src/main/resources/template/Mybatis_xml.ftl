<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package_path}<#if (model?exists && model!="")>.${model}</#if>.mapper.${class_name}Mapper">
    <resultMap type="${package_path}<#if (model?exists && model!="")>.${model}</#if>.entity.${class_name}" id="${class_name?uncap_first}ResultMap">
		<#list table_column as c>
            <result property="${c.javaName}" column="${c.name?upper_case}"/>
        </#list>
    </resultMap>

    <sql id="table_columns">
		<#list table_column as c>
            ${c.name?upper_case}<#if c_has_next>,</#if>
        </#list>
    </sql>

    <sql id="entity_properties">
		<#list table_column as c>
            ${r"#"}{${c.javaName}}<#if c_has_next>,</#if>
        </#list>
    </sql>

    <!-- 使用like用法：columnName like concat('%',${r"#"}{columnName},'%') -->
    <sql id="page_where">
        <trim prefix="where" prefixOverrides="and | or ">
		<#list table_column as c>
			<#if (c.type=="String")>
            <if test="${c.javaName} != null and ${c.javaName} != ''">and ${c.name?upper_case} = ${r"#"}{${c.javaName}}</if>
            <#else>
			<if test="${c.javaName} != null">and ${c.name?upper_case} = ${r"#"}{${c.javaName}}</if>
            </#if>
        </#list>
        </trim>
    </sql>

    <!-- 使用like用法：columnName like concat('%',${r"#"}{columnName},'%') ,是String类型才支持模糊查询,不是很完美，自己去掉ID -->
    <sql id="page_where_fuzzy_query">
        <trim prefix="where" prefixOverrides="and | or ">
		<#list table_column as c>
            <if test="${c.javaName} != null and ${c.javaName} != ''">and ${c.name?upper_case}  like concat('%',${r"#"}{${c.javaName}},'%')</if>
        </#list>
        </trim>
    </sql>

    <!-- 插入并返回主键， keyProperty="主键对应的对象字段名"  -->
	<insert id="insert" parameterType="${package_path}<#if (model?exists && model!="")>.${model}</#if>.entity.${class_name}" useGeneratedKeys="true" keyProperty="${primaryKey_java}">
	     insert into ${table_name}( <include refid="table_columns" /> )
	     values ( <include refid="entity_properties" /> )
	</insert>

    <!-- 批量插入 -->
    <insert id="insertBatch" parameterType="java.util.List">
        insert into ${table_name}( <include refid="table_columns" /> )
        values
        <foreach collection="list" item="item" index="index" separator=",">
            (
		<#list table_column as c>
            ${r"#"}{item.${c.javaName}}<#if c_has_next>,</#if>
        </#list>)
        </foreach>
    </insert>

    <!-- 删除数据 -->
    <delete id="delete" parameterType="java.lang.String">
        delete from ${table_name}
        where ${primaryKey} = ${r"#"}{${primaryKey_java}}
    </delete>

    <!-- 批量删除 -->
    <update id="deleteBatch">
        update ${table_name}
        where ${primaryKey} in
        <foreach collection="array" item="id" open="(" separator="," close=")">
        ${r"#"}{id}
        </foreach>
    </update>

    <!-- 删除数据列表 -->
    <delete id="deleteList" parameterType="java.lang.String">
        delete from ${table_name}
         <include refid="page_where" />
    </delete>

    <!-- 更新数据 -->
    <update id="update" parameterType="${package_path}<#if (model?exists && model!="")>.${model}</#if>.entity.${class_name}">
        update ${table_name}
        <trim prefix="set" suffixOverrides=",">
		<#list table_column as c><#if (c_index>=1)>
            <#if (c.type=="String")>
            <if test="${c.javaName} != null and ${c.javaName} != ''">${c.name?upper_case} = ${r"#"}{${c.javaName}},</if>
            <#else>
			<if test="${c.javaName} != null">${c.name?upper_case} = ${r"#"}{${c.javaName}},</if>
            </#if>
        </#if></#list>
        </trim>
        <where>${primaryKey} = ${r"#"}{${primaryKey_java}}</where>
    </update>

    <!-- 查询所有数据列表 -->
    <select id="findAll" resultMap="${class_name?uncap_first}ResultMap">
        select <include refid="table_columns" />
        from ${table_name}
    </select>

    <!-- 查询数据列表 -->
    <select id="findList" resultMap="${class_name?uncap_first}ResultMap">
        select <include refid="table_columns" />
        from ${table_name}
        <include refid="page_where" />
    </select>

    <!-- 模糊查询数据列表 -->
    <select id="findListByFuzzyQuery" resultMap="${class_name?uncap_first}ResultMap">
        select <include refid="table_columns" />
        from ${table_name}
        <include refid="page_where_fuzzy_query" />
    </select>

    <!-- 查询数据记录 -->
    <select id="getCount" resultType="int" >
        select count(${primaryKey}) from ${table_name}
        <include refid="page_where" />
    </select>

    <!-- 获取单条数据 -->
    <select id="get" resultMap="${class_name?uncap_first}ResultMap" parameterType="java.lang.String" >
        select <include refid="table_columns" />
        from ${table_name}
        where ${primaryKey} = ${r"#"}{id}
    </select>

    <!-- 其他自定义SQL -->
</mapper>
