<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Freemarker测试</title>
</head>
<body>
${name}---${msg}<br>
<hr>
assign指令使用：指定一个变量，可以使用插值输出<br>
<#-- assign的指令 -->
<#assign linkman="黑马"/>
${linkman}<br>
<#assign info={"mobile":"13888888888", "address":"吉山村"} />
mobile = ${info.mobile}；address = ${info.address}
<br><hr><br>
include指令：可以引入其他的模版<br>
<#include "header.ftl"/>

<br><hr><br>
if条件控制语句<br>
<#assign bool=true>

<#if bool>
    bool的值为true.
<#else>
    bool的值为false.
</#if>

<br><hr><br>
list遍历集合<br>
<#list goodsList as goods>
    index = ${goods_index}---name=${goods.name}---price=${goods.price}<br>
</#list>

<br><hr><br>
<br><hr><br>
<br><hr><br>
<br><hr><br>
<br><hr><br>
</body>
</html>