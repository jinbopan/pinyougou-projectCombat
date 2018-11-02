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
size可以获得集合的总数；如：goodsList的记录数为：${goodsList?size}
<br><hr><br>
json字符串转换为对象：<br>
<#assign jsonStr='{"id":123,"name":"itcast"}'/>
<#assign jsonObj=jsonStr?eval/>
${jsonObj.id}---${jsonObj.name}
<br><hr><br>
.now是当前时间：${.now}<br>
格式化显示日期时间：<br>
日期：${today?date}<br>
时间：${today?time}<br>
日期时间：${today?datetime}<br>
格式化显示：${today?string("yyyy年MM月dd日")}
<br><hr><br>
数值直接显示：${number}--->完整显示数值：${number?c}
<br><hr><br>
空值处理；在freemarker中是不可以直接显示空值或者null的。可以如下处理:<br>
如果为空则可以使用!表示什么都不显示：${emp!}；如果空的时候需要显示对应的内容，则可以!"xx"；${emp!"emp的值为空"}<br>

<br>
??? 前面两个??表示变量是否存在，如果存在则返回true，否则false；第三个?表示函数的调用<br>

<#assign bool2 = false/>
${bool2???string}<br>

<#if str??>
    str存在
<#else>
    str不存在
</#if>

<br>
<br>
<br>
<br>

</body>
</html>