<br />
# 每个人的童年都有一个万能的机械手臂 #
<img src='http://img170.poco.cn/mypoco/myphoto/20120319/15/6477300520120319153533078_165.jpg'>
<br />

<h2>一、描述：</h2>
它是一个新颖的、轻量级的、独立的、易于使用的Java DAO Framework<br>
<br />

<h2>二、目的：</h2>

<ul><li>定义简单<br>
<ol><li>写DAO层不必像Hibernate或ibatis那样为每个数据实体都创建一个新的配置文件。:)<br>
</li><li>只需定义一个接口，集中了对数据库的操作、程序的调用、数据源的使用等。:)</li></ol></li></ul>

<ul><li>使用简单<br>
<ol><li>使用时只需为数据实体创建一个接口，不需对其实现 :)<br>
</li><li>使用主或从主库，指定数据源都在此接口中。 :)</li></ol></li></ul>

<ul><li>不用拼写复杂的sql<br>
<ol><li>不用程序拼写sql将参数加入进来，框架对参数自动填充处理。:)<br>
</li><li>除支持标准sql外，另外增加了动态操作语法：if/else、for 循环等。:)</li></ol></li></ul>

<ul><li>主从分离<br>
<ol><li>根据查找或更新操作自动的判断使用主或从库。 :)<br>
</li><li>执行sql时动态的切换主从数据库，不必为分库烦恼。 :)<br>
</li><li>也可在执行强制的使用主或从（暂未实现）。 :(</li></ol></li></ul>

<ul><li>支持散表散库<br>
<ol><li>暂未实现。 :(</li></ol></li></ul>

<br />
<h2>三、建立环境与测试：</h2>

<h3>1. 创建一张表</h3>

<pre><code><br>
CREATE TABLE mail (<br>
id int(10) unsigned NOT NULL AUTO_INCREMENT,<br>
mail_user varchar(32) COLLATE utf8_bin NOT NULL DEFAULT '',<br>
mail_password varchar(32) COLLATE utf8_bin NOT NULL DEFAULT '',<br>
mail_smtp varchar(32) COLLATE utf8_bin NOT NULL,<br>
mail_ssl tinyint(1) NOT NULL DEFAULT '0' COMMENT 'boolean类型',<br>
PRIMARY KEY (id)<br>
)<br>
</code></pre>

<h3>2. 配置数据源</h3>

<pre><code><br>
&lt;?xml version="1.0" encoding="UTF-8"?&gt;<br>
&lt;!-- robotarm DAO framework --&gt;<br>
&lt;!-- 数据源配置 --&gt;<br>
&lt;db-config&gt;<br>
&lt;catalog name="c1"&gt;<br>
&lt;db type="master"&gt;<br>
&lt;driver&gt;org.gjt.mm.mysql.Driver<br>
<br>
Unknown end tag for &lt;/driver&gt;<br>
<br>
<br>
&lt;url&gt;jdbc:mysql://localhost:3307/testuseUnicode=true&amp;amp;characterEncoding=gbk<br>
<br>
Unknown end tag for &lt;/url&gt;<br>
<br>
<br>
&lt;user&gt;root<br>
<br>
Unknown end tag for &lt;/user&gt;<br>
<br>
<br>
&lt;password&gt;<br>
<br>
Unknown end tag for &lt;/password&gt;<br>
<br>
<br>
&lt;ispooled&gt;true<br>
<br>
Unknown end tag for &lt;/ispooled&gt;<br>
<br>
<br>
&lt;minconns&gt;20<br>
<br>
Unknown end tag for &lt;/minconns&gt;<br>
<br>
<br>
&lt;initconns&gt;30<br>
<br>
Unknown end tag for &lt;/initconns&gt;<br>
<br>
<br>
&lt;maxconns&gt;50<br>
<br>
Unknown end tag for &lt;/maxconns&gt;<br>
<br>
<br>
&lt;maxidletime&gt;20<br>
<br>
Unknown end tag for &lt;/maxidletime&gt;<br>
<br>
<br>
<br>
<br>
Unknown end tag for &lt;/db&gt;<br>
<br>
<br>
<br>
&lt;db type="slave"&gt;<br>
&lt;driver&gt;org.gjt.mm.mysql.Driver<br>
<br>
Unknown end tag for &lt;/driver&gt;<br>
<br>
<br>
&lt;url&gt;jdbc:mysql://localhost:3307/test?useUnicode=true&amp;amp;characterEncoding=gbk<br>
<br>
Unknown end tag for &lt;/url&gt;<br>
<br>
<br>
&lt;user&gt;root<br>
<br>
Unknown end tag for &lt;/user&gt;<br>
<br>
<br>
&lt;password&gt;<br>
<br>
Unknown end tag for &lt;/password&gt;<br>
<br>
<br>
&lt;ispooled&gt;true<br>
<br>
Unknown end tag for &lt;/ispooled&gt;<br>
<br>
<br>
&lt;minconns&gt;20<br>
<br>
Unknown end tag for &lt;/minconns&gt;<br>
<br>
<br>
&lt;initconns&gt;30<br>
<br>
Unknown end tag for &lt;/initconns&gt;<br>
<br>
<br>
&lt;maxconns&gt;50<br>
<br>
Unknown end tag for &lt;/maxconns&gt;<br>
<br>
<br>
&lt;maxidletime&gt;20<br>
<br>
Unknown end tag for &lt;/maxidletime&gt;<br>
<br>
<br>
<br>
<br>
Unknown end tag for &lt;/db&gt;<br>
<br>
<br>
<br>
<br>
<br>
Unknown end tag for &lt;/catalog&gt;<br>
<br>
<br>
<br>
<br>
Unknown end tag for &lt;/db-config&gt;<br>
<br>
<br>
<br>
</code></pre>



<h3>3. 定义一个数据访问接口</h3>

<pre><code><br>
@DAO(catalog = "c1")<br>
public interface MailDAO {<br>
// 查询<br>
@SQL(value = "select * from mail")<br>
public List&lt;Mail&gt; queryAll() throws Throwable;<br>
<br>
@SQL(value = "select * from mail where id=:1")<br>
public List&lt;Mail&gt; queryById(long id) throws Throwable;<br>
<br>
@SQL(value = "select * from mail where id in (:1)")<br>
public List&lt;Mail&gt; queryInIds(List&lt;Long&gt; ids) throws Throwable;<br>
<br>
@SQL(value = "select * from mail where mail_user = :1.mailUser and mail_smtp=:1.mailSmtp")<br>
public List&lt;Mail&gt; queryByUser(Mail mail) throws Throwable;<br>
<br>
// 更新<br>
@SQL(value = "update mail set mail_user=:1, mail_password=:2")<br>
public UpdateResult upAll(String mailUser, String mailPassword) throws Throwable;<br>
<br>
@SQL(value = "update mail set mail_user='sunnymoon', mail_password='passwordhe' where id=:1")<br>
public UpdateResult upById(long id) throws Throwable;<br>
<br>
@SQL(value = "update mail set mail_password='passwordhe' where mail_name=:1")<br>
public UpdateResult upByName(String name) throws Throwable;<br>
<br>
// 插入<br>
@SQL(value = "insert into mail (mail_user,mail_password,mail_smtp,mail_ssl) values ('ad','ff','dd','1')")<br>
public UpdateResult addMail() throws Throwable;<br>
<br>
@SQL(value = "insert into mail (mail_user,mail_password,mail_smtp) values (:1.mailUser, :1.mailPassword, :1.mailSmtp)")<br>
public UpdateResult addMail(Mail mail) throws Throwable;<br>
<br>
@SQL(value = "insert into mail (mail_user,mail_password,mail_smtp) values #for(mail in :1) { #if(:current){,} (:mail.mailUser, :mail.mailPassword, :mail.mailSmtp)}")<br>
public UpdateResult addMails(Collection&lt;Mail&gt; mails) throws Throwable;<br>
<br>
// 删除<br>
@SQL(value = "delete from mail where id=:1")<br>
public UpdateResult delById(long id) throws Throwable;<br>
<br>
@SQL(value = "delete from mail where id in (:1)")<br>
public UpdateResult delInIds(List&lt;Long&gt; ids) throws Throwable;<br>
<br>
}<br>
</code></pre>


<h3>4. 执行测试</h3>

junit方式执行MailDAOTest<br>
<br>
<br>
<h3>5. 补充说明</h3>

robotarm-dao 1.0版本只通过了最基本的测试，没有经过性能测试和压力测试, 不适合在生产环境中<br />
欢迎一起测试，发现问题！<br />